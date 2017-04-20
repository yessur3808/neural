package cn.ms.neural.sequence;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.extension.ExtensionLoader;
import cn.ms.neural.sequence.Sequence;

public class UuidSequenceTest {

	Sequence sequence = ExtensionLoader.getExtensionLoader(Sequence.class).getExtension("uuid");

	@Test
	public void testMicroSequenceNext() {
		String id = sequence.next();
		Assert.assertNotNull(id);
		Assert.assertEquals(id.length(), 32);
	}

	@Test
	public void testMicroSequenceNextLong() {
		try {
			sequence.nextLong();
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalStateException);
		}
	}

}
