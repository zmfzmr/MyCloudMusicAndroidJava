package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.fragment.DownloadedFragment;
import com.ixuea.courses.mymusic.fragment.DownloadingFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 下载界面适配器
 * <p>
 * BaseFragmentPagerAdapter:我们自己定义继承FragmentStatePagerAdapter： BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter
 * <p>
 * Integer：我们传入的是占位数据
 */
public class DownloadAdapter extends BaseFragmentPagerAdapter<Integer> {

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
}
