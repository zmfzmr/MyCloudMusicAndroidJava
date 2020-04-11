package com.ixuea.courses.mymusic.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * 尺寸相关工具类
 */
public class SizeUtil {
    /**
     * 状态栏高度
     */
    private static int statusBarHeight;

    /**
     * 获取状态栏高度
     * <p>
     * 这里用到反射(反射是有性能损耗的，有兼容性问题)
     * //反射是有这个兼容性问题，项目中还是减少这个反射的使用
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight == 0) {
            try {
                //获取某一个类的Class
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                //创建一个对象
                Object o = c.newInstance();
                //获取状态栏这个字段
                Field field = c.getField("status_bar_height");
                //获取状态栏这个字段的值
                int height = (int) field.get(o);//这个字段在那个对象上获取(就是通过反射创建出来的那个对象)
                //将值转成px
                //height：可以理解为是个dp的单位，现在转换成px
                //当前也可以用前面用到的DensityUtil来进行dp转换成px
                statusBarHeight = context.getResources().getDimensionPixelSize(height);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
