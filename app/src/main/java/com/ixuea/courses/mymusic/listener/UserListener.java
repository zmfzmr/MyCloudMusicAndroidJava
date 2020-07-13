package com.ixuea.courses.mymusic.listener;

import com.ixuea.courses.mymusic.domain.User;

/**
 * 用户接口
 */
public interface UserListener {
    /**
     * 获取获取成功
     *
     * @param data
     */
    void onUser(User data);
}
