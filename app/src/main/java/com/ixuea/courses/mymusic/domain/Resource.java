package com.ixuea.courses.mymusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 资源
 * 将资源放到单独的对象中
 * 好处是后面还可扩展更多的字段
 * 例如：资源类型；资源大小；资源备注
 * <p>
 * 注意： 这里不用继承BaseModel   因为这个对象是Feed 集合里的单个对象
 */
public class Resource {
    private String uri;//相对地址 如  "uri": "assets/4.jpg"

    /**
     * 防止json解析的时候用到这个无参的
     */
    public Resource() {
    }

    public Resource(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uri", uri)
                .toString();
    }
}
