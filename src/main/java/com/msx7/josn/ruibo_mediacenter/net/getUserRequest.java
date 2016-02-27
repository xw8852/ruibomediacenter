package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述:管理员-会员管理-会员卡号/手机号码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class getUserRequest extends BaseJsonRequest {

    String name;

    public getUserRequest(String name, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_GETUSERLIST, listener, errorListener);
        this.name = name;
        addRequestJson(buildJson());
    }


    String buildJson() {
        return "{\"loginname\":\"" + name + "\"}";
    }
}
