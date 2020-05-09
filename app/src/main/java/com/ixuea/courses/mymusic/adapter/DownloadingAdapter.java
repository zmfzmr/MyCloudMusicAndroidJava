package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.fragment.ConfirmDialogFragment;
import com.ixuea.courses.mymusic.listener.DownloadListener;
import com.ixuea.courses.mymusic.util.FileUtil;
import com.ixuea.courses.mymusic.util.ORMUtil;

import java.lang.ref.SoftReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;

/**
 * 下载中适配器
 * 注意：这类的对象数据是：DownloadInfo
 */
public class DownloadingAdapter extends BaseRecyclerViewAdapter<DownloadInfo, DownloadingAdapter.ViewHolder> {


    private final ORMUtil orm;//数据库工具类
    private final FragmentManager fragmentManager;//fragment管理器
    private final DownloadManager downloader;//下载器

    /**
     * 构造方法
     *
     * @param context
     */
    public DownloadingAdapter(Context context, ORMUtil orm, FragmentManager fragmentManager, DownloadManager downloader) {
        super(context);
        this.orm = orm;
        this.fragmentManager = fragmentManager;
        this.downloader = downloader;
    }

    /**
     * 返回ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //getInflater(): 父类的
        return new ViewHolder(getInflater().inflate(R.layout.item_downloading, parent, false));
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //获取当前位置数据
        DownloadInfo data = getData(position);

        //获取业务数据(也就是保存到Realm数据库里面的数据)
        //DownloadInfo:保存了下载对象（Song对象）的id，用realm通过这个id查找song
        Song song = orm.querySongById(data.getId());
        holder.bindBaseData(song);

        //显示下载信息(这里传入的是 ：DownloadInfo )
        holder.bindData(data);
    }

    /**
     * 下载ViewHolder
     */
    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;//标题

        @BindView(R.id.tv_info)
        TextView tv_info;//下载信息

        @BindView(R.id.pb)
        ProgressBar pb;//进度条

        /**
         * 删除按钮
         */
        @BindView(R.id.ib_delete)
        ImageButton ib_delete;


        private DownloadInfo data;//下载数据

        /**
         * 构造方法
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 显示基础数据
         *
         * @param song Song
         */
        public void bindBaseData(Song song) {
            //标题
            tv_title.setText(song.getTitle());
        }

        /**
         * 显示下载信息
         * 这里传入的 是DownloadInfo任务 对象
         */
        @Override
        public void bindData(Object d) {
            super.bindData(d);
            //进行强转
            this.data = (DownloadInfo) d;
            //设置下载回调监听器
            //注意：DownloadListener：这个是我们自定义的； 还有个SoftReference 传入this
            data.setDownloadListener(new DownloadListener(new SoftReference(this)) {
                @Override
                public void onRefresh() {
                    if (getUserTag() != null && getUserTag().get() != null) {
                        //获取ViewHolder
                        //表示从软应用 中获取到这个对象(ViewHolder: 就是：下载ViewHolder)
                        ViewHolder holder = (ViewHolder) getUserTag().get();

                        //调用显示数据方法
                        holder.refresh();
                    }
                }
            });

            //第一次显示数据(防止 没有下载，是不会调用上面的DownloadListener onRefresh方法)
            refresh();

            //删除按钮点击事件
            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //弹出确认对话框
                    ConfirmDialogFragment.show(fragmentManager, (dialog, which) -> {
                        //下载框架中删除  data： DownloadInfo对象
                        downloader.remove(data);

                        //从适配器中删除  getAdapterPosition：RecyclerView里面的
                        //removeData: 是我们自己定义的方法(父类BaseRecyclerViewAdapter中定义的)
                        removeData(getAdapterPosition());
                    });
                }
            });

        }

        /**
         * 显示下载信息的
         */
        private void refresh() {
            //data: DownloadInfo
            switch (data.getStatus()) {
                case DownloadInfo.STATUS_PAUSED:
                    //暂停
                    tv_info.setText(R.string.click_download);//已暂停，点击继续下载
                    pb.setVisibility(View.GONE);
                    break;
                case DownloadInfo.STATUS_ERROR:
                    //下载失败了
                    tv_info.setText(R.string.download_failed);//下载失败，点击重试
                    pb.setVisibility(View.GONE);
                    break;
                case DownloadInfo.STATUS_DOWNLOADING:
                case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
                    //下载中和准备下载  合在一起处理
                    tv_info.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);

                    //计算进度  data: DownloadInfo  data.getSize(): 这个任务的大小长度
                    if (data.getSize() > 0) {
                        //格式化下载进度
                        String start = FileUtil.formatFileSize(data.getProgress());
                        String size = FileUtil.formatFileSize(data.getSize());
                        //context : 父类初始化的，外面传入进来的
                        //%1$s/%2$s
                        tv_info.setText(context
                                .getResources()
                                .getString(R.string.download_progress, start, size));

                        //设置到进入条
                        pb.setMax((int) data.getSize());
                        pb.setProgress((int) data.getProgress());

                    }
                    break;
                case DownloadInfo.STATUS_COMPLETED:
                    //下载完成
                    break;
                case DownloadInfo.STATUS_WAIT:
                    //等待中
                    tv_info.setText(R.string.wait_download);
                    pb.setVisibility(View.GONE);
                    break;

                default:
                    //未下载
                    break;
            }
        }
    }
}
