package com.ixuea.courses.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

        //测试显示资源目录图片
//        iv_avatar.setImageResource(R.drawable.placeholder);

        //显示资源目录图片
        //就是应用中drawable和mipmap目录
//        Glide.with(this)
//                .load(R.drawable.placeholder)
//                .into(iv_avatar);

//        //测试网络图片
//        Glide.with(this)
//                .load("http://dev-courses-misuc.ixuea.com/1da1c001e89c4b8780ac8f9780ef881f.jpg")
//                .into(iv_avatar);

        //其他配置
//        Glide.with(this)
//                .load("http://dev-courses-misuc.ixuea.com/1da1c001e89c4b8780ac8f9780ef881f.jpg")
//                //从中心裁剪
//                .centerCrop()
//                //占位图
//                //就是当前真实的图片没有显示出来前
//                //显示的图片
//                .placeholder(R.drawable.placeholder)
//                .into(iv_avatar);


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
        // 显示头像
        if (TextUtils.isEmpty(data.getAvatar())) {
            //没有头像

            //显示默认头像
//            iv_avatar.setImageResource(R.drawable.placeholder);

            Glide.with(getMainActivity())
                    .load(R.drawable.placeholder)
                    .into(iv_avatar);

        } else {
            //有头像 是否以http 开头
            if (data.getAvatar().startsWith("http")) {
                Glide.with(getMainActivity())
                        .load(data.getAvatar())
                        .into(iv_avatar);
            } else {
                //相对路径

                //将图片地址转为绝对地址
                //data.getAvatar():相对路径，需要前面的前缀拼接起来，
                // 比如http://dev-courses-misuc.ixuea.com/ 加上ata.getAvatar()
                //注意：这里用uri，统一资源标识符（而url是网址的意思）
                String uri = String.format(Constant.RESOURCE_ENDPOINT, data.getAvatar());
                Glide.with(getMainActivity())
                        .load(uri)
                        .into(iv_avatar);
            }
        }


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
