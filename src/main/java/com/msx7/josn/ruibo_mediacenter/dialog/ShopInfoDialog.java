package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.text.DecimalFormat;

/**
 * 文件名: SetPriceDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/4/2
 */
public class ShopInfoDialog extends BaseCustomDialog {


    @InjectView(R.id.name)
    EditText mName;

    @InjectView(R.id.phone)
    EditText mPhone;

    @InjectView(R.id.address)
    EditText mAddress;


    @InjectView(R.id.submit)
    TextView mSubmitBtn;

    public ShopInfoDialog(Context context) {
        super(context);
        setTitle("曲目定价");
        activity = (BaseActivity) context;
        LayoutInflater.from(context).inflate(R.layout.layout_dialog_shop_info, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(
                Request.Method.POST, UrlStatic.URL_getOwnerInfo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<Post> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<Post>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    mName.setText(baseBean.data.OwnerName);
                    mPhone.setText(baseBean.data.Phone);
                    mAddress.setText(baseBean.data.Address);
                }
            }
        }, null));
    }


    void submit() {
        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();
        String address = mAddress.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入店主姓名");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入电话号码");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show("请输入地址");
            return;
        }

        dismiss();
        Post post = new Post(name, phone, address);
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_saveOwnerInfo(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if (!"200".equals(baseBean.code)) {
                            show();
                            ToastUtil.show(baseBean.msg);
                        }
                        ToastUtil.show(baseBean.msg);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismisProgess();
                ToastUtil.show(R.string.error);
                show();
            }
        });

        request.addRequestJson(
                new Gson().toJson(post)
        );
        RuiBoApplication.getApplication().runVolleyRequest(request);
        showProgess();
    }

    class Post {

        @SerializedName("ownername")
        public String OwnerName;

        @SerializedName("phone")
        public String Phone;

        @SerializedName("address")
        public String Address;

        public Post() {
        }

        public Post(String address, String ownerName, String phone) {
            Address = address;
            OwnerName = ownerName;
            Phone = phone;
        }
    }

}
