package cn.ms.neural.sequence;

import java.util.UUID;

import cn.ms.micro.extension.SpiMeta;

/**
 * 基于JDK的UUID实现分布式ID
 * 
 * @author lry
 */
@SpiMeta(name = "uuid")
public class UuidSequence implements Sequence {

	@Override
	public Long nextLong() {
		throw new IllegalStateException();
	}

	@Override
	public String next() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
