package com.ixuea.courses.mymusic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * View 相关的工具类方法
 */
public class ViewUtil {
    /**
     * 路线是：背景绘制到Canvas->最终绘制到Bitmap上->View上
     * <p>
     * 从view创建bitmap(截图)
     *
     * @param view View
     * @return
     */
    public static Bitmap captureBitmap(View view) {
        //创建一个和view一样大小的bitmap
        //这个控件（View）也就是显示出来了，才能截图（也就是在布局中（当然你隐藏了，也是可以截图的））
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //创建一个画板
        //只是这个画板最终画的内容
        //在Bitmap上
        Canvas canvas = new Canvas(bitmap);
        //获取view的背景
        Drawable background = view.getBackground();

        if (background != null) {
            //如果有背景

            //就显示绘制背景
            //这个背景绘制到这个画布上(Canvas上)
            background.draw(canvas);
        } else {
            //没有背景

            //绘制白色
            //等于null，就是没有背景，那么在画布上背景设置为白色
            canvas.drawColor(Color.WHITE);
        }
        //绘制view内容(路线是：背景绘制到Canvas->最终绘制到Bitmap上->View上（view绘制到Canvas画布上），注意：这里返回的是Bitmap对象)
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 初始化垂直方向 LinearLayoutManager RecyclerView
     * 有效的分割线
     *
     * @param rv RecyclerView
     */
    public static void initVerticalLinearRecyclerView(Context context, RecyclerView rv) {
        //固定尺寸
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }
}
