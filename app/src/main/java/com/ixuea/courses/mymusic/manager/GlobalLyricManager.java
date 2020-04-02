package com.ixuea.courses.mymusic.manager;

/**
 * 全局(桌面)歌词管理器
 */
public interface GlobalLyricManager {

    void show();//显示桌面歌词

    void hide();//隐藏桌面歌词

    boolean isShowing();//桌面歌词是否显示了

    /**
     * 尝试隐藏
     * 为什么是尝试隐藏呢？
     * 因为有可能当前都没有显示桌面歌词
     */
    void tryHide();

    /**
     * 有可能当前不需要显示歌词，或者我们应用没让他显示歌词
     * 所以就是尝试显示歌词
     */
    void tryShow();//尝试显示歌词
}
