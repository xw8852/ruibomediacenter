package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件名: UserManagerDialog
 * 描  述: 管理员页面-会员管理
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class MonthStaticDialog extends BaseCustomDialog {


    @InjectView(R.id.Tv0)
    TextView Tv0;

    @InjectView(R.id.Tv1)
    TextView Tv1;

    @InjectView(R.id.Tv2)
    TextView Tv2;

    @InjectView(R.id.Tv3)
    TextView Tv3;

    @InjectView(R.id.TitleRight)
    TextView TitleRight;


    BaseActivity activity;

    public MonthStaticDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_static, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        activity = (BaseActivity) context;
        setTitle("机器编号");
        TitleRight = (TextView) findViewById(R.id.TitleRight);
        String url = UrlStatic.URL_MONTHSUMMARY();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Tv3.setText(sdf.format(new Date()));

        RuiBoApplication.getApplication().runVolleyRequest(
                new BaseJsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            setTitle("机器编号:" + baseBean.data.No);
                            TitleRight.setVisibility(View.VISIBLE);
                            TitleRight.setText("日期:" + baseBean.data.Date);
                            Tv0.setText("上月充值金额：" + baseBean.data.LastCharge);
                            Tv1.setText("本月充值金额：" + baseBean.data.CurrentCharge);
                            Tv2.setText("上月打印张数：" + baseBean.data.LastPrints);
                            Tv3.setText("本月打印张数：" + baseBean.data.CurrentPrints);
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


        @SerializedName("CurrentCharge")
        public int CurrentCharge;
        @SerializedName("CurrentPrints")
        public int CurrentPrints;
        @SerializedName("Date")
        public String Date;
        @SerializedName("LastCharge")
        public int LastCharge;
        @SerializedName("LastPrints")
        public int LastPrints;
        @SerializedName("No")
        public String No;
    }


}
