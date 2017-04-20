package cn.ms.neural.sequence;

import java.util.concurrent.atomic.AtomicLong;

import cn.ms.neural.extension.SpiMeta;

/**
 * This class is used for generating unique id in a distributed environment. It
 * require a server identifier. Server identifier is a 0-255 integer.
 * DistributedID(64 bits) = ServerID(8) + Time(50) + clk_seq(6)
 * 
 * @author lry
 */
@SpiMeta(name = "ms")
public class MicroSequence implements Sequence {

	/**
	 * The last time value. Used to remove duplicate IDs.
	 */
	private final AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);
	private int serverIdentifier;
	
	public void init(int serverIdentifier) {
		this.serverIdentifier = serverIdentifier;
	}
	
	@Override
	public Long nextLong() {
		throw new IllegalStateException();
	}
	
	/**
	 * generate distributed ID.
	 * 
	 * @param serverIdentifier support 256 servers. 0-255
	 * @return
	 */
	@Override
	public String next() {
		long dentifier = (long) serverIdentifier << 56;
		long distributedID = dentifier | genTimeBasedID();
		String id = Long.toHexString(distributedID);
		while (id.length() < 16) {
			id = "0" + id;
		}

		return id;
	}

	/**
	 * Generates a new time field. Each time field is unique and larger than the
	 * previously generated time field.
	 * 
	 * @return
	 */
	private long genTimeBasedID() {
		return genTimeBasedID(System.currentTimeMillis());
	}

	/**
	 * generate a new time field from the given timestamp. Note that even
	 * identical values of <code>time</code> will produce different time fields.
	 * 
	 * @param time
	 * @return
	 */
	private long genTimeBasedID(long time) {
		time = time << 6;// 6 bits for clk_seq
		while (true) {// try again if compareAndSet failed
			long tempTime = lastTime.get();
			if (time > tempTime) {
				if (lastTime.compareAndSet(tempTime, time)) {
					break;
				}
			} else {// +1 to make sure the timestamp field is different even the
					// time is identical.
				if (lastTime.compareAndSet(tempTime, ++tempTime)) {
					time = tempTime;
					break;
				}
			}
		}

		return time;
	}

}
