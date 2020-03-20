package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 黑胶唱片列表适配器
 */
public class MusicPlayerAdapter extends BaseFragmentPagerAdapter<Song> {

    public MusicPlayerAdapter(Context context, @NonNull FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 返回当前位置fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        //回调getItem会根据集合的大小传递position的值，比如list集合为3，则position为：0 1 2
        //然后通过getData(position)拿到集合里面的具体每个值，从而返回fragment
        return MusicRecordFragment.newInstance(getData(position));
    }
}
