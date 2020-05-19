package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import androidx.annotation.NonNull;

/**
 * 视频适配器
 */
public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Video data) {
        //封面
//        ImageUtil.show(mContext, helper.getView(R.id.iv_banner), data.getBanner());
        ImageUtil.showSmallRadius((Activity) mContext, helper.getView(R.id.iv_banner), data.getBanner());

        //点击数(视频播放次数)
        helper.setText(R.id.tv_count, String.valueOf(data.getClicks_count()));

        //视频时长 data.getDuration(): 这类的时长是秒  ，注意调用formatMinuteSecond2这个方法
        helper.setText(R.id.tv_time,
                TimeUtil.s2ms((int) data.getDuration()));

        //标题
        helper.setText(R.id.tv_title, data.getTitle());

        //头像(showAvatar 里面用了Glide来进行圆形裁剪)
        ImageUtil.showAvatar(mContext, helper.getView(R.id.iv_avatar), data.getUser().getAvatar());

        //昵称
        helper.setText(R.id.tv_nickname, data.getUser().getNickname());

        //评论数
        helper.setText(R.id.tv_comment_count, String.valueOf(data.getComments_count()));
    }

}
