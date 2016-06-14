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
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.ResetAdminPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.ui.NumberTextWatcher;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: ResetPasswdDialog
 * 描  述: 管理员页面-管理员-登录密码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ResetAdminPasswdDialog extends BaseCustomDialog {
    BaseActivity activity;

    public ResetAdminPasswdDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_login, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        getLoginBtn().setText("立即生效");
        mOldPassWd.setVisibility(View.GONE);
        mOldPassWd.setHint("请输入旧密码");
        getLoginNameView().setHint("请输入新密码");
        getLoginPassWdView().setHint("请再次确认新密码");
        mLoginName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mOldPassWd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mLoginPassWd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        mOldPassWd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        getLoginNameView().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        getLoginPassWdView().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        getLoginBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReset();
            }
        });
        setTitle(R.string.reset_admin_password);


        mOldPassWd.setInputType(InputType.TYPE_NULL);
        mOldPassWd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOldPassWd.setText("");
                int right = findViewById(R.id.root).getRight();
                new Keyboard1(v, mOldPassWd).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });


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
        String passwd =keyboard1.getContent().substring(0, Math.min(6, keyboard1.getContent().length()));
//        String oldPasswd = mOldPassWd.getText().toString();
        String surePasswd = keyboard2.getContent().substring(0, Math.min(6, keyboard2.getContent().length()));
//        if (TextUtils.isEmpty(oldPasswd)) {
//            mTips.setText("请输入旧密码");
//            return;
//        }
        if (TextUtils.isEmpty(passwd)) {
            mTips.setText("请输入重设密码");
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
        RuiBoApplication.getApplication().runVolleyRequest(new ResetAdminPasswdRequest(passwd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
                    info.password = getLoginNameView().getText().toString();
                    SharedPreferencesUtil.saveAdminUserInfo(info);
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


    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.oldPassWd)
    TextView mOldPassWd;
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
