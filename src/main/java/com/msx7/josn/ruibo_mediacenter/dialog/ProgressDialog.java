package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Dialog;
import com.msx7.josn.ruibo_mediacenter.R;
import android.content.Context;

/**
 * 文件名: BaseCustomDialog
 * 描  述: 转圈圈 进度条
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context, R.style.Translate_Dialog);
        setContentView(R.layout.layout_dialog_progress);

    }

}
