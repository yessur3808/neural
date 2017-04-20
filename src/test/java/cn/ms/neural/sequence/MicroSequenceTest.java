package cn.ms.neural.sequence;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.extension.ExtensionLoader;
import cn.ms.neural.sequence.MicroSequence;
import cn.ms.neural.sequence.Sequence;

public class MicroSequenceTest {

	Sequence sequence = ExtensionLoader.getExtensionLoader(Sequence.class).getExtension("ms");

	@Test
	public void testMicroSequenceNext() throws Exception {
		MicroSequence microSequence = (MicroSequence) sequence;
		microSequence.init(1000);
		String id = microSequence.next();
		Assert.assertNotNull(id);
		Assert.assertNotNull(id.length() > 0);
	}

	@Test(expected = IllegalStateException.class)
	public void testMicroSequenceNextLong() throws Exception {
		MicroSequence microSequence = (MicroSequence) sequence;
		microSequence.init(1000);
		microSequence.nextLong();
	}

}
