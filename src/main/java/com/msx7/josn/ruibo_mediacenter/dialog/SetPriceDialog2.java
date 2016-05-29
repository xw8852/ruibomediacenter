package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SetPriceDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/4/2
 */
public class SetPriceDialog2 extends BaseCustomDialog {


    @InjectView(R.id.singlePrice)
    EditText mSinglePrice;
    @InjectView(R.id.maxPrice)
    EditText mMaxPrice;
    @InjectView(R.id.printPrice)
    EditText mPrintPrice;
    @InjectView(R.id.submit)
    TextView mSubmitBtn;

    public SetPriceDialog2(Context context) {
        super(context);
        setTitle("曲目定价");
        activity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_set_price2, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
//        mSinglePrice.addTextChangedListener(new NumberTextWatcher(mSinglePrice));
//        mMaxPrice.addTextChangedListener(new NumberTextWatcher(mMaxPrice));
//        mPrintPrice.addTextChangedListener(new NumberTextWatcher(mPrintPrice));
        mSinglePrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mSinglePrice.setInputType(InputType.TYPE_NULL);
        mSinglePrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mSinglePrice));

        mMaxPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mMaxPrice.setInputType(InputType.TYPE_NULL);
        mMaxPrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mMaxPrice));

        mPrintPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mPrintPrice.setInputType(InputType.TYPE_NULL);
        mPrintPrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mPrintPrice));

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    void submit() {
        if (TextUtils.isEmpty(mSinglePrice.getText().toString())) {
            ToastUtil.show("请设置下载单价");
            return;
        }
        if (TextUtils.isEmpty(mMaxPrice.getText().toString())) {
            ToastUtil.show("请设置封顶价格");
            return;
        }
        if (TextUtils.isEmpty(mPrintPrice.getText().toString())) {
            ToastUtil.show("请设置打印价格");
            return;
        }
        showProgess();
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_SETTINGMUSIC(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if ("200".equals(baseBean.code)) {
                            dismiss();
                        }
                        ToastUtil.show(baseBean.msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismisProgess();

                ToastUtil.show(R.string.error);
            }
        });
        request.addRequestJson(new Gson().toJson(new Post(
                Integer.parseInt(mSinglePrice.getText().toString()),
                Integer.parseInt(mMaxPrice.getText().toString()),
                Integer.parseInt(mPrintPrice.getText().toString())
        )));
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    class Post {
        @SerializedName("DownloadOneMusicPrice")
        int DownloadOneMusicPrice;
        @SerializedName("DownloadAllMusicPrice")
        int DownloadAllMusicPrice;
        @SerializedName("PrintPrice")
        int PrintPrice;

        public Post(int downloadOneMusicPrice, int downloadAllMusicPrice, int printPrice) {
            DownloadAllMusicPrice = downloadAllMusicPrice;
            DownloadOneMusicPrice = downloadOneMusicPrice;
            PrintPrice = printPrice;
        }
    }

    class NumberTextWatcher implements TextWatcher {
        EditText editText;

        public NumberTextWatcher(EditText editText) {
            this.editText = editText;
        }

        DecimalFormat df = new DecimalFormat("0.0");

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s))
                return;
            int index = s.toString().indexOf(".");
            if (index < 0 || s.length() - index - 1 < 2) return;
            L.d(s + "  " + s.toString().substring(0, index + 2));
            editText.setText(s.toString().substring(0, index + 2));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}