package org.mozilla.universalchardet;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ShortStringTests {

	public ShortStringTests() {
		super();
	}

	// Tets case for https://github.com/albfernandez/juniversalchardet/issues/22
	@Test
	@Ignore("data too short")
	public void testDecodeBytes() {

		final String string = "aeaCàêäÇ";
		Charset s;
		byte[] bytes;

		bytes = string.getBytes(Charset.forName("UTF-8"));
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS

		bytes = string.getBytes(Charset.forName("ISO-8859-1"));
		s = this.guessCharset(bytes); // detected charset = TIS-620, Thai charset ???!!!
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // FAILS of course !
	}

	// Tets case for https://github.com/albfernandez/juniversalchardet/issues/22
	// With less accute characters, it's improved detection
	@Test
	public void testDecodeBytesBetterStats() {

		final String string = "Château";
		Charset s;
		byte[] bytes;

		bytes = string.getBytes(Charset.forName("UTF-8"));
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS

		bytes = string.getBytes(Charset.forName("ISO-8859-1"));
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS
	}
	

	@Test
	public void testShortString() throws UnsupportedEncodingException {		
		Assert.assertEquals("US-ASCII", guessCharsetName("abcd".getBytes()));
	}

	private Charset guessCharset(final byte[] bytes) {		
		return Charset.forName(guessCharsetName(bytes));
	}
	
	private String guessCharsetName(final byte[] bytes) {
		final UniversalDetector detector = new UniversalDetector();
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		return detector.getDetectedCharset();
	}
}
