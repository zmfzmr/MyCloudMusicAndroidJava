package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.Sheet;

/**
 * 选择了歌单事件
 */
public class OnSelectSheetEvent {
    private Sheet data;

    /**
     * 构造方法
     *
     * @param data
     */
    public OnSelectSheetEvent(Sheet data) {
        this.data = data;
    }

    public Sheet getData() {
        return data;
    }

    public void setData(Sheet data) {
        this.data = data;
    }
}
