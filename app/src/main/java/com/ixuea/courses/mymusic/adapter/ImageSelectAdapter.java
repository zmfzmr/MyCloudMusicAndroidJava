package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.luck.picture.lib.entity.LocalMedia;

import androidx.annotation.NonNull;

/**
 * 显示选择图片后适配器
 * <p>
 * LocalMedia   PublishFeedActivity onActivityResult方法中，
 * 返回的数据集合List<LocalMedia> 中的LocalMedia  （注：List<LocalMedia> 选中图片后返回的集合数据）
 */
public class ImageSelectAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {
    /**
     * 构造方法
     */
    public ImageSelectAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * @param helper
     * @param data
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, LocalMedia data) {
        //获取图片控件
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        //显示图片   data.getCompressPath(): 统一用压缩后的路径(选中返回来的图片压缩了，用这个压缩后的路径)
        ImageUtil.showLocalImage(mContext, iv_banner, data.getCompressPath());

        //给这个 删除按钮点击点击事件 （然后在activity中 可以用adapter.setOnItem）
        helper.addOnClickListener(R.id.iv_close);
    }
}
