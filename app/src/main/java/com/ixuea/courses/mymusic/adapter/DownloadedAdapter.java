package com.ixuea.courses.mymusic.adapter;

import android.content.DialogInterface;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.fragment.ConfirmDialogFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * 下载完成界面适配器
 */
public class DownloadedAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    private final FragmentManager fragmentManager;//fragment管理器
    private final DownloadManager downloader;//下载器

    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public DownloadedAdapter(int layoutResId, FragmentManager fragmentManager, DownloadManager downloadManager) {
        super(layoutResId);
        this.fragmentManager = fragmentManager;
        this.downloader = downloadManager;
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {

        //位置
        helper.setText(R.id.tv_position, String.valueOf(helper.getAdapterPosition() + 1));

        //标题
        helper.setText(R.id.tv_title, data.getTitle());

        //信息(这里是获取 歌手名称)
        helper.setText(R.id.tv_info, data.getSinger().getNickname());

        //删除按钮
        ImageButton ib_delete = helper.getView(R.id.ib_delete);

        //设置删除点击事件
        ib_delete.setOnClickListener(v -> {
            ConfirmDialogFragment.show(fragmentManager, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownloadInfo downloadInfo = downloader.getDownloadById(data.getId());
                    //这里只是从.._download_info.db 这个单独的数据库和 适配器中删除数据，而realm数据库里面还是有数据的
                    //这样删除，只是删除它保存的路径信息(_download_info里面的信息)，实际的音乐文件，还在本地上，并没有删除

                    //从下载框架删除
                    downloader.remove(downloadInfo);

                    //从适配器中删除
                    //helper： convert方法参数里面的的BaseViewHolder
                    remove(helper.getAdapterPosition());
                }
            });
        });

    }
}
