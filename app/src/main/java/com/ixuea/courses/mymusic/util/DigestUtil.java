package com.ixuea.courses.mymusic.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 常用散列/哈希/加解密算法工具类
 */
public class DigestUtil {
    /**
     * sha1 签名
     *
     * @param data
     * @return
     */
    public static String sha1(String data) {
        //在传入的字符串前后加入了一些值，简称加盐
        //前面和后面的是加盐

        //什么是加盐？
        //就是混入一些内容

        //加盐的好处是
        //如果攻击者知道了我们使用的签名算法
        //但他不知道盐
        //也就无法计算出相同的结果

        //这里的盐是随机生成的
        //真实项目中可以随便更改
        //但要和服务端协商好
        data = String.format(Constant.SIGN_FORMAT, data);

        //这种方法在Android中会找不到如下方法
        //encodeHexString([B)Ljava/lang/String;
        //return DigestUtils.sha1Hex(string);

        //使用这种方法
        //DigestUtils.sha1(data) :原始数据其实是二进制，一般都是转为16进制，或者Base64编码，方便查看和传输。

        //Hex.encodeHex(DigestUtils.sha1(data))： 将sha1整除16进制 这个是byte数组,整除字符串
        // DigestUtils： 注意：这个DigestUtils是common包中和我们之前写的那个多个s

        return new String(Hex.encodeHex(DigestUtils.sha1(data)));
    }
}
