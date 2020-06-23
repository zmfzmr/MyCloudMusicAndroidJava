package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.fragment.ConfirmDialogFragment;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseTitleActivity {
    /**
     * 移动网络播放 开关
     */
    @BindView(R.id.st_play_mobile)
    Switch st_play_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取偏好设置里面的值(获取是否开或关的值)
        st_play_mobile.setChecked(sp.isMobilePay());
    }

    /**
     * 关于我的云音乐(可视化布局 点击
     */
    @OnClick(R.id.ll_about)
    public void onAboutClick() {
        startActivity(AboutActivity.class);
    }

    /**
     * 关于我的云音乐(代码布局) 点击
     */
    @OnClick(R.id.ll_about_code)
    public void onAboutCode() {
        startActivity(AboutCodeActivity.class);
    }

    /**
     * 移动网络播放开关状态更改了回调
     *
     * @param view      表示switch这个控件(这个控件继承自CompoundButton)
     * @param isChecked 注意：这个OnCheckedChanged这命名的用法(butterknife里面的)
     */
    @OnCheckedChanged(R.id.st_play_mobile)
    public void onPlayMobileCheckedChanged(CompoundButton view, boolean isChecked) {
        //将值设置到偏好设置
        sp.setMobile(isChecked);
    }

    /**
     * 退出按钮点击了
     */
    @OnClick(R.id.bt_logout)
    public void onLogoutClick() {

        ConfirmDialogFragment.show(getSupportFragmentManager(), (dialog, which) -> {
            //退出
            AppContext.getInstance().logout();
        });
    }
}
