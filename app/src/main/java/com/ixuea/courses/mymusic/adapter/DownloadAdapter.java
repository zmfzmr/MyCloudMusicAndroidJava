package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.fragment.DownloadedFragment;
import com.ixuea.courses.mymusic.fragment.DownloadingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 下载界面适配器(ViewPager适配器)
 * <p>
 * BaseFragmentPagerAdapter:我们自己定义继承FragmentStatePagerAdapter： BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter
 * <p>
 * Integer：我们传入的是占位数据
 */
public class DownloadAdapter extends BaseFragmentPagerAdapter<Integer> {
    //顶部的指示器 标题文字
    private static final String[] titles = {"已下载", "下载中"};

    public DownloadAdapter(Context context, @NonNull FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 返回当前fragment
     * <p>
     * 因为数据中就2个数据，所以position的索引 只有 0 1
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return DownloadedFragment.newInstance();
        }
        //因为我们这里就是2个界面，这种写法是可以的(因为position 不是0 就是1)
        return DownloadingFragment.newInstance();
    }

    /**
     * 获取ViewPager 顶部 指示器的那个标题文字
     *
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
