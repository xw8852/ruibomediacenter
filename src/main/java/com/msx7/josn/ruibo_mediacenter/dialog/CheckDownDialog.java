package com.msx7.josn.ruibo_mediacenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.ui.BeanView;
import com.msx7.josn.ruibo_mediacenter.activity.ui.BeanView.CheckPost;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 文件名: BaseCustomDialog
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/23
 */
public class CheckDownDialog extends Dialog {

    BaseActivity activity;

    ProgressBar mbar1;
    TextView tv1;

    ProgressBar mbar2;
    TextView tv2;
    TextView printNum;
    TextView print;
    View add;
    View minus;
    int _printNum = 1;

    public CheckDownDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_check_down_dialog);
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
            }
        });
        mbar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mbar2 = (ProgressBar) findViewById(R.id.progressBar2);
        tv1 = (TextView) findViewById(R.id.text);
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        printNum = (TextView) findViewById(R.id.printNum);
        tv2 = (TextView) findViewById(R.id.text2);
        print = (TextView) findViewById(R.id.printPrice);
        findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
        findViewById(R.id.noPrint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noPrint();
            }
        });
        minus.setOnClickListener(minusClickListener);
        add.setOnClickListener(addClickListener);
    }

    View.OnClickListener minusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _printNum = Math.max(1, _printNum - 1);
            if (_printNum == 1) minus.setEnabled(false);
            else minus.setEnabled(true);
            printNum.setText(String.valueOf(_printNum));
        }
    };
    View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            minus.setEnabled(true);
            _printNum++;
            printNum.setText(String.valueOf(_printNum));
        }
    };

    void print() {
        post.needprint = 1;
        post.printnumber = _printNum;
//        post.money += _printNum * printPrice;
        new DownProgressDialog(activity).showDown(post);
        dismiss();
    }

    void noPrint() {
        post.needprint = 0;
        new DownProgressDialog(activity).showDown(post);
        dismiss();
    }

    CheckPost post;
    double printPrice;

    public void setPostData(CheckPost postData) {
        post = postData;
    }

    public void show(double need, int needB, double remain, int remianb, double printPrice) {
        DecimalFormat a = new DecimalFormat("0.##");
        this.printPrice = printPrice;
        tv1.setText("下载所需磁卡容量:" + a.format(need) + "M");
        tv2.setText("目前磁卡可用容量:" + a.format(remain) + "M");
        mbar1.setProgress(needB);
        mbar2.setProgress(remianb);
        print.setText("(打印另收费\b:\b" + a.format(printPrice) + "元/张)");

    }
}
