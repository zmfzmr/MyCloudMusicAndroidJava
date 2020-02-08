package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_SHEET;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_SONG;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;

/**
 * 发现界面适配器
 *
 *  * 这里实现三种布局
 *  * 标题，歌单，单曲
 *
 * BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder>
 *     参数1：当前列表里面显示的模型
 *     参数2：列表里面显示的item对应的类是什么 BaseViewHolder extends RecyclerView.ViewHolder
 */
public class DiscoveryAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {

    /**
     * 构造方法
     */
    public DiscoveryAdapter() {
        //第一次他要传入数据
        //而这时候我们还没有准备好数据
        //所以传递一个空列表
        super(new ArrayList<>());

        //添加多类型布局

        //添加标题类型
        addItemType(TYPE_TITLE, R.layout.item_title);
        //添加歌单类型
        addItemType(TYPE_SHEET, R.layout.item_sheet);
        //添加单曲类型
        addItemType(TYPE_SONG, R.layout.item_song);
    }

    /**
     * 绑定数据方法
     * <p>
     * 复用等步骤不用管
     * 框架内部自动处理
     *
     * @param helper BaseViewHolder
     * @param item   BaseMultiItemEntity
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMultiItemEntity item) {

    }
}
