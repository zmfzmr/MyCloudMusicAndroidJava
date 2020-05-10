package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;

/**
 * 用户详情-歌单界面
 */
public class UserDetailSheetFragment extends BaseCommonFragment {
    /**
     * 返回显示的布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_sheet, container, false);
    }

    /**
     * 创建方法
     */
    public static UserDetailSheetFragment newInstance() {

        Bundle args = new Bundle();

        UserDetailSheetFragment fragment = new UserDetailSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
