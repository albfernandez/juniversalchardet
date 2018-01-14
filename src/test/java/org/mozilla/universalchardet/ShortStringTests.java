package org.mozilla.universalchardet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

		bytes = string.getBytes(StandardCharsets.UTF_8);
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS

		bytes = string.getBytes(StandardCharsets.ISO_8859_1);
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

		bytes = string.getBytes(StandardCharsets.UTF_8);
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS

		bytes = string.getBytes(StandardCharsets.ISO_8859_1);
		s = this.guessCharset(bytes);
		Assert.assertEquals(string, new String(string.getBytes(s), s)); // SUCCESS
	}

	private Charset guessCharset(final byte[] bytes) {
		final UniversalDetector detector = new UniversalDetector();
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		return Charset.forName(detector.getDetectedCharset());
	}
}
