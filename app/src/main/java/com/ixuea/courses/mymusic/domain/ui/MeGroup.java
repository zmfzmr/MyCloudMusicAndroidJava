package com.ixuea.courses.mymusic.domain.ui;

import com.ixuea.courses.mymusic.domain.Sheet;

import java.util.List;

/**
 * UI相关的类
 * <p>
 * 首页-我的界面分组模型
 */
public class MeGroup {
    private String title;//标题
    private List<Sheet> datum;//列表数据
    private boolean isMore;//是否显示右侧按钮

    /**
     * 构造方法
     *
     * @param title
     * @param datum
     * @param isMore
     */
    public MeGroup(String title, List<Sheet> datum, boolean isMore) {
        this.title = title;
        this.datum = datum;
        this.isMore = isMore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Sheet> getDatum() {
        return datum;
    }

    public void setDatum(List<Sheet> datum) {
        this.datum = datum;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
}
