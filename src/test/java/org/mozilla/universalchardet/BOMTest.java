package org.mozilla.universalchardet;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BOMTest {

	private static final String TEST_STRING = "========================================================================";

	@Test
	void testUTF8() throws IOException {

		assertEquals(TEST_STRING, getFirstLine("utf8.txt"));
	}

	@Test
	void testUTF8N() throws IOException {

		assertEquals(TEST_STRING, getFirstLine("utf8n.txt"));
	}

	@Test
	void testUTF16LE() throws IOException {

		assertEquals(TEST_STRING, getFirstLine("utf16le.txt"));
	}

	private String getFirstLine(String testFileName) throws IOException {

		String fileName = "src/test/resources/" + testFileName;
		File file = new File(fileName);
		try (BufferedReader reader = ReaderFactory.createBufferedReader(file)) {
			// return first line
			return reader.readLine();
		}
	}
}
