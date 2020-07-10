package com.ixuea.courses.mymusic.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.LoginEvent;
import cn.jiguang.analytics.android.api.RegisterEvent;

/**
 * 统计相关工具类
 */
public class AnalysisUtil {
    /**
     * 登录事件
     *
     * @param context Context
     * @param success 是否登录成功
     * @param method  登录/注册方式  返回qq(qq登录)  weibo(微博登录)  phone(手机号登录)  email(邮箱登录)
     * @param phone   手机号
     * @param email   邮箱
     * @param qqId    qqid
     * @param weiboId weibo id
     */
    public static void onLogin(Context context, boolean success,
                               String method, String phone,
                               String email, String qqId, String weiboId) {
        //创建登录事件
        //参数1：登录类型
        //参数2：是否登录成功
        LoginEvent event = new LoginEvent(method, success);

        //获取拓展信息
        //获取email qqId weiboId 上传到极光平台等第三方平台可能需要加密，我们这里没有加密
        Map<String, String> extra = getExtra(null, null, phone, email, qqId, weiboId);

        //添加拓展信息(这个信息并一定要添加，只是我们这里要传递给极光后端平台，所以需要添加的)
        event.addExtMap(extra);

        //记录事件(前面是创建了一个事件，这里可以理解为将这事件发送到服务端)
        //调试模式是实时发送的，不是调试的，那在合适的时候发送上去
        JAnalyticsInterface.onEvent(context, event);
    }

    /**
     * 注册事件
     *
     * @param context
     * @param success
     * @param method
     * @param avatar
     * @param nickname
     * @param phone
     * @param email
     * @param qqId
     * @param weiboId  比onLogin 多了2个参数： nickname 和avatar
     */
    public static void onRegister(Context context, boolean success,
                                  String method, String avatar,
                                  String nickname, String phone,
                                  String email, String qqId,
                                  String weiboId) {
        //创建事件
        //参数1：注册类型
        //参数2：是否注册成功
        RegisterEvent event = new RegisterEvent(method, success);

        //获取拓展信息
        Map<String, String> extra = getExtra(avatar, nickname, phone, email, qqId, weiboId);

        //添加拓展信息
        event.addExtMap(extra);

        //记录事件(前面是创建了一个事件，这里可以理解为将这事件发送到服务端)
        //调试模式是实时发送的，不是调试的，那在合适的时候发送上去
        JAnalyticsInterface.onEvent(context, event);
    }

    /**
     * 获取拓展信息
     *
     * @param avatar
     * @param nickname
     * @param phone
     * @param email
     * @param qqId
     * @param weiboId
     */
    private static Map<String, String> getExtra(String avatar, String nickname,
                                                String phone, String email,
                                                String qqId, String weiboId) {

        //创建map
        HashMap<String, String> map = new HashMap<>();

        //头像
        if (StringUtils.isNotBlank(avatar)) {
            map.put("avatar", avatar);
        }

        //昵称
        if (StringUtils.isNotBlank(nickname)) {
            map.put("nickname", nickname);
        }

        //手机号
        if (StringUtils.isNotBlank(phone)) {
            map.put("phone", phone);
        }

        //邮件
        if (StringUtils.isNotBlank(email)) {
            map.put("email", email);
        }

        //qq(qq id  这个id被泄漏后，第三方人员是可以直接登录的
        //    所以可以加密后再上传)  可以用我们前面的签名SHA1或者AES加密
        if (StringUtils.isNotBlank(qqId)) {
            map.put("qq", qqId);
        }

        //weibo
        // (weibo id  这个id被泄漏后，第三方人员是可以直接登录的
        //    所以可以加密后再上传)  可以用我们前面的签名SHA1或者AES加密
        if (StringUtils.isNotBlank(weiboId)) {
            map.put("weibo", weiboId);
        }

        return map;
    }

    /**
     * 获取登录/注册 方式
     *
     * @param phone
     * @param email
     * @return
     */
    public static String getMethod(String phone, String email, String qq, String weibo_id) {
        if (StringUtils.isNotBlank(phone)) {
            return "phone";
        } else if (StringUtils.isNotBlank(email)) {
            return "email";
        } else if (StringUtils.isNotBlank(qq)) {
            return "qq";
        }

        return "weibo";
    }

//    /**
//     * 获取第三方登录/注册方式
//     *
//     * @param qq       QQ 的id
//     * @param weibo_id
//     * @return
//     */
//    public static String getThirdMethod(String qq, String weibo_id) {
//        if (StringUtils.isNotBlank(qq)) {
//            return "qq";
//        }
//        return "weibo";
//    }
}
