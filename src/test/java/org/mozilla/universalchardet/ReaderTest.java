package org.mozilla.universalchardet;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {

	private static final String TEST_STRING = "    コンソール アプリケーション : universalchardet プロジェクトの概要";

	@Test
	public void testUTF8() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("utf8.txt"));
	}

	@Test
	public void testUTF8N() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("utf8n.txt"));
	}

	@Test
	public void testUTF16LE() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("utf16le.txt"));
	}

	@Test
	public void testShifJis() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("shiftjis.txt"));
	}

	@Test
	public void testEUC() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("euc.txt"));
	}

	@Test
	public void testISO2022JP() throws IOException {

		assertEquals(TEST_STRING, getSecondLine("iso2022jp.txt"));
	}

	private String getSecondLine(String testFileName) throws IOException {

		String fileName = "src/test/resources/" + testFileName;
		File file = new File(fileName);
		try (BufferedReader reader = ReaderFactory.createBufferedReader(file)) {
			// Skip first line
			reader.readLine();
			// return second line
			return reader.readLine();
		}
	}

}
