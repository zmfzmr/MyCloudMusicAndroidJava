package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.fragment.OtherFragment;
import com.ixuea.courses.mymusic.fragment.SheetFragment;
import com.ixuea.courses.mymusic.fragment.UserFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 搜索结果ViewPager适配器
 */
public class SearchResultAdapter extends BaseFragmentPagerAdapter<Integer> {
    /**
     * 标题数据
     */
    private CharSequence[] titls = {"歌单", "用户", "单曲", "视频", "歌手", "专辑", "主播电台"};

    public SearchResultAdapter(Context context, @NonNull FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 返回当前位置的fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            //歌单搜索结果
            return SheetFragment.newInstance(position);
        } else if (position == 1) {
            //用户搜索结果
            return UserFragment.newInstance(position);
        } else {
            //TODO 其他搜索结果 (这里没有传入position，因为这个fragment不实现)
            return OtherFragment.newInstance();
        }
    }

//    @Override
//    public int getCount() {
//        return 3;
//    }

    /**
     * 返回标题(ViewPager 和 TabLayout配合工作的时候，TabLayout会来 ViewPager的getPageTitle找标题)
     *
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titls[position];
    }
}
