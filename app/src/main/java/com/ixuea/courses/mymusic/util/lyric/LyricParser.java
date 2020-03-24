package com.ixuea.courses.mymusic.util.lyric;

import com.ixuea.courses.mymusic.domain.lyric.Lyric;

import static com.ixuea.courses.mymusic.util.Constant.KSC;

/**
 * 歌词解析器
 */
public class LyricParser {
    /**
     * @param type    歌词类型
     * @param content 歌词内容
     * @return 解析后的歌词对象
     *
     * //注意：switch里面用的是 return
     */
    public static Lyric parse(int type, String content) {

        switch (type) {
            case KSC:
                return KSCLyricParser.parse(content);
            default:
                //默认解析LRC歌词
                return LRCLyricParser.parse(content);
        }

    }
}