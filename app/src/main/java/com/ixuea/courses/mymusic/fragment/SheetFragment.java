package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

/**
 * 歌单搜索结果界面
 */
public class SheetFragment extends BaseSearchFragment {

    public static SheetFragment newInstance() {

        Bundle args = new Bundle();

        SheetFragment fragment = new SheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
