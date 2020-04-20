package com.ixuea.courses.mymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户详情界面
 */
public class UserDetailActivity extends BaseTitleActivity {
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
}
