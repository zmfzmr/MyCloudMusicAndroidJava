package com.ixuea.courses.mymusic.domain.response;

import java.util.List;

/**
 * 解析列表网络请求
 */
public class ListResponse<T> extends BaseResponse {

    /**
     * 定义一个列表
     * <p>
     * 里面的对象使用了泛型
     */
    private List<T> data;//名字要和服务器端的一样

    private Meta meta;//分页模型

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
/*
    分页模型对应的json数据
    "meta": {
        "current_page": 1,
        "next_page": 2,
        "prev_page": null,
        "total_pages": 11,
        "total_count": 102
        },
 */