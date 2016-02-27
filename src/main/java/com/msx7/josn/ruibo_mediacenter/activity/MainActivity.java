package com.msx7.josn.ruibo_mediacenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.AdminLoginRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: MainActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/16
 */
@InjectActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.passwd)
    TextView mPasswd;
    @InjectView(R.id.login)
    View mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
        findViewById(R.id.backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTips.setText("");
    }

    void onLogin() {
        String passwd = mPasswd.getText().toString();
        if (TextUtils.isEmpty(passwd)) {
            mTips.setText("请输入管理员密码");
            return;
        }
        showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new AdminLoginRequest(passwd, responseListener, errorListener));
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            dismisProgess();
            L.d(response);
            BaseBean<BeanAdminInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanAdminInfo>>() {
            }.getType());
            if ("200".equals(baseBean.code)) {
                SharedPreferencesUtil.saveAdminUserInfo(baseBean.data);
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
            } else {
                mTips.setText(baseBean.msg);
            }
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dismisProgess();
            mTips.setText(VolleyErrorUtils.getError(error));
        }
    };
}
