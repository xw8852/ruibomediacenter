package com.msx7.josn.ruibo_mediacenter.net;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

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


    public static final void download(List<String> urls, BaseActivity activity, OkHttpManager.IDownFinish iDownFinish) {
        String path = null;
        StorageManager sm = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);
        try {
            String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths", null).invoke(sm, null);
            for (int i = 0; i < paths.length; i++) {
                Log.i("iii", paths[i]);
                String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, paths[i]);
                if (status.equals(android.os.Environment.MEDIA_MOUNTED)) {
                    Log.i("ddd", paths[i]);
                    path = paths[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        try {
            File file = new File(path + "/11.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.deleteOnExit();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.i("dddd00", path);
        } finally {
        }

        for (String url : urls) {
            Log.d("MSG", url);
            int index = url.indexOf(":88");
            url = url.substring(index);
            url = SharedPreferencesUtil.getServerIp() + url;
            Log.d("MSG", url);
            String pathName = url.substring(url.lastIndexOf("/") + 1);
            File file = new File(path + File.separator +"MUSIC" + File.separator+ pathName);
            if (file.exists()) {
                file = new File(path + File.separator  +"MUSIC" + File.separator+ System.currentTimeMillis() + pathName);
            }
            Log.d("MSG", file.getPath());
            try {
                OkHttpManager.asDown(url, file, iDownFinish);
            } catch (IOException e) {
                e.printStackTrace();
                if (iDownFinish != null) {
                    iDownFinish.finish(url, file);
                }
            } finally {
            }
        }
    }


}
