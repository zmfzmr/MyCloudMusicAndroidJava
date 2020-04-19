package com.ixuea.courses.mymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 话题详情
 */
public class TopicDetailActivity extends BaseTitleActivity {
    /**
     * 通过话题标题显示话题详情
     *
     * @param context 上下文
     * @param title   标题
     */
    public static void startWithTitle(Context context, String title) {
        //创建intent
        Intent intent = new Intent(context, TopicDetailActivity.class);

        //传递数据
        intent.putExtra(Constant.TITLE, title);

        //启动界面
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
    }
}
