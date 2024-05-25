# juniversalchardet


## What is it?

juniversalchardet is a Java port of "universalchardet",
that is the encoding detector library of Mozilla.

Mozilla repo is available at 
http://hg.mozilla.org/mozilla-central/file/tip/extensions/universalchardet/



## Encodings that can be detected

- Chinese
  - ISO-2022-CN
  - BIG-5
  - EUC-TW
  - HZ-GB-2312
  - GB-18030

- Cyrillic
  - ISO-8859-5
  - KOI8-R
  - WINDOWS-1251
  - MACCYRILLIC
  - IBM866
  - IBM855

- Greek
  - ISO-8859-7
  - WINDOWS-1253

- Hebrew
  - ISO-8859-8
  - WINDOWS-1255

- Japanese
  - ISO-2022-JP
  - Shift_JIS
  - EUC-JP

- Korean
  - ISO-2022-KR
  - EUC-KR

- Unicode
  - UTF-8
  - UTF-16BE / UTF-16LE
  - UTF-32BE / UTF-32LE / X-ISO-10646-UCS-4-3412 / X-ISO-10646-UCS-4-2143

- Others
  - WINDOWS-1252
  - US-ASCII

All supported encodings are listed in ``org.mozilla.universalchardet.Constants.``


## How to use it

(1) Construct an instance of ``org.mozilla.universalchardet.UniversalDetector``.

(2) Feed some data (typically some thousand bytes) to the detector
    using ``UniversalDetector.handleData()``.
    
(3) Notify the detector of the end of data by using
    ``UniversalDetector.dataEnd()``.
    
(4) Get the detected encoding name by using
    ``UniversalDetector.getDetectedCharset()``.
    
(5) Don't forget to call ``UniversalDetector.reset()`` before you reuse
    the detector instance for another guess.


------------ Sample Code ------------

```java
import org.mozilla.universalchardet.UniversalDetector;

public class TestDetector
{
  public static void main(String[] args)
  {
    byte[] buf = new byte[4096];
    java.io.InputStream fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get("test.txt"));

    // (1)
    UniversalDetector detector = new UniversalDetector();

    // (2)
    int nread;
    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
      detector.handleData(buf, 0, nread);
    }
    // (3)
    detector.dataEnd();

    // (4)
    String encoding = detector.getDetectedCharset();
    if (encoding != null) {
      System.out.println("Detected encoding = " + encoding);
    } else {
      System.out.println("No encoding detected.");
    }

    // (5)
    detector.reset();
  }
}
```

### Detecting encoding of a File (simple way)

```java

import org.mozilla.universalchardet.UniversalDetector;

public class TestDetectorFile {

	public static void main (String[] args) throws java.io.IOException {
		if (args.length != 1) {
			System.err.println("Usage: java TestDetectorFile FILENAME");
			System.exit(1);
		}
		java.io.File file = new java.io.File(args[0]);
		String encoding = UniversalDetector.detectCharset(file);
		if (encoding != null) {
			System.out.println("Detected encoding = " + encoding);
		} else {
			System.out.println("No encoding detected.");
		}
	}
}

```

### Creating a reader with correct encoding


```java

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

```


## Getting with maven

Put this dependency in your pom.xml

```xml
<dependency>
	<groupId>com.github.albfernandez</groupId>
	<artifactId>juniversalchardet</artifactId>
	<version>2.0.4-SNAPSHOT</version>
</dependency>

```

## Getting with gradle

Put this line in your build.gradle

```groovy
implementation 'com.github.albfernandez:juniversalchardet:2.0.4-SNAPSHOT'
```

## Building from sources

```bash
    git clone https://github.com/albfernandez/juniversalchardet.git
    cd juniversalchardet
    mvn clean package
```

## Related Works

- jchardet  http://jchardet.sourceforge.net/

jchardet is another Java port of the Mozilla's encoding dectection library.
The main difference between jchardet and juniversalchardet is modules
they are based on. jchardet is based on the "chardet" module that has
long existed. juniversalchardet is based on the "universalchardet" module
that is new and generally provides better accuracy on detection results.

- juniversalchardet  https://code.google.com/archive/p/juniversalchardet/

The original repository of this project

## License

The library is subject to the Mozilla Public License Version 1.1.

Alternatively, the library may be used under the terms of either
the GNU General Public License Version 2 or later, or the GNU
Lesser General Public License 2.1 or later.

## Compatibility

juniversalchardet requires JDK 7 or higher.

For Android:

- juniversalchardet 2.0.x requieres Android 4 (API Level 14)
- juniversalchardet 2.1.x and newer requires Android 8 (API Level 26)
