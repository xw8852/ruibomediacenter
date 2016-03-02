package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件名: BeanUserRecord
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/3/1
 */
public class BeanUserRecord {

    /**
     * createtime : /Date(1456575906000+0800)/
     * money : 1121
     * remainmoney : 4706
     * type : 充值
     */

    @SerializedName("createtime")
    public String createtime;
    @SerializedName("money")
    public int money;
    @SerializedName("remainmoney")
    public int remainmoney;
    @SerializedName("type")
    public String type;

    SimpleDateFormat format;

    public String getFormatTime() {
        if (format == null) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        //1456575906000+0800)/"
        createtime = createtime.replace("/Date(", "");
        if (createtime.contains("+")) {
            createtime = createtime.substring(0, createtime.indexOf("+"));
        }
        return  format.format(new Date(Long.parseLong(createtime)));
    }
}
