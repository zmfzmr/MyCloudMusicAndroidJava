package com.ixuea.courses.mymusic.util;

import android.os.Looper;

/**
 * Handler工具类
 *
 * 这里面用不到的，就暂时注释掉
 */
public class HandlerUtil {
//    private static Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//            switch(msg.what) {
//                case MESSAGE_PROGRESS:
//                    //播放进度回调
//
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 是否是主线程
     *
     * @return true 就是主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
//
//    /**
//     * 发送消息
//     *
//     * @param what int
//     */
//    public static void sendMessage(int what) {
//        //发送消息
//        handler.obtainMessage(what).sendToTarget();
//    }
//
//    /**
//     * 获取handler
//     * @return Handler
//     */
//    public static Handler getHandler() {
//        return handler;
//    }
}
