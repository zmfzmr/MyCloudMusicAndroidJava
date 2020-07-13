package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.listener.UserListener;
import com.ixuea.courses.mymusic.manager.impl.UserManager;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.MessageUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import androidx.annotation.NonNull;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

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

        //---------------------------
        //思路： Chat2Activity initDatum 发送消息时候，传递了一个用户d(对方用户的id)
        //      -->根据id我们获取到目标用户对象(比如UserInfo)
        //      -->根据UserInfo对象 的getUserName() 获取到id然后在到用户管理器中获取用户信息

        //获取头像控件
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);

        //获取会话目标用户
        //如果是单聊是聊天对象的UserInfo
        //如果是群聊则是聊天对象群的GroupInfo
        //如果是聊天室类型会话则是聊天室的ChatRoomInfo
        //API可以通过这里查看：https://docs.jiguang.cn/jmessage/client/im_android_api_docs/

        UserInfo user = (UserInfo) data.getTargetInfo();

        //获取用户信息
        UserManager.getInstance(mContext)
                //user: UserInfo 对象
                // 这里的user.getUserName() ： 因为我们在Chat2Activity 中 initDatum 中传递的就是用户id
                .getUser(StringUtil.unwrapUserId(user.getUserName()), new UserListener() {
                    @Override
                    public void onUser(User userData) {
                        //显示头像 userData: 参数里面的User对象（UserManager getUser方法中传递过的）
                        ImageUtil.showAvatar(mContext, iv_avatar, userData.getAvatar());

                        //昵称
                        helper.setText(R.id.tv_nickname, userData.getNickname());
                    }
                });

        //---------------------------
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
