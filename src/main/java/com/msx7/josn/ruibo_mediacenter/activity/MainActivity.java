package com.msx7.josn.ruibo_mediacenter.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.BuildConfig;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.dialog.IpDialog;
import com.msx7.josn.ruibo_mediacenter.net.AdminLoginRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: MainActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/16
 */
@InjectActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @InjectView(R.id.tip)
    TextView mTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);
        findViewById(R.id.backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTips.setText("");
        findViewById(R.id.num1).setOnClickListener(numClickListener);
        findViewById(R.id.num2).setOnClickListener(numClickListener);
        findViewById(R.id.num3).setOnClickListener(numClickListener);
        findViewById(R.id.num4).setOnClickListener(numClickListener);
        findViewById(R.id.num5).setOnClickListener(numClickListener);
        findViewById(R.id.num6).setOnClickListener(numClickListener);
        findViewById(R.id.num7).setOnClickListener(numClickListener);
        findViewById(R.id.num8).setOnClickListener(numClickListener);
        findViewById(R.id.num9).setOnClickListener(numClickListener);
        findViewById(R.id.num10).setOnClickListener(numClickListener);
        findViewById(R.id.num11).setOnClickListener(numClickListener);
        findViewById(R.id.num12).setOnClickListener(numClickListener);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("keep", "keep");
        startActivity(intent);
        super.onBackPressed();
    }

    StringBuffer buffer = new StringBuffer();

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

//        ToastUtil.show("keyCode:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_NUMPAD_0:
                buffer.append("0");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_NUMPAD_1:
                buffer.append("1");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;


            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_NUMPAD_2:
                buffer.append("2");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_NUMPAD_3:
                buffer.append("3");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_NUMPAD_5:
                buffer.append("5");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_NUMPAD_4:
                buffer.append("4");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_NUMPAD_6:
                buffer.append("6");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_NUMPAD_7:
                buffer.append("7");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_NUMPAD_8:
                buffer.append("8");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            case KeyEvent.KEYCODE_9:
            case KeyEvent.KEYCODE_NUMPAD_9:
                buffer.append("9");
                if (buffer.length() == 6) {
                    onLogin(buffer.toString());
                }
                return true;

            default:
                return false;
        }
    }

    View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickNumber(v);
        }
    };

    String passwd = "";

    void clickNumber(View v) {
        passwd = passwd + ((TextView) v).getText().toString();
        if (passwd.equals("*#06#")) {
            passwd = "";
            new IpDialog(this).show();
            return;
        }

        if (BuildConfig.DEBUG) {
            if (passwd.length() == 6)
                onLogin(passwd);
            else if (passwd.length() > 6)
                passwd = "";
            return;
        }
        if (passwd.length() >= "*#06#".length())
            passwd = "";

    }


    void onLogin(String passwd) {
        if (TextUtils.isEmpty(passwd)) {
            mTips.setText("请输入管理员密码");
            return;
        }
        showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new AdminLoginRequest(passwd, responseListener, errorListener));
        buffer = new StringBuffer();
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            dismisProgess();
            L.d(response);
            BaseBean<BeanAdminInfo> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<BeanAdminInfo>>() {
            }.getType());
            if ("200".equals(baseBean.code)) {
                SharedPreferencesUtil.saveAdminUserInfo(baseBean.data);
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
            } else {
                mTips.setText(baseBean.msg);
            }
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dismisProgess();
            mTips.setText(VolleyErrorUtils.getError(error));
        }
    };
}
