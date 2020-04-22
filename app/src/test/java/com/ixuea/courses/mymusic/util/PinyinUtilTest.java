package com.ixuea.courses.mymusic.util;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

//import static org.junit.Assert.assertEquals;


/**
 * 拼音相关工具类测试
 */
public class PinyinUtilTest {
    /**
     * 测试全拼
     */
    @Test
    public void textPinyin() {
        /**
         * 前面导入的是 Assert.assertEquals;
         * 而这类导入的是TestCase.assertEquals
         * 目前导入这2个都是可以的
         *
         * assertEquals：标志这2个值相等（期待(爱学啊)的拼音是： aixuea）
         *
         */
        assertEquals(PinyinUtil.pinyin("爱学啊"), "aixuea");
    }

    /**
     * 测试 首字母
     */
    @Test
    public void testPinyinFirst() {
        //PinyinUtil.pinyinFirst:因为这里思路是：爱学啊-->字符数组遍历-->是中文(ai xue  a)分别截取首字母：axa
        assertEquals(PinyinUtil.pinyinFirst("爱学啊"), "axa");
    }
}
