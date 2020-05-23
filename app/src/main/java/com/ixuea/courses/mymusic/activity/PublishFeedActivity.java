package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.event.OnFeedChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布动态界面
 */
public class PublishFeedActivity extends BaseTitleActivity implements TextWatcher {

    private static final String TAG = "PublishFeedActivity";

    /**
     * 输入框
     */
    @BindView(R.id.et_content)
    EditText et_content;

    /**
     * 当前位置
     */
    @BindView(R.id.tv_position)
    TextView tv_position;

    /**
     * 字数统计
     */
    @BindView(R.id.tv_count)
    TextView tv_count;
    private String content;//输入的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //添加输入框监听器
        et_content.addTextChangedListener(this);
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

        //获取输入的内容
        content = et_content.getText().toString().trim();

        //判断是否输入了
        if (StringUtils.isBlank(content)) {
            //提示: 请输入你想说的
            ToastUtil.errorShortToast(R.string.hint_feed);
            return;
        }

        //判断长度
        //至于为什么是140
        //市面上大部分软件都是这样
        //大家感兴趣可以搜索下   因为EditText限制 最大字符 maxLength = 140，所以内容不可能大于140的，
        // 所以下面的不用写了
//        if (content.length() > 140) {
//            //提示 内容不能超过140字符
//            ToastUtil.errorShortToast(R.string.error_content_length);
//            return;
//        }

        saveFeed();
    }

    /**
     * 保存动态
     */
    private void saveFeed() {
        Feed feed = new Feed();

        //设置内容
        //因为Feed里面有个content(String类型) 我们直接把内容传递到这个字符串里面
        //网络请求的时候以转换成json传递
        feed.setContent(content);

        //调用接口保存
        Api.getInstance()
                .createFeed(feed)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        //发布通知
                        EventBus.getDefault().post(new OnFeedChangedEvent());

                        //关闭界面
                        finish();
                    }
                });
    }

    /**
     * 选择图片快捷按钮点击
     */
    @OnClick(R.id.ib_select_image)
    public void onSelectImageClick() {
        LogUtil.d(TAG, "onSelectImageClick");
    }


    //输入框监听器

    /**
     * 文本改变前
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 文本改变了
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 文本改变后
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        String info = getResources().getString(R.string.feed_count, s.toString().length());
        tv_count.setText(info);

    }
    //end 输入框监听器
}
