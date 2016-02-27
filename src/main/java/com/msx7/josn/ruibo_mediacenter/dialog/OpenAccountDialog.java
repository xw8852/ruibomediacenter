package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
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

    public OpenAccountDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_open_account, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        mTips.setText("");
        setTitle("开通账号");
    }


}
