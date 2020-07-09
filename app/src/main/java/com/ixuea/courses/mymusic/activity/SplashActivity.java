package com.ixuea.courses.mymusic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.annotation.NonNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 启动界面
 */
//这个框架就会该页面生成一些动态处理权限的方法
//声明当前界面有运行时权限
@RuntimePermissions
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
    private Handler handler = new Handler() {

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
        Log.d(TAG, "next");
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

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //检查权限
        checkAppPermission();

        //测试使用偏好设置
        //获取偏移设置对象
        SharedPreferences preferences = getSharedPreferences("ixuea", Context.MODE_PRIVATE);

        //保存一个字符串"我们是爱学啊"
        //存储的键为：username
        preferences.edit().putString("username", "我们是爱学啊").commit();

        //通过键找到上面存储的值
        String username = preferences.getString("username", null);

        //打印出来，方便调试
        Log.d(TAG, "initDatum: " + "第一次获取的值：" + username);

        //删除该key对应的值
        preferences.edit().remove("username").commit();

        //再次获取
        username = preferences.getString("username", null);

        Log.d(TAG, "initDatum: " + "删除后再次获取的值：" + username);

        //测试productFlavors
        //获取ENDPOINT常量
        LogUtil.d(TAG, "initDatum:" + Constant.ENDPOINT);
    }

    /**
     * 检查是否有需要的权限
     * <p>
     * 只有部分权限才需要动态授权
     * 例如：网络权限就不需要动态授权，但相机就需要动态授权
     * <p>
     * Google推荐在用到权限的时候才请求用户
     * 但真实项目中，如果在每个用到的位置请求权限可能比较麻烦
     * 例如：项目中有多个位置都用到了相机
     * <p>
     * 所以说大部分项目，像淘宝，京东等软件
     * 是在启动页请求项目所有需要的权限
     * <p>
     * 但如果大家的项目中有足够的时间
     * 肯定还是推荐在用到的时候再请求权限
     */
    public void checkAppPermission() {
        //让动态框架检查是否授权了

        //如果不使用框架就使用系统提供的API检查
        //它内部也是使用系统API检查
        //只是使用框架就更简单了

        //SplashActivityPermissionsDispatcher:这个是添加了@NeedsPermission和@RuntimePermissions后自动生成的
        //其实前部分：SplashActivity：本类名字  后面部分是:PermissionsDispatcher统一名字
        SplashActivityPermissionsDispatcher.onPermissionGrantedWithPermissionCheck(this);

    }

    /**
     * 权限授权了就会调用该方法
     * 请求相机权限目的是扫描二维码，拍照
     */
    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    })
    void onPermissionGranted() {
        LogUtil.d(TAG, "onPermissionGranted");
        //如果有权限就进入下一步

        //如果是在使用的时候请求权限
        //那这里就可以显示相机预览
        prepareNext();
    }

    /**
     * 显示权限授权对话框
     * 目的是提示用户   Manifest.permission.ACCESS_COARSE_LOCATION,
     *             Manifest.permission.ACCESS_FINE_LOCATION
     *
     * @param request
     */
    @OnShowRationale({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    })
    //PermissionRequest:参数中要写这个，统一写法
    void showRequestPermission(PermissionRequest request) {
        LogUtil.d(TAG, "showRequestPermission");

        new AlertDialog.Builder(this)
                .setMessage(R.string.permisson_hint)
                //DialogInterface里面的OnClickListener
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这个是参数里面的request
                        request.proceed();//调用这个方法说明允许我们的权限请求
                    }
                })
                .setNegativeButton(R.string.deny, (dialog, which) -> request.cancel())
                .show();//记得显示
    }

    /**
     * 拒绝了权限调用
     */
    @OnPermissionDenied({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    })
    void showDenied() {
        LogUtil.d(TAG, "showDenied");

        //退出应用
        finish();
    }

    /**
     * 再次获取权限的提示
     * OnNeverAskAgain 翻译：从未 请求  再次请求
     * <p>
     * 注意：这个方法目前好像并没有执行
     */
    @OnNeverAskAgain({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    })
    void showNeverAsk() {
        LogUtil.d(TAG, "showNeverAsk");

        //继续请求权限
        checkAppPermission();
    }

    /**
     * 授权后回调
     * 系统提供的方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //将授权结果传递到框架
        //注意：这里调用得是onRequestPermissionsResult 而不是onPermissionGrantedWithPermissionCheck
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 准备下一步
     */
    private void prepareNext() {
        if (LogUtil.isDebug) {
            next();
        } else {
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
    }

//    /**
//     * 页面显示了
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //使用极光分析
//        //统计页面
//        //参数2： 取的名称
//        JAnalyticsInterface.onPageStart(getMainActivity(),"Splash");
//    }
//
//    /**
//     * 页面暂停了
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        //页面结束了
//        //参数2： 取的名称
//        JAnalyticsInterface.onPageEnd(getMainActivity(),"Splash");
//    }


    @Override
    protected String pageId() {
        return "Splash";
    }
}
