package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.VideoAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 首页-视频界面
 */
public class VideoFragment extends BaseCommonFragment {

    private static final String TAG = "VideoFragment";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private VideoAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();

        //尺寸固定
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new VideoAdapter(R.layout.item_video);
        //设置适配器
        rv.setAdapter(adapter);

        Api.getInstance()
                .videos()
                .subscribe(new HttpObserver<ListResponse<Video>>() {
                    @Override
                    public void onSucceeded(ListResponse<Video> data) {
                        if (data.getData() != null && data.getData().size() > 0) {
                            LogUtil.d(TAG, "get videos :" + data.getData().size());
                            //:data.getData(): video集合数据
                            adapter.replaceData(data.getData());
                        } else {
                            //没有数据
                        }

                    }
                });
    }

    /**
     * 构造方法
     * 固定写法
     *
     * @return VideoFragment本身
     */
    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
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
        return inflater.inflate(R.layout.fragment_video, null);
    }
}
