package com.msx7.josn.ruibo_mediacenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;

/**
 * 文件名: CloseAppActivity
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/6/30
 */
public class CloseAppActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
