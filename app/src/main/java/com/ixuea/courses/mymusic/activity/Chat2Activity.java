package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ChatAdapter;
import com.ixuea.courses.mymusic.manager.impl.UserManager;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.model.Conversation;

/**
 * 聊天界面
 */
public class Chat2Activity extends BaseTitleActivity {
    private static final String TAG = "Chat2Activity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 输入框
     * EmojiAppCompatEditText 也就是间接继承父类EditText
     */
    @BindView(R.id.et_content)
    EditText et_content;

    private String id;//对方id
    private Conversation conversation;//会话
    private UserManager userManager;//用户管理器
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
    }

    @Override
    protected void initView() {
        super.initView();

        //初始化列表控件
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //初始化用户管理器
        userManager = UserManager.getInstance(getMainActivity());

        //获取id(对方id) 因为是从对方的用户详情 点击 (发送消息过来的)，传递的是对方的用户id
        id = extraId();

        //显示标题
        //以为从上个界面传递过来的id是包裹完成的，所以需要解除包裹
        userManager.getUser(StringUtil.unwrapUserId(id), data -> setTitle(data.getNickname()));

//        //测试聊天SDK是否集成成功
//        //这里的用户必须是通过客户端注册的
//        //也就是要调用了极光IM的注册方法
//        conversation = Conversation.createSingleConversation(id);
//        //创建文本消息
//        Message message = conversation.createSendTextMessage("我们是爱学啊,这是一条文本消息");
//        //发送文本消息
//        JMessageClient.sendMessage(message);

        //上面测试的可以注释掉

        //创建适配器
        adapter = new ChatAdapter(getMainActivity());
        //设置适配器
        rv.setAdapter(adapter);
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

    /**
     * 选择图片按钮
     */
    @OnClick(R.id.iv_select_image)
    public void onSelectImageClick() {
        LogUtil.d(TAG, "onSelectImageClick: ");
    }

    /**
     * 发送按钮
     */
    @OnClick(R.id.iv_send)
    public void onSendClick() {
        LogUtil.d(TAG, "onSendClick: ");
    }
}
