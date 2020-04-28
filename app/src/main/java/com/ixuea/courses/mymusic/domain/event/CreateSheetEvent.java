package com.ixuea.courses.mymusic.domain.event;

/**
 * 创建歌单事件
 */
public class CreateSheetEvent {
    private String data;//歌单名称

    /**
     * 构造方法
     *
     * @param data
     */
    public CreateSheetEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
