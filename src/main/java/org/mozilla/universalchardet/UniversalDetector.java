/* 
(C) Copyright 2016-2017 Alberto Fernández <infjaf@gmail.com>
(C) Copyright 2006-2007 Kohei TAKETA <k-tak@void.in> (Java port)
(C) Copyright 2001 Netscape Communications Corporation.

 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Universal charset detector code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 2001
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *          Shy Shalom <shooshX@gmail.com>
 *          Kohei TAKETA <k-tak@void.in> (Java port)
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * 
*/

package org.mozilla.universalchardet;

import static org.mozilla.universalchardet.Constants.CHARSET_US_ASCII;
import static org.mozilla.universalchardet.Constants.CHARSET_UTF_16BE;
import static org.mozilla.universalchardet.Constants.CHARSET_UTF_16LE;
import static org.mozilla.universalchardet.Constants.CHARSET_UTF_32BE;
import static org.mozilla.universalchardet.Constants.CHARSET_UTF_32LE;
import static org.mozilla.universalchardet.Constants.CHARSET_UTF_8;
import static org.mozilla.universalchardet.Constants.CHARSET_X_ISO_10646_UCS_4_2143;
import static org.mozilla.universalchardet.Constants.CHARSET_X_ISO_10646_UCS_4_3412;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.mozilla.universalchardet.prober.CharsetProber;
import org.mozilla.universalchardet.prober.EscCharsetProber;
import org.mozilla.universalchardet.prober.Latin1Prober;
import org.mozilla.universalchardet.prober.MBCSGroupProber;
import org.mozilla.universalchardet.prober.SBCSGroupProber;

public class UniversalDetector {

    private static final float MINIMUM_THRESHOLD = 0.20f;

	private  enum InputState {
		PURE_ASCII, 
		ESC_ASCII, 
		HIGHBYTE
	}
    

    private InputState  inputState;
    private boolean     done;
    private boolean     start;
    private boolean     gotData;
    private boolean     onlyPrintableASCII = true;
    private byte        lastChar;
    private String      detectedCharset;

    private CharsetProber[]     probers;
    private CharsetProber       escCharsetProber;
    
    private CharsetListener     listener;

        
    /**
     * Create UniversalDetector
     */
    public UniversalDetector() {
    	this(null);
    }
    /**
     * Create UniversalDetector
     * @param listener a listener object that is notified of
     *         the detected encocoding. Can be null.
     */
	public UniversalDetector(CharsetListener listener) {
		super();
        this.listener = listener;
        this.escCharsetProber = null;
        this.probers = new CharsetProber[3];
        
        reset();
    }
    
    public boolean isDone() {
        return this.done;
    }
    
    /**
     * @return The detected encoding is returned. If the detector couldn't
     *          determine what encoding was used, null is returned.
     */
    public String getDetectedCharset()  {
        return this.detectedCharset;
    }
    
    public void setListener(CharsetListener listener)  {
        this.listener = listener;
    }
    
    public CharsetListener getListener() {
        return this.listener;
    }
    /**
     * Feed the detector with more data
     * @param buf The buffer containing the data
     */
    public void handleData(final byte[] buf) {
    	handleData(buf, 0, buf.length);
    }
    /**
     * Feed the detector with more data
     * @param buf Buffer with the data
     * @param offset initial position of data in buf
     * @param length length of data
     */
	public void handleData(final byte[] buf, int offset, int length) {
        if (this.done) {
            return;
        }
        if (length == 0) {
        	return;
        }
        
        
        if (length > 0) {
            this.gotData = true;
        }
        
        if (this.start) {
        	// Check utf8 for the first bytes
            this.start = false;
            if (length > 3) {
                String detectedBOM = detectCharsetFromBOM(buf, offset);                
                if (detectedBOM != null) {
                	this.detectedCharset = detectedBOM;
                    this.done = true;
                    return;
                }
            }
        }
        
        int maxPos = offset + length;
        for (int i=offset; i<maxPos; ++i) {
            int c = buf[i] & 0xFF;
            if ((c & 0x80) != 0 && c != 0xA0) {
                if (this.inputState != InputState.HIGHBYTE) {
                    this.inputState = InputState.HIGHBYTE;
                    
                    if (this.escCharsetProber != null) {
                        this.escCharsetProber = null;
                    }
                    
                    if (this.probers[0] == null) {
                        this.probers[0] = new MBCSGroupProber();
                    }
                    if (this.probers[1] == null) {
                        this.probers[1] = new SBCSGroupProber();
                    }
                    if (this.probers[2] == null) {
                        this.probers[2] = new Latin1Prober();
                    }
                }
            } else {
                if (this.inputState == InputState.PURE_ASCII &&
                    (c == 0x1B || (c == 0x7B && this.lastChar == 0x7E))) {
                    this.inputState = InputState.ESC_ASCII;
                }
                if (this.inputState == InputState.PURE_ASCII && onlyPrintableASCII) {
                	onlyPrintableASCII =
                			(c >= 0x20 && c <= 0x7e) // Printable characters 
                			|| c == 0x0A  // New Line
                			|| c == 0x0D  // Carriage return 
                			|| c== 0x09;  // TAB
                }
                this.lastChar = buf[i];
            }
        } // for end
        
        CharsetProber.ProbingState st;
        if (this.inputState == InputState.ESC_ASCII) {
            if (this.escCharsetProber == null) {
                this.escCharsetProber = new EscCharsetProber();
            }
            st = this.escCharsetProber.handleData(buf, offset, length);
            if (st == CharsetProber.ProbingState.FOUND_IT || 0.99f == this.escCharsetProber.getConfidence()) {
                this.done = true;
                this.detectedCharset = this.escCharsetProber.getCharSetName();
            }
        } else if (this.inputState == InputState.HIGHBYTE) {
            for (int i=0; i<this.probers.length; ++i) {
                st = this.probers[i].handleData(buf, offset, length);
                if (st == CharsetProber.ProbingState.FOUND_IT) {
                    this.done = true;
                    this.detectedCharset = this.probers[i].getCharSetName();
                    return;
                }
            }
        } else { // pure ascii
            // do nothing
        }
    }
    
    
	protected static String detectCharsetFromBOM(final byte[] buf, int offset) {
		if (buf.length > (offset + 3)) {
			int b1 = buf[offset] & 0xFF;
			int b2 = buf[offset+1] & 0xFF;
			int b3 = buf[offset+2] & 0xFF;
			int b4 = buf[offset+3] & 0xFF;
			
			switch (b1) {
			case 0xEF:
			    if (b2 == 0xBB && b3 == 0xBF) {
			        return CHARSET_UTF_8;
			    }
			    break;
			case 0xFE:
			    if (b2 == 0xFF && b3 == 0x00 && b4 == 0x00) {
			        return CHARSET_X_ISO_10646_UCS_4_3412;
			    } else if (b2 == 0xFF) {
			        return CHARSET_UTF_16BE;
			    }
			    break;
			case 0x00:
			    if (b2 == 0x00 && b3 == 0xFE && b4 == 0xFF) {
			        return CHARSET_UTF_32BE;
			    } else if (b2 == 0x00 && b3 == 0xFF && b4 == 0xFE) {
			        return CHARSET_X_ISO_10646_UCS_4_2143;
			    }
			    break;
			case 0xFF:
			    if (b2 == 0xFE && b3 == 0x00 && b4 == 0x00) {
			        return CHARSET_UTF_32LE;
			    } else if (b2 == 0xFE) {
			        return CHARSET_UTF_16LE;
			    }
			    break;
			default: 
				break;
			}
		}
		return null;
	}
    /**
     * Marks end of data reading. Finish calculations.
     */
	public void dataEnd() {
        if (!this.gotData) {
            return;
        }
        
        if (this.detectedCharset != null) {
            this.done = true;
            notifyListener(this.detectedCharset);
            return;
        }
        
        if (this.inputState == InputState.HIGHBYTE) {
            float proberConfidence;
            float maxProberConfidence = 0.0f;
            int maxProber = 0;
            
            for (int i=0; i<this.probers.length; ++i) {
                proberConfidence = this.probers[i].getConfidence();
                if (proberConfidence > maxProberConfidence) {
                    maxProberConfidence = proberConfidence;
                    maxProber = i;
                }
            }
            
            if (maxProberConfidence > MINIMUM_THRESHOLD) {
                this.detectedCharset = this.probers[maxProber].getCharSetName();
                notifyListener(this.detectedCharset);
            }
        } else if (this.inputState == InputState.ESC_ASCII) {
            // do nothing
        } else if (this.inputState == InputState.PURE_ASCII && this.onlyPrintableASCII) {
        	this.detectedCharset = CHARSET_US_ASCII;
        	notifyListener(this.detectedCharset);
        }
        else {
            // do nothing
        }
    }
    
    /**
     * Resets detector to be used again.
     */
	public final void reset() {
        this.done = false;
        this.start = true;
        this.detectedCharset = null;
        this.gotData = false;
        this.inputState = InputState.PURE_ASCII;
        this.lastChar = 0;
        
        if (this.escCharsetProber != null) {
            this.escCharsetProber.reset();
        }
        
        for (int i=0; i<this.probers.length; ++i) {
            if (this.probers[i] != null) {
                this.probers[i].reset();
            }
        }
    }
	
	private void notifyListener (String detectedCharset) {
		if (this.listener != null && detectedCharset != null && detectedCharset.trim().length() > 0) {
			this.listener.report(detectedCharset);
		}
	}
    
    /**
     * Gets the charset of a File.
     *
     * @param file The file to check charset for
     * @return The charset of the file, null if cannot be determined
     * @throws IOException if some IO error occurs
     */
    public static String detectCharset(File file) throws IOException {
        return detectCharset(file.toPath());
    }

    /**
     * Gets the charset of a Path.
     *
     * @param path The path to file to check charset for
     * @return The charset of the file, null if cannot be determined
     * @throws IOException if some IO error occurs
     */
    public static String detectCharset(Path path) throws IOException {
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(path))) {
            return detectCharset(fis);
        }
    }

    /**
     * Gets the charset of content from InputStream.
     *
     * @param inputStream InputStream containing text file
     * @return The charset of the file, null if cannot be determined
     * @throws IOException if some IO error occurs
     */
    public static String detectCharset(InputStream inputStream) throws IOException {
        byte[] buf = new byte[4096];

        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = inputStream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();
        return encoding;
    }

}
