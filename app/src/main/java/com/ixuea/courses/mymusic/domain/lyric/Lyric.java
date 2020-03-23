package com.ixuea.courses.mymusic.domain.lyric;

import java.io.Serializable;
import java.util.List;

/**
 * 解析后的歌词模型
 */
//public class Lyric extends BaseModel {//BaseModel里面也实现了Serializable，所以哪种都可以
public class Lyric implements Serializable {

    private List<Line> datum;//所有的歌词

    public List<Line> getDatum() {
        return datum;
    }

    public void setDatum(List<Line> datum) {
        this.datum = datum;
    }
}
