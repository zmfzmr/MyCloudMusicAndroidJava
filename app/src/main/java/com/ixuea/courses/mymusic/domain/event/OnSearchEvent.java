package com.ixuea.courses.mymusic.domain.event;

/**
 * 搜索事件
 */
public class OnSearchEvent {
    private String data;//搜索关键字
    private int selectedIndex;//当前显示界面的索引

    public OnSearchEvent(String data, int selectedIndex) {
        this.data = data;
        this.selectedIndex = selectedIndex;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
