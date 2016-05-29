package com.msx7.josn.ruibo_mediacenter.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;

/**
 * 文件名: NumberTextWatcher
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/18
 */
public class NumberTextWatcher implements TextWatcher {
    TextView textView;

    public NumberTextWatcher(TextView textView) {
        this.textView = textView;
        textView.setBackgroundResource(R.drawable.bg_text);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        if (s.toString().length() == 0 || s.toString().matches("[0-9]{1,}")) return;
        int _count = s.length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < _count; i++) {
            if (ok(CHARACTERS, s.charAt(i))) {
                buffer.append(s.charAt(i));
            }
        }
        textView.setText(buffer.toString());
    }

    protected static boolean ok(char[] accept, char c) {
        for (int i = accept.length - 1; i >= 0; i--) {
            if (accept[i] == c) {
                return true;
            }
        }

        return false;
    }

    public static final char[] CHARACTERS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

}
