package com.msx7.josn.ruibo_mediacenter.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.AdminDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.BaoYueDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.ChongZhiDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.ClosePcDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.Keyboard1;
import com.msx7.josn.ruibo_mediacenter.dialog.OpenAccountDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.PrintDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.SyncSongDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.UserManagerDialog;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.SyncSongRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;

import java.text.DecimalFormat;

/**
 * 文件名: LoginManager
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/16
 */
public class AdminActivity extends BaseActivity {

    TextView mTipDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferencesUtil.clearUserInfo();
        mTipDir = (TextView) findViewById(R.id.tipDir);
        BeanAdminInfo adminInfo = SharedPreferencesUtil.getAdminUserInfo();
        DecimalFormat a = new DecimalFormat("0.##");
        mTipDir.setText("磁盘空间总共" + a.format(adminInfo.entity.StoreMusicTotalDiskSpace / 1024.0) + "G，可用空间" +
                a.format(adminInfo.entity.StoreMusicRemainDiskSpace / 1024.0) + "G");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Keyboard1.DIVIDER_TIME = 0;
    }

    /**
     * 开通账号
     *
     * @param v
     */
    public void onOpen(View v) {
        new OpenAccountDialog(v.getContext()).show();
    }

    /**
     * 管理员
     *
     * @param v
     */
    public void onAdmin(View v) {
        new AdminDialog(v.getContext()).show();
    }

    /**
     * 会员管理
     *
     * @param v
     */
    public void onUser(View v) {
        new UserManagerDialog(v.getContext()).show();
    }

    public void onPrint(View v) {
        new PrintDialog(v.getContext()).show();
    }


    /**
     * 歌曲管理
     *
     * @param v
     */
    public void onSong(View v) {
//        showProgess();
//        RuiBoApplication.getApplication().runVolleyRequest(new SyncSongRequest(2, responseListener, errorListener));
        new SyncSongDialog(v.getContext()).show();
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            dismisProgess();
            L.d(response);
            BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
            ToastUtil.show(baseBean.msg);

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dismisProgess();
            ToastUtil.show(VolleyErrorUtils.getError(error));
        }
    };


    public void shutdown(View v) {
        new ClosePcDialog(this).show();
    }


    public void chongzhi(View v) {
        new ChongZhiDialog(v.getContext()).show();
    }


    public void baoyue(View v) {
        new BaoYueDialog(v.getContext()).show();
    }


    public void onback(View v) {
        showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.GET, UrlStatic.URL_BACKUPDATA(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismisProgess();
                        L.d(response);
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        ToastUtil.show(baseBean.msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismisProgess();
                ToastUtil.show(VolleyErrorUtils.getError(error));
            }
        }));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration configuration = new Configuration(getResources().getConfiguration());
        configuration.keyboard = Configuration.KEYBOARD_NOKEYS;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}
