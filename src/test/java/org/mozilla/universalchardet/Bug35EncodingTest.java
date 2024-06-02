package org.mozilla.universalchardet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Bug35EncodingTest {

	public Bug35EncodingTest() {
		super();
	}

	@Test
	@Disabled
	public void test_Encoding_UTF8_UTF8() {
		final var bytes = testBytes();

		final var detector = new UniversalDetector(null);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();

		final var expectedCharset = StandardCharsets.UTF_8;
		final var detectedCharset = detector.getDetectedCharset();

		assertNotNull(detectedCharset);

		final var actualCharset = Charset.forName(detectedCharset);

		assertEquals(expectedCharset, actualCharset);
	}

	private static byte[] testBytes() {
		String str = "One humid afternoon during the harrowing heatwave of 2060, Renato Salvatierra, "
				+ "a man with blood sausage fingers and a footfall that silenced rooms, "
				+ "received a box at his police station. Taped to the box was a ransom note; "
				+ "within were his wife's eyes. By year's end, a supermax prison overflowed with felons, "
				+ "owing to Salvatierra's efforts to find his beloved. Soon after, "
				+ "he flipped profession into an entry-level land management position that, "
				+ "his wife insisted, would be, in her words, *infinitamente más relajante*---infinitely more relaxing.";
		return str.getBytes(StandardCharsets.UTF_8);
	}
}