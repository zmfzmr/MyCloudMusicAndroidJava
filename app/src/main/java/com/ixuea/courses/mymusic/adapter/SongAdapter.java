package com.ixuea.courses.mymusic.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

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
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public SongAdapter(int layoutResId) {
        super(layoutResId);
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
