package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 首页-视频界面
 */
public class VideoFragment extends BaseCommonFragment {

    private static final String TAG = "VideoFragment";

    @Override
    protected void initDatum() {
        super.initDatum();

        Api.getInstance()
                .videos()
                .subscribe(new HttpObserver<ListResponse<Video>>() {
                    @Override
                    public void onSucceeded(ListResponse<Video> data) {
                        if (data.getData() != null && data.getData().size() > 0) {
                            LogUtil.d(TAG, "get videos :" + data.getData().size());
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
