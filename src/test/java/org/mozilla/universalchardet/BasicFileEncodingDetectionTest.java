package org.mozilla.universalchardet;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BasicFileEncodingDetectionTest {

	
	public BasicFileEncodingDetectionTest() throws IOException {
		super();
	}
	
	
	@Test
	public void testASCII() throws IOException {
		Assert.assertEquals("US-ASCII", getFileEncoding("ascii.txt"));
	}
	
	@Test
	public void testUTF8 () throws IOException {
		Assert.assertEquals("UTF-8", getFileEncoding("utf8.txt"));
	}
	@Test
	public void testUTF8N () throws IOException {
		Assert.assertEquals("UTF-8", getFileEncoding("utf8n.txt"));
	}
	@Test
	public void testUTF16LE () throws IOException {
		Assert.assertEquals("UTF-16LE", getFileEncoding("utf16le.txt"));
	}
	@Test
	public void testShifJis () throws IOException {
		Assert.assertEquals("SHIFT_JIS", getFileEncoding("shiftjis.txt"));
	}
	
	@Test
	public void testEUC () throws IOException {
		Assert.assertEquals("EUC-JP", getFileEncoding("euc.txt"));
	}	
	@Test
	public void testISO2022JP () throws IOException {
		Assert.assertEquals("ISO-2022-JP", getFileEncoding("iso2022jp.txt"));
	}
	
	
	
	@Test
	public void testBIG5 () throws IOException {
		Assert.assertEquals("BIG5", getFileEncoding("big5.txt"));
	}
	
	@Test
	public void testEUCTW () throws IOException {
		Assert.assertEquals("EUC-TW", getFileEncoding("euctw.txt"));
	}
	@Test
	public void testEUCKR() throws IOException {
		Assert.assertEquals("EUC-KR", getFileEncoding("euckr.txt"));
	}
	@Test
	public void testWindows1255 () throws IOException {
		Assert.assertEquals("WINDOWS-1255", getFileEncoding("windows1255.txt"));
	}
		 
	
	@Test
	public void testUTF8Emoji () throws IOException {
		Assert.assertEquals("UTF-8", getFileEncoding("utf8n-emoji.txt"));
	}
	
	
	private String getFileEncoding(String testFileName) throws IOException{
        String fileName = "src/test/resources/" + testFileName;
        return UniversalDetector.detectCharset(new File(fileName));
	}
}
