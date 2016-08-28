package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.HomeActivity;
import com.msx7.josn.ruibo_mediacenter.activity.ui.BeanView;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import static com.android.volley.Request.Method.POST;


/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class DownProgressDialog extends Dialog {

    BaseActivity activity;
    TextView tv1;


    public DownProgressDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_down_progress_dialog);
        View root = findViewById(R.id.root);
        tv1 = (TextView) findViewById(R.id.text);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 2 / 5;
        root.setLayoutParams(params);
        setCancelable(false);
        findViewById(R.id.downFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDownProgress();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshUserInfo();
            }
        });
    }

    void refreshUserInfo() {
        HomeActivity homeActivity = ((HomeActivity) activity);
        homeActivity.refreshUserInfo();
        if (!isShowing()) return;
        if (homeActivity.searchFragment != null && homeActivity.searchFragment.mSearchView != null) {
            if (HomeActivity.curFragment != homeActivity.searchFragment) {
                HomeActivity.clear = true;
            } else
                homeActivity.searchFragment.mSearchView.clear();
        }
        if (homeActivity.collectionFragment != null && homeActivity.collectionFragment.mCollectionView != null)
            homeActivity.collectionFragment.mCollectionView.clear();
    }


    void checkDownProgress() {

        BaseJsonRequest request = new BaseJsonRequest(POST, UrlStatic.URL_GETDOWNLOADPROGRESS(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if (!"200".equals(baseBean.code)) {
                            ToastUtil.show("获取下载进度失败，请稍后重新尝试");
                            return;
                        }
                        String progress = baseBean.data.toString();
                        if (progress.contains("100")) {
                            dismiss();
                            new UserPrintDownDialog(activity).show();
                            return;
                        }
                        tv1.setText("已下载 " + progress + "，请耐心等待...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.show("获取下载进度失败，请稍后重新尝试");
                    }
                });
        RuiBoApplication.getApplication().runVolleyRequest(request);
        refreshUserInfo();
    }

    BeanView.CheckPost post;


    public void showDown(BeanView.CheckPost postData) {
        post = postData;
        show();
        BaseJsonRequest request = new BaseJsonRequest(POST, UrlStatic.URL_DOWNLOADMUSIC(), resultListener, errorListener);
        request.addRequestJson(new Gson().toJson(postData));
        RuiBoApplication.getApplication().runVolleyRequest(request);
        refreshUserInfo();
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            ToastUtil.show("下载失败，请稍后重新尝试");
            dismiss();
            refreshUserInfo();
        }
    };
    Response.Listener<String> resultListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            refreshUserInfo();
            BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
            if (!"200".equals(baseBean.code)) {
                ToastUtil.show(baseBean.msg);
                dismiss();
                return;
            }
        }


    };
}
