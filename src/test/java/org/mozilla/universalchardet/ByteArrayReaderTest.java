package org.mozilla.universalchardet;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteArrayReaderTest {

	private final String TEST_STRING = "°";

	@Test
	void utf8() throws IOException {

		try (BufferedReader bufferedReader = ReaderFactory.createBufferedReader(
				TEST_STRING.getBytes(StandardCharsets.UTF_8))) {
			assertEquals(TEST_STRING, bufferedReader.readLine());
		}
	}

	@Test
	void ISO_8859_1() throws IOException {

		try (BufferedReader bufferedReader = ReaderFactory.createBufferedReader(
				TEST_STRING.getBytes(StandardCharsets.ISO_8859_1))) {
			assertEquals(TEST_STRING, bufferedReader.readLine());
		}
	}

}
