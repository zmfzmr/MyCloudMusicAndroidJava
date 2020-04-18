package com.ixuea.courses.mymusic.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 正则表达式工具类测试
 */
public class RegUtilTest {
    //测试字符串有1个mention(比如：@爱学啊)和2个hashTag
    private static final String str1 = "这是一条很长的评论，真很长，不信你看看，包含了#各种特性#，有话题，提醒人，例如：这是一个话题#电影教会你的人生道理#还有提醒人，例如：你好@爱学啊smile，评论完毕。";

    //测试字符串没有任何要高亮的信息
    private static final String str2 = "人生苦短，我们只做好课！";

    /**
     * 测试查找mentions
     */
    @Test
    public void testFindMentions() {
        //测试字符串有1个mention和2个hashTag(这个我们这里暂时不关心)
        assertEquals(RegUtil.findMentions(str1).size(), 1);

        //测试字符串没有任务号要高亮的信息
        assertEquals(RegUtil.findMentions(str2).size(), 0);
    }
}
