package com.ixuea.courses.mymusic.util;

import android.widget.TextView;

import com.ixuea.courses.mymusic.manager.ListManager;

import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_LIST;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_ONE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_RANDOM;

/**
 * 播放列表工具类
 */
public class PlayListUtil {
    /**
     * TextView:这个是button的父类
     */
    public static void showLoopModel(ListManager listManager, TextView textView) {
        //获取当前循环模式
        int model = listManager.getLoopModel();
        switch (model) {
            case MODEL_LOOP_LIST:
                textView.setText("列表模式");
                break;
            case MODEL_LOOP_RANDOM:
                textView.setText("随机模式");
                break;
            case MODEL_LOOP_ONE:
                textView.setText("单曲循环");
                break;
            default:
                break;
        }
    }
}
