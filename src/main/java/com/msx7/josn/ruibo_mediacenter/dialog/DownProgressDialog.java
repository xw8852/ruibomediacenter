package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.HomeActivity;
import com.msx7.josn.ruibo_mediacenter.activity.net.UserInfoNet;
import com.msx7.josn.ruibo_mediacenter.activity.ui.BeanView;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.android.volley.Request.Method.POST;

/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class DownProgressDialog extends Dialog {

    BaseActivity activity;
    ProgressBar mbar1;
    TextView tv1;

    public static boolean isDownFinish = true;

    public DownProgressDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_down_progress_dialog);
        View root = findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 2 / 5;
        root.setLayoutParams(params);
        mbar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mbar1.setVisibility(View.GONE);
        setCancelable(true);
        tv1 = (TextView) findViewById(R.id.text);
        tv1.setText("开始下载，请等待目录打印完成");
        findViewById(R.id.downFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               refreshUserInfo();
            }
        });
    }

    void refreshUserInfo(){
//        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
//        userInfo.remainmoney = userInfo.remainmoney - post.money;
//                            if (post.needprint == 1) {
//                                userInfo.remainmoney = userInfo.remainmoney - userInfo.entity.PrintPrice;
//                            }
//        SharedPreferencesUtil.saveUserInfo(userInfo);
        HomeActivity homeActivity = ((HomeActivity) activity);
        homeActivity.refreshUserInfo();
    }
    BeanView.CheckPost post;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Call cal;

    public static final long CONNECT_TIMEOUT = 60;
    public static final long READ_TIMEOUT = 60 * 10;
    public static final long WRITE_TIMEOUT = 60 * 10;

    public void showDown(BeanView.CheckPost postData) {
        post = postData;
        show();
        isDownFinish = false;
        mbar1.setProgress(0);
        startTimer();
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
            L.d(UrlStatic.URL_DOWNLOADMUSIC());
            L.d(new Gson().toJson(postData));
            cal = client.newCall(
                    new Request.Builder()
                            .url(UrlStatic.URL_DOWNLOADMUSIC())
                            .post(RequestBody.create(JSON, new Gson().toJson(postData)))
                            .build());
            cal.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    isDownFinish = true;
                    if (e instanceof SocketTimeoutException
                            ) {
                        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("服务器响应超时，请稍后重新尝试");
//                                dismiss();
                                refreshUserInfo();
                            }
                        });
                        return;
                    }
                    if (e instanceof ConnectTimeoutException
                            || e instanceof ConnectException) {
                        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("连接服务器超时，请稍后重新尝试");
//                                dismiss();
                                refreshUserInfo();
                            }
                        });
                        return;
                    }
                    RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(R.string.error);
//                            dismiss();
                            refreshUserInfo();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    isDownFinish = true;
                    if (response.code() != 200) {
                        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(R.string.error);
//                                dismiss();
                                refreshUserInfo();
                            }
                        });
                        return;
                    }
                    final String body = response.body().string();
                    L.d(body);
                    RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            BaseBean baseBean = new Gson().fromJson(body, BaseBean.class);
                            if (!"200".equals(baseBean.code)) {
//                                dismiss();
                                refreshUserInfo();
                                ToastUtil.show(baseBean.msg);
                                return;
                            }
                            stopTimer();
//                            BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
//                            userInfo.remainmoney = userInfo.remainmoney - post.money;
//                            if (post.needprint == 1) {
//                                userInfo.remainmoney = userInfo.remainmoney - userInfo.entity.PrintPrice;
//                            }
//                            SharedPreferencesUtil.saveUserInfo(userInfo);
                            HomeActivity homeActivity = ((HomeActivity) activity);
                            homeActivity.refreshUserInfo();
                            if (homeActivity.searchFragment != null && homeActivity.searchFragment.mSearchView != null) {
                                if (HomeActivity.curFragment != homeActivity.searchFragment) {
                                    HomeActivity.clear = true;
                                } else
                                    homeActivity.searchFragment.mSearchView.clear();
                            }
                            if (homeActivity.collectionFragment != null && homeActivity.collectionFragment.mCollectionView != null)
                                homeActivity.collectionFragment.mCollectionView.clear();
//                            findViewById(R.id.downFinish).setVisibility(View.VISIBLE);
                        }
                    });

                }
            });
        } catch (Exception e) {
            isDownFinish = true;
            e.printStackTrace();
            RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(R.string.error);
//                    dismiss();
                }
            });
        } finally {
            refreshUserInfo();
        }
    }

    Timer timer;

    long startTime = 0;


    void addPro() {
        addProgess();
        if (System.currentTimeMillis() - startTime >= 1000 * 60 * 10) {
            ToastUtil.show("服务器响应超时，请稍后重新尝试");
            dismiss();
            if (cal != null) cal.cancel();
            cal = null;
        }
    }

    void startTimer() {
//        timer = new Timer();
//        startTime = System.currentTimeMillis();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                RuiBoApplication.getApplication().getHandler().post(new Runnable() {
//                                                                        @Override
//                                                                        public void run() {
//                                                                            addPro();
//                                                                        }
//                                                                    }
//                );
//            }
//        };
//        timer.schedule(timerTask, 600, 600);
    }

    void stopTimer() {
//        tv1.setText("下载进度:100");
//        if (timer != null)
//            timer.cancel();
//        timer = null;
//        mbar1.setProgress(100);
    }

    void addProgess() {
//        int progress = mbar1.getProgress();
//        progress += new Random().nextInt(15);
//        progress = Math.min(99, progress);
//        mbar1.setProgress(progress);
//        tv1.setText("下载进度:" + progress);
//        if (progress == 99) {
//            if (timer != null)
//                timer.cancel();
//            timer = null;
//        }
    }


}
