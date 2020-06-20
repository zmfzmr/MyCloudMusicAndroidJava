package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.fragment.ConfirmDialogFragment;

import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
