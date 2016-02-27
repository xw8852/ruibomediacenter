package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
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
import com.msx7.josn.ruibo_mediacenter.net.CloseUserRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetAdminPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
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
public class UserManagerDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;

    @InjectView(R.id.login)
    TextView mLoginBtn;

    @InjectView(R.id.UserRoot)
    View mUserRoot;

    @InjectView(R.id.loginRoot)
    View mLoginRoot;

    BaseActivity activity;

    public UserManagerDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_user, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        setTitle("会员管理");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }


    public TextView getLoginBtn() {
        return mLoginBtn;
    }

    public TextView getLoginNameView() {
        return mLoginName;
    }


    public TextView getTipView() {
        return mTips;
    }


    void onLogin() {
        String loginName = mLoginName.getText().toString();
        if (TextUtils.isEmpty(loginName)) {
            mTips.setText("请输入会员卡号或手机号码");
            return;
        }
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new getUserRequest(loginName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean<List<BeanUserInfo>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanUserInfo>>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    if (baseBean.data.size() == 0) {
                        mTips.setText("卡号无效");
                        return;
                    }
                    beanUserInfo = baseBean.data.get(0);
                    initUser();
                    mLoginRoot.setVisibility(View.GONE);
                    mUserRoot.setVisibility(View.VISIBLE);
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

    BeanUserInfo beanUserInfo;

    @InjectView(R.id.et1)
    TextView mEt1;

    @InjectView(R.id.et2)
    TextView mEt2;

    @InjectView(R.id.et3)
    TextView mEt3;

    @InjectView(R.id.btn1)
    TextView mbtn1;

    @InjectView(R.id.btn2)
    TextView mbtn2;

    @InjectView(R.id.btn3)
    TextView mbtn3;


    void initUser() {
        mEt1.setText("会员卡号:" + beanUserInfo.loginname);
        mEt2.setText("账户余额:¥" + beanUserInfo.remainmoney);
        mEt3.setText("手机号码:");
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseAccount();
            }
        });
        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInMoney();
            }
        });
        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswd();
            }
        });
    }

    void onCloseAccount() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new CloseUserRequest(beanUserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    ToastUtil.show("销户成功");
                    dismiss();
                } else {
                    getTipView().setText(baseBean.msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.dismisProgess();
            }
        }));
    }

    void onInMoney() {
        new InputMoneyDialog(activity, beanUserInfo, new InputMoneyDialog.IGetMoney() {
            @Override
            public void input(double money) {
                beanUserInfo.totalmoney += money;
                beanUserInfo.remainmoney += money;
                mEt2.setText("账户余额:¥" + beanUserInfo.remainmoney);
            }
        }).show();
    }


    void onResetPasswd() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new ResetAdminPasswdRequest(beanUserInfo.password, "111111", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    beanUserInfo.password = "111111";
                    ToastUtil.show("重设初始密码成功");
                } else {
                    getTipView().setText(baseBean.msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.dismisProgess();
            }
        }));
    }


}
