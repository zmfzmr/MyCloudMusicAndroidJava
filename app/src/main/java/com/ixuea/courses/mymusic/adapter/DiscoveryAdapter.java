package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.Title;
import com.ixuea.courses.mymusic.util.ImageUtil;

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
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                Title title = (Title) item;
                helper.setText(R.id.tv_title, title.getTitle());
                break;
            case TYPE_SHEET:
                //歌单
                Sheet sheet = (Sheet) item;
                //显示图片
                //这个mContext是BaseMultiItemQuickAdapter的父类BaseQuickAdapter里面的
                //helper.getView(R.id.iv_banner)
                //参数1：上下文 2：图片控件 3 ：控件地址
                ImageUtil.show((Activity) mContext, helper.getView(R.id.iv_banner), sheet.getBanner());

                //设置歌单标题(就是图片下面的文字)
                helper.setText(R.id.tv_title, sheet.getTitle());

                //设置播放量(点击数)
                //注意：这里第二个参数是需要传入字符串的（需要转换成String）
                helper.setText(R.id.tv_info, String.valueOf(sheet.getClicks_count()));
                break;
            case TYPE_SONG:
                //单曲
                Song song = (Song) item;

                //显示封面
                ImageUtil.show((Activity) mContext, helper.getView(R.id.iv_banner), song.getBanner());

                //设置标题
                helper.setText(R.id.tv_title, song.getTitle());

                //设置播放量(点击数)
                helper.setText(R.id.tv_info, String.valueOf(song.getClicks_count()));

                //歌曲头像
                //注意：song.getSinger().getAvatar() 这里是先获取到歌手这个对象，然后再获取里面的头像
                ImageUtil.show((Activity) mContext, helper.getView(R.id.iv_avatar), song.getSinger().getAvatar());

                //歌手昵称
                helper.setText(R.id.tv_nickname, song.getSinger().getNickname());

                break;
            default:
                break;
        }
    }
}
