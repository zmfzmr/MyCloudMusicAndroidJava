package com.ixuea.courses.mymusic.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.DensityUtil;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.king.zxing.util.CodeUtils;

import butterknife.BindView;

import static com.ixuea.courses.mymusic.util.Constant.QRCODE_URL;

/**
 * 用户二维码界面
 */
public class CodeActivity extends BaseTitleActivity {
    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    /**
     * 昵称
     */
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    /**
     * 二维码
     */
    @BindView(R.id.iv_code)
    ImageView iv_code;
    private String userId;//用户id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
    }

    @Override
    protected void initView() {
        super.initView();

        //显示亮色状态栏
        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取用户id (之所以把用户id传递过来的意思是，这个界面可以显示任何用户的id(从而获取任意用户的二维码))
        userId = extraId();

        Api.getInstance()
                .userDetail(userId)
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void next(User data) {
        //头像
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getAvatar());

        //昵称
        tv_nickname.setText(data.getNickname());

        //二维码

        //我们这里的二维码的数据
        //就是一个网址
        //真实的数据在网址的查询参数里面
        //http://dev-my-cloud-music-api-rails.ixuea.com/v1/monitors/version?u=

        //quCode: 二维码的简称
        String qrCodeData = QRCODE_URL + data.getId();

        //生成二维码
        showCode(qrCodeData);
    }

    /**
     * //生成二维码
     *
     * @param data
     */
    private void showCode(String data) {

        //生成二维码最好放子线程生成防止阻塞UI
        //这里只是演示
        //Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        //生成二维码
        //logo会覆盖到二维码(也就是二维码中间的那个头像)
        //暂时没找到设置方法(目前这个有点问题，这里就不设置了)
        //所以就不设置了
        //Bitmap bitmap =  CodeUtils.createQRCode(data, DensityUtil.dip2px(getMainActivity(),250),logo);

        //这里我们是250  图片控件那边是220dp centerCrop  ; 也就是说：250>220 那么二维码就缩小居中放到这个图片控件里面
        //否则如果是小于220的，那么显示到图片控件上就会放大显示，就会显得模糊
        //参数1：这个二维码存储的数据(这里存储的是一个网址)
        Bitmap bitmap = CodeUtils.createQRCode(data, DensityUtil.dip2px(getMainActivity(), 250));
        iv_code.setImageBitmap(bitmap);

    }
}
