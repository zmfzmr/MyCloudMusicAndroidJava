package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 订单列表适配器
 */
public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    public OrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Order data) {
        //状态
        helper.setText(R.id.tv_status, mContext.getResources().getString(R.string.order_status_value,
                data.getStatusFormat()));
        //状态颜色
        helper.setTextColor(R.id.tv_status, mContext.getResources()
                .getColor(data.getStatusColor()));

        //订单编号
        helper.setText(R.id.tv_number, mContext.getResources().getString(R.string.order_number_value,
                data.getNumber()));

        //封面  Book里面的banner
        ImageView iv_banner = helper.getView(R.id.iv_banner);
        ImageUtil.show(mContext, iv_banner, data.getBook().getBanner());

        //标题(先要获取到这个Book对象，然后获取商品标题)
        helper.setText(R.id.tv_title, data.getBook().getTitle());

        //价格 这个直接订单Order上到的价格price(因为可能真实项目中又优惠，或者我在后台改了下订单价格)
        helper.setText(R.id.tv_price, mContext.getResources()
                .getString(R.string.price, data.getPrice()));
    }
}
