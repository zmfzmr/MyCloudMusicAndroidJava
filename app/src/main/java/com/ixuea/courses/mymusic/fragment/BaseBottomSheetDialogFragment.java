package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * 所有BottomSheetDialogFragment通用父类
 */
public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    /**
     * 找控件
     */
    protected void initViews() {

    }

    /**
     * 设置数据
     */
    protected void initDatum() {
    }

    /**
     * 绑定监听器
     */
    protected void initListeners() {
    }

    /**
     * 返回要显示的控件
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取view
        View view = getLayoutView(inflater, container, savedInstanceState);
        //初始化注解view框架
        //可以在创建一个Common界面
        //在写这些通用的逻辑
        //由于前面讲解了Common界面
        //所以这里为简单就直接写这里了

        //1：绑定的字段在fragment中，所以传入this
        //2：用哪个view绑定
        //总结：fragment中有2个参数（第二个为view（布局对象）），Activity那边只有一个参数

        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * View创建了
     * <p>
     * 类似Activity的onCreate方法
     * 在这里可以操作控件
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }
}
