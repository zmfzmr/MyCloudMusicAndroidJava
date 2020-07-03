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
}
