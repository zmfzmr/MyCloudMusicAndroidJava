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

    /**
     * 字母 数组
     */
    private String[] letters;
    /**
     * 字母索引 数组
     */
    private Integer[] indexes;

    /**
     * 构造方法
     *
     * @param datum
     * @param letters
     * @param indexes
     */
    public UserResult(List<Object> datum, String[] letters, Integer[] indexes) {
        this.datum = datum;
        this.letters = letters;
        this.indexes = indexes;
    }

    public List<Object> getDatum() {
        return datum;
    }

    public void setDatum(List<Object> datum) {
        this.datum = datum;
    }

    public String[] getLetters() {
        return letters;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    public Integer[] getIndexes() {
        return indexes;
    }

    public void setIndexes(Integer[] indexes) {
        this.indexes = indexes;
    }
}
