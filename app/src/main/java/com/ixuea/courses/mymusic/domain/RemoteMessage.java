package com.ixuea.courses.mymusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 推送消息模型
 */
public class RemoteMessage {
    /**
     * 其他设备登录了
     * 当前设备需要退出事件
     * 0：表示退出
     */
    public static final int STYLE_LOGOUT = 0;

    /**
     * 消息类型
     */
    private int style;

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("style", style)
                .toString();
    }
}
