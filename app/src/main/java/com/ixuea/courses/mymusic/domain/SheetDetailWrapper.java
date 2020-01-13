package com.ixuea.courses.mymusic.domain;

/**
 * 歌单详情包裹对象
 * <p>
 * 只是用来测试
 */
public class SheetDetailWrapper {

    /**
     * 歌单详情
     */
    private Sheet data;

    //这里返回的是单个歌单Sheet，//SheetListWrapper那边返回的是多个歌单，也就是list，
    public Sheet getData() {
        return data;
    }

    public void setData(Sheet data) {
        this.data = data;
    }
}
