package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
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
import com.msx7.josn.ruibo_mediacenter.net.SyncSongRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: SyncSongDialog
 * 描  述:管理员界面-管理员Item
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class AdminDialog extends BaseCustomDialog {

    /**
     * 登录密码
     */
    @InjectView(R.id.btn1)
    View btn1;
    /**
     * 曲目定价
     */
    @InjectView(R.id.btn2)
    View btn2;
    /**
     * 对账单
     */
    @InjectView(R.id.login)
    View login;
    BaseActivity activity;

    public AdminDialog(Context context) {
        super(context);
        activity = (BaseActivity) context;
        getLayoutInflater().inflate(R.layout.layout_dialog_admin, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        setTitle("管理员");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetAdminPasswdDialog(activity).show();
                dismiss();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShopInfoDialog(activity).show();
                dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetPriceDialog2(activity).show();
                dismiss();
            }
        });
    }


}
