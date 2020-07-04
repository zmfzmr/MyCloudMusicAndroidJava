package com.ixuea.courses.mymusic.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * 常用散列/哈希/加解密算法工具类 测试
 */
public class DigestUtilTests {
    /**
     * 测试sha1签名
     */
    @Test
    public void testSHA1() {
        //相等测试
        assertEquals(DigestUtil.sha1("ixueaedu"), "c31c3e896a92ba11382ab4ec92ece29ebfd38ecc");

        //不相等测试
        assertNotEquals(DigestUtil.sha1("ixueaedu"), "ixueaedu");

    }

    //注释的这部分移动android那边

//    /**
//     * 测试AES 128加密
//     */
//    @Test
//    public void testEncryptAES() {
//        //相等测试
//        assertEquals(DigestUtil.encryptAES("ixueaedu"),"3gNwgHqyYLjPzO4xG8976w==");
//
//        //不相等测试
//        assertNotEquals(DigestUtil.encryptAES("ixueaedu"),"ixueaedu");
//    }
//
//    /**
//     * 测试AES 128解码
//     */
//    @Test
//    public void testDecryptAES() {
//        //相等
//        assertEquals(DigestUtil.decryptAES("3gNwgHqyYLjPzO4xG8976w=="),"ixueaedu");
//        //不相等测试
//        assertNotEquals(DigestUtil.decryptAES("ixueaedu"),"ixueaedu");
//
//    }
}
