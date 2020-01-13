package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 引导界面Fragment
 * androidx:用这个包下的好处是，可以适配低版本
 */
public class GuideFragment extends BaseCommonFragment {

    private ImageView iv;
    private int id;

    public GuideFragment() {

    }

    /**
     * 创建方法
     */
    public static GuideFragment newInstance(int id) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.ID,id);
        //将bundle
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    /**
     * View创建完毕
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void initViews() {
        super.initViews();
        iv = findViewById(R.id.iv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //取出传递的数据
        id = getArguments().getInt(Constant.ID, -1);//图片资源id
        if (id == -1) {
            //如果图片不存在
            //就关闭当前界面
            //但在我们这里不会发生该情况
            getActivity().finish();
            return;
        }
        iv.setImageResource(id);

    }
}
