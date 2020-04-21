package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Topic;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 好友话题适配器
 */
public class TopicAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {
    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public TopicAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Topic data) {
        //helper:翻译：帮手
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        //显示封面
        ImageUtil.show((Activity) mContext, iv_banner, data.getBanner());
        //显示标题
        helper.setText(R.id.tv_title, "#" + data.getTitle() + "#");
        //显示参与人数
        helper.setText(R.id.tv_info,
                mContext.getResources().getString(R.string.join_count, data.getJoins_count()));
    }
}
