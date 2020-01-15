package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.Button;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告界面
 */
public class AdActivity extends BaseCommonActivity {

    private static final String TAG = "AdActivity";

    /**
     * 跳过广告按钮
     */
    @BindView(R.id.bt_skip_ad)
    Button bt_skip_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
    }

    @Override
    protected void initView() {
        super.initView();

        //全屏
        fullScreen();
    }

    /**
     * 广告点击了
     */
    @OnClick(R.id.bt_ad)
    public void onAdClick() {
        LogUtil.d(TAG, "onAdClick");
    }

    /**
     * 跳过广告按钮
     */
    @OnClick(R.id.bt_skip_ad)
    public void onSkipClick() {
        LogUtil.d(TAG, "onSkipClick");
    }
}
