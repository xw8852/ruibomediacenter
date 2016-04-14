package com.msx7.josn.ruibo_mediacenter.net;

import android.content.Intent;
import android.util.Log;

import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件名: OkHttpManager
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/3/1
 */
public class OkHttpManager {
    static final OkHttpClient okHttpClient = new OkHttpClient();

    public static final void asDown(final String url, final File file, final IDownFinish finish) throws IOException {
        System.setProperty("http.keepAlive", "false");
        okHttpClient.newCall(new Request.Builder()
                .addHeader("http.keepAlive", "false")
                .url(url)
                .build()).
                enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                if (finish != null) {
                                    finish.error(url, file);
                                }
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) {
                                try {
                                    if (!file.getParentFile().exists()) {
                                        file.getParentFile().mkdirs();
                                    }
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    InputStream is = response.body().byteStream();
                                    FileOutputStream fos = new FileOutputStream(file);
                                    byte[] bytes = new byte[102400];
                                    int len = 0;
                                    while ((len = is.read(bytes)) > 0) {
                                        fos.write(bytes, 0, len);
                                    }
                                    fos.flush();
                                    fos.close();
                                    is.close();
                                    if (finish != null) {
                                        finish.finish(url, file);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if (finish != null) {
                                        finish.error(url, file);
                                    }
                                } finally {
                                }

                            }
                        }
                );
    }

    //                                RuiBoApplication.getApplication().sendBroadcast(new Intent("com.msx7.josn.ruibo_mediacenter.net.DownloadManager"));
//                                Headers responseHeaders = response.headers();
//                                for (int i = 0; i < responseHeaders.size(); i++) {
//                                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                                }
//
//                                System.out.println(response.body().string());
    public interface IDownFinish {
        void finish(String url, File file);

        void error(String url, File file);
    }

}
