package org.mozilla.universalchardet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
 * This charset detection fails because too little data to analize
 */
public class Bug8VariousFailedCharsetsTest {

	@Test
	@Disabled
	public void test1() throws Exception {
		/*
=?KR?B?wPzDvCCwy7v2?=
전체 검색
		 */

		byte[] data = decodeBase64("wPzDvCCwy7v2");
		String charset = detect(data);
		assertNotNull(charset);
		assertEquals("전체 검색", new String(data, charset));
	}

	@Test
	public void test2() throws Exception {
		/*
=?wws-874?B?W1Nhbm9va10gt7TKzbq70a3L0qHSw83o0rnA0snS5LfCzdWhpMPR6acg8fLz?=
[Sanook] ทดสอบปัญหาการอ่านภาษาไทยอีกครั้ง ๑๒๓
		 */

		byte[] data = decodeBase64("W1Nhbm9va10gt7TKzbq70a3L0qHSw83o0rnA0snS5LfCzdWhpMPR6acg8fLz");
		String charset = detect(data);
		assertNotNull(charset);
		assertEquals("[Sanook] ทดสอบปัญหาการอ่านภาษาไทยอีกครั้ง ๑๒๓", new String(data, charset));
	}

	@Test
	@Disabled
	public void test3() throws Exception {
/*
=?gb2312?B?zfjS19PKz+TX1Lavu9i4tDo=?= Re: �˲ű�
网易邮箱自动回复: Re: 人才表	
 */
		byte[] data = decodeBase64("zfjS19PKz+TX1Lavu9i4tDo=");
		String charset = detect(data);
		assertNotNull(charset);
		assertEquals("网易邮箱自动回复:", new String(data, charset));

	}

	private String detect(byte[] data) {

		UniversalDetector detector = new UniversalDetector();
		detector.handleData(data);
		detector.dataEnd();
		String detected = detector.getDetectedCharset();
		detector.reset();

		return detected;
	}

	private byte[] decodeBase64(String string) {

		return org.apache.commons.codec.binary.Base64.decodeBase64(string);
	}

}
