package com.ixuea.courses.mymusicold.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 登录后模型（使用POST请求，返回来的json信息模型）
 * <p>
 * 这个看情况是否需要继承BaseModel
 */
public class Session {
    /**
     * 用户Id
     */
    private String user;
    /**
     * 登录后的Session
     */
    private String session;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    //这里用了 commons lang3包
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("session", session)
                .toString();
    }

}
