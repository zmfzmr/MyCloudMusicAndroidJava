package com.ixuea.courses.mymusic.listener;

import com.ixuea.android.downloader.callback.AbsDownloadListener;
import com.ixuea.android.downloader.exception.DownloadException;

import java.lang.ref.SoftReference;

/**
 * 下载监听器
 * 将所有回调都调用onRefresh
 * (这样的好处是:可以在同一个方法去判断更方便)
 */
public abstract class DownloadListener extends AbsDownloadListener {

    //重写2个构造方法
    public DownloadListener() {
    }

    public DownloadListener(SoftReference<Object> userTag) {
        super(userTag);
    }

    @Override
    public void onStart() {
        onRefresh();
    }

    @Override
    public void onWaited() {
        onRefresh();
    }

    @Override
    public void onPaused() {
        onRefresh();
    }

    @Override
    public void onDownloading(long progress, long size) {
        onRefresh();
    }

    @Override
    public void onRemoved() {
        onRefresh();
    }

    @Override
    public void onDownloadSuccess() {
        onRefresh();
    }

    @Override
    public void onDownloadFailed(DownloadException e) {
        onRefresh();
    }

    /**
     * 刷新状态 我们自己定义的抽象方法，子类实现(前面的重写方法里面都调用了这个onRefresh() 方法)
     */
    public abstract void onRefresh();
}
