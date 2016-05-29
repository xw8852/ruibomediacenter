package com.msx7.josn.ruibo_mediacenter.activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.msx7.josn.ruibo_mediacenter.dialog.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 文件名: BaseActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class BaseActivity extends FragmentActivity{

    ProgressDialog mProgessDialog;

    public void showProgess() {
        showProgess(false);
    }
    public void showProgess(boolean isCancelable) {
        if (mProgessDialog == null) mProgessDialog = new ProgressDialog(this);
        mProgessDialog.setCancelable(isCancelable);
        mProgessDialog.show();
    }

    public void dismisProgess() {
        if (mProgessDialog != null) mProgessDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
