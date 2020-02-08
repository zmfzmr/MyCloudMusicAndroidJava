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

    /**
     * 构造方法
     *
     * @param title 标题
     */
    public Title(String title) {
        this.title = title;
    }

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
     *
     * 根据返回类型区分创建了多少个布局：比如DiscoveryFragment创建了1个title，添加到集合中，
     * 然后adapter.replaceData(datum)，DiscoveryAdapter适配器根据添加的布局类型（比如TYPE_TITLE），
     * 来判断到底需要创建多少个title布局
     */
    @Override
    public int getItemType() {
        //直接导入静态的变量
        return TYPE_TITLE;
    }
}
