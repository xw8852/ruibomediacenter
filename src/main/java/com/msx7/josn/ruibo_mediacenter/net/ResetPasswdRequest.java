package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: LoginRequest
 * 描  述: 首页-重设密码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ResetPasswdRequest extends BaseJsonRequest {

    String password;

    public ResetPasswdRequest(String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_CHANGEUSERPASSWORD(), listener, errorListener);
        this.password = password;
        addRequestJson(buildJson());
    }


    String buildJson() {
        return "{\"loginname\":\"" + SharedPreferencesUtil.getUserInfo().loginname + "\",\"password\":\"" + SharedPreferencesUtil.getUserInfo().password + "\",\"newpassword\":\"" + password + "\",\"confirmnewspassword\":\"" + password + "\"}";
    }
}
