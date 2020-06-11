package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;

import butterknife.BindView;

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

        //TODO 二维码
    }
}
