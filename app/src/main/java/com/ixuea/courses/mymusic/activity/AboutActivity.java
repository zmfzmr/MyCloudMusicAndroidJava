package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我的云音乐(可视化布局
 */
public class AboutActivity extends BaseTitleActivity {

    private static final String TAG = "AboutActivity";
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * 版本点击
     */
    @OnClick(R.id.version_container)
    public void onVersionClick() {
        LogUtil.d(TAG, "onVersionClick");
    }

    /**
     * 版本点击
     */
    @OnClick(R.id.about_container)
    public void onAboutClick() {
        LogUtil.d(TAG, "onAboutClick");
    }

}
