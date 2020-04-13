package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享工具类
 */
public class ShareUtil {
    /**
     * @param context 上下文
     * @param data    Song
     * @param lyric   选中的歌词文本
     */
    public static void shareLyricText(Context context, Song data, String lyric) {
        //分享字符串：分享歌词：这是歌词。分享%s的单曲《%s》：http://dev-courses-misuc.ixuea.com/songs/%s (来自@我的云音乐)
        //具体的可以根据业务需求更改

        //真实项目中分享的地址
        //一般是音乐的网页
        //但由于我们这里没有实现网页
        //所以这个地址是随便写的

        //格式化url
        String url = String.format("http://dev-my-cloud-music-api-rails.ixuea.com/v1/songs/%s", data.getId());

        //分享的文本
        //1，选中的歌词 2，歌手名称 3，单曲名称 4，歌词的url地址
        String shareContent = String.format("分享歌词：%s。分享%s的单曲《%s》：%s (来自@我的云音乐)",
                lyric,
                data.getSinger().getNickname(),
                data.getTitle(),
                url);
        //应用的名称
        String name = context.getString(R.string.app_name);

        //ShareSDK:里面的OnekeyShare  ShareSDK：之前QQ 微信 统一用的封装SDK（里面有分享功能）
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(name);

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareContent);

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("这个听歌软件还不错哟！");

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(name);

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        //启动分享
        oks.show(context);
    }
}
