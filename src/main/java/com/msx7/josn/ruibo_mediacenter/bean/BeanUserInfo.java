package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BeanLogin
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/27
 */
public class BeanUserInfo {

    /**
     * consumemoney : 0
     * id : 1
     * loginid : 0
     * loginname : 111111
     * password : 111111
     * remainmoney : 0
     * totalmoney : 0
     */
    /**
     * 消费金额
     */
    @SerializedName("consumemoney")
    public double consumemoney;
    @SerializedName("id")
    public int id;
    @SerializedName("loginid")
    public int loginid;
    @SerializedName("loginname")
    public String loginname;
    @SerializedName("password")
    public String password;
    /**
     * 剩余金额
     */
    @SerializedName("remainmoney")
    public double remainmoney;
    /**
     * 充值总金额
     */
    @SerializedName("totalmoney")
    public double totalmoney;
    /**
     * enddate : null
     * startdate : null
     * type : 0
     * typename : null
     */

    @SerializedName("enddate")
    public String enddate;
    @SerializedName("startdate")
    public String startdate;
    @SerializedName("type")
    public int type;
    @SerializedName("typename")
    public String typename;
}
