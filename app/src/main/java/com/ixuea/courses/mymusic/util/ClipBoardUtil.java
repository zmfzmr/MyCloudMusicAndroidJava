package com.ixuea.courses.mymusic.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 剪贴板工具类
 */
public class ClipBoardUtil {
    /**
     * 拷贝文本
     */
    public static void copyText(Context context, String data) {
        //获取剪贴板管理器
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //固定写法  参数2：把剪贴的内容放在这类
        ClipData clipData = ClipData.newPlainText("text", data);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
    }
}
