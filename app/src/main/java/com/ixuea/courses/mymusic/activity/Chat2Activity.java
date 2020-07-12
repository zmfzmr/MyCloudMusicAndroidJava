package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * 聊天界面
 */
public class Chat2Activity extends BaseTitleActivity {

    private String id;//对方id
    private Conversation conversation;//会话

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取id(对方id) 因为是从对方的用户详情 点击 (发送消息过来的)，传递的是对方的用户id
        id = extraId();
        //测试聊天SDK是否集成成功
        //这里的用户必须是通过客户端注册的
        //也就是要调用了极光IM的注册方法
        conversation = Conversation.createSingleConversation(id);
        //创建文本消息
        Message message = conversation.createSendTextMessage("我们是爱学啊,这是一条文本消息");
        //发送文本消息
        JMessageClient.sendMessage(message);
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
