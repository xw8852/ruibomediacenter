package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BeanMoneyDetailInfo
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class BeanMoneyDetailInfo {

    /**
     * closeusercount : 1
     * inmoney : 7073.9
     * openusercount : 6
     * outmoney : 0
     * yearandmonth : 2016.2
     */
    /**
     * 销户数
     */
    @SerializedName("closeusercount")
    public int closeusercount;
    /**
     * 充值金额
     */
    @SerializedName("inmoney")
    public double inmoney;
    /**
     * 开户数
     */
    @SerializedName("openusercount")
    public int openusercount;
    /**
     * 销户金额
     */
    @SerializedName("outmoney")
    public int outmoney;
    /**
     * 期间
     */
    @SerializedName("yearandmonth")
    public String yearandmonth;
}
