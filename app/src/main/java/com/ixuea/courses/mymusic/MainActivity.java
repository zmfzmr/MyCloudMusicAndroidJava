package com.ixuea.courses.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.activity.BaseTitleActivity;
import com.ixuea.courses.mymusic.activity.WebViewActivity;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.dl)//侧滑控件
            DrawerLayout dl;

    @BindView(R.id.iv_avatar)//头像
            ImageView iv_avatar;

    @BindView(R.id.tv_nickname)//昵称
            TextView tv_nickname;

    @BindView(R.id.tv_description)//描述
            TextView tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtil.d(TAG, "onCreate");

        //处理动作
        processIntent(getIntent());
    }

    @Override
    protected void initView() {
        super.initView();

        //侧滑配置 3 4参数 可以认为是2个字符串，目前还用不到
        //这里的toobar是父类里面的
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        //添加监听器
////        dl.setDrawerListener(toggle);//这个过时了
        dl.addDrawerListener(toggle);//用这个最新的
//
        //同步状态(ActionBarDrawerToggle 相当于个监听器，这个监听检测Drawlayout 关闭 和打开状态，然后同步样式)
        //本来是返回箭头的，同步状态后，变为了另外一个(三)的图标；往右划的时候图标也会变化
        toggle.syncState();
    }


    @Override
    protected void initDatum() {
        super.initDatum();

        //获取用户信息
        //当然可以在用户要显示侧滑的时候
        //才获取用户信息
        //这样可以减少请求
        fetchData();
    }

    /**
     * 请求数据
     * sp.getUserId():因为登录成功后保存了用户的id，所以通过地址和id连接起来可以访问改用户的详细信息
     */
    private void fetchData() {
        Api.getInstance().userDetail(sp.getUserId())
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
     * @param data User
     */
    private void next(User data) {
        //TODO 显示头像

        //显示昵称
        tv_nickname.setText(data.getNickname());

        //显示描述
        tv_description.setText(data.getDescriptionFormat());
    }

    /**
     * 界面已经显示了
     * 不需要再次创建新界面的时候调用
     *
     * @param intent Intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtil.d(TAG, "onNewIntent");
        processIntent(intent);
    }

    /**
     * 处理动作
     *
     * @param intent Intent
     */
    private void processIntent(Intent intent) {
        if (Constant.ACTION_AD.equals(intent.getAction())) {
            //广告点击

            //显示广告界面
            WebViewActivity.start(getMainActivity(), "活动标题", intent.getStringExtra(Constant.URL));
        }
    }

    /**
     * 用户点击了（这里是外层的ll_user 设置点击事件）
     */
    @OnClick(R.id.ll_user)
    public void onUserClick() {
        LogUtil.d(TAG, "onUserClick:");
    }
}
