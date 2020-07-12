package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.ViewUtil;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 消息(会话) 界面
 */
public class ConversationActivity extends BaseTitleActivity {

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
}
