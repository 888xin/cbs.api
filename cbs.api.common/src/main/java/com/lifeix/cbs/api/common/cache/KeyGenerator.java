package com.lifeix.cbs.api.common.cache;

import java.security.MessageDigest;

public class KeyGenerator {

	private static String[] PWD_CODE = { "!+!~", "&^#@", "*$(<", ">?%{" };
	
	public static String generateSimpleKey(String key){
		return key;
	}

	public static String generateKey(String key) {
		if (key == null || key.equals(""))
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(key);
		sb.append(PWD_CODE[0]);

		sb.append(key);
		sb.append(PWD_CODE[3]);

		String s1 = MD5Encode(sb.toString());

		sb.reverse();

		sb.append(PWD_CODE[1]);
		sb.append(key);

		sb.append(PWD_CODE[2]);
		sb.append(key);

		String s2 = MD5Encode(sb.toString());
		return s1.substring(0, 11) + s2.substring(11, 32);
	}

	/**
	 * MD5加密
	 * 
	 * @param origin
	 * @return
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes("UTF-8")));
		} catch (Exception ex) {

		}
		return resultString;
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
