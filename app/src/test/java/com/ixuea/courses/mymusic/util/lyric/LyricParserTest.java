package com.ixuea.courses.mymusic.util.lyric;

import com.ixuea.courses.mymusic.domain.lyric.Lyric;

import org.junit.Test;

import static com.ixuea.courses.mymusic.util.Constant.LRC;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 歌词解析工具类测试
 */
public class LyricParserTest {

    /**
     * LRC歌词 其中的\n 不要删除 是每一行歌词的分隔符
     * 粘贴进来是\\n 这个是错误，正确的是一个斜杆 \n
     */
    private String lrcLyric = "[ti:爱的代价]\n[ar:李宗盛]\n[al:爱的代价]\n[00:00.50]爱的代价\n[00:02.50]演唱：李宗盛\n[00:06.50]\n[00:16.70]还记得年少时的梦吗\n[00:21.43]像朵永远不调零的花\n[00:25.23]陪我经过那风吹雨打\n[00:28.59]看世事无常\n[00:30.57]看沧桑变化\n[00:33.31]那些为爱所付出的代价\n[00:37.10]是永远都难忘的啊\n[00:41.10]所有真心的痴心的话\n[00:44.57]永在我心中虽然已没有他\n[00:51.46]走吧 走吧 人总要学着自己长大\n[00:59.53]走吧 走吧 人生难免经历苦痛挣扎\n[01:07.19]走吧 走吧 为自己的心找一个家\n[01:15.41]也曾伤心流泪\n[01:17.55]也曾黯然心碎\n[01:19.57]这是爱的代价\n[01:22.57]\n[01:40.67]也许我偶尔还是会想他\n[01:45.28]偶尔难免会惦记着他\n[01:49.10]就当他是个老朋友吧\n[01:52.60]也让我心疼也让我牵挂\n[01:57.37]只是我心中不再有火花\n[02:01.21]让往事都随风去吧\n[02:05.10]所有真心的痴心的话\n[02:08.53]仍在我心中\n[02:10.39]虽然已没有他\n[02:15.26]走吧 走吧 人总要学着自己长大\n[02:23.14]走吧 走吧 人生难免经历苦痛挣扎\n[02:31.26]走吧 走吧 为自己的心找一个家\n[02:39.22]也曾伤心流泪\n[02:41.54]也曾黯然心碎\n[02:43.60]这是爱的代价\n[02:46.44]\n[03:22.77]走吧 走吧 人总要学着自己长大\n[03:31.16]走吧 走吧 人生难免经历苦痛挣扎\n[03:39.08]走吧 走吧 为自己的心找一个家\n[03:47.12]也曾伤心流泪\n[03:49.41]也曾黯然心碎\n[03:51.58]这是爱的代价\n";

    /**
     * 测试LRC歌词解析
     * <p>
     * 加上 @Test 表示这个是单元测试
     */
    @Test
    public void textLRCParse() {
        //解析歌词
        Lyric lyric = LyricParser.parse(LRC, lrcLyric);

        //确认返回的数组大于0
        //因为我们给的数据是正确的
        //所以结果肯定大于0才正确

//        Assert.assertTrue();
        assertTrue(lyric.getDatum().size() > 0);

        //不太好判断歌词是否解析正确
        //所以就判断第10行歌词的开始时间必须大于0
        //第10行歌词必须有内容
        //因为我们提供的歌词是正确的
        assertTrue(lyric.getDatum().get(10).getStartTime() > 0);

        //歌词内容也不为空
        assertNotNull(lyric.getDatum().get(10).getData());
    }
}
