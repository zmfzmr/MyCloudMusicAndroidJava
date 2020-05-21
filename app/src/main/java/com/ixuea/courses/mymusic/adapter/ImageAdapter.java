package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Resource;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 图片适配器
 * <p>
 * Resource: 前面定义的类
 */
public class ImageAdapter extends BaseQuickAdapter<Resource, BaseViewHolder> {
    public ImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Resource data) {
        //找图片控件
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        //显示图片 注意：这类是里面的uri(字符串类型)
        ImageUtil.show(mContext, iv_banner, data.getUri());
    }
}
