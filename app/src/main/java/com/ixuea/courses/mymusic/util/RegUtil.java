package com.ixuea.courses.mymusic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式相关方法
 */
public class RegUtil {
    /**
     * 匹配mention的正则表达式
     * 详细的请参考《详解正则表达式》课程
     *
     * @ 开头
     * \\u4e00-\\u9fa5: 这部分表示汉字  a-zA-Z0-9_-  : a到z A到Z 0到9 下划线 和杠
     * 而空格 句号 换行 这些不包括在内
     * <p>
     * ():可以理解为把匹配到的内容放到这个括号里面
     * 正则表达式可以有多个括号
     * <p>
     * {1,30}：表示匹配1到30个这样的内容，也就是它前面的这段（ [\u4e00-\u9fa5a-zA-Z0-9_-] ）1 到 30个
     * @ 是开头的，可以不算
     */
    private static final String REG_MENTION = "(@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,30})";

    /**
     * 查找mmentions: 翻译 ：提到谁
     * <p>
     * MatchResult：MatchResult 自定义的类
     *
     * @return
     */
    public static List<MatchResult> findMentions(String data) {
        List<MatchResult> results = new ArrayList<>();
        //编译正则表达式
        Pattern pattern = Pattern.compile(REG_MENTION);
        //通过正则表达式匹配内容
        Matcher matcher = pattern.matcher(data);

        //用while循环，如果有则继续查找匹配内容
        while (matcher.find()) {
            //如果还有匹配到的内容

            //将开始位置
            //结束位置
            //保存到一个类中

            //1:开始位置 2：结束位置 3：内容（matcher.group(0).trim()：第一次匹配到的内容）
            MatchResult matchResult = new MatchResult(matcher.start(), matcher.end(), matcher.group(0).trim());

            //添加到列表list
            results.add(matchResult);
        }

        //返回结果
        return results;
    }
}
