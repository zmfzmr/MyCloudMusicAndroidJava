package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 好友列表适配器
 */
public class FriendAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public FriendAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 绑定数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, User data) {

        //获取头像(这里获取的是ImageView，而获取的是CircleImageView，因为并没有用到子类里面的方法，用ImageView就行了)
        ImageView iv_banner = helper.getView(R.id.iv_banner);

        //显示头像(data: User)
        ImageUtil.showAvatar((Activity) mContext, iv_banner, data.getAvatar());

        //昵称
        helper.setText(R.id.tv_title, data.getNickname());

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
        helper.setText(R.id.tv_info, data.getDescriptionFormat());
    }
}
