package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;

/**
 * 用户二维码界面
 */
public class CodeActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
    }

    @Override
    protected void initView() {
        super.initView();

        //显示亮色状态栏
        lightStatusBar();
    }
}
