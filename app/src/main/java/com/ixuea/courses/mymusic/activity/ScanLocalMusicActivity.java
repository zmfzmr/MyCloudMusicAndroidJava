package com.ixuea.courses.mymusic.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.SongLocal;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描本地音乐界面
 */
public class ScanLocalMusicActivity extends BaseTitleActivity {
    private static final String TAG = "ScanLocalMusicActivity";
    /**
     * 扫描进度
     */
    @BindView(R.id.tv_progress)
    TextView tv_progress;

    /**
     * 扫描线
     */
    @BindView(R.id.iv_scan_line)
    ImageView iv_scan_line;

    /**
     * 扫描放大镜
     */
    @BindView(R.id.iv_scan_zoom)
    ImageView iv_scan_zoom;

    /**
     * 扫描按钮
     */
    @BindView(R.id.bt_scan)
    Button bt_scan;
    /**
     * 扫描音乐的异步任务
     */
    private AsyncTask<Void, String, List<SongLocal>> scanMusicAsyncTask;
    private boolean isScanComplete;//是否扫描完成了
    private boolean hasFoundMusic;//是否有音乐
    private boolean isScanning;//是否在扫描中


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local_music);
    }

    /**
     * 扫描按钮点击了
     */
    @SuppressLint("ResourceType")
    @OnClick(R.id.bt_scan)
    public void onScanClick() {
        LogUtil.d(TAG, "onScanClick: ");
        //如果扫描完成了
        //点击就是关闭界面
        //按钮样式在扫描完成后以及更改了(就扫描完成，按钮文字变成了(回到我的音乐))
        if (isScanComplete) {
            finish();
            return;
        }

        if (isScanning) {
            //如果是正在扫描(扫描中)

            //点击停止扫描
            stopScan();
            //按钮背景
            bt_scan.setBackgroundResource(R.drawable.selector_color_primary);
            //这里比上面多写了getResources().getColor
            //如果这里单单写：getColorStateList或者getColor（这个方法是API 23以后才会用到，所以前面最好加上getResources().）
            bt_scan.setTextColor(getResources().getColor(R.drawable.selector_text_color_primary_reverse));
            //设置按钮文本
            bt_scan.setText(R.string.start_scan);
        } else {
            //没有扫描

            //开始扫描
            startScan();
            //按钮背景
            bt_scan.setBackgroundResource(R.drawable.selector_color_primary_reverse);
            //按钮文字颜色
            bt_scan.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary));
            //设置为停止扫描
            bt_scan.setText(R.string.stop_scan);
        }
        //改变扫描状态
        //一开始是开始扫描的(也就是isScanning = false)，点击后，就要变成停止扫描文字，这取反走if里面的额
        isScanning = !isScanning;

    }

    /**
     * 开始扫描
     */
    private void startScan() {

        //扫描音乐(之所以在这类再定义一个方法，因为在这前面还需要扫描动画)
        startScanMusic();
    }

    /**
     * 扫描音乐
     * 我们这里只扫描媒体库
     * 不全盘扫描
     * 扫描媒体库的好处是：
     * 不用手动解析音乐
     * 但也有弊端：
     * 媒体库更新可能不及时
     * 大部分手机都重启后才会刷新媒体库
     * 还有就是媒体库也不能识别所以格式
     * 所以如果要做一个商业级的音乐软件
     * 那么可能还需要全盘扫描
     * 手动解析音乐
     * 但这个我们就超出我们课程范围了
     * 因为我们这个课程定位是一个通用课程
     * 所以大家学到这里就够了
     * 如果以后需要做商业级音乐软件
     * 在去深入学习这方面知识
     */
    @SuppressLint("StaticFieldLeak")
    private void startScanMusic() {
        //扫描音乐是耗时任务
        //所以放到子线程
        //这里用的是AsyncTask
        scanMusicAsyncTask = new AsyncTask<Void, String, List<SongLocal>>() {

            @Override
            protected List<SongLocal> doInBackground(Void... voids) {
                //创建结果列表
                ArrayList<SongLocal> datum = new ArrayList<>();

                /**
                 * 使用内容提供者查询
                 * 我们这里是查询音乐大小大于1M，时间大于60秒
                 * @param uri 资源标识符
                 * @param projection 选择那些字段
                 * @param selection 条件
                 * @param selectionArgs 条件参数
                 * @param sortOrder 排序
                 */
                Cursor cursor = getContentResolver().query(
                        //查询地址（可以理解为表）
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        //选择要返回哪些字段（是媒体库返回来的）
                        new String[]{
                                //媒体Id
                                BaseColumns._ID,
                                MediaStore.Audio.AudioColumns.TITLE,

                                //艺术家（歌手）
                                MediaStore.Audio.AudioColumns.ARTIST,

                                //专辑：可以理解为是专辑的名称
                                MediaStore.Audio.AudioColumns.ALBUM,
                                //专辑Id
                                MediaStore.Audio.AudioColumns.ALBUM_ID,

                                //这个文件路径在10以下的版本是可以直接播放的
                                //但是10以后，是不能播放；我们这里获取是为了上方的那个扫描进度
                                MediaStore.Audio.AudioColumns.DATA,

                                //显示名称(可能和上方的那个title一样，也可能不一样，这个不确定)
                                //例如:"爱的代价.mp3"
                                MediaStore.Audio.AudioColumns.DISPLAY_NAME,

                                //文件大小
                                //单位：字节
                                MediaStore.Audio.AudioColumns.SIZE,
                                //时长
                                //单位：毫秒
                                MediaStore.Audio.AudioColumns.DURATION
                        },
                        Constant.MEDIA_AUDIO_SELECTION,

                        new String[]{
                                String.valueOf(Constant.MUSIC_FILTER_SIZE),
                                String.valueOf(Constant.MUSIC_FILTER_DURATION)
                        },
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
                );

                while (cursor != null && cursor.moveToNext()) {
                    //媒体id
                    //注意：这类是long类型 getLong
                    long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                    //媒体标题
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));

                    //显示名称
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));

                    //艺术家(歌手)
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                    //专辑
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                    //专辑Id
                    long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
                    //文件路径
                    //DATA列在Android Q以下版本代表了文件路径
                    //但在 Android Q上该路径无法被访问

                    //获取这个路径的目的是
                    //扫描的时候显示到界面上
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                    //需要通过这种方式方法
                    //该方法也兼容Android Q以下版本
                    //uri:如：content://media/external/audio/media/28
                    String uri = StorageUtil.getAudioContentUri(id);
                    //文件大小和时长
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));

                    //打印日志
                    //目的是方便调试
                    LogUtil.d(TAG, String.format("$d,$s,$s,$s", id, title, artist, album));

                    //发布进度
                    publishProgress(path);

                    //这里模拟延迟
                    SystemClock.sleep(1000);
                }

                Log.d(TAG, "doInBackground: ");

                return datum;//返回结果
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                String path = values[0];

                LogUtil.d(TAG, "path :" + path);

                tv_progress.setText(path);
            }

            /**
             * 异步任务执行完成后
             * 主线程中执行
             * @param songs
             */
            @SuppressLint("ResourceType")
            @Override
            protected void onPostExecute(List<SongLocal> songs) {
                super.onPostExecute(songs);

                //清除扫描异步任务(记得清空，下次还得使用这个对象的execute)
                scanMusicAsyncTask = null;

                //是扫描完成了
                isScanComplete = true;

                //是否有音乐
                hasFoundMusic = songs.size() > 0;

                //停止扫描
                stopScan();

                ////显示扫描到的音乐数量
                //扫描进度(就是顶部变换的那串进度，扫描完成后，变成：如：共扫描到几首音乐)
                tv_progress.setText(getString(R.string.found_music_count, songs.size()));

                bt_scan.setBackgroundResource(R.drawable.selector_color_primary);
                //这里比上面多写了getResources().getColor
                //如果这里单单写：getColorStateList或者getColor（这个方法是API 23以后才会用到，所以前面最好加上getResources().）
                bt_scan.setTextColor(getResources().getColor(R.drawable.selector_text_color_primary_reverse));

                //设置按钮文本
                bt_scan.setText(R.string.to_my_music);
            }
        };

        //启动异步任务
        scanMusicAsyncTask.execute();
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        if (scanMusicAsyncTask != null) {
            //终止异步任务
            scanMusicAsyncTask.cancel(true);
            scanMusicAsyncTask = null;
        }
    }
}
