package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.support.v4.app.NavUtils;
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
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: ResetPasswdDialog
 * 描  述: 首页-重置密码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ResetPasswdDialog extends BaseCustomDialog {
    BaseActivity activity;

    public ResetPasswdDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_login, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        getLoginBtn().setText("立即生效");
        getLoginNameView().setHint("请重设密码(6位)");
        getLoginPassWdView().setHint("请再次确认密码(6位)");
        mLoginName. setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        mLoginPassWd. setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        getLoginBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReset();
            }
        });
        setTitle(R.string.reset_password);

        mLoginName.setInputType(InputType.TYPE_NULL);
        mLoginName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginName.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard1 = new Keyboard1(v, mLoginName).setState(Keyboard1.State.password);
                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


        mLoginPassWd.setInputType(InputType.TYPE_NULL);
        mLoginPassWd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPassWd.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard2 = new Keyboard1(v, mLoginPassWd).setState(Keyboard1.State.password);
                keyboard2.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


    }

    Keyboard1 keyboard1;
    Keyboard1 keyboard2;
    void goReset() {
        if(keyboard1== null||keyboard2==null)return;
        String passwd = keyboard1.getContent().substring(0, Math.min(6, keyboard1.getContent().length()));
        String surePasswd = keyboard2.getContent().substring(0, Math.min(6, keyboard2.getContent().length()));
        if (TextUtils.isEmpty(passwd)) {
            mTips.setText("请输入重设密码");
            return;
        }
        if (passwd.length() != 6) {
            mTips.setText("密码位数不正确");
            return;
        }
        if (TextUtils.isEmpty(surePasswd)) {
            mTips.setText("请再次确认密码");
            return;
        }
        if (!passwd.equals(surePasswd)) {
            mTips.setText("2次输入密码不一致");
            return;
        }
        activity.showProgess();
        ResetPasswdRequest.Post post = new ResetPasswdRequest.Post();
        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
        post.loginid = userInfo.loginid;
        if (userInfo.loginid == 0) {
            post.loginid = userInfo.id;
        }
        post.id = userInfo.id;
        post.loginname = userInfo.loginname;
        post.confirmnewspassword = surePasswd;
        post.newpassword = passwd;
        post.password = userInfo.password;
        post.inmoneypassword = surePasswd;
        RuiBoApplication.getApplication().runVolleyRequest(new ResetPasswdRequest(post, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    BeanUserInfo info = SharedPreferencesUtil.getUserInfo();
                    info.password = getLoginNameView().getText().toString();
                    SharedPreferencesUtil.saveUserInfo(info);
                    dismiss();
                    ToastUtil.show("重设密码成功");
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


    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;
    @InjectView(R.id.loginPasswd)
    TextView mLoginPassWd;
    @InjectView(R.id.login)
    TextView mLoginBtn;


    public TextView getLoginBtn() {
        return mLoginBtn;
    }

    public TextView getLoginNameView() {
        return mLoginName;
    }

    public TextView getLoginPassWdView() {
        return mLoginPassWd;
    }

    public TextView getTipView() {
        return mTips;
    }
}
