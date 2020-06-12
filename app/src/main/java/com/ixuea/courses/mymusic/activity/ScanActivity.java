package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.SurfaceView;

import com.ixuea.courses.mymusic.R;
import com.king.zxing.ViewfinderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描二维码界面
 */
public class ScanActivity extends BaseTitleActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //显示亮色状态栏
        lightStatusBar();
    }

    /**
     * 我的二维码点击
     */
    @OnClick(R.id.bt_code)
    public void onCodeClick() {
        //这里跟首页抽屉布局里面跳转是一样的
        startActivityExtraId(CodeActivity.class, sp.getUserId());
    }
}
