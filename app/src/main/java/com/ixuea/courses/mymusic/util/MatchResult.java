package com.ixuea.courses.mymusic.util;

/**
 * 匹配的结果
 */
public class MatchResult {
    private int start;//开始位置
    private int end;//结束位置
    private String content;//匹配到的内容

    public MatchResult(int start, int end, String content) {
        this.start = start;
        this.end = end;
        this.content = content;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
