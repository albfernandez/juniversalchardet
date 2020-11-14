package org.mozilla.universalchardet;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class GB18030SamplesTest {

	public GB18030SamplesTest() {
		super();
	}
	
	@Test
	public void testGB18030Sample() throws IOException {
		Assert.assertEquals("GB18030", getFileEncoding("gb2312-sample.txt"));
	}
	
	@Test
	public void testGBKSample() throws IOException {
		Assert.assertEquals("GB18030", getFileEncoding("gbk-sample.txt"));
	}
	
	private String getFileEncoding(String testFileName) throws IOException{
        String fileName = "src/test/resources/" + testFileName;
        return UniversalDetector.detectCharset(new File(fileName));
	}
}
