package com.ixuea.courses.mymusic.domain.response;

/**
 * 分页数据
 * （这个Meta并不需要在界面中去传递，所以并不需要实现Serializable这种接口）
 */
public class Meta {
    private int current_page;//当前页数
    private int next_page;//下一页
    //这里上一页和下一页 设置为Integer(没有上一页和下一页的时候，为null)
    private Integer prev_page;//上一页
    private Integer total_pages;//总分页数
    private int total_count;//总数据量

    /**
     * 获取下一页(也就是page = 1  中page中对应的键值：如1)
     * 当前Meta对象中有next_page这个字段，直接用这个就行
     * 如果没有，则用page+1 (当前页+1)
     *
     * @param page Meta 分页模型
     * @return
     */
    public static int nextPage(Meta page) {
        if (page != null) {
            //如果有分页模型（也就是有返回的Meta对象）
            //那么当前页+1
            //注意：在CommentActivity中loadMore第一次调用的时候，这个传入的Meta为null，那么默认返回1
            //也就是page=1，中的1（注意：这里是用传递的Meta对象的current_page +1）
            return page.getCurrent_page() + 1;
        }
        return 1;//这个值是，参数字段page=1，中的1(第一页)
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getNext_page() {
        return next_page;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public Integer getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(Integer prev_page) {
        this.prev_page = prev_page;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}

/*

    "meta": {
        "current_page": 1,
        "next_page": 2,
        "prev_page": null,
        "total_pages": 11,
        "total_count": 102
    }

 */
