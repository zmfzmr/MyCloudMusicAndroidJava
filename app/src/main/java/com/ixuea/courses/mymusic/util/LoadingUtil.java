package com.ixuea.courses.mymusic.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.ixuea.courses.mymusic.R;

public class LoadingUtil {
    private static ProgressDialog progressDialog;

    /**
     * 使用一个加载对话框，使用默认提示文字
     *
     * @param activity Activity
     */
    public static void showLoading(Activity activity) {
        showLoading(activity, activity.getString(R.string.loading));
    }

    /**
     * 显示一个加载对话框（可以输入任何的message）
     *
     * @param activity Activity
     * @param message  Message
     */
    public static void showLoading(Activity activity, String message) {

        //判断activity为空或者已经销毁了
        if (activity == null || activity.isFinishing()) {
            return;
        }

        //判断是否显示了
//        if (progressDialog != null) {
//            //已经显示了 不需要再次显示
//            //就不再显示了
//            return;
//        }


        //更改
        //判断是否显示了
        if (progressDialog == null) {
            //创建一个进度对话框
            progressDialog = new ProgressDialog(activity);
        }

        progressDialog.setTitle("提示");//提示标题
        progressDialog.setMessage(message);//提示信息
        //点击外部弹窗不会自动隐藏
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 隐藏加载提示对话框
     */
    public static void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog = null;
        }
    }
}
