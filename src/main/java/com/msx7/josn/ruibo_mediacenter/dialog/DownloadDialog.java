package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;

/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class DownloadDialog extends Dialog {

    public DownloadDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        setContentView(R.layout.dialog_download_layout);
    }


}
