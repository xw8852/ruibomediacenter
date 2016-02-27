package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BeanAdminInfo
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class BeanAdminInfo {

    /**
     * id : 2
     * inmoneypassword : 111111
     * loginid : 0
     * loginname : admin
     * password : 111111
     */

    @SerializedName("id")
    public int id;
    @SerializedName("inmoneypassword")
    public String inmoneypassword;
    @SerializedName("loginid")
    public int loginid;
    @SerializedName("loginname")
    public String loginname;
    @SerializedName("password")
    public String password;
}
