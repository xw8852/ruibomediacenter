package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.AdminActivity;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.net.AdminLoginRequest;
import com.msx7.josn.ruibo_mediacenter.net.SyncSongRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: SyncSongDialog
 * 描  述: 管理员页面-歌曲库item
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class SyncSongDialog extends BaseCustomDialog {

    @InjectView(R.id.sure)
    TextView mSure;


    @InjectView(R.id.cancel)
    TextView mCancel;

    BaseActivity activity;

    public SyncSongDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_song, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        activity = (BaseActivity) context;
        setTitle("歌曲库");
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showProgess();
                RuiBoApplication.getApplication().runVolleyRequest(new SyncSongRequest(2, responseListener, errorListener));
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            activity.dismisProgess();
            L.d(response);
            BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
            if ("200".equals(baseBean.code)) {
                dismiss();
            }
            ToastUtil.show(baseBean.msg);

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            activity.dismisProgess();
            ToastUtil.show(VolleyErrorUtils.getError(error));
        }
    };


}
