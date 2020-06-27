package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

/**
 * 用户 搜索结果界面
 */
public class UserFragment extends BaseSearchFragment {
    /**
     * 创建方法
     */
    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
