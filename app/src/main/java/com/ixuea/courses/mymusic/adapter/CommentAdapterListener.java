package com.ixuea.courses.mymusic.adapter;

import com.ixuea.courses.mymusic.domain.Comment;

/**
 * 评论适配器监听器
 */
public interface CommentAdapterListener {
    /**
     * 头像点击了
     * Comment：这里对象里面有个user（里面有用户id），如果有需要可以从Comment->user-id 获取
     *
     * @param data
     */
    void onAvatarClick(Comment data);

    /**
     * 点赞回调
     */
    void onLikeClick(Comment data);
}
