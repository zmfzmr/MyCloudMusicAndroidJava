package com.ixuea.courses.mymusic.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.annotation.NonNull;

/**
 * 启动界面
 */
public class SplashActivity extends BaseCommonActivity {

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
//        startActivityAfterFinishThis(GuideActivity.class);

        if (sp.isShowGuide()) {//第一次为true进入引导界面
            startActivityAfterFinishThis(GuideActivity.class);
        } else if (sp.isLogin()) {
            //已经登录了

            //就显示广告页面；在广告页面再进入主界面
            //可以根据自己的需求来更改
            //同时只有用户登录了
            //才显示也给用户有更好的体验
//            startActivityAfterFinishThis(MainActivity.class);
            startActivityAfterFinishThis(AdActivity.class);
        } else {
            //没有登录
            //跳转到登录注册界面
            startActivityAfterFinishThis(LoginOrRegisterActivity.class);
        }

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
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                handler.sendEmptyMessage(MSG_NEXT);
//            }
//        }, DEFAULT_DELAY_TIME);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //测试使用偏好设置
        //获取偏移设置对象
        SharedPreferences preferences = getSharedPreferences("ixuea", Context.MODE_PRIVATE);

        //保存一个字符串"我们是爱学啊"
        //存储的键为：username
        preferences.edit().putString("username","我们是爱学啊").commit();

        //通过键找到上面存储的值
        String username=preferences.getString("username",null);

        //打印出来，方便调试
        Log.d(TAG, "initDatum: "+"第一次获取的值："+username);

        //删除该key对应的值
        preferences.edit().remove("username").commit();

        //再次获取
        username=preferences.getString("username",null);

        Log.d(TAG, "initDatum: "+"删除后再次获取的值："+username);

        //测试productFlavors
        //获取ENDPOINT常量
        LogUtil.d(TAG, "initDatum:" + Constant.ENDPOINT);
    }
}
