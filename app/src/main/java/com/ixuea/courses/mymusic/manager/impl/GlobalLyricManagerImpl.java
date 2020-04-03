package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.ixuea.courses.mymusic.manager.GlobalLyricManager;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.view.GlobalLyricView;

/**
 * 全局(桌面)歌词管理器实现
 */
public class GlobalLyricManagerImpl implements GlobalLyricManager {

    private static final String TAG = "GlobalLyricManagerImpl";
    private static GlobalLyricManagerImpl instance;//实例
    private final Context context;//上下文
    private WindowManager windowManager;//窗口管理器
    private WindowManager.LayoutParams layoutParams;//窗口的布局样式
    private GlobalLyricView globalLyricView;//全局歌词View

    /**
     * 构造方法
     *
     * @param context
     */
    public GlobalLyricManagerImpl(Context context) {
        this.context = context.getApplicationContext();//记得写这个，否则会有内存泄漏
        //初始化窗口管理器
        initWindowManager();

        //创建全局歌词View
//        initGlobalLyricView();
    }

    /**
     * 创建全局歌词View
     */
    private void initGlobalLyricView() {
//        //创建一个测试文本(传入Context对象)
//        TextView tv = new TextView(context);
//        tv.setText("这是一个简单的文本");
//        if (tv.getParent() == null) {//tv.getParent():view的父容器（父view）
//            //如果没有添加
//            //就添加
//            windowManager.addView(tv, layoutParams);
//        }

        if (globalLyricView == null) {
            //创建全局歌词控件
            globalLyricView = new GlobalLyricView(context);
        }

        if (globalLyricView.getParent() == null) {
            //如果没有添加
            //就添加
            windowManager.addView(globalLyricView, layoutParams);
        }

    }

    /**
     * 初始化窗口管理器
     * <p>
     * 因为全局View是通过他显示的
     * <p>
     * 可以在构造方法中初始化
     * <p>
     * 也可以延迟到要显示的时候才初始化
     * 这样效果更高
     */
    private void initWindowManager() {
        if (windowManager == null) {
            //获取系统窗口管理器
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            //窗体的布局样式（显示一个窗体 告诉系统，我的位置是什么 宽高是什么）
            layoutParams = new WindowManager.LayoutParams();

            //设置窗体显示类型
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //6.0及以上版本要设置该类型
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            //设置显示的模式
            layoutParams.format = PixelFormat.RGBA_8888;

            //设置对齐的方法
            //CENTER_HORIZONTAL:这里是水平居中（水平居中其实可以不用设置，因为宽高（宽高 下面代码会设置）都是和父窗体一样）
            //Gravity.TOP:这是是位于顶部
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

            //设置窗体宽度和高度
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);//这里传入dm，会这个值 保存到dm(DisplayMetrics对象)里面

            //和屏幕一样宽
            layoutParams.width = dm.widthPixels;
            //高度是包裹内容(WindowManager:大写)
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            //从偏好设置中获取坐标
            //如果在真实项目中
            //可能还会实现不同的用户有不同的偏好设置

            //目前是写死的
            layoutParams.y = 100;

        }


    }

    /**
     * 获取全局歌词管理器
     * <p>
     * 这种单例 在移动端是适用的，但是想java web 那种高并发就是另一种写法
     * 因为移动端 用不到这种高并发，写的代码越大 就越慢
     */

    public static GlobalLyricManagerImpl getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalLyricManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void show() {
        LogUtil.d(TAG, "show");

        //初始化全局全局歌词控件
        initGlobalLyricView();
    }

    @Override
    public void hide() {
        //移除全局歌词控件
        windowManager.removeView(globalLyricView);
        globalLyricView = null;//记得显示为null，下次显示的时候 就会重新创建这个globalLyricView对象了

    }

    @Override
    public boolean isShowing() {
        //这个控件globalLyricView不为null 说明显示了（说明 窗口管理器已经添加view了：windowManager.addView(globalLyricView, layoutParams);）
        return globalLyricView != null;
    }

    @Override
    public void tryHide() {

    }

    @Override
    public void tryShow() {

    }
}
