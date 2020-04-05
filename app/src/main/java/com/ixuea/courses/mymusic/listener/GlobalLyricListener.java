package com.ixuea.courses.mymusic.listener;

/**
 * 全局歌词监听器
 */
public interface GlobalLyricListener {

    void onLogoClick();//logo点击

    void onCloseClick();//关闭点击

    void onLockClick();//锁定点击

    void onPreviousClick();//上一首点击

    void onPlayClick();//播放点击

    void onNextClick();//下一首点击
}
