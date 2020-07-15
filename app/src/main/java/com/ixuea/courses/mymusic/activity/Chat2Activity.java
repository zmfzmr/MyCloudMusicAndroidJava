package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ChatAdapter;
import com.ixuea.courses.mymusic.domain.event.OnNewMessageEvent;
import com.ixuea.courses.mymusic.manager.impl.UserManager;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.HandlerUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 聊天界面
 */
public class Chat2Activity extends BaseTitleActivity implements ViewTreeObserver.OnGlobalLayoutListener {
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

        //固定尺寸
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
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

        //测试聊天SDK是否集成成功
        //这里的用户必须是通过客户端注册的
        //也就是要调用了极光IM的注册方法

        //这个不能注释
        //当在Conversation 界面中点击item进来的时候，根据这个id(对方的id)可以获取到对方发送过来的会话
        //然后根据会话获取所有的消息
        conversation = Conversation.createSingleConversation(id);
//        //创建文本消息
//        Message message = conversation.createSendTextMessage("我们是爱学啊,这是一条文本消息");
//        //发送文本消息
//        JMessageClient.sendMessage(message);

        //上面测试的可以注释掉

        //清除未读消息
        //这个只要是进入到这个会话，就是全部为已读的了
        //如果要想实现成钉钉那样，显示到这条消息的时候才设置为已读，
        // 那么在ChatAdapter中TextViewHolder->bindData中 这个文本设置完成后才设置为已读
        conversation.setUnReadMessageCnt(0);

        //创建适配器
        adapter = new ChatAdapter(getMainActivity());
        //设置适配器
        rv.setAdapter(adapter);
    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        fetchData();

        //添加布局监听器
        //布局的每次改变都会调用实现的回调方法，所以说是比较耗费性能的
        rv.getViewTreeObserver().addOnGlobalLayoutListener(this);

        //注册发布订阅框架
        EventBus.getDefault().register(this);

        //进入了聊天回话
        //标记后
        //不会显示新消息到通知栏
        //同时也会清除原来显示的消息

        //可以看到该SDK，默认会显示收到的消息到通知栏，但如果正在和这个人聊天，这条消息就不应该显示到通知栏，
        // 只需要调用聊天SDK方法，设置进入聊天了，进入后该消息就不会显示；或退出聊天了，退出了，消息就会显示到通知栏。
        JMessageClient.enterSingleConversation(id);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //移除布局管理器
        rv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        //取消发布订阅框架
        EventBus.getDefault().unregister(this);

        //退出了回话(退出会话后，就可以显示到通知栏里面了)
        JMessageClient.exitConversation();
    }

    private void fetchData() {
        //获取会话中所有消息
        //消息按照时间升序排列
        List<Message> messages = conversation.getAllMessage();
        //这里就不在实现分页了
        //因为评论列表已经讲解了
        if (messages == null) {
            //因为这个会话中可能没有数据
            adapter.setDatum(new ArrayList<>());
        } else {
            adapter.setDatum(messages);
        }
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

        String content = et_content.getText().toString().trim();

        if (StringUtils.isEmpty(content)) {
            //提示： 请输入你要发送的消息
            ToastUtil.errorShortToast(R.string.hint_enter_message);
            return;
        }
        //创建文本消息
        Message message = conversation.createSendTextMessage(content);

        //设置消息监听器
        setSendMessageCallback(message);

        //发送文本消息
        JMessageClient.sendMessage(message);
    }

    /**
     * 设置消息监听器
     *
     * @param message Message
     */
    private void setSendMessageCallback(Message message) {
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    //发送成功

                    LogUtil.d(TAG, "send message success");
                    addMessage(message);

                    //清空输入框
                    clearInput();

                } else {
                    //发送失败
                    LogUtil.d(TAG, "send message failed:" + responseCode + "," + responseMessage);

                    //弹出提示
                    ToastUtil.successShortToast(R.string.error_send_message);
                }
            }
        });
    }

    /**
     * 清空输入框
     */
    private void clearInput() {
        et_content.setText("");
    }

    /**
     * 添加消息到列表
     *
     * @param data Message
     */
    private void addMessage(Message data) {
        //将消息添加到列表后面
        //这样列表就会刷新这条数据
        adapter.addData(data);

        //滚动到底部（发送数据完成后,才滚动到底部）
        scrollBottom();
    }

    /**
     * 滚动到底部
     * rv.post： View已经onLayout完毕了(里面的item已经添加完毕后才行)
     */
    private void scrollBottom() {
        //之所以用这个方法，因为rv.post方法执行是在添加item 后才执行，也就是rv里面的item加载完成后才用这个方法
        rv.post(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("是否是主线程: ", "" + HandlerUtil.isMainThread());
                //使用动画滚动到底部(这里传递集合的大小，说明滚动到集合的size-1 个，也就是最后一个)
                rv.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    /**
     * 布局改变了
     * RecyclerView布局监听器
     * <p>
     * 第一次进来，如果发生布局改变了（也就是item消息变多了）,那么就滚动到底部
     * 注意： 第一次进来的时候，并没有和原来的RecyclerView(也就是销毁的RecyclerView)比较
     * 只要第一次进啦，添加了数据，那么就会滚动到底部
     */
    @Override
    public void onGlobalLayout() {

        //滚动到底部()
        scrollBottom();

        //之所以在这里实现
        //是因为像文本消息没什么问题
        //直接就可以滚动到底部
        //但图片就有可能有问题
        //因为图片是异步加载的
        //也就是说有可能界面滚动时
        //图片还没显示出来
        //等图片显示出来后
        //就不是界面底部了
    }

    /**
     * 有新消息了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessageEvent(OnNewMessageEvent event) {
        //获取消息
        Message data = event.getData();

        //获取消息发送人
        UserInfo user = data.getFromUser();

        //id： 聊天对方人的 id
        //user.getUserName()： 发送人的id
        //如果发送人的id 不是等于聊天对方人的id，那就直接返回
        //在ChatActivity界面 表示我是接受方； 当前聊天节的id(也就是聊天对方的id)
        //                                 user.getUserName(): 我接收的消息对方的id
        if (!user.getUserName().equals(id)) {
            //不是这个人的消息
            //就不能显示
            return;
        }

        //设置为已读，并添加数据到列表 (而有些是需要用户看到，才能是已读的: 这个只需要在adapter里面调用这个方法，也就是显示的时候才调用它)
        data.setHaveRead(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //添加到列表
                adapter.addData(data);
            }
        });

    }
}
