package com.ixuea.courses.mymusic.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Build.VERSION_CODES.Q;

/**
 * 存储工具
 *
 * 知识：Uri：统一资源标识符（比如名称或者网址）  URl：统一资源定位符（比如网址等）（URl都是Uri）
 */
public class StorageUtil {
    public static final String JPG = "jpg";//jpg图片
    public static final String MP3 = "mp3";//mp3 后缀
    /**
     * MediaStore，ContentProviders内容提供者data列
     * 这个值不能更改（因为列名就是这个，根据这个查询）
     */
    private static final String COLUMN_DATA = "_data";
//    private static Uri uri;//savePicture方法执行后的uri

    /**
     * 保存图片到系统相册
     * <p>
     * 写法是Android 10写法，也兼容低版本
     */
    public static Uri savePicture(Context context, File file) {
        //创建媒体路径
        Uri uri = insertPictureMediaStore(context, file);

        if (uri == null) {
            //获取路径失败
            return null;
        }
        // content://media/external/images/media/30
//        LogUtil.d("调试：", "1" + uri);

        //将原来的图片保存到目标uri
        //也就是保存到系统图片媒体库
        return saveFile(context, file, uri);//这个方法返回的是 uri
    }

    /**
     * 将file保存到uri对应的路径
     */
    private static Uri saveFile(Context context, File file, Uri uri) {
        FileOutputStream fileOutputStream = null;

        try {
            //获取内容提供者
            ContentResolver contentResolver = context.getContentResolver();

            //获取uri对应的文件描述器
            ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "w");

            //创建文件输出流
            fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            //将file拷贝到输出流
            FileUtils.copyFile(file, fileOutputStream);

            //content://media/external/images/media/30 这个和savePicture方法里面传入进来的一样，没有变过
//            LogUtil.d("调试2：", "2" + uri);

            //操作成功 返回uri
            return uri;
        } catch (IOException e) {
            //发生了异常
            e.printStackTrace();
        } finally {
            //判断是否要关闭流
            if (fileOutputStream != null) {
                try {
                    //关闭流
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //操作失败 返回null
        return null;
    }

    /**
     * 创建图片媒体路径
     * <p>
     * 只有操作这一步以后，在媒体库生成一个文件，会给我们返回一个Uri，这个Uri里面有一个方法
     * 就是通过Uri创建一个流，然后把我们的File对象拷贝到它给我们返回的地址里面去，这样的话，才实现了这个图片的保存
     * 这其实Android保存图片的一个流程
     */
    private static Uri insertPictureMediaStore(Context context, File file) {
        //创建内容集合
        ContentValues contentValues = new ContentValues();

        //媒体显示的名称
        //这里设置为文件的名称（这里可以把Images改成 Video）
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());

        //媒体类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Q) {
            //Q:29
            //拍摄媒体的时间( //file.lastModified():最后修改的时间)
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, file.lastModified());
        }

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /**
     * 获取应用sdcard中的路径
     *
     * @param context 上下文
     * @param userId  用户id
     * @param title   标题
     * @param suffix  类型（后缀）
     * @return
     */
    public static File getExternalPath(Context context, String userId, String title, String suffix) {
        //获取下载文件类型目录
        //该路径下的文件卸载应用后会清空
        //在Android 10路径为：
        // /storage/emulated/0/Android/data/com.ixuea.courses.mymusic/files/Download/
        //  /storage/emulated/0 = /sdcard/ 就是外部目录 = sd卡目录
        //如果是10.0以下的路径，那么可能是其他路径

        //这里不用担心是什么路径，目前这种写法，对于10和10以下的版本也就是兼容的
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        //格式化路径(例如：MyCloudMusic/1/jpg/share.jpg)
        String path = String.format("MyCloudMusic/%s/%s/%s.%s", userId, suffix, title, suffix);

        //创建文件对象(这个文件对象，可能是目录，也可能是具体的文件)
        //dir：/storage/emulated/0/Android/data/com.ixuea.courses.mymusic/files/Download/
        //path:  MyCloudMusic/1/jpg/share.jpg
        File file = new File(dir, path);//拼接起来创建一个文件对象

        if (!file.getParentFile().exists()) {
            //如果上级目录不存在，

            // 就创建(mkdirs：加s，表示可以创建多个层级的目录)
            //其实file指的是：share.jpg
            //父目录：指的是/storage/emulated/0/Android/data/com.ixuea.courses.mymusic/files/Download/MyCloudMusic/1/jpg/
            //就是把这个目录创建出来（这个要file.getParentFile().mkdirs()，不要写成了file.mkdirs()）
            file.getParentFile().mkdirs();
        }
        //返回文件file 外界肯需要用到这个file对象
        return file;
    }

    //因为前面已经return uri，所以下面这些代码不需要了
//    /**
//     * 获取Uri
//     *
//     * @return
//     */
//    public static Uri getUri() {
//        return uri;
//    }

    /**
     * 获取MediaStore uri路径
     * (通过savePicture 方法返回的uri(可以理解为表)， 通过列名(_data) 找到保存到相册的这个路径)
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getMediaStorePath(Context context, Uri uri) {
        return getDataColumn(context, uri);
    }

    /**
     * 获取uri对应的data列值
     * (通过savePicture 方法返回的uri(可以理解为表)， 通过列名(_data) 找到保存到相册的这个路径)
     *
     * 其实就是文件路径
     * 这种写法支持MediaStore，ContentProviders
     */
    private static String getDataColumn(Context context, Uri uri) {
//        Cursor cursor = null;
//        try {
//            cursor = context  //获取内容提供者
//                    .getContentResolver()
//                    //查询数据
//                    /**
//                     * @param uri 以content://开通的地址
//                     * @param projection 返回哪些列
//                     * @param selection 查询条件，类似sql中where条件
//                     * @param selectionArgs 查询条件参数
//                     * @param sortOrder 排序参数
//                     */
//                    .query(uri, new String[]{COLUMN_DATA},
//                            null,
//                            null,
//                            null);
//            if (cursor != null && cursor.moveToFirst()) {
//                //获取这一列的索引 (getColumnIndexOrThrow:如果没有找到该列名,会抛出IllegalArgumentException异常)
//                int index = cursor.getColumnIndexOrThrow(COLUMN_DATA);
//
//                //获取这一列字符类型数据
//                return cursor.getString(index);
//            }
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } finally {
//            //关闭游标
//            if (cursor != null) {
//                cursor.close();
//            }
//        }

        //改进：这样Cursor对象不用释放了
        try (Cursor cursor = context  //获取内容提供者
                .getContentResolver()
                    //查询数据
                    /**
                     * @param uri 以content://开通的地址
                     * @param projection 返回哪些列
                     * @param selection 查询条件，类似sql中where条件
                     * @param selectionArgs 查询条件参数
                     * @param sortOrder 排序参数
                     */
                    .query(uri, new String[]{COLUMN_DATA},
                            null,
                            null,
                            null)) {

            if (cursor != null && cursor.moveToFirst()) {
                //获取这一列的索引 (getColumnIndexOrThrow:如果没有找到该列名,会抛出IllegalArgumentException异常)
                int index = cursor.getColumnIndexOrThrow(COLUMN_DATA);

                //获取这一列字符类型数据
                return cursor.getString(index);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据id获取audio content uri
     * <p>
     * 注意：这里传入的是long类型
     * <p>
     * 返回的是uri  如：content://media/external/audio/media/28
     */
    public static String getAudioContentUri(long id) {
        //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon(): 创建一个builder,
        // 并将原来的参数拷贝到新的builder
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon()
                //这个id,就是添加到，MediaStore.Audio.Media.EXTERNAL_CONTENT_URI的后面
                .appendPath(String.valueOf(id))
                //创建uri
                .build()
                //以字符串的类型返回(转为字符串)
                .toString();
    }
}
