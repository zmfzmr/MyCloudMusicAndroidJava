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
     * 匹配hashTag的正则表达式
     * ？表示禁用贪婪模式
     */
    private static final String REG_HASH_TAG = "(#.*?#)";

    /**
     * 查找mmentions: 翻译 ：提到谁
     * <p>
     * MatchResult：MatchResult 自定义的类
     *
     * @return
     */
    public static List<MatchResult> findMentions(String data) {
        //返回结果
        return find(REG_MENTION, data);
    }

    /**
     * 查找话题
     * #内容# ：就是HashTag，可以理解为引用一个话题。
     */
    /**
     * 查找话题
     *
     * @return 举例： .*?  (点:表示匹配任意单字符(\n除外)； *：表示匹配前面的0次或多次； ?:贪婪模式（意思就是匹配到一次，后面的不匹配了）)
     * <p>
     * .* 很简单，就是一个字符循环0次或者很多次
     * 多一个？在后面，意思是 懒惰循环
     * 举一个例子你就明白了
     * 对于字符串 aaaaaaa
     * .* 一下子很勤劳的把整个字符串都匹内配完了，结果是
     * aaaaaaa
     * .*? 很懒，发现匹配字符串中一个a后，就完成了匹配，就不用再容继续尝试下去了，因此就第一次只匹配
     * a，然后进行第二轮匹配了，还是只吃一个a就结束。因为他懒嘛
     * 所以最终结果匹配为： a
     */
    public static List<MatchResult> findHashTag(String data) {
        return find(REG_HASH_TAG, data);
    }

    /**
     * 查找话题(重构后的方法)
     *
     * @param reg  正则表达式
     * @param data 匹配的数据
     */
    public static List<MatchResult> find(String reg, String data) {
        //创建结果列表
        List<MatchResult> results = new ArrayList<>();
        //编译正则表达式
        Pattern pattern = Pattern.compile(reg);
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
