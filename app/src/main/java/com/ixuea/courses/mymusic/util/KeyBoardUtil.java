package com.ixuea.courses.mymusic.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘相关工具类
 */
public class KeyBoardUtil {
    /**
     * 隐藏软件盘
     *
     * @param activity 注意：这里传入的是Activity 只有activity才有弹出键盘（才能获取当前焦点的token）,
     *                 activity.getCurrentFocus().getWindowToken()
     */
    public static void hideKeyBoard(Activity activity) {
        //获取输入法管理器
        //InputMethodManager:输入法管理器
        //INPUT_METHOD_SERVICE:输入法服务
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        //隐藏软键盘
        //参数2：是个flag标志：HIDE_NOT_ALWAYS
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
