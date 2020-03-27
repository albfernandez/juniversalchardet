package org.mozilla.universalchardet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class Bug33USASCIIToGenerous {
	
	public Bug33USASCIIToGenerous() {
		super();
	}
	

	
	@Test
	@Ignore("Not sure")
	public void testUTF16 () throws IOException {
		Assert.assertEquals("UTF-16BE", detect("ab".getBytes("UTF-16BE")));
		Assert.assertEquals("UTF-16LE", detect("ab".getBytes("UTF-16LE")));
	}
	@Test
	public void testZipHeader() throws IOException {
		byte[] zipHeader = new byte[]{0x50, 0x4b, 0x03, 0x04, 0x14, 0x00, 0x02, 0x00}; 
		Assert.assertNull(detect(zipHeader));
	}
	
	private String detect(byte[] data) throws IOException {
		return UniversalDetector.detectCharset(new ByteArrayInputStream(data));
	}

}
