package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 歌单搜索结果界面
 */
public class SheetFragment extends BaseSearchFragment {

    private static final String TAG = "SheetFragment";

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);

        LogUtil.d(TAG, "fetchData: " + data);
    }

    public static SheetFragment newInstance(int position) {

        Bundle args = new Bundle();
        //传递参数
        args.putInt(Constant.ID, position);
        SheetFragment fragment = new SheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
