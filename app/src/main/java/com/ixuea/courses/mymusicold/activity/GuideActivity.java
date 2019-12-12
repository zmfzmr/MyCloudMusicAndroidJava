package com.ixuea.courses.mymusicold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ixuea.courses.mymusicold.R;
/**
 * 引导界面
 */
public class GuideActivity extends AppCompatActivity {

    /**
     * 当前界面创建的时候
     * 一定要注意：
     * 要用onCreate的方法参数是Bundle的这个方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将activity_guide布局设置当前Activity的界面
        //很明显也设置为其他布局文件
        setContentView(R.layout.activity_guide);
    }
}
