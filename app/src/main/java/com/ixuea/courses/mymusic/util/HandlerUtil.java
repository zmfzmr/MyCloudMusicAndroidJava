package com.ixuea.courses.mymusic.util;

import android.os.Looper;

/**
 * Handler工具类
 */
public class HandlerUtil {
    /**
     * 是否是主线程
     *
     * @return true 就是主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
