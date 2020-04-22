package com.ixuea.courses.mymusic.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文拼音相关工具方法
 */
public class PinyinUtil {
    /**
     * 全拼
     * 除中文以外字符保存不变
     */
    public static String pinyin(String value) {
        //创建拼音配置
        //拼音的大小还是小写 音调多音怎么来处理等，这个配置主要是来配置这个
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        //设置输出为小写
        //LOWERCASE:输出小写
        //UPPERCASE:输出大写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        //如何显示音调
        //WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常）
        //WITH_TONE_NUMBER：1-4数字表示音标
        //WITHOUT_TONE：没有音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        //特殊音标ü设置
        //WITH_V：用v表示ü
        //WITH_U_AND_COLON：用"u:"表示ü
        //WITH_U_UNICODE：直接用ü (注意：使用这个WITH_TONE_MARK:直接用音标符，必须设置WITH_U_UNICODE，否则会抛出异常)
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        //将输入的值转为字符数组
        char[] input = value.trim().toCharArray();
        //StringBuffer 是线程安全，我们这里没有用到多线程，用StringBuilder 反而性能更高
        StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < input.length; i++) {

                //转换成字符串 看是否是汉字（是否匹配汉字）
                //因为是字符串String类型才有matches 方法，所以需要转换成字符串String
                //   [\u4E00-\u9FA5]] : UniCode 的范围
                //[\u4E00-\u9FA5]+ : 中文字符 一次 或者 多次
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    //如果是汉字

                    //获取拼音
                    //如果是多音字
                    //也就是会返回多个拼音
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    //这里只取第一个[
                    String pinyin = temp[0];

                    sb.append(pinyin);

                } else {
                    //普通字母
                    //就直接转为字符串
                    sb.append(input[i]);
                }

            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        //转换成字符串 return
        return sb.toString();
    }
}
