package cn.ms.neural.sequence;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.micro.extension.ExtensionLoader;

public class SnowflakeSequenceTest {

	Sequence sequence = ExtensionLoader.getExtensionLoader(Sequence.class).getExtension("snowflake");

	@Test
	public void testMicroSequenceNext() throws Exception {
		SnowflakeSequence snowflakeSequence = (SnowflakeSequence) sequence;
		snowflakeSequence.init(10, 20);
		String id = snowflakeSequence.next();
		Assert.assertNotNull(id);
		Assert.assertNotNull(id.length() == 18);
	}

	@Test
	public void testMicroSequenceNextLong() throws Exception {
		SnowflakeSequence snowflakeSequence = (SnowflakeSequence) sequence;
		snowflakeSequence.init(10, 20);
		Long id = snowflakeSequence.nextLong();
		Assert.assertNotNull(id);
	}

}
