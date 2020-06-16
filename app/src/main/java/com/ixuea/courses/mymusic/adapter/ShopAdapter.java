package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Book;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 商品适配器
 */
public class ShopAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    public ShopAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Book data) {
        //查找封面控件
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        //封面
        ImageUtil.show(mContext, iv_banner, data.getBanner());

        //标题
        helper.setText(R.id.tv_title, data.getTitle());

        //价格
        //￥%1$.2f  : 其中的.2f 表示保留2位小数  ，$d:表示整数类型  $.2f: 保留2位 f：浮点类型
        String price = mContext.getResources().getString(R.string.price, data.getPrice());
        helper.setText(R.id.tv_price, price);

    }
}
