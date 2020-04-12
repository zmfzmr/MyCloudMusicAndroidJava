package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.lyric.Line;

import java.util.Collection;

import androidx.annotation.NonNull;

/**
 * 选择歌词适配器
 */
public class SelectLyricAdapter extends BaseQuickAdapter<Line, BaseViewHolder> {
    /**
     *
     */
    private int[] selectedIndexes;

    /**
     * 构造方法
     */
    public SelectLyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 绑定数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Line item) {
        //显示数据 item.getData():获取的是整行歌词
        helper.setText(R.id.tv_title, item.getData());

        int position = helper.getAdapterPosition();
        //这里因为数组的长度和数据的长度一样，选中哪个索引，就会从数组获取值，判断是0还是1
        if (selectedIndexes[position] == 0) {
            //没有选中

            //ImageView控件（打√那个控件 显示设置为false）
            helper.setVisible(R.id.iv_check, false);
            //设置背景颜色
            helper.setBackgroundColor(R.id.rl_container, mContext.getResources().getColor(R.color.transparent));

        } else {
            //选中状态

            helper.setVisible(R.id.iv_check, true);
            //背景设置为黑色
            helper.setBackgroundColor(R.id.rl_container, mContext.getResources().getColor(R.color.black));
        }
    }

    /**
     * data:这个就是从外面传入的进来的集合
     */
    @Override
    public void replaceData(@NonNull Collection<? extends Line> data) {
        super.replaceData(data);

        //创建一个和数据长度一样的数组
        selectedIndexes = new int[data.size()];
    }

    /**
     * 设置位置是否选中
     *
     * @param position   这个是点击的传入的position
     * @param isSelected 这个是调用isSelected()方法人会的 boolean值
     *                   <p>
     *                   注意：这里的position的使用技巧
     */
    public void setSelected(int position, boolean isSelected) {
        //数组长度 和 数据长度一样  position:当前选中item的索引，把当前的选中状态0或者1保存到这个数组里面
        //然后通过索引刷新(也就是notifyItemChanged(position))的时候，会根据这个数组的值是1还是0刷新item

        //简单一句：当前是打√状态（true），取反false（这个结果0存入数组），刷新状态，根据这个数组值 0 刷新
        //否则取反
        selectedIndexes[position] = isSelected ? 1 : 0;
        //通过索引刷新
        notifyItemChanged(position);//调用这个会重新调用convert 重新刷新数据
    }

    /**
     * 外界调用这个方法的时候，如果等于1，返回true，说明已经选中了
     * 而我们点击的时候效果是相反的（也就是点击的时候：选中了设置为不选中，
     * 所以在setSelected中设置相反的值（并存储到数组里面），然后调用notifyItemChanged，然后在convert中就可以判断了）
     * <p>
     * //另一种实现：在这个setSelected方法中，如果选中了，把 1 0位置互换下
     * 1：选中 0：不选中
     *
     * @param position
     * @return
     */
    public boolean isSelected(int position) {
        //等于1就会返回true
        return selectedIndexes[position] == 1;
    }

    /**
     * 获取和数据相同长度的 数组（这个数组在replaceData 设置完成后，初始化默认都为0的）
     */
    public int[] getSelectedIndexes() {
        return selectedIndexes;
    }
}
