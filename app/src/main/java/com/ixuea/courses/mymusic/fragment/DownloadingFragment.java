package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.BaseRecyclerViewAdapter;
import com.ixuea.courses.mymusic.adapter.DownloadingAdapter;
import com.ixuea.courses.mymusic.listener.OnItemClickListener;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 下载中界面
 */
public class DownloadingFragment extends BaseCommonFragment implements OnItemClickListener {
    private static final String TAG = "DownloadingFragment";
    /**
     * 下载按钮(默认是全部暂停 点击后判断有下载任务就是 全部下载)
     */
    @BindView(R.id.bt_download)
    Button bt_download;

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
        //虽然这里是从downloader下载器获取下载中任务给适配器，
        // 注意：这里的是给了一个引用给适配器，实际指向的对象和下载器中的任务都是同一个对象的
        List<DownloadInfo> downloads = downloader.findAllDownloading();

        //设置到适配器(数据是：List<DownloadInfo>)
        adapter.setDatum(downloads);

        //显示按钮状态
        showButtonStatus();
    }

    /**
     * 显示按钮状态
     */
    private void showButtonStatus() {
        if (isDownloading()) {
            bt_download.setText(R.string.pause_all);
        } else {
            //全部开始
            bt_download.setText(R.string.download_all);
        }
    }

    /**
     * 是否是下载中 状态
     */
    private boolean isDownloading() {
        List<DownloadInfo> datum = adapter.getDatum();
        //遍历所有下载任务
        for (DownloadInfo downloadInfo : datum) {
            if (downloadInfo.getStatus() == DownloadInfo.STATUS_DOWNLOADING) {
                //只有有一个任务是下载中，那么就返回true，然后在showButtonStatus中 改为全部暂停
                return true;
            }
        }
        //默认返回false
        return false;
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

        //点击item也要改变(全部开始 按钮状态)
        showButtonStatus();
    }

    /**
     * 下载按钮点击
     */
    @OnClick(R.id.bt_download)
    public void onDownloadClick() {
        LogUtil.d(TAG, "onDownloadClick");

        //无数据
        //可以按照需求来处理
        //原版是没有下载任务不能进入到该界面
        //我们这里就显示一个提示就行了
        if (adapter.getItemCount() == 0) {
            ToastUtil.errorShortToast(R.string.error_not_download);
            return;
        }

        if (isDownloading()) {
            //有下载任务

            //点击暂停下载
            pauseALl();
        } else {
            resumeAll();
        }

        //显示按钮状态(全部开始按钮 状态)
        showButtonStatus();
    }

    /**
     * 继续所有下载
     */
    private void resumeAll() {
        downloader.resumeAll();
        //这个不调用也是可以，因为执行downloader.resumeAll()后，
        // 在DownloadingAdapter中的data.setDownloadListener中 调用holder.refresh方法刷新item内容的状态
        adapter.notifyDataSetChanged();
    }

    /**
     * 暂停所有 下载
     */
    private void pauseALl() {
        downloader.pauseAll();
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除按钮点击
     */
    @OnClick(R.id.bt_delete)
    public void onDeleteClick() {
        LogUtil.d(TAG, "onDeleteClick");

        if (adapter.getItemCount() == 0) {
            ToastUtil.errorShortToast(R.string.error_not_download);
            return;
        }

        //adapter.getDatum(): 这里获取的引用
        //在下载器中根据引用删除下载任务
        for (DownloadInfo downloadInfo : adapter.getDatum()) {
            downloader.resume(downloadInfo);
        }

        //清除适配器数据(也就是清除引用)
        adapter.clearData();
    }
}
