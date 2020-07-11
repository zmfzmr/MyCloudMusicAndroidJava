package com.ixuea.courses.mymusic.util;

import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;

/**
 * 消息工具类
 */
public class MessageUtil {
    /**
     * 获取聊天内容
     *
     * @param data
     * @return
     */
    public static String getContent(MessageContent data) {
        if (data instanceof ImageContent) {
            //消息是图片内容
            return "[图片]";
        } else if (data instanceof TextContent) {
            //消息是文本内容，向下转型并获取文字返回
            return ((TextContent) data).getText();
        }
        return "[其他消息]";
    }
}
