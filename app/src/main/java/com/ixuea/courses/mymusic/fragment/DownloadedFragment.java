package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.MusicPlayerActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.DownloadedAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 下载完成界面
 */
public class DownloadedFragment extends BaseCommonFragment implements BaseQuickAdapter.OnItemClickListener {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private DownloadedAdapter adapter;
    private DownloadManager downloader;
    private ListManager listManager;//列表管理器

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

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        //初始化下载管理器
        downloader = AppContext.getInstance().getDownloadManager();

        //创建适配器
        adapter = new DownloadedAdapter(R.layout.item_downloaded);

        //设置适配器
        rv.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置点击监听器
        adapter.setOnItemClickListener(this);
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

    /**
     * item点击事件
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //设置播放列表(把下载的歌曲放到播放列表(添加之后，之前列表的数据被清空了))
        //这个适配器里面的数据 在本类的fetchData方法中，就已经添加数据进来了
        listManager.setDatum(adapter.getData());
        //获取当前点击的音乐
        Song data = (Song) adapter.getItem(position);
        //播放点击的已下载音乐
        listManager.play(data);
        //跳转到音乐播放界面
        startActivity(MusicPlayerActivity.class);
    }
}
