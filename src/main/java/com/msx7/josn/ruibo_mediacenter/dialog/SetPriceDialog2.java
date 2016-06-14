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
    /**
     * 下载总容量
     */
    @InjectView(R.id.maxSize)
    EditText mMaxSize;
    /**
     * 下载上限
     */
    @InjectView(R.id.maxCount)
    EditText mMaxCount;

    @InjectView(R.id.submit)
    TextView mSubmitBtn;

    public SetPriceDialog2(Context context) {
        super(context);
        setTitle("曲目定价");
        activity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_set_price2, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));


        mSinglePrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mMaxPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPrintPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        mSinglePrice.setInputType(InputType.TYPE_NULL);
        mSinglePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSinglePrice.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard1 = new Keyboard1(v, mSinglePrice).setState(Keyboard1.State.SignNumber);
                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });

        mMaxPrice.setInputType(InputType.TYPE_NULL);
        mMaxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaxPrice.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard2 = new Keyboard1(v, mMaxPrice).setState(Keyboard1.State.SignNumber);
                keyboard2.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });

        mPrintPrice.setInputType(InputType.TYPE_NULL);
        mPrintPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrintPrice.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard3 = new Keyboard1(v, mPrintPrice).setState(Keyboard1.State.SignNumber);
                keyboard3.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


        mMaxCount.setInputType(InputType.TYPE_NULL);
        mMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrintPrice.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard4 = new Keyboard1(v, mPrintPrice).setState(Keyboard1.State.Number);
                keyboard4.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


        mMaxSize.setInputType(InputType.TYPE_NULL);
        mMaxSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrintPrice.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard5 = new Keyboard1(v, mPrintPrice).setState(Keyboard1.State.Number);
                keyboard5.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


//        mSinglePrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//        mSinglePrice.setInputType(InputType.TYPE_NULL);
//        mSinglePrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mSinglePrice));
//
//        mMaxPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//        mMaxPrice.setInputType(InputType.TYPE_NULL);
//        mMaxPrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mMaxPrice));
//
//        mPrintPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//        mPrintPrice.setInputType(InputType.TYPE_NULL);
//        mPrintPrice.addTextChangedListener(new com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher(mPrintPrice));

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    Keyboard1 keyboard1;
    Keyboard1 keyboard2;
    Keyboard1 keyboard3;
    Keyboard1 keyboard4;
    Keyboard1 keyboard5;

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
        if (TextUtils.isEmpty(mMaxCount.getText().toString())) {
            ToastUtil.show("下载上限");
            return;
        }
        if (TextUtils.isEmpty(mMaxSize.getText().toString())) {
            ToastUtil.show("下载总容量");
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
        request.addRequestJson(
                new Gson().toJson(
                        new Post(
                                Integer.parseInt(mSinglePrice.getText().toString()),
                                Integer.parseInt(mMaxPrice.getText().toString()),
                                Integer.parseInt(mPrintPrice.getText().toString()),
                                Integer.parseInt(mMaxCount.getText().toString()),
                                Integer.parseInt(mMaxSize.getText().toString())
                        )
                )
        );
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    class Post {
        @SerializedName("DownloadOneMusicPrice")
        int DownloadOneMusicPrice;
        @SerializedName("DownloadAllMusicPrice")
        int DownloadAllMusicPrice;
        @SerializedName("DownloadMusicAmount")
        int DownloadMusicAmount;
        @SerializedName("DownloadMusicSize")
        int DownloadMusicSize;
        @SerializedName("PrintPrice")
        int PrintPrice;

        public Post(int downloadOneMusicPrice, int downloadAllMusicPrice, int printPrice, int DownloadMusicAmount, int DownloadMusicSize) {
            DownloadAllMusicPrice = downloadAllMusicPrice;
            DownloadOneMusicPrice = downloadOneMusicPrice;
            this.DownloadMusicAmount = DownloadMusicAmount;
            this.DownloadMusicSize = DownloadMusicSize;
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
