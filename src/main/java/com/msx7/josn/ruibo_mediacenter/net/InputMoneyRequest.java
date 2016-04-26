package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述:管理员页面-会员管理-充值
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class InputMoneyRequest extends BaseJsonRequest {


    public InputMoneyRequest(String json, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_USERREGISTER(), listener, errorListener);
        addRequestJson(json);
    }

}
