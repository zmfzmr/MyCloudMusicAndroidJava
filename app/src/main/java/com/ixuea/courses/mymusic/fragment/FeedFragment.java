package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 首页-我的好友界面
 */
public class FeedFragment extends BaseCommonFragment {

    /**
     * 这个是主界面的那个MainAdapter的时候用到
     */
    public static FeedFragment newInstance() {
        return FeedFragment.newInstance(null);//这样的好处是，外面不需要传入参数null了
    }

    /**
     * 构造方法
     * 固定写法
     *
     * @return FriendFragment本身
     */
    public static FeedFragment newInstance(String userId) {

        Bundle args = new Bundle();
        //传递用户id
        args.putString(Constant.ID, userId);

        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局文件
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, null);
    }
}
