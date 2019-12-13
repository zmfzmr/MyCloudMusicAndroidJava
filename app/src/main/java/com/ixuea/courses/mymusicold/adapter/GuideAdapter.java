package com.ixuea.courses.mymusicold.adapter;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.fragment.GuideFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GuideAdapter extends FragmentPagerAdapter {

    public GuideAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * 当前的item（当前位置fragment）
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(R.drawable.guide1);
    }

    /**
     * 返回多少个
     * @return
     */
    @Override
    public int getCount() {
        return 10;
    }
}
