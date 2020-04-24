package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;
import butterknife.BindView;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_USER;

/**
 * 好友列表适配器
 * <p>
 * 这里后面：BaseRecyclerViewAdapter.ViewHolder<Objecct> 后面默认泛型就是Object,所以可以把后面的泛型去掉
 * 泛型第二个参数：用的是父类的ViewHolder
 */
//public class FriendAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
public class FriendAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewAdapter.ViewHolder> {
    /**
     * @param context 这里Context父类已经保存到成员变量了，所以子类可以直接使用
     */
    public FriendAdapter(Context context) {
        super(context);
    }

//    /**
//     * 绑定数据
//     */
//    @Override
//    protected void convert(@NonNull BaseViewHolder helper, User data) {
//
//        //获取头像(这里获取的是ImageView，而获取的是CircleImageView，因为并没有用到子类里面的方法，用ImageView就行了)
//        ImageView iv_banner = helper.getView(R.id.iv_banner);
//
//        //显示头像(data: User)
//        ImageUtil.showAvatar((Activity) mContext, iv_banner, data.getAvatar());
//
//        //昵称
//        helper.setText(R.id.tv_title, data.getNickname());
//
//        //描述信息
//        /*
//         * User:对象里面的方法
//         *
//         *  public String getDescriptionFormat() {
//         *         if (TextUtils.isEmpty(description)) {
//         *             return "这个人很懒，没有填写个人介绍!";
//         *         }
//         *         return description;
//         *     }
//         *
//         * description：这个description字段是我们自己设置的，所以默认返回 这个人很懒，没有填写个人介绍
//         */
//        helper.setText(R.id.tv_info, data.getDescriptionFormat());
//    }

    /**
     * 返回ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Constant TYPE_TITLE = 0
        if (TYPE_TITLE == viewType) {
            //创建标题ViewHolder
            //参数3：false，表示不要添加到parent(ViewGroup上)，这个要ViewHolder帮我们处理，如果传入true会出问题
            return new FriendAdapter.TitleViewHolder(getInflater().inflate(R.layout.item_title_small, parent, false));
        }
        //创建用户ViewHolder
        return new FriendAdapter.UserViewHolder(getInflater().inflate(R.layout.item_user, parent, false));
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //获取当前用户的数据
        Object data = getData(position);

        //只要onCreateViewHolder返回的是哪个ViewHolder，这里的就是那个ViewHolder
        holder.bindData(data);
    }

    /**
     * 返回View类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //获取当前位置数据 (getData（）是父类封装的方法：里面调用了datum.get(position))
        Object data = getData(position);

        if (data instanceof String) {
            //返回标题值 0
            return TYPE_TITLE;//==0
        }
        //用户值 3
        return TYPE_USER;//==3
    }

    /**
     * 标题ViewHolder
     */
    public class TitleViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;//标题

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         */
        @Override
        public void bindData(Object data) {
            super.bindData(data);

            //标题
            tv_title.setText((String) data);
        }
    }

    /**
     * 用户ViewHolder
     */
    public class UserViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.iv_banner)
        ImageView iv_banner;//头像

        @BindView(R.id.tv_title)
        TextView tv_title;//昵称

        @BindView(R.id.tv_info)
        TextView tv_info;//描述

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         */
        @Override
        public void bindData(Object data) {
            super.bindData(data);
            //转为真实类型
            User user = (User) data;

            //显示头像(data: User)
            ImageUtil.showAvatar((Activity) context, iv_banner, user.getAvatar());

            //昵称
            tv_title.setText(user.getNickname());

            //描述信息
            /*
             * User:对象里面的方法
             *
             *  public String getDescriptionFormat() {
             *         if (TextUtils.isEmpty(description)) {
             *             return "这个人很懒，没有填写个人介绍!";
             *         }
             *         return description;
             *     }
             *
             * description：这个description字段是我们自己设置的，所以默认返回 这个人很懒，没有填写个人介绍
             */
            tv_info.setText(user.getDescriptionFormat());
        }
    }
}
