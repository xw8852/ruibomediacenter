package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: LoginRequest
 * 描  述: 首页-重设密码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ResetPasswdRequest extends BaseJsonRequest {


    public ResetPasswdRequest(Post post, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_CHANGEUSERPASSWORD(), listener, errorListener);
        addRequestJson(new Gson().toJson(post));
    }


    public  static class Post{

        /**
         * confirmnewspassword : 字符串内容
         * id : 2147483647
         * inmoneypassword : 字符串内容
         * loginid : 2147483647
         * loginname : 字符串内容
         * newpassword : 字符串内容
         */

        @SerializedName("confirmnewspassword")
        public String confirmnewspassword;
        @SerializedName("id")
        public int id;
        @SerializedName("inmoneypassword")
        public String inmoneypassword;
        @SerializedName("loginid")
        public int loginid;
        @SerializedName("loginname")
        public String loginname;
        @SerializedName("newpassword")
        public String newpassword;
        @SerializedName("password")
        public String password;
    }

}
