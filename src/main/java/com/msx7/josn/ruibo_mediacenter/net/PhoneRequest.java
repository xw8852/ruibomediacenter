package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述:管理员-会员管理-会员卡号/手机号码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class PhoneRequest extends BaseJsonRequest {

    String name;
    long id;

    public PhoneRequest(String name, long id, Response.Listener<String>listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_UPDATEUSERPHONE(), listener, errorListener);
        this.name = name;
        this.id = id;
        addRequestJson(buildJson());
    }


    String buildJson() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", name);
        jsonObject.addProperty("id", id);
        return jsonObject.toString();
    }
}
