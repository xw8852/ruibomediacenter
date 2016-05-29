package com.msx7.josn.ruibo_mediacenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.ui.CollectionView;
import com.msx7.josn.ruibo_mediacenter.activity.ui.SearchView;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.LoginDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.ResetPasswdDialog;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.LoginRequest;
import com.msx7.josn.ruibo_mediacenter.util.DateUtil;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

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

    @InjectView(R.id.toLogin)
    View toLogin;

    @InjectView(R.id.collection)
    View collection;

    @InjectView(R.id.group)
    RadioGroup group;

    LoginDialog mLoginDialog;

    @InjectView(R.id.SearchView)
    public  SearchView mSearchView;

    @InjectView(R.id.CollectionView)
   public CollectionView mCollectionView;

    void click(View v) {

    }

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
        //登录按钮
        toLogin.setOnClickListener(loginClick);
    }

    //登录点击
    View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSearchView.showEnable(false);
            getLoginDialog().show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getUserInfo() != null) {
            showLogin();
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
            logOut();
            mSearchView.clear();
            collection.setEnabled(false);
            if (group.getCheckedRadioButtonId() == R.id.collection) {
                ((RadioButton) group.findViewById(R.id.home)).setChecked(true);
            }
            mSearchView.showEnable(false);
        }
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
//        if (SharedPreferencesUtil.getFailCount() >= 5 &&
//                System.currentTimeMillis() - SharedPreferencesUtil.getFailTime() < 1000 * 3600) {
//            mLoginDialog.getTipView().setText("登录失败5次，1小时内不能再次登录");
//            return;
//        } else if (System.currentTimeMillis() - SharedPreferencesUtil.getFailTime() >= 1000 * 3600) {
//            SharedPreferencesUtil.setFailCount(0);
//        }
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
                    showLogin();
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
                    showLogin();
                }
            }
        }, null));
    }


    public void logOut() {
        SharedPreferencesUtil.saveUserInfo(null);
        SharedPreferencesUtil.clearCollection();
        toLogin.setVisibility(View.VISIBLE);
        mUserView.setVisibility(View.INVISIBLE);
        collection.setEnabled(false);
        mSearchView.showEnable(false);
        mSearchView.setVisibility(View.VISIBLE);
        mCollectionView.setVisibility(View.GONE);
    }

    public void showLogin() {
        collection.setEnabled(true);
        mSearchView.showEnable(true);
        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
        toLogin.setVisibility(View.GONE);
        mUserView.setVisibility(View.VISIBLE);
        logOut.setOnClickListener(logoutClickListener);
        restPasswd.setOnClickListener(resetPasswdListener);
        StringBuffer html = new StringBuffer("会员:");
        html.append("<font color=\"#ff971e\">");
        html.append(userInfo.loginname);
        html.append("</font>");
        mAccount.setText(Html.fromHtml(html.toString()));

        html = new StringBuffer("余额:");
        html.append("<font color=\"#ff971e\">");
        html.append(userInfo.remainmoney + "");
        html.append("</font>");
        mMoney.setText(Html.fromHtml(html.toString()));

    }

    View.OnClickListener logoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logOut();
        }
    };
    View.OnClickListener resetPasswdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new ResetPasswdDialog(HomeActivity.this).show();
        }
    };

    @InjectView(R.id.userInfo)
    View mUserView;
    @InjectView(R.id.logOut)
    View logOut;
    @InjectView(R.id.resetPasswd)
    View restPasswd;
    @InjectView(R.id.account)
    TextView mAccount;
    @InjectView(R.id.money)
    TextView mMoney;

}
