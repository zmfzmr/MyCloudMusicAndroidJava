package com.ixuea.courses.mymusic.domain;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;

/**
 * 标题
 */
public class Title extends BaseMultiItemEntity {
    /**
     * 标题
     */
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 返回Item类型
     * <p>
     * 对应现在使用的BaseRecyclerViewAdapterHelper框架
     * 如果要显示多类型这就是固定写法
     *
     * @return Item类型
     */
    @Override
    public int getItemType() {
        //直接导入静态的变量
        return TYPE_TITLE;
    }
}
