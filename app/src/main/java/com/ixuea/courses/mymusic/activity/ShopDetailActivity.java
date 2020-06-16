package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Book;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品详情界面
 */
public class ShopDetailActivity extends BaseTitleActivity {

    private static final String TAG = "ShopDetailActivity";
    /**
     * 封面
     */
    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 价格
     */
    @BindView(R.id.tv_price)
    TextView tv_price;

    /**
     * 价格
     */
    @BindView(R.id.bt_control)
    Button bt_control;
    private String id;//商品id
    private Book data;//商品对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取id
        id = extraId();
        fetchData();
    }

    private void fetchData() {
        Api.getInstance()
                .shopDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Book>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Book> data) {
                        next(data.getData());
                    }
                });

    }

    private void next(Book data) {
        this.data = data;

        //封面
        ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());

        //标题
        tv_title.setText(data.getTitle());

        //价格
        String price = getResources().getString(R.string.price, data.getPrice());
        tv_price.setText(price);

        //判断是否有购买
        //为什么不写else ，因为很少有一进来，就把已支付的状态，改成未支付的状态；可以不用写else
        if (data.isBuy()) {
            showBuySuccess();
        }
    }

    /**
     * 显示购买成功状态
     */
    private void showBuySuccess() {
        //变成：立即学习
        bt_control.setTextColor(R.string.go_study);
        //绿色
        bt_control.setBackgroundColor(getResources().getColor(R.color.color_pass));
    }


    /**
     * 控制按钮点击
     */
    @OnClick(R.id.bt_control)
    public void onControlClick() {
        LogUtil.d(TAG, "onControlClick");
    }
}
