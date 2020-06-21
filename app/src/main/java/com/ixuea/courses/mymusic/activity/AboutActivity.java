package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PackageUtil;

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

    @Override
    protected void initDatum() {
        super.initDatum();
        //版本name : 比如： 2.0.0
        String versionName = PackageUtil.getViersionName(getMainActivity());
        //版本代码号：比如200
        long versionCode = PackageUtil.getVersionCode(getMainActivity());
        //设置版本号到控件上
        tv_version.setText(getResources().getString(R.string.version_value,
                versionName, versionCode));
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
