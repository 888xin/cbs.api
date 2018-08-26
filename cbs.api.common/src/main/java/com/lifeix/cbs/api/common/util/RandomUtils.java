package com.lifeix.cbs.api.common.util;

import java.util.Random;

public class RandomUtils {

    private static String FOLDER_TABLE = "0123456789abcdef";

    /**
     * random folder name
     * 
     * @return 字符串
     */
    public static String randomFolderName() {
	Random random = new Random();
	StringBuffer retStr = new StringBuffer();
	int len = FOLDER_TABLE.length();
	for (int i = 0; i < 3; i++) {
	    int intR = random.nextInt(len);
	    retStr.append(FOLDER_TABLE.charAt(intR));
	}
	return retStr.toString();
    }

    /**
     * 创建指定数量的随机字符串
     * 
     * @param numberFlag
     *            是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
	String retStr = "";
	String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
	int len = strTable.length();
	boolean bDone = true;
	do {
	    retStr = "";
	    int count = 0;
	    for (int i = 0; i < length; i++) {
		double dblR = Math.random() * len;
		int intR = (int) Math.floor(dblR);
		char c = strTable.charAt(intR);
		if (('0' <= c) && (c <= '9')) {
		    count++;
		}
		retStr += strTable.charAt(intR);
	    }
	    if (count >= 2) {
		bDone = false;
	    }
	} while (bDone);

	return retStr;
    }

    final static long masx = 9013427188L;

    /**
     * 加密混淆数字
     * 
     * @param $id
     * @return
     */
    public static long numLongEncoder(Long $id) {
	$id ^= masx;
	String ids = String.valueOf($id);
	int length = ids.length();
	int midd = length / 2;
	ids = ids.substring(midd, length) + "8" + ids.substring(0, midd);
	return Long.valueOf(ids);
    }

    /**
     * 解密混淆数字
     * 
     * @param $id
     * @return
     */
    public static long numLongDecoder(Long $id) {
	String ids = String.valueOf($id);
	int length = ids.length();
	int midd = length / 2;
	ids = ids.substring(midd + 1, length) + ids.substring(0, midd);
	$id = Long.valueOf(ids);
	$id ^= masx;
	return $id;
    }

    /**
     * 加密混淆数字
     * 
     * @param $id
     * @return
     */
    public static long numEncoder(int $id) {
	$id ^= masx;
	String ids = String.valueOf($id);
	int length = ids.length();
	int midd = length / 2;
	ids = ids.substring(midd, length) + "8" + ids.substring(0, midd);
	return Long.valueOf(ids);
    }

    /**
     * 解密混淆数字
     * 
     * @param $id
     * @return
     */
    public static int numDecoder(Long $id) {
	String ids = String.valueOf($id);
	int length = ids.length();
	int midd = length / 2;
	ids = ids.substring(midd + 1, length) + ids.substring(0, midd);
	$id = Long.valueOf(ids);
	$id ^= masx;
	return $id.intValue();
    }

    public static void main(String[] args) {
	// System.out.println(createRandom(false, 16));
	// System.out.println(createRandom(false, 64));
	System.out.println(numLongDecoder(41L));
    }
}
