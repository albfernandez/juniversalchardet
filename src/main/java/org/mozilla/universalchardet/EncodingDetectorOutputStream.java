/*

(C) Copyright 2016-2017 Alberto Fernández <infjaf@gmail.com>

The contents of this file are subject to the Mozilla Public License Version
1.1 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the
License.

Alternatively, the contents of this file may be used under the terms of
either the GNU General Public License Version 2 or later (the "GPL"), or
the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
in which case the provisions of the GPL or the LGPL are applicable instead
of those above. If you wish to allow use of your version of this file only
under the terms of either the GPL or the LGPL, and not to allow others to
use your version of this file under the terms of the MPL, indicate your
decision by deleting the provisions above and replace them with the notice
and other provisions required by the GPL or the LGPL. If you do not delete
the provisions above, a recipient may use your version of this file under
the terms of any one of the MPL, the GPL or the LGPL.

*/

package org.mozilla.universalchardet;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Stream that detect encoding while reading.
 * The normal usage is to fully read from inputstream and call close before check for charset.
 *
 */
public class EncodingDetectorOutputStream extends OutputStream {
	
	private OutputStream out;
	private final UniversalDetector detector = new UniversalDetector(null);

	/**
	 * Create stream
	 * @param out OutputStream
	 */
	public EncodingDetectorOutputStream(OutputStream out) {
		super();
		this.out = out;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		out.close();
		detector.dataEnd();
	}

	/**
	 * {@inheritDoc}
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		if (!detector.isDone()) {
			detector.handleData(b, off, len);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void write(byte[] b) throws IOException {
		this.write(b, 0, b.length);
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(int b) throws IOException {
		this.write(new byte[] { (byte) b });
	}

	/**
	 * Gets the detected charset, null if not yet detected.
	 * @return The detected charset
	 */
	public String getDetectedCharset() {
		return detector.getDetectedCharset();
	}
}
