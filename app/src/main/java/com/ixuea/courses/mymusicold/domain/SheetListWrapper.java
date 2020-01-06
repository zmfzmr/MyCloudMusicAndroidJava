package com.ixuea.courses.mymusicold.domain;

import java.util.List;

/**
 * 歌单列表模型
 * <p>
 * 只是用来测试
 * <p>
 * 地址：http://dev-my-cloud-music-api-rails.ixuea.com/v1/sheets
 * 后面没有数字，有数字的是具体某个歌单
 */
public class SheetListWrapper {
    /**
     * 歌单列表
     */
    private List<Sheet> data;

    public List<Sheet> getData() {
        return data;
    }

    public void setData(List<Sheet> data) {
        this.data = data;
    }
}
