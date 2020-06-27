package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

/**
 * 其他 搜索结果界面
 * 搜索结果界面其他Fragment
 * 只是占位的
 * 因为我们不实现其他的搜索
 */
public class OtherFragment extends BaseSearchFragment {
    /**
     * 创建方法
     */
    public static OtherFragment newInstance() {

        Bundle args = new Bundle();

        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
