package com.ixuea.courses.mymusicold.domain.response;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
