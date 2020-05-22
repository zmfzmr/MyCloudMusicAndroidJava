package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 发布动态界面
 */
public class PublishFeedActivity extends BaseTitleActivity {

    private static final String TAG = "PublishFeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return true;
    }

    /**
     * 按钮点击了
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            //发布按钮
            onSendMessageClick();

            return true;//返回true 表示处理了事件
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布按钮点击
     */
    private void onSendMessageClick() {
        LogUtil.d(TAG, "onSendMessageClick");
    }
}
