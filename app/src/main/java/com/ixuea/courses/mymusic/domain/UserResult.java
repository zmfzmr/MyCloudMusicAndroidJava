package com.ixuea.courses.mymusic.domain;

import java.util.List;

/**
 * 处理完用户数据模型
 */
public class UserResult {
    /**
     * 用来在列表显示的
     * 注意:是Object类型
     */
    private List<Object> datum;

    public UserResult(List<Object> datum) {
        this.datum = datum;
    }

    public List<Object> getDatum() {
        return datum;
    }

    public void setDatum(List<Object> datum) {
        this.datum = datum;
    }
}
