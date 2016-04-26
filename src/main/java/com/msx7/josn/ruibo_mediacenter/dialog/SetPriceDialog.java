package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SetPriceDialog extends BaseCustomDialog {

    @InjectView(R.id.progress_wheel)
    ProgressWheel mProgress;

    @InjectView(R.id.priceRoot)
    ViewGroup mPriceRoot;


    @InjectView(R.id.ErrorTip)
    View mErrorTip;

    @InjectView(R.id.left)
    ViewGroup mPriceLeft;

    @InjectView(R.id.submit)
    View mSubmitPrice;

    @InjectView(R.id.right)
    ViewGroup mPriceRight;

    BaseActivity activity;

    public SetPriceDialog(Context context) {
        super(context);
        setTitle("曲目定价");
        activity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_set_price, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMusicType();
            }
        });
        loadMusicType();
        show();
        mSubmitPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    void dimissProgress() {
        activity.dismisProgess();
    }

    void showProgress() {
        activity.showProgess();
    }

    List<MusicTypeCell> musicTypeCells = new ArrayList<>();

    /**
     * 加载音乐类别
     */
    void loadMusicType() {
        mProgress.setVisibility(View.VISIBLE);
        mErrorTip.setVisibility(View.GONE);
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETMUSICTYPE(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<List<MusicType>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<MusicType>>>() {
                }.getType());
                mProgress.setVisibility(View.GONE);
                if ("-2".equals(baseBean.code)) {
                    ToastUtil.show(baseBean.msg);
                    mErrorTip.setVisibility(View.VISIBLE);
                    return;
                } else if (!"200".equals(baseBean.code)) {
                    mErrorTip.setVisibility(View.VISIBLE);
                    return;
                }
                mErrorTip.setVisibility(View.GONE);
                if (baseBean.data == null || baseBean.data.isEmpty()) {
                    ToastUtil.show("请在后台服务器上设置歌曲类别");
                    dismiss();
                    return;
                }
                musicTypeCells.clear();
                mPriceLeft.removeAllViews();
                mPriceRight.removeAllViews();
                int leftCount = baseBean.data.size() / 2;
                if (baseBean.data.size() % 2 > 0) leftCount++;
                mPriceLeft.setVisibility(View.VISIBLE);
                mPriceRight.setVisibility(View.VISIBLE);
                mPriceRoot.setVisibility(View.VISIBLE);
                mSubmitPrice.setVisibility(View.VISIBLE);
                int i = 0;
                while (i < leftCount) {
                    MusicTypeCell cell = new MusicTypeCell(getContext());
                    cell.setType(baseBean.data.get(i));
                    mPriceLeft.addView(cell);
                    musicTypeCells.add(cell);
                    i++;
                }
                while (i < baseBean.data.size()) {
                    MusicTypeCell cell = new MusicTypeCell(getContext());
                    cell.setType(baseBean.data.get(i));
                    mPriceRight.addView(cell);
                    musicTypeCells.add(cell);
                    i++;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.setVisibility(View.GONE);
                mErrorTip.setVisibility(View.VISIBLE);
            }
        }));
    }

    /**
     * 提交设置的音乐类别的价格
     */
    void submit() {
        List<MusicType> types = new ArrayList<>();
        for (MusicTypeCell cell : musicTypeCells) {
            types.add(cell.getCurrentMusicType());
        }
        String post = new Gson().toJson(types);
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_RESETMUSICTYPE(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dimissProgress();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("-2".equals(baseBean.code)) {
                    ToastUtil.show(baseBean.msg);
                    return;
                } else if (!"200".equals(baseBean.code)) {
                    ToastUtil.show(R.string.net_error);
                    return;
                }
                ToastUtil.show(baseBean.msg);
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissProgress();
                ToastUtil.show(R.string.net_error);
            }
        });
        request.addRequestJson(post);
        showProgress();
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    class MusicTypeCell extends LinearLayout {
        @InjectView(R.id.title)
        TextView name;

        @InjectView(R.id.price)
        TextView price;
        Keyboard1 keyboard1;

        public MusicTypeCell(Context context) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.layout_price_cell, this);
            Inject.inject(this, this);

            price.setInputType(InputType.TYPE_NULL);
            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    price.setText("");
                    int right = SetPriceDialog.this.findViewById(R.id.root).getRight();
                    keyboard1 = new Keyboard1(v, price);
                    keyboard1.setState(Keyboard1.State.SignNumber);
                    keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
                }
            });

            price.addTextChangedListener(new TextWatcher() {
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
                    price.setText(s.toString().substring(0, index + 2));
//                    if (d % 0.1 > 0) {
//                        d = d - d % 0.1;
//                        price.setText(df.format(d));
//                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        MusicType musicType;

        public void setType(MusicType musicType) {
            this.musicType = musicType;
            musicType.loginid = SharedPreferencesUtil.getAdminUserInfo().id;
            name.setText(musicType.name);
            price.setText(musicType.money + "");
        }

        public MusicType getCurrentMusicType() {
            if (TextUtils.isEmpty(price.getText().toString().trim())) musicType.money = 0;
            else musicType.money = Double.parseDouble(price.getText().toString().trim());
            return musicType;
        }
    }

    class MusicType {

        /**
         * id : 1
         * loginid : 0
         * money : 1
         * name : 流行歌曲
         */

        @SerializedName("id")
        public long id;
        @SerializedName("loginid")
        public long loginid;
        @SerializedName("money")
        public double money;
        @SerializedName("name")
        public String name;
    }
}
