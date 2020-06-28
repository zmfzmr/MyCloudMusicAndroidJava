package com.ixuea.courses.mymusic.domain;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 搜索历史模型
 * Table:指定这是一张表
 */
@Table("search_history")
public class SearchHistory {

    /**
     * 标题
     * PrimaryKey:主键
     * BY_MYSELF:使用该字段的值 作为主键
     * 还可以指定自动生成
     */
    @PrimaryKey(AssignType.BY_MYSELF)
    private String content;


    /**
     * 创建时间
     *
     * @ NotNull:不为空  这个一定要导入 orm框架里面的类
     */
    @NotNull
    private long created_at;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
