package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.DownloadedAdapter;
import com.ixuea.courses.mymusic.domain.Song;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 下载完成界面
 */
public class DownloadedFragment extends BaseCommonFragment {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private DownloadedAdapter adapter;
    private DownloadManager downloader;

    @Override
    protected void initViews() {
        super.initViews();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        //设置分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //初始化下载管理器
        downloader = AppContext.getInstance().getDownloadManager();

        //创建适配器
        adapter = new DownloadedAdapter(R.layout.item_downloaded);

        //设置适配器
        rv.setAdapter(adapter);

        fetchData();
    }


    private void fetchData() {
        //思路：List<DownloadInfo>保存了下载音乐的id->遍历->根据id从数据库中找到保存到list集合datum中，
        //      然后设置到适配器

        //查询所有下载完成的任务
        List<DownloadInfo> downloads = downloader.findAllDownloaded();
        //转为音乐对象
        //目的是播放的时候
        //直接就可以播放了
        List<Song> datum = new ArrayList<>();
        Song data = null;
        for (DownloadInfo downloadInfo : downloads) {
            //这个List<DownloadInfo> 对象保存 Song歌曲的id
            data = orm.querySongById(downloadInfo.getId());
            datum.add(data);
        }
        //设置数据到适配器
        adapter.replaceData(datum);
    }

    /**
     * 返回布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloaded, container, false);
    }

    /**
     * 创建方法
     */
    public static DownloadedFragment newInstance() {

        Bundle args = new Bundle();

        DownloadedFragment fragment = new DownloadedFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
