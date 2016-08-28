package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

/**
 * 文件名: LoginRequest
 * 描  述: 管理员页面-歌曲库
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class CheckURequest extends BaseJsonRequest {


    public CheckURequest( Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_CHECKUDISK(), listener, errorListener);
    }


}
