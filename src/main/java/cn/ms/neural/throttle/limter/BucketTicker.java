package cn.ms.neural.throttle.limter;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class BucketTicker {
  
    /**
     * Returns the number of nanoseconds elapsed since this ticker's fixed
     * point of reference.
     */
    public abstract long read();

    /**
     * A ticker that reads the current time using {@link System#nanoTime}.
     * 
     * @return
     */
    public static BucketTicker systemTicker() {
        return SYSTEM_TICKER;
    }

    private static final BucketTicker SYSTEM_TICKER = new BucketTicker() {
        @Override
        public long read() {
            return System.nanoTime();
        }
    };

    /**
     * 阻塞睡眠sleepForMicros微秒
     *
     * @param sleepForMicros 睡眠的微秒数
     */
    public static void sleepMicros(long sleepForMicros) {
        boolean interrupted = false;
        try {
            long remainingNanos = TimeUnit.MICROSECONDS.toNanos(sleepForMicros);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.sleep() treats negative timeouts just like zero.
                    NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 阻塞睡眠sleepForSecond秒
     *
     * @param sleepForSecond 睡眠的秒数
     */
    public static void sleepSeconds(double sleepForSecond) {
        long sleepForMicrs = (long) (sleepForSecond * 1000 * 1000);
        sleepMicros(sleepForMicrs);
    }

    /**
     * 阻塞睡眠sleepForMills秒
     *
     * @param sleepForMicros 睡眠的微秒数
     */
    public static void sleepMillis(double sleepForMills) {
        long sleepForMicrs = (long) (sleepForMills * 1000);
        sleepMicros(sleepForMicrs);
    }

    /**
     * 为了保护SYSTEM_TICKER的值，SleepingTicker不对外开放
     * 
     * @author lry
     */
    static abstract class SleepingTicker extends BucketTicker {
        final static SleepingTicker SYSTEM_TICKER = new SleepingTicker() {
            @Override
            public long read() {
                return systemTicker().read();
            }
        };
    }
    
}
