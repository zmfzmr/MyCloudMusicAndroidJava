package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.Topic;

/**
 * 选中了话题事件
 */
public class SelectedTopEvent {
    private Topic data;

    /**
     * 构造方法（传入Topic）
     *
     * @param data
     */
    public SelectedTopEvent(Topic data) {
        this.data = data;
    }

    public Topic getData() {
        return data;
    }

    public void setData(Topic data) {
        this.data = data;
    }
}