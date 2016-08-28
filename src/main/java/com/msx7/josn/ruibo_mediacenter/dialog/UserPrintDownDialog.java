package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.ui.BeanView.CheckPost;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import java.text.DecimalFormat;

/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class UserPrintDownDialog extends Dialog {

    BaseActivity activity;


    TextView printNum;
    TextView print;
    TextView down;

    View add;
    View noPrint;
    View minus;
    int _printNum = 1;

    public UserPrintDownDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_user_down_print_dialog);
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
            }
        });

        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        printNum = (TextView) findViewById(R.id.printNum);
        down = (TextView) findViewById(R.id.down);
        noPrint = (TextView) findViewById(R.id.down1);
        print = (TextView) findViewById(R.id.printPrice);

        noPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        minus.setOnClickListener(minusClickListener);
        add.setOnClickListener(addClickListener);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });


        DecimalFormat a = new DecimalFormat("0.##");
        print.setText("(打印另收费\b:\b" + a.format(SharedPreferencesUtil.getUserInfo().entity.PrintPrice) + "元/张)");
    }

    void print() {
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_PRINTDOWNLOADLIST(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (!"200".equals(baseBean.code)) {
                    ToastUtil.show("请稍后重新尝试");
                    return;
                }
                dismiss();
                ToastUtil.show("请确保打印机正常连接，开始打印...");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show("请稍后重新尝试");
            }
        });
        request.addRequestJson(printNum.getText().toString());
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    View.OnClickListener minusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _printNum = Math.max(1, _printNum - 1);
            if (_printNum == 1) minus.setEnabled(false);
            else minus.setEnabled(true);
            printNum.setText(String.valueOf(_printNum));
        }
    };
    View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            minus.setEnabled(true);
            _printNum++;
            printNum.setText(String.valueOf(_printNum));
        }
    };


}
