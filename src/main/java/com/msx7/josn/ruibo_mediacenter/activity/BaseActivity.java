package com.msx7.josn.ruibo_mediacenter.activity;

import android.app.Activity;

import com.baidu.mobstat.StatService;
import com.msx7.josn.ruibo_mediacenter.dialog.ProgressDialog;

/**
 * 文件名: BaseActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class BaseActivity extends Activity{

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
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
