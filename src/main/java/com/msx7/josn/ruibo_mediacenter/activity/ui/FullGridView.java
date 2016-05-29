package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 文件名: FullGridView
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/22
 */
public class FullGridView extends GridView {
    public FullGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
