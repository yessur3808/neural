package cn.ms.neural.common.bloomfilter;

import org.junit.Assert;
import org.junit.Test;

public class BloomFilterTest {

	@Test
	public void test() {
		String path="/app/code/work_neon/neural/src/main/resources/readme.txt";
		String value="http://www.plating.org/news_info.asp?pid=28&id=2857";
		BloomFilter bloomFilterTest = new BloomFilter(path);
		bloomFilterTest.init();
		bloomFilterTest.add(value);
		Assert.assertTrue(bloomFilterTest.isExit(value));
	}
	
}
