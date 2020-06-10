package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取id
        userId = extraId();
        //获取样式
        style = extraInt(Constant.INT);
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
            }
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
