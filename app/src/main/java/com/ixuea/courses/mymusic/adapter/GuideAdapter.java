package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.fragment.GuideFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * ViewPager 的适配器
 *
 * 引导界面适配器
 *
 */
public class GuideAdapter extends BaseFragmentPagerAdapter<Integer>{

//    protected final Context context;//这个可以不用写，因为父类里面已经继承了
    public GuideAdapter(Context context, @NonNull FragmentManager fm) {
        super(context,fm);
//        this.context = context;
    }

    /**
     * 当前的item（当前位置fragment）
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
//        return GuideFragment.newInstance(R.drawable.guide1);
        return GuideFragment.newInstance(getData(position));
    }
}
