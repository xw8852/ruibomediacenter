package com.msx7.josn.ruibo_mediacenter.down;

import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SDUtils;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 文件名: ThreadPool
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/24
 */
public class ThreadPool {


    public ThreadPool() {
        urlQueue = new LinkedBlockingDeque<Status>();
    }

    BlockingQueue<Status> urlQueue;
    Down down;
    List<String> urls;
    IDownListener listener;
    String path;


    void downMode(List<String> urls) {
        L.d("downMode---1");
        FileDownloader.getImpl().stopForeground(true);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        int i = 0;
        for (String url : urls) {
            String pathName = url.substring(url.lastIndexOf("/") + 1);
            File file = new File(path + pathName);
            if (file.exists()) {
                pathName = System.currentTimeMillis() + pathName;
            }


            if (url.startsWith("http://")) {
                int index = url.indexOf(":88");
                url = url.substring(index);
                url = SharedPreferencesUtil.getServerIp() + url;
            } else {
                url = SharedPreferencesUtil.getServerIp() + ":88/ftp/" + url;
                try {
                    url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                }
            }
            L.d("downMode---1--" + i);
            tasks.add(FileDownloader.getImpl().create(url).setPath(path + pathName)
                    .setTag(i + 1));
            i++;
//            Status status = new Status(url, pathName);
//            urlQueue.add(status);
        }
        queueSet = new FileDownloadQueueSet(downloadListener);
// 所有任务在下载失败的时候都自动重试一次
        queueSet.setAutoRetryTimes(1);
        // 串行执行该任务队列
        queueSet.downloadSequentially(tasks);
        L.d("downMode---2--" + i);
        queueSet.start();
        L.d("downMode---3--" + i);
        // 并行执行该任务队列
//        queueSet.downloadTogether(tasks);

    }

    FileDownloadQueueSet queueSet;

    void downMode2(List<String> urls) {
        for (String url : urls) {
            Status status = new Status();
            status.name = new String(url);
            String pathName = url.substring(url.lastIndexOf("/") + 1);
            File file = new File(path + pathName);
            if (file.exists()) {
                pathName = System.currentTimeMillis() + pathName;
            }


            if (url.startsWith("http://")) {
                int index = url.indexOf(":88");
                url = url.substring(index);
                url = SharedPreferencesUtil.getServerIp() + url;
            } else {
                try {
                    url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                }
                url = SharedPreferencesUtil.getServerIp() + ":88/ftp/" + url;
            }
            status.url = url;
            status.toPath = pathName;
            urlQueue.add(status);
        }
        //        //开始线程下载
        downStatus(urlQueue.poll());
        downStatus(urlQueue.poll());
        downStatus(urlQueue.poll());
    }

    public void enqueue(List<String> urls, String path, IDownListener listener) {
        this.urls = urls;
        this.path = path;
        if (!TextUtils.isEmpty(path) && !path.endsWith("/"))
            path += "/";
        this.listener = listener;
        down = new Down();
        downMode(urls);
//        downMode2(urls);
    }

    FileDownloadLargeFileListener downloadListener = new FileDownloadLargeFileListener() {
        @Override
        protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            //等待，已经进入下载队列
//            L.d("pending--->" + task.getUrl());
//            L.d("pending--->" + task.getPath());
        }

        @Override
        protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            L.d("progress--->" + task.getPath() + "," + ((0.0f + soFarBytes) / totalBytes));
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            //在完成前同步调用该方法，此时已经下载完成
//            onStatus(new Status(task.getUrl(), task.getPath(), true));
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            //完成整个下载过程
//            L.d("completed--->" + task.getUrl());
//            L.d("completed--->" + task.getPath());
            onStatus(new Status(task.getUrl(), task.getPath(), true));
        }

        @Override
        protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            //下载出现错误
            e.printStackTrace();
//            L.d("error--->" + task.getUrl());
//            L.d("error--->" + task.getPath());
            onStatus(new Status(task.getUrl(), task.getPath(), false));
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
//            L.d("warn--->" + task.getUrl());
//            L.d("warn--->" + task.getPath());
            onStatus(new Status(task.getUrl(), task.getPath(), false));
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            L.d("pending--1->" + task.getUrl());
//            L.d("pending--1->" + task.getPath());
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//            L.d("progress--1->" + task.getUrl());
//            L.d("progress--1->" + task.getPath());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }
    };

    void onStatus(Status status) {
        if (status.isSuccess) {
            down.successList.add(status);
        } else down.failList.add(status);
        if (down.failList.size() + down.successList.size() >= urls.size()) {
            if (listener != null) {
                listener.finish(down);
            }
        }
    }

    void downStatus(Status _status) {
        if (_status == null) return;
        Log.d("MSG", "name：" + _status.name);
        new MultipartThreadDownloador(_status, path, _status.toPath, 2)
                .download(new MultipartThreadDownloador.IDownListner() {
                    @Override
                    public void onStatus(Status status) {
                        Log.d("MSG", "ThreadPool---status:" + status.name);
                        if (status.isSuccess) {
                            down.successList.add(status);
                        } else {
                            down.failList.add(status);
                            L.d("----" + status.name);
                        }
                        Status _status = urlQueue.poll();
                        if (_status != null) {
                            downStatus(_status);
                        }
                        if (down.failList.size() + down.successList.size() >= urls.size()) {
                            if (!down.failList.isEmpty()) {
                                List<String> _urls = new ArrayList<String>();
                                for (Status _tmp : down.failList) {
                                    _urls.add(_tmp.name);
                                }
                                down.failList.clear();
                                downMode(_urls);
                                return;
                            }
                            if (listener != null) {
                                listener.finish(down);
                            }
                        }
                    }
                });
    }

    public interface IDownListener {
        void finish(Down down);
    }

    public static class Down {
        public List<Status> successList = new ArrayList<>();
        public List<Status> failList = new ArrayList<>();
    }
}
