package com.msx7.josn.ruibo_mediacenter;

import android.app.Application;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.liulishuo.filedownloader.FileDownloader;
import com.msx7.josn.ruibo_mediacenter.util.L;

/**
 * 文件名: RuiBoApplication
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class RuiBoApplication extends Application {
    private static RuiBoApplication application;

    public static final String MUSIC_FILE = "/mnt/usb_storage/";

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        application = this;
        /**
         * 仅仅是缓存Application的Context，不耗时
         */
        FileDownloader.init(getApplicationContext());

    }

    Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public static final RuiBoApplication getApplication() {
        return application;
    }


    public RequestQueue mQueue;

    //运行网络请求
    public void runVolleyRequest(Request request) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
            mQueue.start();
        }
        L.d(request.getUrl());
        try {
            if (request.getBody() != null)
                L.d(new String(request.getBody()));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } finally {
        }
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

}
