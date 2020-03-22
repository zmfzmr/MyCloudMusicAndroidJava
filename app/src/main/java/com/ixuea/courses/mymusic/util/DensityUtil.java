package com.ixuea.courses.mymusic.util;

import android.content.Context;

/**
 * Android尺寸相关工具类
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从dip 的单位 装成为 px(像素)
     * 第二个参数是float类型
     */
    public static int dip2px(Context context, float data) {
        //获取手机的缩放
        //在1倍图上就为1 2倍就为2
        float scale = context.getResources().getDisplayMetrics().density;
        //0.5f:就是四舍五入的意思
        //比如：2倍图 * 缩放 （四舍五入）
        //简写：px = dp *缩放（四舍五入）
        return (int) (data * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素)的单位 转成 dp
     */
    public static int px2dip(Context context, float data) {
        //获取手机的缩放
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (data / scale + 0.5f);//注意：px 转成dp  这里是整除/
    }
}
