package com.ixuea.courses.mymusicold.util;

import android.content.Context;

import com.ixuea.courses.mymusicold.activity.BaseCommonActivity;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import es.dmoral.toasty.Toasty;

/**
 * Toast工具类
 */
public class ToastUtil {

    /**
     * 显示短时间错误toast
     * <p>
     * 校验参数
     *
     * @param context Context @NonNull:不能为null
     * @param id      @StringRe：必须是字符串的id
     */
    public static void errorShortToast(@NonNull Context context, @StringRes int id) {
        //必须要调用show方法
        Toasty.error(context, id, Toasty.LENGTH_SHORT).show();
    }

    /**
     * 显示长时间错误toast
     *
     * @param context Context
     * @param id      字符串id
     */
    public static void errorLongToast(@NonNull Context context, @StringRes int id) {
        Toasty.error(context, id, Toasty.LENGTH_SHORT).show();
    }

    public static void successLongToast(@NonNull Context context, @StringRes int id) {
        Toasty.success(context, id, Toasty.LENGTH_SHORT).show();
    }
}
