package org.mozilla.universalchardet;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author ian
 * @since  Jul 13, 2011
 *
 */
public class GB18030SMFalsePositiveTest {
	
	public GB18030SMFalsePositiveTest() {
		super();
	}
	
	@Test
	@Ignore
	public void testFalsePositiveBug11() throws UnsupportedEncodingException {
		String testString = "[°4°0°T°C°C°0°C°T";
		byte[] testBuf = new byte[]{91, -80, 52, -80, 48, -80, 84, -80, 67, -80, 67, -80, 48, -80, 67, -80, 84};
		byte[] buf = testString.getBytes("WINDOWS-1252");
		Assert.assertArrayEquals(testBuf, buf);

		UniversalDetector detector = new UniversalDetector();
		detector.handleData(buf, 0, buf.length);
		detector.dataEnd();
		
		String encoding = detector.getDetectedCharset();
		detector.reset();
		assertEquals("WINDOWS-1252", encoding);
	}
	
	@Test
	@Ignore
	public void testFalsePositiveBug9() throws UnsupportedEncodingException {
		String testString = "Wykamol,£588.95,0.18,0.12,testingSpecialised Products for DIY and Professionals£12";
		byte[] buf = testString.getBytes("WINDOWS-1252");
		
		UniversalDetector detector = new UniversalDetector();
		detector.handleData(buf);
		detector.dataEnd();
		
		String encoding = detector.getDetectedCharset();
		detector.reset();
		
		assertEquals("WINDOWS-1252", encoding);
	}
}