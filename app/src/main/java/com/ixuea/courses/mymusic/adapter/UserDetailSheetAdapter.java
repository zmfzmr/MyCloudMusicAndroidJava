package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Title;
import com.ixuea.courses.mymusic.util.ImageUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_SHEET;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;

/**
 * 用户详情歌单列表适配器
 * 单类型用：BaseQuickAdapter   多类型用： BaseMultiItemQuickAdapter
 */
public class UserDetailSheetAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {

    public UserDetailSheetAdapter() {
        //第一次他要穿入数据
        //而这时候我们还没有准备好数据
        //所以传递一个空列表
        super(new ArrayList<>());

        //添加多类型布局
        addItemType(TYPE_TITLE, R.layout.item_title_small);
        //添加歌单布局(复用item_topic 话题布局)
        addItemType(TYPE_SHEET, R.layout.item_topic);
    }

    /**
     * 绑定数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                Title title = (Title) item;
                helper.setText(R.id.tv_title, title.getTitle());
                break;
            case TYPE_SHEET:
                //歌单
                Sheet sheet = (Sheet) item;
                //图片
                ImageUtil.show(mContext, helper.getView(R.id.iv_banner), sheet.getBanner());
                //标题
                helper.setText(R.id.tv_title, sheet.getTitle());

                //这里是音乐数量
                String info = mContext.getResources().getString(R.string.song_count, sheet.getSongsCount());
                helper.setText(R.id.tv_info, info);
                break;
        }
    }
}
