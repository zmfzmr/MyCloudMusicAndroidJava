package com.ixuea.courses.mymusic.fragment;

import android.content.Intent;

import com.ixuea.courses.mymusic.activity.BaseCommonActivity;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

import butterknife.ButterKnife;

public abstract class BaseCommonFragment extends BaseFragment {

    private PreferenceUtil sp;

    @Override
    protected void initViews() {
        super.initViews();

        //初始化注解找控件
        //绑定方法框架
        if (isBindView()) {
            bindView();
        }
    }

    /**
     * 绑定View
     */
    protected void bindView() {
        //Fragment中需要多传入一个参数 ， getView()：获取当前的View
        ButterKnife.bind(this, getView());
    }

    /**
     * 是否绑定View
     *
     * @return true
     * <p>
     * protected
     */
    protected boolean isBindView() {
        return true;
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //初始化偏好设置工具类
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
