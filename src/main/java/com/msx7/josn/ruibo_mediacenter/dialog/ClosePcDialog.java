package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.msx7.josn.ruibo_mediacenter.CloseAppActivity;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: ClosePcDialog
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/22
 */
public class ClosePcDialog extends BaseCustomDialog {

    @InjectView(R.id.sure)
    TextView mSureBtn;
    @InjectView(R.id.cancel)
    TextView mCancelBtn;


    public ClosePcDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_close_pc, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mSureBtn.setOnClickListener(sureClickListener);
        mCancelBtn.setOnClickListener(cancelClickListener);
    }

    View.OnClickListener sureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProgess();
            RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.GET, UrlStatic.URL_CLOSEPC(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dismisProgess();
                            closeApp();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismisProgess();
                    closeApp();
                }
            }));
            dismiss();
        }
    };

    void closeApp() {
        ToastUtil.show("关机成功");
        Intent intent = new Intent(activity, CloseAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

}
