package com.ixuea.courses.mymusic.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕工具类
 */
public class ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        //获取window管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //创建显示对象
        DisplayMetrics outDisplayMetrics = new DisplayMetrics();

        //获取默认显示对象  (理解为： WindowManager获取到的显示对象， 把值放在DisplayMetrics这个对象里面)
        wm.getDefaultDisplay().getMetrics(outDisplayMetrics);

        //返回屏幕宽度
        return outDisplayMetrics.widthPixels;
    }
}
