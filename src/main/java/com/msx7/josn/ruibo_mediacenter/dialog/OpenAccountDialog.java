package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: SyncSongDialog
 * 描  述:管理员页面-开通账号item
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class OpenAccountDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginName)
    TextView mLoginName;
    @InjectView(R.id.loginPasswd)
    TextView mLoginPassWd;
    @InjectView(R.id.login)
    TextView mLoginBtn;

    BaseActivity activity;

    public OpenAccountDialog(Context context) {
        super(context);
        activity = (BaseActivity) context;
        getLayoutInflater().inflate(R.layout.layout_dialog_open_account, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        setTitle("开通账号");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    void onLogin() {
        String loginName = mLoginName.getText().toString();
        String money = mLoginPassWd.getText().toString().trim();
        if (TextUtils.isEmpty(loginName)) {
            mTips.setText("请输入卡号");
            return;
        }
        double _money = 0.0;
        try {
            _money = Double.parseDouble(money);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mTips.setText("你输入的金额有误");
            return;
        } finally {
        }
        if (_money < 0) {
            mTips.setText("你输入的金额有误");
            return;
        }
        BeanUserInfo userInfo = new BeanUserInfo();
        userInfo.loginname = loginName;
        userInfo.password = "111111";
        userInfo.totalmoney = _money;
        userInfo.remainmoney = _money;
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_USERREGISTER(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean baseBean=new Gson().fromJson(response,BaseBean.class);
                if("200".equals(baseBean.code)){
                    dismiss();
                }else{
                    mTips.setText(baseBean.msg);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTips.setText(VolleyErrorUtils.getError(error));
            }
        });
        request.addRequestJson(new Gson().toJson(userInfo));
        RuiBoApplication.getApplication().runVolleyRequest(request);
        activity.showProgess();
    }

}
