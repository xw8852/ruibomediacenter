package com.msx7.josn.ruibo_mediacenter.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 文件名: HomeLinearLayout
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/28
 */
public class HomeLinearLayout extends LinearLayout {
    public HomeLinearLayout(Context context) {
        super(context);
    }

    public HomeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (Math.abs(getMeasuredHeight() - getMeasuredWidth() * 2) < 5) return;
        int height = getMeasuredHeight();
        setMeasuredDimension(height / 2, height);
        setLayoutParams(new LinearLayout.LayoutParams(height / 2, height));
    }
}
