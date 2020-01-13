package com.ixuea.courses.mymusic.util;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.StringRes;
import es.dmoral.toasty.Toasty;

/**
 * Toast工具类
 */
public class ToastUtil {

    /**
     * 上下文
     * 这里的话，是持有应用的引用，所以应该不会造成内存泄漏
     */
    @SuppressLint("StaticFieldLeak")//最好写上这句（教程没有的）
    private static Context context;

    /**
     * 初始化方法
     *
     * @param context Context
     */
    public static void init(Context context) {
        ToastUtil.context = context;
    }

    /**
     * 显示短时间错误toast
     * <p>
     * 校验参数
     *
     * @param id      @StringRe：必须是字符串的id
     */
//    public static void errorShortToast(@NonNull Context context, @StringRes int id) {
    public static void errorShortToast(@StringRes int id) {
        //必须要调用show方法
        Toasty.error(context, id, Toasty.LENGTH_SHORT).show();
    }

    /**
     * 显示短时间错误toast 字符串的形式  目前requestErrorHandler方法使用到
     *
     * @param message Message
     */
    public static void errorShortToast(String message) {
        //必须要调用show方法
        Toasty.error(context, message, Toasty.LENGTH_SHORT).show();
    }


    /**
     * 显示长时间错误toast
     * @param id      字符串id
     */
//    public static void errorLongToast(@NonNull Context context, @StringRes int id) {
    public static void errorLongToast(@StringRes int id) {
        Toasty.error(context, id, Toasty.LENGTH_SHORT).show();
    }

    //    public static void successLongToast(@NonNull Context context, @StringRes int id) {
    public static void successLongToast(@StringRes int id) {
        Toasty.success(context, id, Toasty.LENGTH_SHORT).show();
    }


}
