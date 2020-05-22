package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.PublishFeedActivity;
import com.ixuea.courses.mymusic.adapter.FeedAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.event.OnFeedChangedEvent;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.FeedListener;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页-我的好友界面
 */
public class FeedFragment extends BaseCommonFragment implements FeedListener {

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
        //注册发布订阅框架
        EventBus.getDefault().register(this);

        //获取用户id(这个用户id 目前只有从用户详情那边 过来才有值)
        userId = extraId();
        //设置适配器
        adapter = new FeedAdapter(R.layout.item_feed, this);
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

    /**
     * 动态图片点击了
     *
     * @param rv
     * @param images
     * @param index
     */
    @Override
    public void onImageClick(RecyclerView rv, List<String> images, int index) {
        LogUtil.d(TAG, "onImageClick:" + "," + images.size() + "," + index);

        //将List转为ArrayList
        //因为图片框架需要的是ArrayList
        //最好就是一步转换为ArrayList
        //避免浪费更多的资源
        ArrayList<String> imagesUri = Lists.newArrayList(images);

        //PhotoViewer框架是Kotlin写的
        //静态的方法要通过INSTANCE字段使用
        //注意：是这个PhotoViewer  不是PhotoView
        PhotoViewer.INSTANCE
                //设置图片数据
                .setData(imagesUri)
                //设置当前位置
                //设置当前点击的是哪个
                .setCurrentPage(index)

                //设置图片控件容器 (这个容器 是RecyclerView)
                //他需要容器的目的是
                //显示缩放动画
                .setImgContainer(rv)

                //意思说: 它不帮我们显示图片，需要我们自己取设置
                //uri: imagesUri 集合里面的 uri字符串
                .setShowImageViewInterface((imageView, uri) -> {
                    //使用Glide显示图片
                    Glide.with(getMainActivity())
                            // 注意：这个uri是相对路径 需要转换成绝对路径
                            .load(ResourceUtil.resourceUri(uri))
                            .into(imageView);
                })
                //启动界面
                .start(this);
    }

    /**
     * 发布动态按钮回调
     */
    @OnClick(R.id.fab)
    public void onPublishFeedClick() {
        LogUtil.d(TAG, "onPublishFeedClick");

        startActivity(PublishFeedActivity.class);
    }

    /**
     * 动态改了了事件 回调
     * <p>
     * 因为我们在PublishFeedActivity 那边中发布了动态，所以在本页面重新调用下全球 动态的方法，就会刷新出数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFeedChangedEvent(OnFeedChangedEvent event) {
        fetchData();
    }

    /**
     * 界面销毁时
     */
    @Override
    public void onDestroy() {
        //解除注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
