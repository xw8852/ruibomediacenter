package com.msx7.josn.ruibo_mediacenter.util;

import android.os.StatFs;
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
    public static final boolean isExist() {

        return new File(RuiBoApplication.MUSIC_FILE).exists() && getTotalSize() > 0;
    }

    /**
     * 获取可用空间大小
     */
    public static final long getRemainSize() {
        StatFs statFs = new StatFs(RuiBoApplication.MUSIC_FILE);

        long blockSize = statFs.getBlockSize();

        long availableBlocks = statFs.getAvailableBlocks();

        return availableBlocks * blockSize;
    }

    /**
     * 获取总空间大小
     *
     * @return
     */
    public static final long getTotalSize() {
        StatFs statFs = new StatFs(RuiBoApplication.MUSIC_FILE);

        long blockSize = statFs.getBlockSize();      //每个block 占字节数

        long totalBlocks = statFs.getBlockCount();   //block总数

        return totalBlocks * blockSize;
    }
}
