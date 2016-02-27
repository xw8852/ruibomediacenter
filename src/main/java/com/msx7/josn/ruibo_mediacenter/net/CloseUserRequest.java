package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述:首页-销户
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class CloseUserRequest extends BaseJsonRequest {

    BeanUserInfo info;

    public CloseUserRequest(BeanUserInfo info, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_CLOSEUSER, listener, errorListener);
        this.info = info;
        addRequestJson(new Gson().toJson(info));
    }

}
