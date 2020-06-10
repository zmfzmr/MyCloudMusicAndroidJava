package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.UserAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 好友列表界面
 */
public class UserActivity extends BaseTitleActivity {

    private static final String TAG = "UserActivity";
    public static final int FRIEND = 10;//好友
    public static final int FANS = 20;//粉丝列表
    private String userId;//用户id
    private int style;//入口样式
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取id
        userId = extraId();
        //获取样式
        style = extraInt(Constant.INT);

        //创建适配器（复用前面用到的item_user）
        adapter = new UserAdapter(R.layout.item_user);

        rv.setAdapter(adapter);

        Observable<ListResponse<User>> api = null;
        //判断样式设置Toolbar标题
        if (isFriend()) {
            //好友
            setTitle(R.string.my_friend);

            api = Api.getInstance().friends(userId);
        } else {
            //粉丝
            setTitle(R.string.my_fans);

            api = Api.getInstance().fans(userId);
        }

        api.subscribe(new HttpObserver<ListResponse<User>>() {
            @Override
            public void onSucceeded(ListResponse<User> data) {
                LogUtil.d(TAG, "users:" + data.getData().size());

                //TODO 设置到适配器
                adapter.replaceData(data.getData());
            }
        });
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //获取点击的数据
            User data = (User) adapter.getItem(position);
            //跳转到用户详情
            UserDetailActivity.startWithId(getMainActivity(), data.getId());

            //其实我们进入到用户详情界面以后，取消关注回来到列表，发现没有刷新列表
            //很简单，我们在UserDetailActivity中 取消关注后，发送一个事件，然后在本类UserActivity监听到，
            //然后刷新就行了。我们这里就不再实现
        });
    }

    /**
     * 是否是好友界面
     */
    private boolean isFriend() {
        //FRIEND： 本类的常量FRIEND = 10;
        return style == FRIEND;
    }

    /**
     * 启动界面
     *
     * @param activity Activity
     * @param userId   用户id
     */
    public static void start(Activity activity, String userId, int style) {
        Intent intent = new Intent(activity, UserActivity.class);
        //注意：这里到的key是String类型的，但是value可以是任意类型
        intent.putExtra(Constant.ID, userId);
        intent.putExtra(Constant.INT, style);
        activity.startActivity(intent);
    }
}
