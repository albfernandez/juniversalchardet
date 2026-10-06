package org.mozilla.universalchardet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodingDetectorReadTest {
    @Test
    public void singleByteReadsReturnUnsignedValues() throws IOException {
        byte[] bytes = new byte[256];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        try (EncodingDetectorInputStream in = new EncodingDetectorInputStream(
                new ByteArrayInputStream(bytes))) {
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(i, in.read(), "byte " + i);
            }
            assertEquals(-1, in.read());
        }
    }

    @Test
    public void ffDoesNotSignalEndOfStream() throws IOException {
        try (EncodingDetectorInputStream in = new EncodingDetectorInputStream(
                new ByteArrayInputStream(new byte[] {(byte) 0xff, 65}))) {
            assertEquals(255, in.read());
            assertEquals(65, in.read());
            assertEquals(-1, in.read());
        }
    }
}
