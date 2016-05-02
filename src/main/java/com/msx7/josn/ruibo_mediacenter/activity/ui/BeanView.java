package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.HomeActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.DownloadDialog;
import com.msx7.josn.ruibo_mediacenter.down.ThreadPool;
import com.msx7.josn.ruibo_mediacenter.net.OkHttpManager;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 文件名: BeanView
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/3/2
 */
public class BeanView extends LinearLayout {
    public BeanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    protected void download(final List<BeanMusic> urls, final String path) {
        showDialog();
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(15, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            client.newCall(
                    new Request.Builder()
                            .url(UrlStatic.URL_DOWNLOADMUSIC())
                            .post(RequestBody.create(JSON, new Gson().toJson(urls)))
                            .build())
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    dismissDialog();
                                    ToastUtil.show("下载失败，请稍后重试");
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                String str = response.body().string();
                                L.d(call.request().url().toString());
                                L.d(str);
                                BaseBean baseBean = new Gson().fromJson(str, BaseBean.class);
                                if ("200".equals(baseBean.code)) {
                                    ((HomeActivity) getContext()).refreshUserInfo();
                                    downloadMusic(urls, path);
                                }
                            } else {

                                RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        ToastUtil.show("下载失败，请稍后重试");
                                    }
                                });
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            dismissDialog();
            RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show("下载失败，请稍后重试");
                }
            });
        } finally {

        }

    }

    Dialog mDialog;

    void showDialog() {
        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mDialog == null) {
                    mDialog = new DownloadDialog(getContext());
                    mDialog.setCancelable(false);
                }
                mDialog.setCancelable(false);
                mDialog.show();
            }
        });
    }

    void dismissDialog() {
        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });

    }

    void downloadMusic(List<BeanMusic> _urls, String path) {
        List<String> urls = new ArrayList<String>();
        for (BeanMusic music : _urls) {
            urls.add(music.path);
        }
        new ThreadPool().enqueue(urls, path, new ThreadPool.IDownListener() {
            @Override
            public void finish(ThreadPool.Down down) {
                RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                        ToastUtil.show("下载成功！");
                    }
                });
            }
        });
//        final int count = urls.size();
//        com.msx7.josn.ruibo_mediacenter.net.DownloadManager.download(urls, (BaseActivity) getContext(), new OkHttpManager.IDownFinish() {
//            int downcount = 0;
//
//            @Override
//            public void finish(String url, File file) {
//                downcount++;
//                Log.d("finish", url);
//                if (downcount == count) {
//                    dismissDialog();
//                    RuiBoApplication.getApplication().getHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtil.show("下载成功！");
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void error(String url, File file) {
//                Log.d("FAIL", url);
//                downcount++;
//                if (downcount == count) {
//                    dismissDialog();
//                }
//            }
//        });
    }
}
