package com.ixuea.courses.mymusicold.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusicold.R;

/**
 * 引导界面Fragment
 * androidx:用这个包下的好处是，可以适配低版本
 */
public class GuideFragment extends Fragment {


    public GuideFragment() {

    }

    /**
     * 创建方法
     */
    public static GuideFragment newInstance() {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 返回要显示的View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

}
