package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.BaseRecyclerViewAdapter;
import com.ixuea.courses.mymusic.adapter.DownloadingAdapter;
import com.ixuea.courses.mymusic.listener.OnItemClickListener;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 下载中界面
 */
public class DownloadingFragment extends BaseCommonFragment implements OnItemClickListener {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private DownloadingAdapter adapter;//适配器
    private DownloadManager downloader;//下载器

    @Override
    protected void initViews() {
        super.initViews();

        //初始化下载管理器
        downloader = AppContext.getInstance().getDownloadManager();

        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new DownloadingAdapter(getMainActivity(), orm, getChildFragmentManager(), downloader);

        //设置适配器
        rv.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置item点击事件
        adapter.setOnItemClickListener(this);
    }

    /**
     * 获取数据
     */
    private void fetchData() {
        //查询所欲除下载完成的任务(下载中任务)
        //（如果杀死应用了，这个下载框架，每秒保存一次下载任务，杀死后，重启应用，那还是可以查询的到）
        List<DownloadInfo> downloads = downloader.findAllDownloading();

        //设置到适配器(数据是：List<DownloadInfo>)
        adapter.setDatum(downloads);
    }

    /**
     * 返回布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloading, container, false);
    }

    /**
     * 创建方法
     */
    public static DownloadingFragment newInstance() {

        Bundle args = new Bundle();

        DownloadingFragment fragment = new DownloadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * item 点击回调方法
     *
     * @param holder   点击的ViewHolder
     * @param position 点击的位置
     */
    @Override
    public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        //获取点击的数据
        DownloadInfo data = adapter.getData(position);

        //判断状态，处理相应的状态事件
        switch (data.getStatus()) {
            case DownloadInfo.STATUS_NONE:
            case DownloadInfo.STATUS_PAUSED:
            case DownloadInfo.STATUS_ERROR:
                //没有下载 暂停 错误
                //点击：都是继续下载
                downloader.resume(data);
                break;
            default:
                //暂停下载
                downloader.pause(data);
                break;
        }
    }
}
