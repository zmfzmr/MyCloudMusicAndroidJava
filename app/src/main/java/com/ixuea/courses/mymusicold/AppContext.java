package com.ixuea.courses.mymusicold;

import android.app.Application;

import es.dmoral.toasty.Toasty;

/**
 * 全局Application
 */
public class AppContext extends Application {

    /**
     * 创建了 （第一次创建应用或者杀掉应用后就会执行一次这个方法）
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化toast工具类
        Toasty.Config.getInstance().apply();
    }
}
