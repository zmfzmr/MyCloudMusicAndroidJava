package com.ixuea.courses.mymusic.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 歌单详情-歌曲适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    /**
     * 选中索引
     */
    private int selectedIndex = -1;

    private SongListener songListener;//监听器
    private boolean editing;//是否正在编辑(也就是点击(批量编辑)按钮，这里会变成true)

    /**
     * 1表示选中
     * 0表示没选中
     * <p>
     * 目前这个用在本地音乐那里
     */
    private int[] selectIndexes;
    private final DownloadManager downloader;//下载管理器
    private DownloadInfo downloadInfo;//下载任务

    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public SongAdapter(int layoutResId) {
        super(layoutResId);
        //下载管理器
        downloader = AppContext.getInstance().getDownloadManager();
    }

    /**
     * 显示数据
     *
     * @param helper BaseViewHolder
     * @param data Song 因为继承泛型的时候已经传入了Song类型，所以这里不需要向下转型了
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {
        //显示位置 helper.getAdapterPosition():获取集合数据的索引（所以从0开始，所以需要+1）
        helper.setText(R.id.tv_position, String.valueOf(helper.getAdapterPosition()));
        //显示标题
        helper.setText(R.id.tv_title, data.getTitle());
        //显示信息(歌曲名称)
        helper.setText(R.id.tv_info, data.getSinger().getNickname());

        //处理选中状态
        if (selectedIndex == helper.getAdapterPosition()) {
            //选中行
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            //未选中
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.text));
        }

        //设置更多点击事件
        //可以像PlayListAdapter中那样的方法实现
        //这里就用普通方法实现
        View ib_more = helper.getView(R.id.ib_more);

        //设置点击事件
        ib_more.setOnClickListener(view -> songListener.onMoreClick(data));

        //处理编辑状态
        if (editing) {
            //位置隐藏 勾选框显示
            helper.setVisible(R.id.tv_position, false);
            helper.setVisible(R.id.iv_check, true);
            //isSelected:是本类里面的那个方法
            if (isSelected(helper.getAdapterPosition())) {
                //编辑状态下

                //是选中状态
                helper.setImageResource(R.id.iv_check, R.drawable.ic_checkbox_selected);
            } else {
                //不是选中状态
                helper.setImageResource(R.id.iv_check, R.drawable.ic_checkbox);
            }

        } else {
            //否则取反
            helper.setVisible(R.id.tv_position, true);
            helper.setVisible(R.id.iv_check, false);
        }

        //这个SongAdapter 在SheetDetailActivity中用到了，在onResume方法调用scrollPositionAsync选中当前播放的音乐
        //所以自然的会调用到 adapter.setSelectedIndex方法，刷新当前item，所以这个下载图标自然的就会刷新出来了

        //如果downloader(下载器)里面的那个任务 有这个歌曲的id值，说明这首歌曲在下载任务中
        downloadInfo = downloader.getDownloadById(data.getId());

        if (downloadInfo != null && downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED) {
            //下载完成了

            //显示下载完成了图标
            helper.setGone(R.id.iv_download, true);
        } else {
            //没有下载
            //没有下载完成

            //隐藏下载完成图标
            helper.setGone(R.id.iv_download, false);
        }

    }

    @Override
    public void replaceData(@NonNull Collection<? extends Song> data) {
        super.replaceData(data);
        //创建一个和数据长度一样的数组
        //这里可以优化
        //因为就目前来说
        //在歌单详情不需要多选
        selectIndexes = new int[data.size()];
    }

    /**
     * 选中音乐
     *
     * @param index index
     */
    public void setSelectedIndex(int index) {
        selectIndex();

        //保存选中索引
        selectedIndex = index;

        selectIndex();
    }


    /**
     * 选中当前位置(刷新当前行)
     */
    private void selectIndex() {
        //刷新当前位置
        if (selectedIndex != -1) {
            notifyItemChanged(selectedIndex);
        }
    }

    /**
     * 设置音乐监听器
     */
    public void setSongListener(SongListener songListener) {
        this.songListener = songListener;
    }

    /**
     * 是否正在批量编辑
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * 设置批量编辑状态
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
        //这里是没有点击批量点击(或取消编辑) 都恢复没有选中的状态
        for (int i = 0; i < selectIndexes.length; i++) {
            //这里置为0，然后下面调用notifyDataSetChanged，刷新所有item
            //走convert方法 ，把打钩的换成 变成  没有打钩的图标
            selectIndexes[i] = 0;
        }

        //设置进来要通知适配器刷新状态(通知数据改变了)
        notifyDataSetChanged();
    }

    /**
     * 设置选中状态
     */
    public void setSelected(int position, boolean isSelected) {
        selectIndexes[position] = isSelected ? 1 : 0;
        notifyItemChanged(position);//记得调用这个
    }

    /**
     * 是否是选中状态
     *
     * @param position
     * @return
     */
    public boolean isSelected(int position) {
        return selectIndexes[position] == 1;
    }

    /**
     * 获取选中的索引(这里是获取选中的索引数组)
     */
    public List<Integer> getSelectIndexes() {
        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < selectIndexes.length; i++) {
            if (selectIndexes[i] == 1) {
                //是选中的
//                indexes.add(selectIndexes[i]);

                //注意：这里添加的是i(因为selectIndexes数组和数据元素是同等大小，所以这里的i就是item数据的索引)
                indexes.add(i);
            }
        }
        //返回选中索引数组
        return indexes;
    }

    /**
     * 定义监听器
     * <p>
     * 这里是定义在SongAdapter中
     * 因为目前还没有其他位置使用
     * 只是在SongAdapter中使用
     */
    public interface SongListener {
        /**
         * 音乐更多点击
         *
         * @param data Song
         */
        void onMoreClick(Song data);
    }

}
