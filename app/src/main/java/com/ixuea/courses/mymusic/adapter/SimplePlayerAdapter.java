package com.ixuea.courses.mymusic.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 注意：这里Song对应到的是item对象
 */
public class SimplePlayerAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    /**
     * 选中索引
     * 为啥要写-1呢
     * 从迷你播放控制器进入时，简单播放界面重写打开了，重写刷新了RecyclerView列表，
     * 如果这时selectedIndex = 0，那第一个item回调到convert，就是显示选中的状态
     * <p>
     * 所以改为-1
     */
    private int selectedIndex = -1;

    /**
     * 构造方法
     */
    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        //获取到文本控件
        TextView tv_title = helper.getView(android.R.id.text1);

        //显示标题
        helper.setText(android.R.id.text1, item.getTitle());

        //处理选中状态
        if (selectedIndex == helper.getAdapterPosition()) {
            //选中
            //mContext：是父类里面的
            tv_title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            //未选中
            //也可以不获取控件，直接用BaseViewHolder设置
            helper.setTextColor(android.R.id.text1, mContext.getResources().getColor(R.color.text));
        }

    }

    /**
     * 选中音乐
     *
     * @param selectedIndex selectedIndex
     *                      <p>
     *                      比如说：第0个，先把第0个的状态（颜色）去掉，然后传递index，然后再刷新下当前状态
     */
    public void setSelectedIndex(int selectedIndex) {
        //刷新上一行
        notifyItemChanged(this.selectedIndex);
        this.selectedIndex = selectedIndex;
        //刷新当前行
        notifyItemChanged(this.selectedIndex);
    }
}
