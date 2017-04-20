package cn.ms.neural.retryer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.timelimiter.AttemptTimeLimiters;

import com.google.common.util.concurrent.UncheckedTimeoutException;

public class AttemptTimeLimiterTest {

    Retryer<Void> r = RetryerBuilder.<Void>newBuilder()
            .withAttemptTimeLimiter(AttemptTimeLimiters.<Void>fixedTimeLimit(1, TimeUnit.SECONDS))
            .build();

    @Test
    public void testAttemptTimeLimit() throws ExecutionException, RetryException {
        try {
            r.call(new SleepyOut(0L));
        } catch (ExecutionException e) {
            Assert.fail("Should not timeout");
        }

        try {
            r.call(new SleepyOut(10 * 1000L));
            Assert.fail("Expected timeout exception");
        } catch (ExecutionException e) {
            // expected
            Assert.assertEquals(UncheckedTimeoutException.class, e.getCause().getClass());
        }
    }

    static class SleepyOut implements Callable<Void> {

        final long sleepMs;

        SleepyOut(long sleepMs) {
            this.sleepMs = sleepMs;
        }

        @Override
        public Void call() throws Exception {
            Thread.sleep(sleepMs);
            System.out.println("I'm awake now");
            return null;
        }
    }
}
