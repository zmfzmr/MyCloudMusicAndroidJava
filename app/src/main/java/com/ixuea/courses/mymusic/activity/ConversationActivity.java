package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * 消息(会话) 界面
 */
public class ConversationActivity extends BaseTitleActivity {

    private static final String TAG = "ConversationActivity";
    @BindView(R.id.rv)
    RecyclerView rv;

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

    /**
     * 获取会话列表数据
     */
    private void fetchData() {
        //获取会话数据
        List<Conversation> datum = JMessageClient.getConversationList();
        if (datum != null) {
            LogUtil.d(TAG, "fetchData:" + datum.size());
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
    }
}
