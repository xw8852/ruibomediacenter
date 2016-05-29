package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class BaseCustomDialog extends Dialog {

    BaseActivity activity;

    public BaseCustomDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.dialog_layout);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTitleView = (TextView) findViewById(R.id.Title);
    }

    TextView mTitleView;

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setTitle(int title) {
        mTitleView.setText(title);
    }

    public void showProgess() {
        activity.showProgess();
    }


    public void dismisProgess() {
        activity.dismisProgess();
    }

}
