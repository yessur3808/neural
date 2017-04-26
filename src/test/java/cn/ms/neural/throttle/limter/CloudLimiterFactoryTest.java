package cn.ms.neural.throttle.limter;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import cn.ms.neural.throttle.support.FlowUnit;

public class CloudLimiterFactoryTest {
    final AtomicLong sumSize = new AtomicLong(0);

    @Test
    public void createFlowLimiter() throws Exception {
        final FlowLimiter flowLimiter = new FlowLimiter(200, FlowUnit.BYTE, TimeUnit.DAYS.toSeconds(1));

        for (int i = 0; i < 10000; i++) {
            String msg = buildMessage((new Random().nextInt(1000)));
            long msgSize = msg.getBytes().length;
            send(msg, msgSize);
            flowLimiter.acquire(msgSize, FlowUnit.BYTE);
        }
    }

    private static String buildMessage(final int messageSize) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageSize; i += 10) {
            sb.append("hello baby");
        }
        return String.valueOf(sb);
    }

    public void send(String msg, long msgSize) {
        System.out.println("[" + new Date() + "]: sumMsgSize == " + sumSize.addAndGet(msgSize));
    }

}