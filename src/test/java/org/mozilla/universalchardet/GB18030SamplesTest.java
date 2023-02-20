package org.mozilla.universalchardet;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GB18030SamplesTest {

	@Test
	void testGB18030Sample() throws IOException {

		assertEquals("GB18030", getFileEncoding("gb2312-sample.txt"));
	}

	@Test
	void testGBKSample() throws IOException {

		assertEquals("GB18030", getFileEncoding("gbk-sample.txt"));
	}

	private String getFileEncoding(String testFileName) throws IOException {

		String fileName = "src/test/resources/" + testFileName;
		return UniversalDetector.detectCharset(new File(fileName));
	}
}
