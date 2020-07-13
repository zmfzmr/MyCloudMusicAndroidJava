package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.MessageUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import androidx.annotation.NonNull;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * 会话适配器
 * <p>
 * Conversation Jiguang SDK cn.jpush..包中的模型类
 */
public class ConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {

    public ConversationAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Conversation data) {

        //获取最后一条消息
        Message latestMessage = data.getLatestMessage();

        if (latestMessage == null) {
            //清空时间和消息
            helper.setText(R.id.tv_time, "");
            //清空消息内容
            helper.setText(R.id.tv_info, "");
        } else {
            //显示最后一条消息的时间和内容

            //最后一条消息的 时间
            long time = latestMessage.getCreateTime();
            helper.setText(R.id.tv_time, TimeUtil.commonFormat(time));
            helper.setText(R.id.tv_info, MessageUtil.getContent(latestMessage.getContent()));

        }

        //未读消息数
        int count = data.getUnReadMsgCnt();
        if (count > 0) {
            //显示出来(显示未读消息数控件)
            helper.setVisible(R.id.tv_count, true);
            //显示数据
            helper.setText(R.id.tv_count, StringUtil.formatMessageCount(count));
        } else {
            //显示未读消息数控件
            helper.setVisible(R.id.tv_count, false);
        }

    }
}
