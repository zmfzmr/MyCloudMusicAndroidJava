package com.ixuea.courses.mymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.UserDetailAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户详情界面(点击用户头像或者 点击 @爱学啊 传递进来的)
 */
public class UserDetailActivity extends BaseTitleActivity {

    private static final String TAG = "UserDetailActivity";
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
     * 信息
     */
    @BindView(R.id.tv_info)
    TextView tv_info;

    /**
     * 关注按钮
     */
    @BindView(R.id.bt_follow)
    Button bt_follow;

    /**
     * 发送消息按钮(默认是隐藏的)
     */
    @BindView(R.id.bt_send_message)
    Button bt_send_message;

    /**
     * 指示器 （也可是实现底部导航的效果，原理是一样的， 一个是顶部导航，一个是底部导航）
     */
    @BindView(R.id.tl)
    TabLayout tl;
    /**
     * 左右滚动控件
     */
    @BindView(R.id.vp)
    ViewPager vp;

    private String id;//传递的id(用户的id)
    private String nickname;//用户昵称
    private User data;//当前用户对象
    private UserDetailAdapter adapter;
    private MenuItem editMenuItem;//编辑资料按钮

    /**
     * 根据昵称显示用户详情(这里主要传递昵称 到本类)
     *
     * @param context  上下文
     * @param nickname 昵称
     */
    public static void startWithNickname(Context context, String nickname) {
        start(context, null, nickname);
    }

    /**
     * 根据用户id显示用户详情（这里主要传递用户id 到本类）
     *
     * @param context 上下文
     * @param id      用户id
     */
    public static void startWithId(Context context, String id) {
        start(context, id, null);
    }

    /**
     * 启动界面
     *
     * @param context  上下文
     * @param id       用户详情也可以通过这个id来进行启动的
     * @param nickname 昵称
     */
    private static void start(Context context, String id, String nickname) {
        //创建Intent
        Intent intent = new Intent(context, UserDetailActivity.class);
        //如果有id 和 昵称 再添加
        if (!StringUtils.isEmpty(id)) {

            intent.putExtra(Constant.ID, id);
        }

        if (!StringUtils.isEmpty(nickname)) {

            intent.putExtra(Constant.NICKNAME, nickname);
        }
        //启动界面
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        //设置缓存数
        vp.setOffscreenPageLimit(3);
    }

    /**
     * 这里是在评论列表那边点击进来的
     * 点击头像传递用户id：           http://dev-my-cloud-music-api-rails.ixuea.com/v1/users/152
     * 点击@爱学啊,传入昵称，如爱学啊：http://dev-my-cloud-music-api-rails.ixuea.com/v1/users/-1?nickname=爱学啊
     */
    @Override
    protected void initDatum() {
        super.initDatum();
        //获取用户id  (注意： 这里用的是字符串类型的)
        id = extraId();
        //判断id是否为空
        if (TextUtils.isEmpty(id)) {
            //如果为空就给他设置一个默认值
            //这是和服务端协商好的
            id = "-1";
        }
        //获取昵称
        nickname = extraString(Constant.NICKNAME);

        fetchData();
    }

    /**
     * 获取数据
     */
    private void fetchData() {
        Api.getInstance()
                .userDetail(id, nickname)
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
        this.data = data;
        LogUtil.d(TAG, "next:" + data.toString());
        //之所以现在才初始化界面
        //是因为适配器需要用户Id (不过点击的是用户头像(点击头像可以获取到真正用户id)，
        // 还是@爱学啊 昵称，昵称获取到的id是-1，不是真正的用户id)
        //统一进行网络请求后，请求到的是DetailResponse<User> 对象，获取到的才是用户真正id
        initUI();

        //显示头像
        //如果要背景根据头像改变
        //就和歌单详情实现一样
        //所以这里就不在重复讲解了
        ImageUtil.show(getMainActivity(), iv_avatar, data.getAvatar());

        //昵称
        tv_nickname.setText(data.getNickname());
        //参数2：关注的人 3：我的粉丝(关注我的人)
        String info = getResources().getString(R.string.user_friend_info,
                data.getFollowings_count(),
                data.getFollowersCount());

        tv_info.setText(info);

        //显示关注状态
        showFollowStatus();

        //显示编辑用户信息按钮状态
        // (请求回来的User对象id 等于 登录账号的用户id，表示是我自己的用户详情，就显示编辑按钮)
        editMenuItem.setVisible(data.getId().equals(sp.getUserId()));
    }

    /**
     * 显示关注状态
     *    一进来 进行网络请求时候 和 点击关注按钮时候用到
     */
    private void showFollowStatus() {
        if (data.getId().equals(sp.getUserId())) {
            //自己

            //隐藏关注按钮，发送消息按钮
            bt_follow.setVisibility(View.GONE);
            bt_send_message.setVisibility(View.GONE);
        } else {
            //别人的用户详情 (不管有没有关注，这个关注按钮都要显示) 因为默认隐藏了
            bt_follow.setVisibility(View.VISIBLE);

            //判断用户是否关注
            if (data.isFollowing()) {
                //已经关注了
                bt_follow.setText(R.string.cancel_follow);
                //显示发送消息按钮(因为xml布局里面已经设置了文字，所以直接显示就行)
                bt_send_message.setVisibility(View.VISIBLE);
            } else {
                //没有关注
                bt_follow.setText(R.string.follow);
                //隐藏发送消息按钮
                bt_send_message.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 初始化界面
     */
    private void initUI() {
        //创建适配器  data.getId():用户id
        adapter = new UserDetailAdapter(getMainActivity(), getSupportFragmentManager(), data.getId());

        //设置适配器
        vp.setAdapter(adapter);

        //设置占位数据
        List<Integer> datum = new ArrayList<>();

        datum.add(0);
        datum.add(1);
        datum.add(2);

        //设置数据
        adapter.setDatum(datum);

        //将TabLayout和ViewPager绑定
        tl.setupWithViewPager(vp);
    }

    /**
     * 关注按钮点击
     */
    @OnClick(R.id.bt_follow)
    public void onFollowClick() {
        if (data.isFollowing()) {
            //已经关注了

            //取消关注
            Api.getInstance()
                    //取消关注(就是取消关注的哪个人，这个传入的是用户的id)
                    .deleteFollow(data.getId())
                    .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                        @Override
                        public void onSucceeded(DetailResponse<BaseModel> d) {
                            //取消关注成功
                            data.setFollowing(null);

                            //刷新关注状态
                            //(这里我们直接本地按钮的状态即可，因为调用网络请求接口后，服务器那边已经取消用户关注了)
                            showFollowStatus();
                        }
                    });

        } else {
            //没有关注

            //关注
            Api.getInstance()
                    .follow(data.getId())
                    .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                        @Override
                        public void onSucceeded(DetailResponse<BaseModel> d) {
                            //关注成功(只要有一个值，说明是关注过的)
                            data.setFollowing(1);

                            //刷新关注状态
                            showFollowStatus();
                        }
                    });
        }
    }

    /**
     * 发送消息按钮点击
     */
    @OnClick(R.id.bt_send_message)
    public void onSendMessage() {
        //data.getId(): 用户id
        Chat2Activity.start(getMainActivity(), data.getId());
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    /**
     * 准备显示按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        editMenuItem = menu.findItem(R.id.action_edit);

        //隐藏  一开始让按钮隐藏
        editMenuItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 菜单点击了回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //编辑按钮点击了
                LogUtil.d(TAG, "edit click");
                startActivity(ProfileActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
