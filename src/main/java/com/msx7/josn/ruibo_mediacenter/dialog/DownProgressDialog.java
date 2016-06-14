package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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


    public DownProgressDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_down_progress_dialog);
        View root = findViewById(R.id.root);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 2 / 5;
        root.setLayoutParams(params);
        mbar1 = (ProgressBar) findViewById(R.id.progressBar1);
        tv1 = (TextView) findViewById(R.id.text);
        findViewById(R.id.downFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    BeanView.CheckPost post;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void showDown(BeanView.CheckPost postData) {
        post = postData;
        show();
        setCancelable(false);
        mbar1.setProgress(0);
        startTimer();
        try {
            Call cal = new OkHttpClient().newCall(
                    new Request.Builder()
                            .url(UrlStatic.URL_DOWNLOADMUSIC())
                            .post(RequestBody.create(JSON, new Gson().toJson(postData)))
                            .build());
            cal.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {
                        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                stopTimer();
                                findViewById(R.id.downFinish).setVisibility(View.VISIBLE);
                                HomeActivity homeActivity = ((HomeActivity) activity);
                                if (homeActivity.searchFragment != null && homeActivity.searchFragment.mSearchView != null)
                                    homeActivity.searchFragment.mSearchView.clear();
                                if (homeActivity.collectionFragment != null && homeActivity.collectionFragment.mCollectionView != null)
                                    homeActivity.collectionFragment.mCollectionView.clear();
                            }
                        });
                        return;
                    }
                    RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(R.string.error);
                            dismiss();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (response.code() != 200) {
                        RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(R.string.error);
                                dismiss();
                            }
                        });
                        return;
                    }
                    final String body = response.body().string();
                    RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            BaseBean baseBean = new Gson().fromJson(body, BaseBean.class);
                            if (!"200".equals(baseBean.code)) {
                                dismiss();
                                ToastUtil.show(baseBean.msg);
                                return;
                            }
                            stopTimer();
                            HomeActivity homeActivity = ((HomeActivity) activity);
                            if (homeActivity.searchFragment != null && homeActivity.searchFragment.mSearchView != null)
                                homeActivity.searchFragment.mSearchView.clear();
                            if (homeActivity.collectionFragment != null && homeActivity.collectionFragment.mCollectionView != null)
                                homeActivity.collectionFragment.mCollectionView.clear();
                            findViewById(R.id.downFinish).setVisibility(View.VISIBLE);
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(R.string.error);
                    dismiss();
                }
            });
        } finally {
        }

//        BaseJsonRequest
//                jsonRequest = new BaseJsonRequest(POST, UrlStatic.URL_DOWNLOADMUSIC()
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
//                if (!"200".equals(baseBean.code)) {
//                    dismiss();
//                    ToastUtil.show(baseBean.msg);
//                    return;
//                }
//                stopTimer();
//                findViewById(R.id.downFinish).setVisibility(View.VISIBLE);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                ToastUtil.show(R.string.error);
//                dismiss();
//            }
//        }
//        );
//        jsonRequest.addRequestJson(new Gson().toJson(post));
//        RuiBoApplication.getApplication().runVolleyRequest(jsonRequest);
    }

    Timer timer;

    void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RuiBoApplication.getApplication().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        addProgess();
                    }
                });
            }
        }, 600, 600);
    }

    void stopTimer() {
        tv1.setText("下载进度:100");
        if (timer != null)
            timer.cancel();
        timer = null;
        mbar1.setProgress(100);
    }

    void addProgess() {
        int progress = mbar1.getProgress();
        progress += new Random().nextInt(15);
        progress = Math.min(99, progress);
        mbar1.setProgress(progress);
        tv1.setText("下载进度:" + progress);
        if (progress == 99) {
            if (timer != null)
                timer.cancel();
            timer = null;
        }
    }


}
