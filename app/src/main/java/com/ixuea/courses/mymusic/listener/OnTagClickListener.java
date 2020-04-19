package com.ixuea.courses.mymusic.listener;

import com.ixuea.courses.mymusic.util.MatchResult;

/**
 * Tag 点击回调接口
 */
public interface OnTagClickListener {
    /**
     * 点击回调内容
     *
     * @param data        点击的内容
     * @param matchResult 点击范围(是一个对象，保存了开始 和结果 和点击的内容)
     */
    void onTagClick(String data, MatchResult matchResult);
}
