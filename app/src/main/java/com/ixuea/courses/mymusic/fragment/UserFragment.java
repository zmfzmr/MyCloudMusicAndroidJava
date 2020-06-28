package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 用户 搜索结果界面
 */
public class UserFragment extends BaseSearchFragment {

    private static final String TAG = "UserFragment";

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);
        LogUtil.d(TAG, "fetchData: " + data);
    }

    /**
     * 创建方法
     * @param position
     */
    public static UserFragment newInstance(int position) {

        Bundle args = new Bundle();
        //传递参数
        args.putInt(Constant.ID, position);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
