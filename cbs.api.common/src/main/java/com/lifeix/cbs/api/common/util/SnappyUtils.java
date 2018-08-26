/**
 * 
 */
package com.lifeix.cbs.api.common.util;

import org.xerial.snappy.Snappy;

/**
 * coppyright © lifeix 2014-12-12
 * 
 * Snappy 工具
 * 
 * @author jacky_LongZ
 * 
 */
public abstract class SnappyUtils {

    /**
     * 压缩字节数组
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {

	byte[] compressed = Snappy.compress(data);

	return compressed;
    }

    /**
     * 解压缩字节数组
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] deCompress(byte[] data) throws Exception {

	byte[] uncompressed = Snappy.uncompress(data);

	return uncompressed;
    }
}
