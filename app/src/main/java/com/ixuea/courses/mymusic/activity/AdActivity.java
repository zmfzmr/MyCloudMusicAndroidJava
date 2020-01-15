package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;

/**
 * 广告界面
 */
public class AdActivity extends BaseCommonActivity {

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
}
