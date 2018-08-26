/**
 * 
 */
package com.lifeix.cbs.api.common.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author jacky
 * 
 */
public class ZipBytesUtil {

    public static byte[] zipBytes(byte[] srcData) throws Exception {
	byte[] result = null;
	ByteArrayOutputStream bos = null;
	try {
	    Deflater deflater = new Deflater();
	    deflater.setInput(srcData);
	    deflater.finish();
	    bos = new ByteArrayOutputStream();
	    byte[] buf = new byte[1024 * 100];// The buffer size is 100KB.
	    while (!deflater.finished()) {
		int count = deflater.deflate(buf);
		bos.write(buf, 0, count);
	    }
	    result = bos.toByteArray();
	} finally {
	    if (bos != null)
		bos.close();
	}
	return result;
    }

    public static byte[] unzipBytes(byte[] srcData) throws Exception {
	byte[] result = null;
	ByteArrayOutputStream bos = null;
	try {
	    Inflater inflater = new Inflater();
	    inflater.setInput(srcData);
	    bos = new ByteArrayOutputStream();
	    byte[] buf = new byte[1024 * 100];// The buffer size is 100KB.
	    while (!inflater.finished()) {
		int count = inflater.inflate(buf);
		bos.write(buf, 0, count);
	    }
	    result = bos.toByteArray();
	} finally {
	    if (bos != null)
		bos.close();
	}
	return result;
    }

}
