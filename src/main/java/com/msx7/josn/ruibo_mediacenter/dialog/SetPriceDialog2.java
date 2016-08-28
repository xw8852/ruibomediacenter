package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
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
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
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
    /**
     * 列数
     */
    @InjectView(R.id.vec)
    EditText vec;
    /**
     * 行数
     */
    @InjectView(R.id.hor)
    EditText hor;

    @InjectView(R.id.submit)
    TextView mSubmitBtn;

    public SetPriceDialog2(Context context) {
        super(context);
        setTitle("曲目定价");
        activity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_set_price2, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));


        //列数
        vec.setText(SharedPreferencesUtil.getRow1() + "");
        //行数
        hor.setText(SharedPreferencesUtil.getRow2() + "");

        mSinglePrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mMaxPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPrintPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        hor.setInputType(InputType.TYPE_CLASS_NUMBER);
        vec.setInputType(InputType.TYPE_CLASS_NUMBER);


        vec.setInputType(InputType.TYPE_NULL);
        vec.setFilters(new InputFilter[]{new DialerKeyListener(), new InputFilter.LengthFilter(6)});
        vec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vec.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard1 = new Keyboard1(v, vec).setState(Keyboard1.State.Number);
                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });

        hor.setInputType(InputType.TYPE_NULL);
        hor.setFilters(new InputFilter[]{new DialerKeyListener(), new InputFilter.LengthFilter(6)});
        hor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hor.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard1 = new Keyboard1(v, hor).setState(Keyboard1.State.Number);
                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });

        mSinglePrice.setInputType(InputType.TYPE_NULL);
        mSinglePrice.setFilters(new InputFilter[]{new DialerKeyListener(), new InputFilter.LengthFilter(6)});
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
        mMaxPrice.setFilters(new InputFilter[]{new DialerKeyListener(), new InputFilter.LengthFilter(6)});
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
        mPrintPrice.setFilters(new InputFilter[]{new DialerKeyListener(), new InputFilter.LengthFilter(6)});
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
        mMaxCount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaxCount.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard4 = new Keyboard1(v, mMaxCount).setState(Keyboard1.State.Number);
                keyboard4.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


        mMaxSize.setInputType(InputType.TYPE_NULL);
        mMaxSize.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mMaxSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaxSize.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard5 = new Keyboard1(v, mMaxSize).setState(Keyboard1.State.Number);
                keyboard5.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


        BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
        mSinglePrice.setText(String.valueOf(info.entity.DownloadOneMusicPrice));
        mMaxPrice.setText(String.valueOf(info.entity.DownloadAllMusicPrice));
        mPrintPrice.setText(String.valueOf(info.entity.PrintPrice));
        mMaxCount.setText(String.valueOf((int) info.entity.DownloadMusicAmount));
        mMaxSize.setText(String.valueOf(info.entity.DownloadMusicSize / 1024));

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
                try {
                    submit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
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
            ToastUtil.show("请设置下载上限");
            return;
        }
        if (TextUtils.isEmpty(mMaxSize.getText().toString())) {
            ToastUtil.show("请设置下载总容量");
            return;
        }
        if (!TextUtils.isDigitsOnly(vec.getText().toString())) {
            ToastUtil.show("请输入列数");
            return;
        }
        if (!TextUtils.isDigitsOnly(hor.getText().toString())) {
            ToastUtil.show("请输入行数");
            return;
        }
        SharedPreferencesUtil.saveRow1(Integer.parseInt(vec.getText().toString()));

        SharedPreferencesUtil.saveRow2(Integer.parseInt(vec.getText().toString()));

        showProgess();
        if (!TextUtils.isEmpty(vec.getText().toString()))
            SharedPreferencesUtil.saveRow1(Integer.parseInt(vec.getText().toString()));
        if (!TextUtils.isEmpty(hor.getText().toString()))
            SharedPreferencesUtil.saveRow2(Integer.parseInt(hor.getText().toString()));
        final Post post = new Post(
                Double.parseDouble(mSinglePrice.getText().toString()),
                Double.parseDouble(mMaxPrice.getText().toString()),
                Double.parseDouble(mPrintPrice.getText().toString()),
                Long.parseLong(mMaxCount.getText().toString()),
                Long.parseLong(mMaxSize.getText().toString()) * 1024
        );
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_SETTINGMUSIC(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if ("200".equals(baseBean.code)) {
                            BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
                            info.entity.DownloadAllMusicPrice = post.DownloadAllMusicPrice;
                            info.entity.DownloadOneMusicPrice = post.DownloadOneMusicPrice;
                            info.entity.DownloadMusicAmount = post.DownloadMusicAmount;
                            info.entity.DownloadMusicSize = post.DownloadMusicSize;
                            info.entity.PrintPrice = post.PrintPrice;
                            SharedPreferencesUtil.saveAdminUserInfo(info);
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
                new Gson().toJson(post)
        );
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    class Post {
        @SerializedName("DownloadOneMusicPrice")
        double DownloadOneMusicPrice;
        @SerializedName("DownloadAllMusicPrice")
        double DownloadAllMusicPrice;
        @SerializedName("DownloadMusicAmount")
        long DownloadMusicAmount;
        @SerializedName("DownloadMusicSize")
        long DownloadMusicSize;
        @SerializedName("PrintPrice")
        double PrintPrice;

        public Post(double downloadOneMusicPrice, double downloadAllMusicPrice, double printPrice, long DownloadMusicAmount, long DownloadMusicSize) {
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
