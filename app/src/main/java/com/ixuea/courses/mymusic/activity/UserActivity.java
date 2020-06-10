package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 好友列表界面
 */
public class UserActivity extends BaseTitleActivity {

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
        //判断样式设置Toolbar标题
        if (isFriend()) {
            setTitle(R.string.my_friend);
        } else {
            setTitle(R.string.my_fans);
        }
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
