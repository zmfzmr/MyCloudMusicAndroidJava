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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    /**
     * 启动界面
     *
     * @param activity Activity
     * @param userId   用户id
     */
    public static void start(Activity activity, String userId) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(Constant.ID, userId);
        activity.startActivity(intent);
    }
}
