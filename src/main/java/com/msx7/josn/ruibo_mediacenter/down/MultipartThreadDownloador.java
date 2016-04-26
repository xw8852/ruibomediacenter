package com.msx7.josn.ruibo_mediacenter.down;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件名: MultipartThreadDownloador
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/24
 */
public class MultipartThreadDownloador {

    /**
     * 需要下载资源的地址
     */
    private String urlStr;
    /**
     * 下载的文件
     */
    private File localFile;
    /**
     * 需要下载文件的存放的本地文件夹路径
     */
    private String dirStr;
    /**
     * 存储到本地的文件名
     */
    private String filename;

    /**
     * 开启的线程数量
     */
    private int threadCount;
    /**
     * 下载文件的大小
     */
    private long fileSize;

    public MultipartThreadDownloador(String urlStr, String dirStr,
                                     String filename, int threadCount) {
        this.urlStr = urlStr;
        this.dirStr = dirStr;
        this.filename = filename;
        this.threadCount = threadCount;
        status = new Status();
        status.url = urlStr;
        status.toPath = dirStr + "/" + filename;
    }

    Status status;
    int count;

    public void download(final IDownListner listner) {

        DownloadThread[] threads = new DownloadThread[threadCount];
        try {
            createFileByUrl();
            if (fileSize / (1024 * 1024.0) > 100) {
                threadCount = threadCount * 2;
                threadCount = Math.min(threadCount, 10);
                threads = new DownloadThread[threadCount];
            }

            /**
             * 计算每个线程需要下载的数据长度
             */
            long block = fileSize % threadCount == 0 ? fileSize / threadCount
                    : fileSize / threadCount + 1;

            for (int i = 0; i < threadCount; i++) {
                long start = i * block;
                long end = start + block >= fileSize ? fileSize : start + block - 1;

                threads[i] = new DownloadThread(new URL(urlStr), localFile, start, end, new IDownListner() {
                    @Override
                    public void onStatus(Status status) {
                        count++;
                        if (!status.isSuccess) {
                            if (listner != null) {
                                status.isSuccess = false;
                                listner.onStatus(status);
                            }
                        } else if (count == threadCount) {
                            if (listner != null) {
                                status.isSuccess = true;
                                listner.onStatus(status);
                            }
                        }
                    }
                });
                threads[i].start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listner != null) {
                status.isSuccess = false;
                listner.onStatus(status);
            }
            for (DownloadThread thread : threads) {
                if (thread != null) thread.cancel();
            }
        } finally {
        }

    }

    /**
     * 根据资源的URL获取资源的大小，以及在本地创建文件
     */
    public void createFileByUrl() throws IOException {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15 * 1000);
        conn.setReadTimeout(15 * 1000);
        conn.setAllowUserInteraction(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        conn.setRequestProperty(
                "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.connect();

        if (conn.getResponseCode() == 200) {
            System.out.println("开始获取文件大小 , 存储位置为： "
                    + dirStr + "/" + filename);
            this.fileSize = conn.getContentLength();// 根据响应获取文件大小
            if (fileSize <= 0)
                throw new IOException(
                        "the file that you download has a wrong size ... ");
            File dir = new File(dirStr);
            if (!dir.exists())
                dir.mkdirs();
            this.localFile = new File(dir, filename);
            RandomAccessFile raf = new RandomAccessFile(this.localFile, "rw");
            raf.setLength(fileSize);
            raf.close();

            System.out.println("需要下载的文件大小为 :" + this.fileSize + " , 存储位置为： "
                    + dirStr + "/" + filename);

        } else {
            throw new IOException("url that you conneted has error ...");
        }
    }

    private class DownloadThread extends Thread {
        /**
         * 下载文件的URI
         */
        private URL url;
        /**
         * 存的本地路径
         */
        private File localFile;
        /**
         * 是否结束
         */
        private boolean isFinish;
        /**
         * 开始的位置
         */
        private Long startPos;
        /**
         * 结束位置
         */
        private Long endPos;

        IDownListner listner;

        public DownloadThread(URL url, File savefile, Long startPos, Long endPos, IDownListner listner) {
            this.url = url;
            this.localFile = savefile;
            this.startPos = startPos;
            this.endPos = endPos;
            this.listner = listner;
        }

        boolean cancel;

        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始下载...");
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(15 * 1000);
                conn.setReadTimeout(15 * 1000);
                conn.setAllowUserInteraction(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
                conn.setRequestProperty(
                        "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("Referer", url.toString());
//                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Range", "bytes=" + startPos + "-"
                        + endPos);// 设置获取实体数据的范围


                if (cancel) return;
                conn.connect();
                if (cancel) return;
                /**
                 * 代表服务器已经成功处理了部分GET请求
                 */
                if (conn.getResponseCode() == 206) {
                    InputStream is = conn.getInputStream();
                    int len = 0;
                    byte[] buf = new byte[102400 * 2];

                    RandomAccessFile raf = new RandomAccessFile(localFile,
                            "rwd");
                    raf.seek(startPos);
                    if (cancel) return;
                    while ((len = is.read(buf)) != -1) {
                        if (cancel) return;
                        raf.write(buf, 0, len);
                        if (cancel) return;
                    }
                    raf.close();
                    is.close();
                    System.out.println(Thread.currentThread().getName() + "完成下载  ： " + startPos + " -- " + endPos);
                    this.isFinish = true;
                    if (this.listner != null) {
                        listner.onStatus(new Status(true));
                    }
                } else {
                    if (this.listner != null) {
                        listner.onStatus(new Status(false));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (this.listner != null) {
                    listner.onStatus(new Status(false));
                }
            }
        }
    }

    public interface IDownListner {
        void onStatus(Status status);
    }
}
