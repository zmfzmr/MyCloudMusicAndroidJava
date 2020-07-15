package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.manager.impl.UserManager;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.StringUtil;

import androidx.annotation.NonNull;
import butterknife.BindView;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_IMAGE_LEFT;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_IMAGE_RIGHT;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TEXT_LEFT;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TEXT_RIGHT;

/**
 * 聊天界面适配器
 * <p>
 * 注意： Message 就是：cn.jpush 包下的
 */
public class ChatAdapter extends BaseRecyclerViewAdapter<Message, ChatAdapter.ViewHolder> {
    private final UserManager userManager;//用户管理器

    /**
     * 构造方法
     *
     * @param context
     */
    public ChatAdapter(Context context) {
        super(context);

        //初始化用户管理器
        userManager = UserManager.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //这里要区分是我发送的
        //还是其他人发送的

        //我发送的显示在右边
        //其他人发送的显示在左边

        //他们的布局都一样只是方向不一样
        //所以可以用同一个ViewHolder
        switch (viewType) {
            case TYPE_TEXT_LEFT:
                //图片的那个ViewHolder 已经在TextViewHolder里面实现了
                return new TextViewHolder(getInflater().inflate(R.layout.item_chat_text_left, parent, false));
            default:
                //我的item布局 加载右边的那个
                return new TextViewHolder(getInflater().inflate(R.layout.item_chat_text_right, parent, false));
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.bindData(getData(position));
    }

    /**
     * 返回view类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Message data = getData(position);

        //获取消息方向(显示在左边还是在右边)
        MessageDirect messageDirect = data.getDirect();

        //获取消息内容(也可以直接一步到位，直接获取消息类型data.getContentType())
        MessageContent messageContent = data.getContent();

        if (messageContent instanceof ImageContent) {
            //图片信息
            return messageDirect == MessageDirect.send ? TYPE_IMAGE_RIGHT : TYPE_IMAGE_LEFT;
        }

        //TODO 其他消息可以继续在这里扩展

        //文本消息
        //我发送的消息在右边

        return messageDirect == MessageDirect.send ? TYPE_TEXT_RIGHT : TYPE_TEXT_LEFT;
    }

    /**
     * 聊天消息公共ViewHolder
     * 例如头像
     * <p>
     * 注意： Message 就是：cn.jpush 包下的
     */
    abstract class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder<Message> {

        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;//头像

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         *
         * @param data D 是 类class ViewHolder<D> 中的D数据
         *
         *             Message 就是：cn.jpush 包下的
         */
        @Override
        public void bindData(Message data) {
            super.bindData(data);

            //显示当前消息发送人
            // data.getFromUser(): UserInfo对象 也就是谁发送的
            //data.getFromUser().getUserName(): 发送人的用户id
            String userId = data.getFromUser().getUserName();
            //去掉外层包裹
            userId = StringUtil.unwrapUserId(userId);
            userManager.getUser(userId, userData -> {
                //显示头像
                ImageUtil.showAvatar(context, iv_avatar, userData.getAvatar());
            });
        }
    }

    /**
     * 文本消息
     */
    class TextViewHolder extends ChatAdapter.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tv_content;//文本控件

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Message data) {
            super.bindData(data);
            //转成文本内容控件
            TextContent content = (TextContent) data.getContent();
            tv_content.setText(content.getText());

            //真实项目中
            //可能还会实现像评论那边的mention和hashTag
            //因为在评论那边讲解了
            //所以这里就不在重复讲解了
        }
    }

    /**
     * 图片消息 ViewHolder
     * ChatAdapter.ViewHolder 的后面不需要<Message>了，因为父类已经传入了
     */
    class ImageViewHolder extends ChatAdapter.ViewHolder {

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据方法
         *
         * @param data D 是 类class ViewHolder<D> 中的D数据
         */
        @Override
        public void bindData(Message data) {
            super.bindData(data);
        }
    }
}
