package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.InputMoneyRequest;
import com.msx7.josn.ruibo_mediacenter.net.ResetAdminPasswdRequest;
import com.msx7.josn.ruibo_mediacenter.net.getUserRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.util.List;

/**
 * 文件名: UserManagerDialog
 * 描  述: 管理员页面-充值
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class InputMoneyDialog extends BaseCustomDialog {
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
    BeanUserInfo info;
    IGetMoney mIGetMoney;

    public InputMoneyDialog(Context context, BeanUserInfo info, IGetMoney getMoney) {
        super(context);
        this.info = info;
        this.mIGetMoney = getMoney;
        getLayoutInflater().inflate(R.layout.layout_dialog_user, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        activity = (BaseActivity) context;
        setTitle("账户充值");
        mLoginRoot.setVisibility(View.VISIBLE);
        mUserRoot.setVisibility(View.GONE);
        mLoginName.setHint("请输入充值金额");
        mLoginName.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputMoney();
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

    double _money;

    void onInputMoney() {
        double money = 0;
        try {
            money = Double.parseDouble(mLoginName.getText().toString());
            if (money < 0) {
                mTips.setText("充值金额不能为0");
                return;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mTips.setText("充值金额有误");
            return;
        } finally {
        }
        activity.showProgess();
        info.totalmoney = money;
        _money = money;
        RuiBoApplication.getApplication().runVolleyRequest(new InputMoneyRequest(info, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean baseBean = new Gson().fromJson(response,BaseBean.class);
                if ("200".equals(baseBean.code)) {
                    if (mIGetMoney != null) mIGetMoney.input(_money);
                    dismiss();
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

    public static interface IGetMoney {
        void input(double money);
    }

}
