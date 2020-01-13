package com.ixuea.courses.mymusic.fragment;

import android.content.Intent;

import com.ixuea.courses.mymusic.activity.BaseCommonActivity;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

public abstract class BaseCommonFragment extends BaseFragment {

    private PreferenceUtil sp;

    @Override
    protected void initViews() {
        super.initViews();
        sp = PreferenceUtil.getInstance(getMainActivity());
    }

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
        getActivity().finish();
    }


    /**
     * 获取界面方法
     * @return
     */
    protected BaseCommonActivity getMainActivity() {
        return (BaseCommonActivity) getActivity();
    }
}
