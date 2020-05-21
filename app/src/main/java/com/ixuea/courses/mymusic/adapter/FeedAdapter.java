package com.ixuea.courses.mymusic.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 动态适配器
 */
public class FeedAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
    /**
     * 构造方法
     */
    public FeedAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Feed data) {
        //头像
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);
        ImageUtil.showAvatar(mContext, iv_avatar, data.getUser().getAvatar());

        //昵称
        helper.setText(R.id.tv_nickname, data.getUser().getNickname());

        //时间
        helper.setText(R.id.tv_time, TimeUtil.commentFormat(data.getCreated_at()));

        StringBuilder sb = new StringBuilder();
        sb.append(data.getContent());
        if (StringUtils.isNotBlank(data.getProvince())) {
            //如果有省
            //就显示地理位置
            sb.append("\n来自：")
                    .append(data.getProvince())
                    .append(".")
                    .append(data.getCity());
        }

        //动态内容
//        helper.setText(R.id.tv_content,data.getContent());
        helper.setText(R.id.tv_content, sb.toString());

        //获取列表控件
        RecyclerView rv = helper.getView(R.id.rv);

        if (data.getImages() != null && data.getImages().size() > 0) {
            //有图片

            //显示图片列表控件
            rv.setVisibility(View.VISIBLE);

            //尺寸固定(当然这个也可以放到if外面)
            rv.setHasFixedSize(true);

            //禁用嵌套滚动（不希望内容多了后，里面的内容滚动(禁用内容滚动)）
            //注意：传入的是false
            rv.setNestedScrollingEnabled(false);
        } else {
            //没有图片

            //隐藏控件
            rv.setVisibility(View.GONE);
        }

    }
}
