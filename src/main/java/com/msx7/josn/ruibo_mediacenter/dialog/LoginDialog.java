package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
        mLoginName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        mLoginPassWd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//        mLoginPassWd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mLoginPassWd.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//        ip.setVisibility(View.VISIBLE);
        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IpDialog(context).show();
            }
        });


        mLoginName.setInputType(InputType.TYPE_NULL);
        mLoginName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginName.setText("");
                int right = findViewById(R.id.root).getRight();
                new Keyboard1(v, mLoginName).getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });

        mLoginPassWd.setInputType(InputType.TYPE_NULL);
        mLoginPassWd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPassWd.setText("");
                int right = findViewById(R.id.root).getRight();
                keyboard1 = new Keyboard1(v, mLoginPassWd).setState(Keyboard1.State.password);
                keyboard1.getPopupWindow().showAtLocation(v, Gravity.CENTER, right, 0);
            }
        });
    }

    Keyboard1 keyboard1;

    public String getPasswd() {
        return keyboard1.getContent().substring(0, Math.min(6, keyboard1.getContent().length()));
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
