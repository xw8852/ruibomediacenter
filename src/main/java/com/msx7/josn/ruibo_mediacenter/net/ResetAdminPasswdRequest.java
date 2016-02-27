package com.msx7.josn.ruibo_mediacenter.net;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: LoginRequest
 * 描  述:管理员页面-管理员-设置登录密码
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class ResetAdminPasswdRequest extends BaseJsonRequest {

    String password;
    String newpassword;

    public ResetAdminPasswdRequest(String password, String newpassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlStatic.URL_CHANGEADMINPASSWORD, listener, errorListener);
        this.password = password;
        this.newpassword = newpassword;
        ResetAdmin admin = new ResetAdmin();
        BeanAdminInfo info = SharedPreferencesUtil.getAdminUserInfo();
        admin.id = info.id;
        admin.loginid = info.id;
        admin.loginname = info.loginname;
        admin.password = password;
        admin.newpassword = newpassword;
        admin.confirmnewspassword = newpassword;
        addRequestJson(buildJson(admin));
    }


    String buildJson(ResetAdmin admin) {
        return new Gson().toJson(admin);
    }

    class ResetAdmin {

        /**
         * confirmnewspassword : 111111
         * id : 1
         * loginid : 1
         * loginname : admin
         * newpassword : 111111
         * password : 111111
         */

        @SerializedName("confirmnewspassword")
        public String confirmnewspassword;
        @SerializedName("id")
        public int id;
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
