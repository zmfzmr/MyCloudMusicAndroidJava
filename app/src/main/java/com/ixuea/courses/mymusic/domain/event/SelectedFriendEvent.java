package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.User;

/**
 * 选择了好友事件
 */
public class SelectedFriendEvent {

    private User user;//用户

    public SelectedFriendEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
