package com.msx7.josn.ruibo_mediacenter.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件名: SDUtils
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/12
 */
public class SDUtils {

    /**
     * 判断sb接口1，是否插入U盘
     *
     * @return
     */
    public static final boolean isExist(String path) {

        return new File(path).exists() && getTotalSize(path) > 0;
    }

    /**
     * 获取可用空间大小
     */
    public static final long getRemainSize(String path) {
        StatFs statFs = new StatFs(path);

        long blockSize = statFs.getBlockSize();

        long availableBlocks = statFs.getAvailableBlocks();

        return availableBlocks * blockSize;
    }

    /**
     * 获取总空间大小
     *
     * @return
     */
    public static final long getTotalSize(String path) {
        StatFs statFs = new StatFs(path);

        long blockSize = statFs.getBlockSize();      //每个block 占字节数

        long totalBlocks = statFs.getBlockCount();   //block总数

        return totalBlocks * blockSize;
    }


    public static final String getPath() {
        String path = null;
//        StorageManager sm = (StorageManager) RuiBoApplication.getApplication().getSystemService(Context.STORAGE_SERVICE);
//        try {
//            String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths", null).invoke(sm, null);
//
//            for (int i = 0; i < paths.length; i++) {
//                Log.i("iii", paths[i]);
//                String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, paths[i]);
//                if (status.equals(android.os.Environment.MEDIA_MOUNTED)) {
//                    Log.i("ddd", paths[i] + "," + Environment.getExternalStorageDirectory().getPath());
//                    if (Environment.getExternalStorageDirectory().getPath().equals(paths[i]))
//                        continue;
//                    path = paths[i];
//                    path += "/";
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
        return path;
    }
}
