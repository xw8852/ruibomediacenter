package com.msx7.josn.ruibo_mediacenter.common;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: SyncUserInfo
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/29
 */
public class SyncUserInfo {
    public static final void SyncUserInfo() {

        BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
        if (userInfo == null) return;
        String url = UrlStatic.URL_GETUSERINFO();
        String postBody = "{\"loginname\":\"" + userInfo.loginname + "\"}";
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) return;
                BaseBean<BeanUserInfo> baseBean = new Gson().fromJson(response, new TypeToken< BaseBean<BeanUserInfo>>() {
                }.getType());
                if (baseBean == null) return;
                if ("200".equals(baseBean.code)) {
                    if (baseBean.data == null) return;
                    SharedPreferencesUtil.saveUserInfo(baseBean.data);
                }
            }
        }, null);
        request.addRequestJson(postBody);
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }
}
