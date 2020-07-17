package com.ixuea.courses.mymusic.util;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * 推送工具类
 */
public class PushUtil {
    /**
     * 获取推送注册id
     *
     * @param context
     * @return
     */
    public static String getPushId(Context context) {
        return JPushInterface.getRegistrationID(context);
    }
}
