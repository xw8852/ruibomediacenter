package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.net.URL;

/**
 * 文件名: LoginDialog
 * 描  述:首页-用户登录
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class IpDialog extends BaseCustomDialog {
    @InjectView(R.id.tip)
    TextView mTips;
    @InjectView(R.id.loginPasswd)
    TextView mLoginPassWd;

    @InjectView(R.id.login)
    TextView mLoginBtn;
    @InjectView(R.id.row1)
    TextView row1;
    @InjectView(R.id.row2)
    TextView row2;

    public IpDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_ip, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        setTitle("服务器地址");
        mLoginPassWd.setText(SharedPreferencesUtil.getServerIp());
        //列数
        row1.setText(SharedPreferencesUtil.getRow1()+"");
        //行数
        row2.setText(SharedPreferencesUtil.getRow2()+"");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mLoginPassWd.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    mTips.setText("请输入服务器ip地址或者域名");
                    return;
                }

                if (!str.startsWith("http://")) {
                    str = "http://" + str;
                }
                SharedPreferencesUtil.saveServerIp(str);

                if (!TextUtils.isEmpty(row1.getText().toString()))
                    SharedPreferencesUtil.saveRow1(Integer.parseInt(row1.getText().toString()));
                if (!TextUtils.isEmpty(row2.getText().toString()))
                    SharedPreferencesUtil.saveRow2(Integer.parseInt(row2.getText().toString()));
                dismiss();
            }
        });
    }


}
