package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.Config;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.net.UserInfoNet;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.CloseUserRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetAdminPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher;
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

        mLoginName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Config.MAX_LOGIN_NAME_LENGTH)});
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
            mTips.setText("请输入会员卡号");
            return;
        }
        activity.showProgess();
        UserInfoNet.getUserInfoNet(loginName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanUserInfo>>() {
                }.getType());
                if ("200".equals(baseBean.code)) {
                    if (baseBean.data == null) {
                        mTips.setText("卡号无效");
                        return;
                    }
                    beanUserInfo = baseBean.data;
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
        });
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
        if (beanUserInfo.type == 1) {
            mEt2.setText("会员类型:包月用户");
        } else
            mEt2.setText("账户余额:¥" + beanUserInfo.remainmoney);
        mEt3.setText("手机号码:" + (TextUtils.isEmpty(beanUserInfo.phone) ? "" : beanUserInfo.phone));
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseAccount();
            }
        });
        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhone();
            }
        });
        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswd();
            }
        });

//        mEt1.setInputType(InputType.TYPE_NULL);
//        mEt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEt1.setText("");
//                int right = findViewById(R.id.root).getRight();
//                new Keyboard1(v, mEt1).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
//            }
//        });
//
//        mEt2.setInputType(InputType.TYPE_NULL);
//        mEt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEt2.setText("");
//                int right = findViewById(R.id.root).getRight();
//                new Keyboard1(v, mEt2).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
//            }
//        });
    }

    SetPhoneDialog phoneDialog;

    void setPhone() {
        dismiss();
        phoneDialog = new SetPhoneDialog(activity, beanUserInfo.id);
        phoneDialog.setPhone(beanUserInfo.phone);
        phoneDialog.setListener(new SetPhoneDialog.IFinish() {
            @Override
            public void finish(String phone) {
                beanUserInfo.phone = phone;
                initUser();
                if(!isShowing())show();
            }
        });
        phoneDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!isShowing()) show();
            }
        });
        phoneDialog.show();
    }

    void onCloseAccount() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new CloseUserRequest(beanUserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.d(response);
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
        ResetPasswdRequest.Post post = new ResetPasswdRequest.Post();
        post.id = beanUserInfo.id;
        post.loginid = beanUserInfo.id;
        post.loginname = beanUserInfo.loginname;
        post.confirmnewspassword = "111111";
        post.newpassword = "111111";
        post.password = beanUserInfo.password;
        post.inmoneypassword = "111111";
        RuiBoApplication.getApplication().runVolleyRequest(new ResetPasswdRequest(post, new Response.Listener<String>() {
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
