package com.ixuea.courses.mymusic.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)//这里理解为这里面的测试：使用哪一个类去代理他（这里使用的是AndroidJUnit4）
public class DigestUtilTest {

    /**
     * 测试AES 128加密
     */
    @Test
    public void testEncryptAES() {
        //相等测试
        assertEquals(DigestUtil.encryptAES("ixueaedu"), "3gNwgHqyYLjPzO4xG8976w==");

        //不相等测试
        assertNotEquals(DigestUtil.encryptAES("ixueaedu"), "ixueaedu");
    }

    /**
     * 测试AES 128解码
     */
    @Test
    public void testDecryptAES() {
        //相等
        assertEquals(DigestUtil.decryptAES("3gNwgHqyYLjPzO4xG8976w=="), "ixueaedu");
        //不相等测试
        assertNotEquals(DigestUtil.decryptAES("ixueaedu"), "ixueaedu");
    }
}
