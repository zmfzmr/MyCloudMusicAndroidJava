package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ixuea.courses.mymusic.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * 迷你控制器 播放列表
 * <p>
 * 由于BottomSheetDialogFragment在现在这个项目中
 * 使用的比较少
 * 所以不用像BaseFragment那样封装
 * 如果使用的比较多
 * 可以进一步封装
 * <p>
 * BottomSheetDialogFragment：这个是需要和父布局配合使用的（外层是CoordinatorLayout配合使用）
 */
public class PlayListDialogFragment extends BottomSheetDialogFragment {


    /**
     * 返回要显示的控件
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list, null);
    }

    /**
     * View创建了
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 构造方法
     */
    public static PlayListDialogFragment newInstance() {

        Bundle args = new Bundle();

        PlayListDialogFragment fragment = new PlayListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager FragmentManager
     */
    public static void show(FragmentManager fragmentManager) {
        //创建fragment
        PlayListDialogFragment fragment = newInstance();

        //显示
        //TAG只是用来查找Fragment的
        //我们这里不需要查找
        //所以值可以随便写
        fragment.show(fragmentManager, "song_play_list_dialog");
    }
}
