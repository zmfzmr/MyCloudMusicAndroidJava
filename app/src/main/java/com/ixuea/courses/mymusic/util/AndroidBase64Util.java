package com.ixuea.courses.mymusic.util;

import android.util.Base64;

/**
 * Android Base64工具类
 * 使用Android中自带的工具类实现的
 */
public class AndroidBase64Util {
    /**
     * 编码
     * return string类型
     * string2string : 命名意思： 传入String类型 返回String类型
     */
    public static String encodeString2string(String data) {
        return Base64.encodeToString(data.getBytes(), Base64.NO_WRAP);
    }

    /**
     * 编码
     *
     * @param data byte[]
     * @return byte[]
     */
    public static byte[] encodeByte2byte(byte[] data) {
        //Base64: Android.util 包下的   Base64.NO_WRAP： 用这个flag 这样才和java那边兼容
        return Base64.encode(data, Base64.NO_WRAP);
    }

    /**
     * 编码  传入byte[]  返回字符串
     */
    public static String encodeByte2String(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /**
     * 解码
     * return string类型
     */
    public static String decodeString2string(String data) {
//        //返回来的是个字符数组，所以把这个字节数组转换成一个字符串，只需要传入String的构造方法里面即可
//        return new String(decode(data.getBytes()));
        return new String(decodeByte2byte(data.getBytes()));
    }

    /**
     * 解码 传入字节数组byte[]    byte[]  返回 byte[]
     */
    public static byte[] decodeByte2byte(byte[] data) {

        return Base64.decode(data, Base64.NO_WRAP);
    }

    /**
     * 解码
     *
     * @param data 字符串   转换成 byte[]
     */
    public static byte[] decodeString2byte(String data) {
        return Base64.decode(data.getBytes(), Base64.NO_WRAP);
    }
}
