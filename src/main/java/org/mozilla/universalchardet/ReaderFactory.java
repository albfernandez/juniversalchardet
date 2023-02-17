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

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Create a reader from a file with correct encoding
 */
public final class ReaderFactory {

	private ReaderFactory() {
		throw new AssertionError("No instances allowed");
	}

	/**
	 * Create a reader from a file with correct encoding
	 * @param file The file to read from
	 * @param defaultCharset defaultCharset to use if can't be determined
	 * @return BufferedReader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 */
	public static BufferedReader createBufferedReader(File file, Charset defaultCharset) throws IOException {
		Charset cs = Objects.requireNonNull(defaultCharset, "defaultCharset must be not null");
		String detectedEncoding = UniversalDetector.detectCharset(file);
		if (detectedEncoding != null) {
			cs = Charset.forName(detectedEncoding);
		}		
		if (!cs.name().contains("UTF")) {
			return Files.newBufferedReader(file.toPath(), cs);			
		}
		Path path = file.toPath();
		return new BufferedReader(new InputStreamReader(new UnicodeBOMInputStream(new BufferedInputStream(Files.newInputStream(path))), cs));
	}
	
	/**
	 * Create a reader from a file with correct encoding. If charset cannot be determined, 
	 * it uses the system default charset.
	 * @param file The file to read from
	 * @return BufferedReader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 */
	public static BufferedReader createBufferedReader(File file) throws IOException {
		return createBufferedReader(file, Charset.defaultCharset());
	}

	/**
	 * Create a reader from a file with correct encoding
	 * @param data The byte[] to read from
	 * @param defaultCharset defaultCharset to use if can't be determined
	 * @return BufferedReader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 */
	public static BufferedReader createBufferedReader(byte[] data, Charset defaultCharset) throws IOException {
		Charset cs = Objects.requireNonNull(defaultCharset, "defaultCharset must be not null");
		String detectedEncoding = null;
		try (InputStream is = new ByteArrayInputStream(data)) {
			detectedEncoding = UniversalDetector.detectCharset(is);
		}

		if (detectedEncoding != null) {
			cs = Charset.forName(detectedEncoding);
		}

		return new BufferedReader(new InputStreamReader(new UnicodeBOMInputStream(new ByteArrayInputStream(new String(data, cs).getBytes(cs)))));
	}
	
	/**
	 * Create a reader from a byte[] with correct encoding. If charset cannot be determined, 
	 * it uses the system default charset.
	 * @param data The byte[] to read from
	 * @return BufferedReader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 */
	public static BufferedReader createBufferedReader(byte[] data) throws IOException {
		return createBufferedReader(data, Charset.defaultCharset());
	}
	
	/**
	 * Create a reader from a file with the correct encoding
	 * @param file The file to read from
	 * @param defaultCharset defaultCharset to use if can't be determined
	 * @return Reader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 * @deprecated Use {@link #createBufferedReader(File, Charset)}
	 * 	 
	 */
	@Deprecated
	public static Reader createReaderFromFile(File file, Charset defaultCharset) throws IOException {
		return createBufferedReader(file, defaultCharset);
	}
	/**
	 * Create a reader from a file with the correct encoding. If charset cannot be determined, 
	 * it uses the system default charset.
	 * @param file The file to read from
	 * @return Reader for the file with the correct encoding
	 * @throws java.io.IOException if some I/O error occurs
	 * @deprecated Use {@link #createBufferedReader(File)}
	 */
	@Deprecated
	public static Reader createReaderFromFile(File file) throws IOException {
		return createReaderFromFile(file, Charset.defaultCharset());
	}
}
