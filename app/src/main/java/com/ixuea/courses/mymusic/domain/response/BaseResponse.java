package com.ixuea.courses.mymusic.domain.response;

/**
 * 通用网络请求响应模型
 * <p>
 * 前面看到每个网络请求，最外层都有可能有，message，status两个字段，他们是必要的时候才有，
 * 还有一个data字段，只是不同的接口，类型不一样；所以我们可以创建一个BaseResponse。
 */
public class BaseResponse {

    /**
     * 状态码
     * <p>
     * 只有发生了错误才会有
     * <p>
     * 如果用int类型的话，全局变量会默认初始化，这个值就默认为0了
     * 而我们不想为0，让发生了错误的时候才会值（不想有值，出错了才有值）
     * 所以我们这里定义为引用类型Integer，就会默认初始化为null
     */
    private Integer status;

    /**
     * 出错的提示信息
     * <p>
     * 发生了错误不一定有
     */
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
