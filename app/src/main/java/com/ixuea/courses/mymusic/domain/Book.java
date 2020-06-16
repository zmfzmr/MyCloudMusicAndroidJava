package com.ixuea.courses.mymusic.domain;

/**
 * 图书模型
 */
public class Book extends BaseModel {
    private String title;//标题
    private String banner;//封面
    private double price;//价格  注意：是double类型
    private User user;//谁创建了该商品  （我们客户端并没有实现谁创建）

    private Integer buy;//有值表示已经购买

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    /**
     * 是否购买了
     */
    public boolean isBuy() {
        return buy != null;
    }
}
