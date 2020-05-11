package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 用户详情-关于界面
 */
public class UserDetailAboutFragment extends BaseCommonFragment {
    /**
     * 返回显示的布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_about, container, false);
    }

    /**
     * 创建方法
     * @param userId
     */
    public static UserDetailAboutFragment newInstance(String userId) {

        Bundle args = new Bundle();
        //传递用户id
        args.putString(Constant.ID, userId);

        UserDetailAboutFragment fragment = new UserDetailAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
