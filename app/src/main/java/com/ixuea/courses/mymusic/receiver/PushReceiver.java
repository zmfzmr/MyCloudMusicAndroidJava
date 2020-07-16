package com.ixuea.courses.mymusic.receiver;

import android.content.Context;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.domain.RemoteMessage;
import com.ixuea.courses.mymusic.util.JsonUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.service.JPushMessageReceiver;

import static com.ixuea.courses.mymusic.domain.RemoteMessage.STYLE_LOGOUT;

/**
 * 极光推送广播接收者
 * <p>
 * 数据格式：{"style":0} : 表示我们后面实现的话，用这种方式来推送
 */
public class PushReceiver extends JPushMessageReceiver {

    private static final String TAG = "PushReceiver";

    /**
     * 当后台推送自定义消息时调用
     * <p>
     * 以前我们使用的话，用的onReceive方法，而我们自定义消息的娿，用这个OnMessage方法
     *
     * @param context
     * @param customMessage
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);

        try {
            //对应控制台自定义消息推送内容
            //对应api的message字段
            //获取推送的自定义消息
            //它是一个json，它的格式可以随意推送；推送字符串 json，当然我们后面推送的是json
            String messageString = customMessage.message;

            //打印日记
            LogUtil.d(TAG, "onMessage:" + messageString);

            if (StringUtils.isNotBlank(messageString)) {
                //有推送消息

                //用JSON解析(把json解析成一个对象上面)
                RemoteMessage message = JsonUtil.formJson(messageString, RemoteMessage.class);

                switch (message.getStyle()) {
                    case STYLE_LOGOUT:
                        //退出事件

                        //将事件代理到Application
                        AppContext.getInstance().onRemoteLogout();
                        break;
                    default:
                        //暂时还不支持的类型
                        //这里类型属于警告
                        //因为虽然有错误
                        //当不是很严重
                        LogUtil.w(TAG, "push message style unknown:" + message);
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            //因为后台如果发送的不是json的话，客户端目前还没有发现出错，所以这里捕获以下异常
            LogUtil.w(TAG, "push error:" + e.getLocalizedMessage());
        }

    }
}
