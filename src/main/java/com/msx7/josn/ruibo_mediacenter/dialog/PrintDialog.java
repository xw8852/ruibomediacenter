package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.CloseUserRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 文件名: UserManagerDialog
 * 描  述: 管理员页面-会员管理
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class PrintDialog extends BaseCustomDialog {


    @InjectView(R.id.Tv0)
    TextView Tv0;

    @InjectView(R.id.Tv1)
    TextView Tv1;

    @InjectView(R.id.Tv2)
    TextView Tv2;

    @InjectView(R.id.Tv3)
    TextView Tv3;


    BaseActivity activity;

    public PrintDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_print, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        activity = (BaseActivity) context;
        setTitle("打印统计");

        String url = UrlStatic.URL_PRINTREPORT();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Tv3.setText(sdf.format(new Date()));

        RuiBoApplication.getApplication().runVolleyRequest(
                new BaseJsonRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseBean<Post> baseBean
                                = new Gson().fromJson(response,
                                new TypeToken<BaseBean<Post>>() {
                                }.getType());
                        if (!"200".equals(baseBean.code)) {
                            ToastUtil.show(baseBean.msg);
                            return;
                        } else {
                            Tv0.setText("机器编号:"+baseBean.data.ServerNumber);
                            Tv1.setText("上月打印统计"+baseBean.data.LastMonthPrintQty+"张");
                            Tv2.setText("本月打印统计"+baseBean.data.CurrentMonthPrintQty+"张");
                            Tv3.setText(baseBean.data.CurrentDateTime);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismiss();
                        ToastUtil.show(R.string.error);
                    }
                })
        );
    }


    class Post {
        @SerializedName("LastMonthPrintQty")
        long LastMonthPrintQty;
        @SerializedName("CurrentMonthPrintQty")
        long CurrentMonthPrintQty;
        @SerializedName("ServerNumber")
        String ServerNumber;
        @SerializedName("CurrentDateTime")
        String CurrentDateTime;

    }


}
