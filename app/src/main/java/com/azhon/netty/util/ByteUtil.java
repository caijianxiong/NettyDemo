package com.azhon.netty.util;

/**
 * 文件名:    ByteUtil
 * 创建时间:  2018/10/24 on 21:50
 * 描述:     TODO
 *
 * @author 阿钟
 */

public class ByteUtil {
    /**
     * 多个数组合并一个
     *
     * @return
     */
    public static byte[] byteMergerAll(byte[]... bytes) {
        int allLength = 0;
        for (byte[] b : bytes) {
            allLength += b.length;
        }
        byte[] allByte = new byte[allLength];
        int countLength = 0;
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, allByte, countLength, b.length);
            countLength += b.length;
        }
        return allByte;
    }

    /**
     * 将数据数据恢复成0
     */
    public static void resetBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
    }

    /**
     * 计算校验和
     *
     * @param bytes
     * @param index 校验和结果所在的下标
     * @return 是否成功
     */
    public static boolean checkSum(byte[] bytes, int index) {
        if (index > bytes.length - 1) {
            return false;
        }
        byte right = bytes[index];
        int plus = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (index != i) {
                plus += bytes[i];
            }
        }
        return int2Byte(plus) == right;
    }


    /**
     * int 转byte字节
     *
     * @param value int数字
     * @return byte 字节
     */
    public static byte int2Byte(int value) {
        return (byte) value;
    }

    /**
     * byte 转int字节
     *
     * @param value int数字
     * @return byte 字节
     */
    public static int byte2Int(byte value) {
        return value & 0xFF;
    }

    /**
     * 两个十六进制字节转成一个int
     *
     * @return int
     */
    public static int bytes2Int(byte[] bytes) {
        int a = ((bytes[0] & 0xf0) >> 4) * 4096;
        int b = (bytes[0] & 0x0f) * 256;
        int c = bytes[1] & 0xf0;
        int d = bytes[1] & 0x0f;
        return a + b + c + d;
    }

    /**
     * 4字节byte转int
     *
     * @return
     */
    public static int fourBytes2Int(byte[] bytes) {
        return (bytes[0] & 0xff)
                | (bytes[1] & 0xff) << 8
                | (bytes[2] & 0xff) << 16
                | (bytes[3] & 0xff) << 24;
    }

    /**
     * byte字节转Bit
     * bit位（0～8位）是从右往左数的 eg:10000011 (位0：1，位2：1，位3：0)
     *
     * @param b        字节
     * @param bitIndex 获取bit位的下标
     * @return bit
     */
    public static byte byteToBit(byte b, int bitIndex) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        //倒序取
        return array[8 - 1 - bitIndex];
    }

    /**
     * long转DWORD数据类型
     * 低位到高位
     */
    public static byte[] longToDword(long value) {
        byte[] data = new byte[4];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (value >> (8 * (3 - i)));
        }
        return data;
    }


    /**
     * DWORD无符号整型数据转换为java的long型
     */
    public static long DwordToLong(byte buf[], int index) {
        int firstByte = (0x000000FF & ((int) buf[index]));
        int secondByte = (0x000000FF & ((int) buf[index + 1]));
        int thirdByte = (0x000000FF & ((int) buf[index + 2]));
        int fourthByte = (0x000000FF & ((int) buf[index + 3]));
        return ((long) (firstByte | secondByte << 8 | thirdByte << 16 |
                fourthByte << 24)) & 0xFFFFFFFFL;
    }

    /**
     * 一个int转4个字节的byte数组
     *
     * @param value
     * @return
     */
    public static byte[] intTo4Bytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) (value & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[3] = (byte) ((value >> 24) & 0xFF);
        return src;
    }

    public static byte[] intTo2Bytes(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 计算校验和
     *
     * @param bytes
     * @return 是否成功
     */
    public static byte getSum(byte[] bytes) {
        int plus = 0;
        for (int i = 0; i < bytes.length; i++) {
            plus += bytes[i];
        }
        return (byte) plus;
    }
}
