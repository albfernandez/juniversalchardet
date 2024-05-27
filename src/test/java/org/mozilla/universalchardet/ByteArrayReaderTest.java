package org.mozilla.universalchardet;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ByteArrayReaderTest {
	
	private String TEST_STRING = "\u00b0";

	@Test
	public void utf8() throws IOException {
		byte[] data = TEST_STRING.getBytes(StandardCharsets.UTF_8);
		try(BufferedReader bufferedReader = ReaderFactory.createBufferedReader(data)) {
			assertEquals(TEST_STRING, bufferedReader.readLine());
		}
	}

	@Test
	public void ISO_8859_1() throws IOException {
		byte[] data = TEST_STRING.getBytes(StandardCharsets.ISO_8859_1);
		try(BufferedReader bufferedReader = ReaderFactory.createBufferedReader(data)) {
			assertEquals(TEST_STRING, bufferedReader.readLine());
		}
	}

}
