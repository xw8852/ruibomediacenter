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
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.dialog.ChongZhiDialog.PostData;
import com.msx7.josn.ruibo_mediacenter.net.CloseUserRequest;
import com.msx7.josn.ruibo_mediacenter.net.InputMoneyRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
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
public class BaoYueDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;

    @InjectView(R.id.login)
    TextView mLoginBtn;


    BaseActivity activity;

    public BaoYueDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_baoyue, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        setTitle("会员包月");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        mLoginName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mLoginName.setInputType(InputType.TYPE_NULL);
        mLoginName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginName.setText("");
                int right = findViewById(R.id.root).getRight();
                new Keyboard1(v, mLoginName).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });
    }


    void onLogin() {
        String loginName = mLoginName.getText().toString();
        if (TextUtils.isEmpty(loginName)) {
            mTips.setText("请输入会员卡号");
            return;
        }
        activity.showProgess();
        BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
        PostData postData = new PostData(info.id, mLoginName.getText().toString(), "0");
        postData.type = 1;
        RuiBoApplication.getApplication().runVolleyRequest(new InputMoneyRequest(new Gson().toJson(postData), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                L.d(response);
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    ToastUtil.show("会员包月成功");
                    dismiss();
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


}
