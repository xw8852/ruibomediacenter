package com.msx7.josn.ruibo_mediacenter.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;

public class VolleyErrorUtils {
    public static void showError(VolleyError error) {
        if (error.getMessage() != null) {
            L.d(error.getMessage());
        }
        error.printStackTrace();
        if (error instanceof NetworkError) {
            ToastUtil.show(R.string.net_error);
        } else if (error instanceof NoConnectionError) {
            ToastUtil.show(R.string.net_error);
        } else if (error instanceof ServerError) {
            ToastUtil.show(R.string.error);
        } else if (error instanceof TimeoutError) {
            ToastUtil.show(R.string.error);
        } else if (error instanceof ParseError) {
            ToastUtil.show(R.string.error);
        } else if (error instanceof AuthFailureError) {
            ToastUtil.show(R.string.error);
        } else {
            ToastUtil.show(R.string.error);
        }
    }

    public static String getError(VolleyError error) {
        String e = "";
        if (error instanceof NetworkError) {
            e = RuiBoApplication.getApplication().getString(R.string.net_error);
        } else if (error instanceof NoConnectionError) {
            e = RuiBoApplication.getApplication().getString(R.string.net_error);
        } else if (error instanceof ServerError) {
            e = RuiBoApplication.getApplication().getString(R.string.error);
        } else if (error instanceof TimeoutError) {
            e = RuiBoApplication.getApplication().getString(R.string.error);
        } else if (error instanceof ParseError) {
            e = RuiBoApplication.getApplication().getString(R.string.error);
        } else if (error instanceof AuthFailureError) {
            e = RuiBoApplication.getApplication().getString(R.string.error);
        } else {
            e = RuiBoApplication.getApplication().getString(R.string.error);
        }
        return e;
    }
}
