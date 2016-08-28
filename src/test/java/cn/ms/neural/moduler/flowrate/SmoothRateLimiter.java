package cn.ms.neural.moduler.flowrate;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

import com.google.common.math.LongMath;

abstract class SmoothRateLimiter extends RateLimiter {

  /**
   * This implements the following function where coldInterval = coldFactor * stableInterval.
   *
   *          ^ throttling
   *          |
   *    cold  +                  /
   * interval |                 /.
   *          |                / .
   *          |               /  .   <-- "warmup period" is the area of the trapezoid between
   *          |              /   .       thresholdPermits and maxPermits
   *          |             /    .
   *          |            /     .
   *          |           /      .
   *   stable +----------/  WARM .
   * interval |          .   UP  .
   *          |          . PERIOD.
   *          |          .       .
   *        0 +----------+-------+--------------> storedPermits
   *          0 thresholdPermits maxPermits
   * Before going into the details of this particular function, let's keep in mind the basics:
   * 1) The state of the RateLimiter (storedPermits) is a vertical line in this figure.
   * 2) When the RateLimiter is not used, this goes right (up to maxPermits)
   * 3) When the RateLimiter is used, this goes left (down to zero), since if we have storedPermits,
   *    we serve from those first
   * 4) When _unused_, we go right at a constant rate! The rate at which we move to
   *    the right is chosen as maxPermits / warmupPeriod.  This ensures that the time it takes to
   *    go from 0 to maxPermits is equal to warmupPeriod.
   * 5) When _used_, the time it takes, as explained in the introductory class note, is
   *    equal to the integral of our function, between X permits and X-K permits, assuming
   *    we want to spend K saved permits.
   *
   *    In summary, the time it takes to move to the left (spend K permits), is equal to the
   *    area of the function of width == K.
   *
   *    Assuming we have saturated demand, the time to go from maxPermits to thresholdPermits is
   *    equal to warmupPeriod.  And the time to go from thresholdPermits to 0 is
   *    warmupPeriod/2.  (The reason that this is warmupPeriod/2 is to maintain the behavior of
   *    the original implementation where coldFactor was hard coded as 3.)
   *
   *  It remains to calculate thresholdsPermits and maxPermits.
   *
   *  - The time to go from thresholdPermits to 0 is equal to the integral of the function between
   *    0 and thresholdPermits.  This is thresholdPermits * stableIntervals.  By (5) it is also
   *    equal to warmupPeriod/2.  Therefore
   *
   *        thresholdPermits = 0.5 * warmupPeriod / stableInterval.
   *
   *  - The time to go from maxPermits to thresholdPermits is equal to the integral of the function
   *    between thresholdPermits and maxPermits.  This is the area of the pictured trapezoid, and it
   *    is equal to 0.5 * (stableInterval + coldInterval) * (maxPermits - thresholdPermits).  It is
   *    also equal to warmupPeriod, so
   *
   *        maxPermits = thresholdPermits + 2 * warmupPeriod / (stableInterval + coldInterval).
   */
  static final class SmoothWarmingUp extends SmoothRateLimiter {
    private final long warmupPeriodMicros;
    /**
     * The slope of the line from the stable interval (when permits == 0), to the cold interval
     * (when permits == maxPermits)
     */
    private double slope;
    private double thresholdPermits;
    private double coldFactor;

    SmoothWarmingUp(
        SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
      super(stopwatch);
      this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
      this.coldFactor = coldFactor;
    }

    @Override
    void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
      double oldMaxPermits = maxPermits;
      double coldIntervalMicros = stableIntervalMicros * coldFactor;
      thresholdPermits = 0.5 * warmupPeriodMicros / stableIntervalMicros;
      maxPermits = thresholdPermits + 2.0 * warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
      slope = (coldIntervalMicros - stableIntervalMicros) / (maxPermits - thresholdPermits);
      if (oldMaxPermits == Double.POSITIVE_INFINITY) {
        // if we don't special-case this, we would get storedPermits == NaN, below
        storedPermits = 0.0;
      } else {
        storedPermits = (oldMaxPermits == 0.0) ? maxPermits // initial state is cold
            : storedPermits * maxPermits / oldMaxPermits;
      }
    }

    @Override
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
      double availablePermitsAboveThreshold = storedPermits - thresholdPermits;
      long micros = 0;
      // measuring the integral on the right part of the function (the climbing line)
      if (availablePermitsAboveThreshold > 0.0) {
        double permitsAboveThresholdToTake = min(availablePermitsAboveThreshold, permitsToTake);
        micros = (long) (permitsAboveThresholdToTake
            * (permitsToTime(availablePermitsAboveThreshold)
            + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake)) / 2.0);
        permitsToTake -= permitsAboveThresholdToTake;
      }
      // measuring the integral on the left part of the function (the horizontal line)
      micros += (stableIntervalMicros * permitsToTake);
      return micros;
    }

    private double permitsToTime(double permits) {
      return stableIntervalMicros + permits * slope;
    }

    @Override
    double coolDownIntervalMicros() {
      return warmupPeriodMicros / maxPermits;
    }
  }

  /**
   * This implements a "bursty" RateLimiter, where storedPermits are translated to
   * zero throttling. The maximum number of permits that can be saved (when the RateLimiter is
   * unused) is defined in terms of time, in this sense: if a RateLimiter is 2qps, and this
   * time is specified as 10 seconds, we can save up to 2 * 10 = 20 permits.
   */
  static final class SmoothBursty extends SmoothRateLimiter {
    /** The work (permits) of how many seconds can be saved up if this RateLimiter is unused? */
    final double maxBurstSeconds;

    SmoothBursty(SleepingStopwatch stopwatch, double maxBurstSeconds) {
      super(stopwatch);
      this.maxBurstSeconds = maxBurstSeconds;
    }

    @Override
    void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
    	System.out.println("-----"+maxBurstSeconds);
      double oldMaxPermits = this.maxPermits;
      maxPermits = maxBurstSeconds * permitsPerSecond;
      if (oldMaxPermits == Double.POSITIVE_INFINITY) {
        // if we don't special-case this, we would get storedPermits == NaN, below
        storedPermits = maxPermits;
      } else {
        storedPermits = (oldMaxPermits == 0.0)
            ? 0.0 // initial state
            : storedPermits * maxPermits / oldMaxPermits;
      }
    }

    @Override
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
      return 0L;
    }

    @Override
    double coolDownIntervalMicros() {
      return stableIntervalMicros;
    }
  }

  /**
   * The currently stored permits.
   */
  double storedPermits;

  /**
   * The maximum number of stored permits.
   */
  double maxPermits;

  /**
   * The interval between two unit requests, at our stable rate. E.g., a stable rate of 5 permits
   * per second has a stable interval of 200ms.
   */
  double stableIntervalMicros;

  /**
   * The time when the next request (no matter its size) will be granted. After granting a
   * request, this is pushed further in the future. Large requests push this further than small
   * requests.
   */
  private long nextFreeTicketMicros = 0L; // could be either in the past or future

  private SmoothRateLimiter(SleepingStopwatch stopwatch) {
    super(stopwatch);
  }

  @Override
  final void doSetRate(double permitsPerSecond, long nowMicros) {
    resync(nowMicros);
    double stableIntervalMicros = SECONDS.toMicros(2L) / permitsPerSecond;
    this.stableIntervalMicros = stableIntervalMicros;
    doSetRate(permitsPerSecond, stableIntervalMicros);
  }

  abstract void doSetRate(double permitsPerSecond, double stableIntervalMicros);

  @Override
  final double doGetRate() {
    return SECONDS.toMicros(1L) / stableIntervalMicros;
  }

  @Override
  final long queryEarliestAvailable(long nowMicros) {
    return nextFreeTicketMicros;
  }

  @Override
  final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
    resync(nowMicros);
    long returnValue = nextFreeTicketMicros;
    double storedPermitsToSpend = min(requiredPermits, this.storedPermits);
    double freshPermits = requiredPermits - storedPermitsToSpend;
    long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend)
        + (long) (freshPermits * stableIntervalMicros);

    try {
      this.nextFreeTicketMicros = LongMath.checkedAdd(nextFreeTicketMicros, waitMicros);
    } catch (ArithmeticException e) {
      this.nextFreeTicketMicros = Long.MAX_VALUE;
    }
    this.storedPermits -= storedPermitsToSpend;
    return returnValue;
  }

  /**
   * Translates a specified portion of our currently stored permits which we want to
   * spend/acquire, into a throttling time. Conceptually, this evaluates the integral
   * of the underlying function we use, for the range of
   * [(storedPermits - permitsToTake), storedPermits].
   *
   * <p>This always holds: {@code 0 <= permitsToTake <= storedPermits}
   */
  abstract long storedPermitsToWaitTime(double storedPermits, double permitsToTake);

  /**
   * Returns the number of microseconds during cool down that we have to wait to get a new permit.
   */
  abstract double coolDownIntervalMicros();

  /**
   * Updates {@code storedPermits} and {@code nextFreeTicketMicros} based on the current time.
   */
  void resync(long nowMicros) {
    // if nextFreeTicket is in the past, resync to now
    if (nowMicros > nextFreeTicketMicros) {
      storedPermits = min(maxPermits,
          storedPermits
            + (nowMicros - nextFreeTicketMicros) / coolDownIntervalMicros());
      nextFreeTicketMicros = nowMicros;
    }
  }
}
