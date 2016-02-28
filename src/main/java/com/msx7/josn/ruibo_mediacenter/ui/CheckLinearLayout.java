package com.msx7.josn.ruibo_mediacenter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * 文件名: CheckLinearLayout
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/28
 */
public class CheckLinearLayout extends LinearLayout implements Checkable {
    public CheckLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckLinearLayout(Context context) {
        super(context);
    }

    boolean mChecked;

    @Override
    public void setChecked(boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }
}
