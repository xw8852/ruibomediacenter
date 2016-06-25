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
import com.msx7.josn.ruibo_mediacenter.Config;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.CloseUserRequest;
import com.msx7.josn.ruibo_mediacenter.net.PhoneRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
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
public class SetPhoneDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;

    @InjectView(R.id.login)
    TextView mLoginBtn;


    BaseActivity activity;

    long id;

    public SetPhoneDialog(Context context, long id) {
        super(context);
        this.id = id;
        getLayoutInflater().inflate(R.layout.layout_dialog_user, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        setTitle("设置手机号码");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
        mLoginName.setHint("请输入手机号码");
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

    /**
     * 显示已有的号码
     *
     * @param phone
     */
    public void setPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return;
        mLoginName.setText(phone);
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

    IFinish finish;

    public void setListener(IFinish finish) {
        this.finish = finish;
    }

    void onLogin() {
        final String loginName = mLoginName.getText().toString();
        if (TextUtils.isEmpty(loginName)) {
            mTips.setText("请输入手机号码");
            return;
        }
        if (loginName.length() != 11) {
            mTips.setText("手机号码长度不正确");
            return;
        }
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new PhoneRequest(loginName, id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    ToastUtil.show("设置手机号码成功");
                    dismiss();
                    if (finish != null) finish.finish(loginName);
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

    public interface IFinish {
        void finish(String phone);
    }
}
