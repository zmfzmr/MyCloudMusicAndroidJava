package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.fragment.DiscoveryFragment;
import com.ixuea.courses.mymusic.fragment.FeedFragment;
import com.ixuea.courses.mymusic.fragment.MeFragment;
import com.ixuea.courses.mymusic.fragment.VideoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 主界面ViewPager的Adapter
 * <p>
 * 这里传入Integer 并没有用到 ，在MainActivity中传入1 2 3 4 表示有4个界面
 * （因为BaseFragmentPagerAdapter中需要）返回当前的个数（这里返回4个）
 */
public class MainAdapter extends BaseFragmentPagerAdapter<Integer> {

    public MainAdapter(Context context, @NonNull FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 根据position
     * 返回当前Fragment
     *
     * @param position position
     * @return Fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MeFragment.newInstance();
            case 1:
                return DiscoveryFragment.newInstance();
            case 2:
                return FeedFragment.newInstance();
            default:
                return VideoFragment.newInstance();
        }
        //也可以用if
//        if (position == 0) {
//            return MeFragment.newInstance();
//        } else if (position == 1) {
//            return DiscoveryFragment.newInstance();
//        } else if (position == 2) {
//            return FeedFragment.newInstance();
//        } else {
//            return VideoFragment.newInstance();
//        }

    }
}
