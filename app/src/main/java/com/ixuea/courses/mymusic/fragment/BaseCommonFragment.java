package com.ixuea.courses.mymusic.fragment;

import android.content.Intent;
import android.text.TextUtils;

import com.ixuea.courses.mymusic.activity.BaseCommonActivity;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ORMUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

import butterknife.ButterKnife;

public abstract class BaseCommonFragment extends BaseFragment {

    private PreferenceUtil sp;
    /**
     * 改为protected 这样子类才能调用
     * <p>
     * 数据库对象（数据库工具类）
     */
    protected ORMUtil orm;

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

        //初始化数据库对象(数据库工具类)
        orm = ORMUtil.getInstance(getMainActivity());
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

    /**
     * 启动界面，可以传递一个字符串参数
     *
     * @param clazz Class
     * @param id    String(字符串id)
     */
    protected void startActivityExtraId(Class<?> clazz, String id) {
        //创建Intent
        Intent intent = new Intent(getMainActivity(), clazz);

        //传递数据
        if (!TextUtils.isEmpty(id)) {
            //不为空才传递
            intent.putExtra(Constant.ID, id);//这个id是字符串类型
        }

        //启动界面
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
