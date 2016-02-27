package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述: 管理员登录
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class AdminLoginRequest extends BaseJsonRequest {

    String password;

    public AdminLoginRequest(String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_ADMINLOGIN, listener, errorListener);
        this.password = password;
        addRequestJson(buildJson());
    }


    String buildJson() {
        return "{\"loginname\":\"admin\",\"password\":\"" + password + "\"}";
    }
}
