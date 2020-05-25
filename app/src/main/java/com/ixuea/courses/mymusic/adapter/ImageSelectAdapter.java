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
//public class ImageSelectAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {
public class ImageSelectAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
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
//    protected void convert(@NonNull BaseViewHolder helper, LocalMedia data) {
    protected void convert(@NonNull BaseViewHolder helper, Object data) {
        //获取图片控件
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        if (data instanceof LocalMedia) {
            //选择的图片
            //显示图片   data.getCompressPath(): 统一用压缩后的路径(选中返回来的图片压缩了，用这个压缩后的路径)
            ImageUtil.showLocalImage(mContext, iv_banner, ((LocalMedia) data).getCompressPath());

            //显示删除按钮
            helper.setVisible(R.id.iv_close, true);

            //给这个 删除按钮点击点击事件 （然后在activity中 可以用adapter.setOnItem）
            helper.addOnClickListener(R.id.iv_close);
        } else {
            //显示图片  参数 转成Integer (因为是Object对象转的，只能是对象Integer，不能是int)
            ImageUtil.showLocalImage(mContext, iv_banner, (Integer) data);

            //隐藏删除按钮
            helper.setVisible(R.id.iv_close, false);
        }
    }
}
