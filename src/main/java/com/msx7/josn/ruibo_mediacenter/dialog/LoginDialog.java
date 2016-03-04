package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: LoginDialog
 * 描  述:首页-用户登录
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class LoginDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.ip)
    TextView ip;
    @InjectView(R.id.loginName)
    TextView mLoginName;
    @InjectView(R.id.loginPasswd)
    TextView mLoginPassWd;
    @InjectView(R.id.login)
    TextView mLoginBtn;

    public LoginDialog(final Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_login, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        setTitle(R.string.user_login);
        ip.setVisibility(View.VISIBLE);
        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IpDialog(context).show();
            }
        });
    }

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
