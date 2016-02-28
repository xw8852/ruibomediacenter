package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述:首页-用户登录
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class LoginRequest extends BaseJsonRequest {

    String password;
    String name;

    public LoginRequest( String name,String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_LOGIN, listener, errorListener);
        this.password = password;
        this.name = name;
        addRequestJson(buildJson());
    }


    String buildJson() {
        return "{\"loginname\":\"" + name + "\",\"password\":\"" + password + "\"}";
    }
}
