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
import com.msx7.josn.ruibo_mediacenter.util.DateUtil;
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
    @InjectView(R.id.collection)
    View collection;
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
        mSearchView.showEnable(false);
        SharedPreferencesUtil.saveUserInfo(null);
        SharedPreferencesUtil.clearCollection();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = Math.min(dm.widthPixels, dm.heightPixels);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height / 2, height);
        root.setLayoutParams(params);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.home) {
                    mSearchView.setVisibility(View.VISIBLE);
                    mCollectionView.setVisibility(View.GONE);
                } else if (checkedId == R.id.collection) {
                    mSearchView.clear();
                    mSearchView.setVisibility(View.GONE);
                    mCollectionView.setVisibility(View.VISIBLE);
                    mCollectionView.showData();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getUserInfo() != null) {
            showUserInfo();
            collection.setEnabled(true);
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
            request.addRequestJson(new Gson().toJson(SharedPreferencesUtil.getUserInfo()));
            RuiBoApplication.getApplication().runVolleyRequest(request);
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mLoginRootView.setVisibility(View.GONE);
            mSearchView.clear();
            collection.setEnabled(false);
            if (group.getCheckedRadioButtonId() == R.id.collection) {
                ((RadioButton) group.findViewById(R.id.home)).setChecked(true);
            }
            unlogin();
        }
    }

    /**
     * 初始化 登录界面信息
     */
    void unlogin() {
        mSearchView.showEnable(false);
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoginDialog().show();
            }
        });
    }


    LoginDialog getLoginDialog() {
        mLoginDialog = new LoginDialog(this);
        mLoginDialog.getLoginBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginName = mLoginDialog.getLoginNameView().getText().toString();
                if (TextUtils.isEmpty(loginName)) {
                    mLoginDialog.getTipView().setText("账号不能为空");
                    return;
                }
                String loginPassWd = mLoginDialog.getPasswd();
                if (TextUtils.isEmpty(loginPassWd)) {
                    mLoginDialog.getTipView().setText("密码不能为空");
                    return;
                }
                showProgess();
                login(loginName, loginPassWd);
            }
        });
        return mLoginDialog;
    }

    public static long FAIL_TIME = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtil.setFailCount(0);
    }

    /**
     * 执行登录
     *
     * @param loginName
     * @param loginPassWd
     */
    void login(String loginName, String loginPassWd) {
        if (SharedPreferencesUtil.getFailCount() >= 5 &&
                System.currentTimeMillis() - SharedPreferencesUtil.getFailTime() < 1000 * 3600) {
            mLoginDialog.getTipView().setText("登录失败5次，1小时内不能再次登录");
            return;
        } else if (System.currentTimeMillis() - SharedPreferencesUtil.getFailTime() >= 1000 * 3600) {
            SharedPreferencesUtil.setFailCount(0);
        }
        RuiBoApplication.getApplication().runVolleyRequest(new LoginRequest(loginName, loginPassWd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.d(response);
                dismisProgess();
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanUserInfo>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    mLoginDialog.dismiss();
                    SharedPreferencesUtil.saveUserInfo(baseBean.data);
                    collection.setEnabled(true);
                    showUserInfo();
                } else {
                    SharedPreferencesUtil.setFailTIME(System.currentTimeMillis());
                    SharedPreferencesUtil.setFailCount(SharedPreferencesUtil.getFailCount() + 1);
                    mLoginDialog.getLoginNameView().setText("");
                    mLoginDialog.getLoginPassWdView().setText("");
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
        mSearchView.showEnable(true);
        mLoginView.setVisibility(View.GONE);
        mLoginRootView.setVisibility(View.VISIBLE);
        mLoginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.clearUserInfo();
                mLoginView.setVisibility(View.VISIBLE);
                mLoginRootView.setVisibility(View.GONE);
                mSearchView.clear();
                collection.setEnabled(false);
                if (group.getCheckedRadioButtonId() == R.id.collection) {
                    ((RadioButton) group.findViewById(R.id.home)).setChecked(true);
                }
                unlogin();
            }
        });
        mAccountName.setText(getString(R.string.account_name, SharedPreferencesUtil.getUserInfo().loginname));
        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
        mAccountMoney.setText(getString(R.string.account_money, String.valueOf(userInfo.remainmoney)));
        if (userInfo.type == 1) {
            mAccountMoney.setText(getString(R.string.huiyuan, "包月制"));
            mstartTime.setVisibility(View.VISIBLE);
            mendTime.setVisibility(View.VISIBLE);
            mstartTime.setText(getString(R.string.start_time, DateUtil.converTime(userInfo.startdate)));
            mendTime.setText(getString(R.string.start_time, DateUtil.converTime(userInfo.enddate)));
        } else {
            mstartTime.setVisibility(View.GONE);
            mendTime.setVisibility(View.GONE);
        }
        mResetPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetPasswdDialog(v.getContext()).show();
            }
        });
//        mRecordBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new RecordListDialog(v.getContext()).show();
//            }
//        });
    }

    /**
     * 更新用户信息
     */
    public void refreshUserInfo() {
        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
        RuiBoApplication.getApplication().runVolleyRequest(new LoginRequest(userInfo.loginname, userInfo.password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.d(response);
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanUserInfo>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    SharedPreferencesUtil.saveUserInfo(baseBean.data);
                    collection.setEnabled(true);
                    showUserInfo();
                }
            }
        }, null));
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

    @InjectView(R.id.startTime)
    TextView mstartTime;

    @InjectView(R.id.endTime)
    TextView mendTime;

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

//    /**
//     * 交易记录
//     */
//    @InjectView(R.id.record)
//    View mRecordBtn;

}
