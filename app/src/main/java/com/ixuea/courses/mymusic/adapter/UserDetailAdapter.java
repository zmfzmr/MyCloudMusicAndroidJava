package com.ixuea.courses.mymusic.adapter;

import android.content.Context;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.fragment.FeedFragment;
import com.ixuea.courses.mymusic.fragment.UserDetailAboutFragment;
import com.ixuea.courses.mymusic.fragment.UserDetailSheetFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 用户详情界面适配器(ViewPager适配器)
 */
//里面上String字符串没有Integer的效率高，我们这里用Integer就行了
public class UserDetailAdapter extends BaseFragmentPagerAdapter<Integer> {
    /**
     * 标题字符串id
     */
    private static final int[] titleIds = {
            R.string.music,
            R.string.feed,
            R.string.about_ta};

    private final String userId;//用户id

    /**
     * @param context 上下文
     * @param fm      FragmentManager
     * @param userId  用户id
     */
    public UserDetailAdapter(Context context, @NonNull FragmentManager fm, String userId) {
        super(context, fm);
        this.userId = userId;
    }

    /**
     * 返回fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserDetailSheetFragment.newInstance(userId);
            case 1:
                return FeedFragment.newInstance(userId);
            default:
                return UserDetailAboutFragment.newInstance(userId);
        }
    }

    /**
     * 返回标题
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //获取字符串id (注意：这里只是获取到了一个字符串id，下面需要根据这个id，获取string.xml里面的文本)
        int resourceId = titleIds[position];
        //获取字符串(通过id获取)
        return context.getResources().getString(resourceId);
    }
}
