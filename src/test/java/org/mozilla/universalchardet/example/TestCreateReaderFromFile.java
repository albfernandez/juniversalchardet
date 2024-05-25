package org.mozilla.universalchardet.example;

import org.mozilla.universalchardet.ReaderFactory;

public class TestCreateReaderFromFile {
	
	public static void main (String[] args) throws java.io.IOException {
		if (args.length != 1) {
			System.err.println("Usage: java TestCreateReaderFromFile FILENAME");
			System.exit(1);
		}
	
		java.io.Reader reader = null;
		try {
			java.io.File file = new java.io.File(args[0]);
			reader = ReaderFactory.createBufferedReader(file);
			
			// Do whatever you want with the reader
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
		
	}

}
