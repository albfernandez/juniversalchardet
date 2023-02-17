package org.mozilla.universalchardet;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ByteArrayReaderTest {

	@Test
	public void utf8() throws IOException {
		try(BufferedReader bufferedReader = ReaderFactory.createBufferedReader("°".getBytes(StandardCharsets.UTF_8))) {
			assertEquals("°", bufferedReader.readLine());
		}
	}

	@Test
	public void ISO_8859_1() throws IOException {
		String reference = Character.toString((char) 186);
		try(BufferedReader bufferedReader = ReaderFactory.createBufferedReader(
				reference.getBytes(StandardCharsets.ISO_8859_1))) {
			assertEquals(reference, bufferedReader.readLine());
		}
	}

}
