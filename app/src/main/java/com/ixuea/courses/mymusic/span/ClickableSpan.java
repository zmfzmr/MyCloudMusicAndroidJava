package com.ixuea.courses.mymusic.span;

import android.text.TextPaint;

import androidx.annotation.NonNull;

/**
 * onClick 由子类实现
 */
public abstract class ClickableSpan extends android.text.style.ClickableSpan {
    /**
     * 更新绘制状态
     *
     * @param ds
     */
    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        //只设置颜色
        ds.setColor(ds.linkColor);
        //去掉下划线（underline：下划线）
//        ds.setUnderlineText(true);
    }
}
