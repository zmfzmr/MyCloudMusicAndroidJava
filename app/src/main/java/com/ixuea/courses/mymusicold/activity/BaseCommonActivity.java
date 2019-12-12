package com.ixuea.courses.mymusicold.activity;

import android.content.Intent;

/**
 * 通用界面逻辑
 */
public class BaseCommonActivity extends BaseActivity {

    /**
     * 启动界面
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        //创建Intent
        Intent intent = new Intent(getMainActivity(), clazz);
        startActivity(intent);
    }

    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(clazz);
        //关闭当前界面
        finish();
    }

    protected BaseCommonActivity getMainActivity() {
        return this;
    }

}
