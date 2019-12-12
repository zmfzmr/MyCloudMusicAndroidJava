package com.ixuea.courses.mymusicold.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.ixuea.courses.mymusicold.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    /**
     * 下一步常量
     */
    private static final int MSG_NEXT = 100;

    /**
     * 默认延时时间
     */
    private static final long DEFAULT_DELAY_TIME = 3000;

    /**
     * 创建Handle
     * 这样创建有内存泄漏
     * 在性能优化我们具体讲解
     */
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_NEXT:
                    next();
                    break;
            }
        }
    };

    /**
     * 下一步
     */
    private void next() {
        Log.d(TAG,"next");
        ////创建Intent
        //Intent intent = new Intent(this, GuideActivity.class);
        //
        ////启动界面
        //startActivity(intent);
        //
        ////关闭当前界面
        //finish();

        //使用重构后的方法
        startActivityAfterFinishThis(GuideActivity.class);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置全屏
        fullScreen();

        //延时3秒
        //在企业中通常会有很多逻辑处理
        //所以延时时间最好是用3-消耗的的时间
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_NEXT);
            }
        }, DEFAULT_DELAY_TIME);
    }

    /**
     * 全屏
     */
    protected void fullScreen() {
        //设置界面全屏

        //获取decorView
        View decorView = getWindow().getDecorView();

        //判断版本
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 19) {
            //11~18版本
            decorView.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //19及以上版本
            //SYSTEM_UI_FLAG_HIDE_NAVIGATION:隐藏导航栏
            //SYSTEM_UI_FLAG_IMMERSIVE_STICKY:从状态栏下拉会半透明悬浮显示一会儿状态栏和导航栏
            //SYSTEM_UI_FLAG_FULLSCREEN:全屏
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;

            //设置到控件
            decorView.setSystemUiVisibility(options);
        }
    }

    /**
     * 启动界面
     * @param clazz
     */
    private void startActivity(Class<?> clazz) {
        //创建Intent
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(clazz);
        //关闭当前界面
        finish();
    }
}
