package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ConversationAdapter;
import com.ixuea.courses.mymusic.domain.event.OnNewMessageEvent;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 消息(会话) 界面
 */
public class ConversationActivity extends BaseTitleActivity {

    private static final String TAG = "ConversationActivity";
    @BindView(R.id.rv)
    RecyclerView rv;
    private ConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }

    @Override
    protected void initView() {
        super.initView();
        //初始化RecyclerView
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new ConversationAdapter(R.layout.item_conversation);

        //设置适配器
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的数据
                Conversation data = (Conversation) adapter.getItem(position);

                //获取目标用户
                UserInfo user = (UserInfo) data.getTargetInfo();

                //跳转到聊天界面 user.getUserName(): 这个是其实就是发送方的用户id
                Chat2Activity.start(getMainActivity(), user.getUserName());
            }
        });
    }

    /**
     * 获取会话列表数据
     */
    private void fetchData() {
        //获取会话数据
        List<Conversation> datum = JMessageClient.getConversationList();
        if (datum == null) {
            //没有消息

            //情况数据(传入一个空数据，把原来的清空)
            adapter.replaceData(new ArrayList<>());
        } else {
            LogUtil.d(TAG, "fetchData:" + datum.size());

            //设置数据
            adapter.replaceData(datum);
        }
    }

    /**
     * 界面显示了
     * <p>
     * 放在这里的目前是 退出后台，重新进入这个页面，可以重新刷新界面(有消息进来可以接收的到)
     */
    @Override
    protected void onResume() {
        super.onResume();
        //获取会话列表数据
        fetchData();

        //注册发布订阅框架
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //取消发布订阅框架
        EventBus.getDefault().unregister(this);
    }

    /**
     * 有新消息了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessageEvent(OnNewMessageEvent event) {
        //这个只需要刷新下界面就可以了
        fetchData();
    }


}
