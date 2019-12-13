package com.ixuea.courses.mymusicold.adapter;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.fragment.GuideFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GuideAdapter extends FragmentPagerAdapter {

    protected List<Integer> datum = new ArrayList<>();

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
//        return GuideFragment.newInstance(R.drawable.guide1);
        return GuideFragment.newInstance(getData(position));
    }

    private int getData(int position) {//返回当前的数据
        return datum.get(position);
    }

    /**
     * 返回多少个
     * @return
     */
    @Override
    public int getCount() {
//        return 10;
        return datum.size();
    }

    /**
     * 设置数据
     * @param datum
     */
    public void setDatum(List<Integer> datum) {
        if (datum != null && datum.size() > 0) {
            this.datum.clear();
            this.datum.addAll(datum);
            notifyDataSetChanged();
        }
    }
}
