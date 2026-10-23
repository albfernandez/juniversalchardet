package org.mozilla.universalchardet;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GB18030RepeatedLatinTest {
	
	public GB18030RepeatedLatinTest() {
		super();
	}

    @Test
    public void repeatedUtf8LatinIsNotMisclassifiedAsGB18030() {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < 5; i++) {
    		sb.append("Córdoba");
    	}
        byte[] input = sb.toString().getBytes(StandardCharsets.UTF_8);
        UniversalDetector detector = new UniversalDetector();
        detector.handleData(input);
        detector.dataEnd();

        assertEquals("UTF-8", detector.getDetectedCharset());
    }
}
