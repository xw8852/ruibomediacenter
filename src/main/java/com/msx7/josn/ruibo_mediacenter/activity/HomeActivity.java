package com.msx7.josn.ruibo_mediacenter.activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.ui.CollectionView;
import com.msx7.josn.ruibo_mediacenter.activity.ui.SearchView;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.LoginDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.RecordListDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.ResetPasswdDialog;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.LoginRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.net.OkHttpManager;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

import com.msx7.josn.ruibo_mediacenter.R;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    @InjectView(R.id.root)
    View root;
    @InjectView(R.id.group)
    RadioGroup group;

    LoginDialog mLoginDialog;

    @InjectView(R.id.SearchView)
    SearchView mSearchView;

    @InjectView(R.id.CollectionView)
    CollectionView mCollectionView;

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
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = Math.min(dm.widthPixels, dm.heightPixels);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height / 2, height);
        root.setLayoutParams(params);
//        L.d("way", dm.widthPixels + "," + dm.heightPixels);
//        L.d("density = " + dm.density + ",densityDpi = " + dm.densityDpi);
//        L.d("xDPi = " + dm.xdpi + ",yDpi = " + dm.ydpi);
//        L.d("scaledDensity = " + dm.scaledDensity);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.home) {
                    mSearchView.setVisibility(View.VISIBLE);
                    mCollectionView.setVisibility(View.GONE);
                } else if (checkedId == R.id.collection) {
                    mSearchView.setVisibility(View.GONE);
                    mCollectionView.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETUSERINFO(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanUserInfo>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    SharedPreferencesUtil.saveUserInfo(baseBean.data);
                }
            }
        }, null);
        RuiBoApplication.getApplication().runVolleyRequest(request);


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
                mLoginDialog.getLoginPassWdView().setText("");
                mLoginDialog.getLoginNameView().setText("");
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
        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordListDialog(v.getContext()).show();
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

}
