package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 聊天界面
 */
public class Chat2Activity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
    }

    /**
     * 启动界面
     *
     * @param activity 当前Activity
     * @param id       目标聊天用户Id
     */
    public static void start(Activity activity, String id) {
        //创建意图
        Intent intent = new Intent(activity, Chat2Activity.class);
        //传递id
        intent.putExtra(Constant.ID, id);
        //启动界面
        activity.startActivity(intent);
    }
}
