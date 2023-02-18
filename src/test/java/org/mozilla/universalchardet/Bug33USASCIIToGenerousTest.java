package org.mozilla.universalchardet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Bug33USASCIIToGenerousTest {

	@Test
	@Disabled("Not sure")
	void testUTF16() throws IOException {

		assertEquals("UTF-16BE", detect("ab".getBytes(StandardCharsets.UTF_16BE)));
		assertEquals("UTF-16LE", detect("ab".getBytes(StandardCharsets.UTF_16LE)));
	}

	@Test
	void testZipHeader() throws IOException {

		byte[] zipHeader = new byte[] { 0x50, 0x4b, 0x03, 0x04, 0x14, 0x00, 0x02, 0x00 };
		assertNull(detect(zipHeader));
	}

	private String detect(byte[] data) throws IOException {

		return UniversalDetector.detectCharset(new ByteArrayInputStream(data));
	}

}
