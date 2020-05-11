package com.ixuea.courses.mymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.UserDetailAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 用户详情界面(点击用户头像或者 点击 @爱学啊 传递进来的)
 */
public class UserDetailActivity extends BaseTitleActivity {

    private static final String TAG = "UserDetailActivity";
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
}
