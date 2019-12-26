package com.ixuea.courses.mymusicold.util;

import android.util.Log;

import com.ixuea.courses.mymusicold.BuildConfig;

/**
 * 日志工具类
 * 对Android日志API做一层简单的封装
 */
public class LogUtil {
    /**
     * 是否是调试状态
     *
     * studio工具左边Build Variants栏 为debug状态，
     * 那么这个BuildConfig.DEBUG为true，否则为false
     */
    private static boolean isDebug = BuildConfig.DEBUG;
    /**
     * 调试级别日志
     * @param tag TAG
     * @param value 值
     */
    public static void d(String tag, String value) {
        if (isDebug) {
            Log.d(tag, value);
        }
    }

    /**
     * 警告级别日志
     * @param tag TAG
     * @param value 值
     */
    public static void w(String tag, String value) {
        if (isDebug) {
            Log.w(tag, value);
        }
    }
}
