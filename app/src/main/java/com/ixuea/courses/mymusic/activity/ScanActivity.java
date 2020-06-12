package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描二维码界面
 */
public class ScanActivity extends BaseTitleActivity implements OnCaptureCallback {
    private static final String TAG = "ScanActivity";
    /**
     * 扫描预览视图
     */
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    /**
     * 扫描框
     */
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    private CaptureHelper captureHelper;//扫描工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void initView() {
        super.initView();
        //显示亮色状态栏
        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建扫描工具类(里面是怎么扫描 预览的我们不用担心，只需要借助封装工具类即可)
        captureHelper = new CaptureHelper(this, surfaceView, viewfinderView);

        //社会组扫描结果回调
        captureHelper.setOnCaptureCallback(this);

        //设置支持连续扫描(如果数据格式不正确，就显示不支持，继续扫描)
        captureHelper.continuousScan(true);

        //执行创建
        captureHelper.onCreate();

    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        //开始扫描
        captureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停扫描
        captureHelper.onPause();
    }

    /**
     * 这个这扫描(也就是拍照的这个功能)，在系统中是唯一的，
     * 如果不销毁的话，系统的那个手机拍照是无法运行的
     */
    @Override
    protected void onDestroy() {
        //销毁扫描工具类
        captureHelper.onDestroy();
        super.onDestroy();
    }

    /**
     * 我们也没有仔细了解过，推测是点击对焦功能(就是点击扫描框的里面 对焦图片)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件发送到扫描工具类
        captureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 我的二维码点击
     */
    @OnClick(R.id.bt_code)
    public void onCodeClick() {
        //这里跟首页抽屉布局里面跳转是一样的
        startActivityExtraId(CodeActivity.class, sp.getUserId());
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onResultCallback(String result) {
        LogUtil.d(TAG, "onResultCallback: " + result);
        //拦截结果
        return true;
    }
}
