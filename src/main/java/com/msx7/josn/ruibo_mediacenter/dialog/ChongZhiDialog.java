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
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.InputMoneyRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.util.List;

/**
 * 文件名: UserManagerDialog
 * 描  述: 管理员页面-会员管理
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ChongZhiDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;

    @InjectView(R.id.price)
    TextView price;

    @InjectView(R.id.login)
    TextView mLoginBtn;

    @InjectView(R.id.UserRoot)
    View mUserRoot;

    @InjectView(R.id.loginRoot)
    View mLoginRoot;

    BaseActivity activity;

    public ChongZhiDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_chongzhi, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        setTitle("会员充值");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputMoney();
            }
        });

        mLoginName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mLoginName.setInputType(InputType.TYPE_NULL);
        mLoginName.addTextChangedListener(new NumberTextWatcher(mLoginName));
//        mLoginName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.requestFocus();
////                mLoginName.setText("");
////                int right = findViewById(R.id.root).getRight();
////                new Keyboard1(v, mLoginName).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
//            }
//        });
        price.setInputType(InputType.TYPE_NULL);
        price.addTextChangedListener(new NumberTextWatcher(price));
//        price.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.requestFocus();
////                price.setText("");
////                int right = findViewById(R.id.root).getRight();
////                Keyboard1 keyboard1 = new Keyboard1(v, price);
////                keyboard1.setState(Keyboard1.State.SignNumber);
////                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
//            }
//        });
    }


    void onInputMoney() {
        double money = 0;
        try {
            money = Double.parseDouble(price.getText().toString());
            if (money < 0) {
                mTips.setText("充值金额不能为0");
                activity.dismisProgess();
                return;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mTips.setText("充值金额有误");
            activity.dismisProgess();
            return;
        } finally {
        }
        BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
        PostData postData = new PostData(info.id, mLoginName.getText().toString(), price.getText().toString());
        RuiBoApplication.getApplication().runVolleyRequest(new InputMoneyRequest(new Gson().toJson(postData), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    dismiss();
                    ToastUtil.show("充值成功");
                } else {
                    mTips.setText(baseBean.msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.dismisProgess();
                mTips.setText(VolleyErrorUtils.getError(error));
            }
        }));
    }


    public static class PostData {

        /**
         * id : 1
         * loginid : 1
         * loginname : 111111
         * totalmoney : 2
         */

        @SerializedName("id")
        public int id;
        @SerializedName("type")
        public int type;
        @SerializedName("loginid")
        public int loginid;
        @SerializedName("loginname")
        public String loginname;
        @SerializedName("password")
        public String password;
        @SerializedName("totalmoney")
        public String totalmoney;

        public PostData(int id, String loginname, String totalmoney) {
            this.id = id;
            this.loginid = id;
            type = 0;
            password = "111111";
            this.loginname = loginname;
            this.totalmoney = totalmoney;
        }
    }

}
