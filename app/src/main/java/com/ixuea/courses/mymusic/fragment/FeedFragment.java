package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.FeedAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 首页-我的好友界面
 */
public class FeedFragment extends BaseCommonFragment {

    private static final String TAG = "FeedFragment";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;


    private String userId;//用户id
    private FeedAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();
        //设置尺寸固定
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取用户id(这个用户id 目前只有从用户详情那边 过来才有值)
        userId = extraId();
        //设置适配器
        adapter = new FeedAdapter(R.layout.item_feed);
        rv.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        Api.getInstance()
                .feeds(userId)
                .subscribe(new HttpObserver<ListResponse<Feed>>() {
                    @Override
                    public void onSucceeded(ListResponse<Feed> data) {
                        LogUtil.d(TAG, "get feeds: " + data.getData().size());
                        adapter.replaceData(data.getData());
                    }
                });
    }

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
