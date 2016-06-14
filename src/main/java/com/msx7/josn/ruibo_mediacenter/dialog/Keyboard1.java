package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import org.w3c.dom.Text;

/**
 * 文件名: Keyboard1
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/14
 */
public class Keyboard1 {

    public static long DIVIDER_TIME = 400;
    State state = State.Normal;
    PopupWindow popupWindow;

    View item;
    View root;
    TextView editText;
    CharSequence hint;

    public Keyboard1(View item, final TextView editText) {
        this.item = item;
        this.editText = editText;
        root = View.inflate(getContext(), R.layout.layout_keybroad_1, null);
        popupWindow = new PopupWindow(root);
        root.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        popupWindow.setWidth(root.getMeasuredWidth());
        popupWindow.setHeight(root.getMeasuredHeight());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);  //设置点击屏幕其它地方弹出框消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        root.findViewById(R.id.num1).setOnClickListener(numClickListener);
        root.findViewById(R.id.num2).setOnClickListener(numClickListener);
        root.findViewById(R.id.num3).setOnClickListener(numClickListener);
        root.findViewById(R.id.num4).setOnClickListener(numClickListener);
        root.findViewById(R.id.num5).setOnClickListener(numClickListener);
        root.findViewById(R.id.num6).setOnClickListener(numClickListener);
        root.findViewById(R.id.num7).setOnClickListener(numClickListener);
        root.findViewById(R.id.num8).setOnClickListener(numClickListener);
        root.findViewById(R.id.num9).setOnClickListener(numClickListener);
        root.findViewById(R.id.num10).setOnClickListener(numClickListener);
        root.findViewById(R.id.num11).setOnClickListener(numClickListener);
        root.findViewById(R.id.num12).setOnClickListener(numClickListener);
        if (editText.getTag() == null || !"mSearchContent".equals(editText.getTag().toString())) {
            editText.setBackgroundResource(R.drawable.bg_text);
        }
        editText.setSelected(true);
        hint = editText.getHint();
        editText.setHint("");
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editText.setSelected(false);
                editText.setHint(hint);
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                ToastUtil.show("KeyCode:" + keyCode);
                return false;
            }
        });
    }

    public State getState() {
        return state;
    }

    public Keyboard1 setState(State state) {
        this.state = state;
        if (state != State.Normal) {
            if (state == State.Number) {
                ((TextView) root.findViewById(R.id.num11)).setText("*");
                root.findViewById(R.id.num11).setEnabled(false);
            } else {
                ((TextView) root.findViewById(R.id.num11)).setText(".");
                root.findViewById(R.id.num11).setEnabled(true);
            }
            root.findViewById(R.id.num12).setEnabled(false);
        } else {
            ((TextView) root.findViewById(R.id.num11)).setText("*");
            root.findViewById(R.id.num11).setEnabled(true);
            root.findViewById(R.id.num12).setEnabled(true);
        }
        return this;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    long lastTime;
    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (System.currentTimeMillis() - lastTime > DIVIDER_TIME) {
                lastTime = System.currentTimeMillis();
                clickNumber(v);
            }
        }
    };

    StringBuffer buffer = new StringBuffer();

    public String getContent() {
        return buffer.toString();
    }

    void clickNumber(View v) {
        if (state == State.password) {
            buffer.append(((TextView) v).getText().toString());
            String text = editText.getText().toString();
            editText.setText(text + "*");
            return;
        }
        String text = editText.getText().toString();
        editText.setText(text + ((TextView) v).getText().toString());
    }

    public final Context getContext() {
        return item.getContext();
    }

    public static enum State {
        Normal, Number, SignNumber, password
    }
}
