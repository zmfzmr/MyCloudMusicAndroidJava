package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

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
     * @param userId
     */
    public static UserDetailSheetFragment newInstance(String userId) {

        Bundle args = new Bundle();
        //添加用户id
        args.putString(Constant.ID, userId);

        UserDetailSheetFragment fragment = new UserDetailSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
