package com.msx7.josn.ruibo_mediacenter.activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.dialog.LoginDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.ResetPasswdDialog;
import com.msx7.josn.ruibo_mediacenter.net.LoginRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

import com.msx7.josn.ruibo_mediacenter.R;

/**
 * 文件名: HomeActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/20
 */
@InjectActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity {
    /**
     * 跳转至管理中心
     */
    @InjectView(R.id.toAdmin)
    View toAdmin;

    LoginDialog mLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);
        toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        if (SharedPreferencesUtil.getUserInfo() != null) {
            showUserInfo();
        } else
            unlogin();
    }

    /**
     * 初始化 登录界面信息
     */
    void unlogin() {
        mLoginDialog = new LoginDialog(this);
        mLoginDialog.getLoginBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginName = mLoginDialog.getLoginNameView().getText().toString();
                if (TextUtils.isEmpty(loginName)) {
                    mLoginDialog.getTipView().setText("账号不能为空");
                    return;
                }
                String loginPassWd = mLoginDialog.getLoginPassWdView().getText().toString();
                if (TextUtils.isEmpty(loginPassWd)) {
                    mLoginDialog.getTipView().setText("密码不能为空");
                    return;
                }
                showProgess();
                login(loginName, loginPassWd);
            }
        });
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.show();
            }
        });
    }

    /**
     * 执行登录
     *
     * @param loginName
     * @param loginPassWd
     */
    void login(String loginName, String loginPassWd) {
        RuiBoApplication.getApplication().runVolleyRequest(new LoginRequest(loginName, loginPassWd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismisProgess();
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanUserInfo>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    mLoginDialog.dismiss();
                    SharedPreferencesUtil.saveUserInfo(baseBean.data);
                    showUserInfo();
                } else {
                    mLoginDialog.getTipView().setText(baseBean.msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismisProgess();
                mLoginDialog.getTipView().setText(VolleyErrorUtils.getError(error));
            }
        }));
    }

    /**
     * 展示用户信息
     */
    void showUserInfo() {
        mLoginView.setVisibility(View.GONE);
        mLoginRootView.setVisibility(View.VISIBLE);
        mLoginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.clearUserInfo();
                mLoginView.setVisibility(View.VISIBLE);
                mLoginRootView.setVisibility(View.GONE);
                unlogin();
            }
        });
        mAccountName.setText(getString(R.string.account_name, SharedPreferencesUtil.getUserInfo().loginname));
        mAccountMoney.setText(getString(R.string.account_money, SharedPreferencesUtil.getUserInfo().remainmoney + ""));
        mResetPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetPasswdDialog(v.getContext()).show();
            }
        });
    }

    /**
     * 未登录默认显示
     */
    @InjectView(R.id.loginbg)
    View mLoginView;

    /**
     * 用户名
     */
    @InjectView(R.id.account_name)
    TextView mAccountName;

    /**
     * 余额
     */
    @InjectView(R.id.account_money)
    TextView mAccountMoney;

    /**
     * 用户信息根节点
     */
    @InjectView(R.id.loginRoot)
    View mLoginRootView;
    /**
     * 登出
     */
    @InjectView(R.id.loginOut)
    View mLoginOutBtn;

    /**
     * 重设密码
     */
    @InjectView(R.id.resetPasswd)
    View mResetPasswdBtn;

    /**
     * 交易记录
     */
    @InjectView(R.id.record)
    View mRecordBtn;


    void login() {

    }
}
