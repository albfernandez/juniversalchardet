package org.mozilla.universalchardet;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GB18030RepeatedLatinTest {
	
	public GB18030RepeatedLatinTest() {
		super();
	}

    @Test
    public void repeatedUtf8LatinIsNotMisclassifiedAsGB18030() {
        byte[] input = "Córdoba".repeat(5).getBytes(StandardCharsets.UTF_8);
        UniversalDetector detector = new UniversalDetector();
        detector.handleData(input);
        detector.dataEnd();

        assertEquals("UTF-8", detector.getDetectedCharset());
    }
}
