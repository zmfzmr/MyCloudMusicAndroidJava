package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected void initView() {//初始化控件

    }

    /**
     * 初始化数据
     */
    protected void initDatum() {

    }

    /**
     * 初始化监听器
     */
    protected void initListeners() {

    }

    /**
     * 在onCreate方法后面调用(因为要初始化控件，需要子类那边加载xml布局后，才能初始化控件)
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initView();
        initDatum();
        initListeners();
    }
}
