package com.ixuea.courses.mymusic.util;

import android.graphics.Paint;

/**
 * 文本绘制相关工具类
 */
public class TextUtil {
    /**
     * 获取文本的宽度
     */
    public static float getTextWidth(Paint paint, String data) {
        //画笔 去测量文本的一个宽
        return paint.measureText(data);
    }

    /**
     * 获取文本的高度
     */
    public static float getTextHeight(Paint paint) {
        //获取和字体绘制相关测量类   Metrics：测量标准
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //Math.ceil向上取整   fontMetrics.descent - fontMetrics.ascent)：这部分，只需要记得这么写就可以了
        return (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }
}
