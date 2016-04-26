package com.msx7.josn.ruibo_mediacenter.net;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.util.SdCardUtils;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 文件名: Download
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/3/1
 */
public class DownloadManager {


    public static final void download(List<String> urls, BaseActivity activity, final OkHttpManager.IDownFinish iDownFinish) {


        for (String url : urls) {
            if (url.startsWith("http://")) {
                int index = url.indexOf(":88");
                url = url.substring(index);
                url = SharedPreferencesUtil.getServerIp() + url;
            } else {
                url = SharedPreferencesUtil.getServerIp() + ":88/ftp/" + url;
            }
            String pathName = url.substring(url.lastIndexOf("/") + 1);
            File file = new File(RuiBoApplication.MUSIC_FILE + pathName);
            if (file.exists()) {
                file = new File(RuiBoApplication.MUSIC_FILE  + System.currentTimeMillis() + pathName);
            }
            Log.d("MSG", "url:" + url + " path: " + file.getPath());
            try {
                OkHttpManager.asDown(url, file, iDownFinish);
            } catch (IOException e) {
                e.printStackTrace();
                if (iDownFinish != null) {
                    iDownFinish.error(url, file);
                }
            } finally {
            }
        }
    }


}
