package com.ixuea.courses.mymusic.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 用户适配器
 */
public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, User data) {
        //头像
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);

        ImageUtil.showAvatar(mContext, iv_avatar, data.getAvatar());
        //昵称
        helper.setText(R.id.tv_title, data.getNickname());
        //描述
        helper.setText(R.id.tv_info, data.getDescriptionFormat());

    }
}
