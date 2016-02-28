package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BeanMusic
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/28
 */
public class BeanMusic {

    /**
     * id : 1
     * loginid : 0
     * money : 2
     * name : 陈学冬-不再见.mp3
     * path : http://json:88/ftp/陈学冬-不再见.mp3
     * size : 9793699
     * typeid : 1
     * typename : 戏曲
     */

    @SerializedName("id")
    public int id;
    @SerializedName("loginid")
    public int loginid;
    @SerializedName("money")
    public double money;
    @SerializedName("name")
    public String name;
    @SerializedName("path")
    public String path;
    @SerializedName("size")
    public long size;
    @SerializedName("typeid")
    public int typeid;
    @SerializedName("typename")
    public String typename;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof BeanMusic)) return false;
        BeanMusic music = (BeanMusic) o;
        if (music.id <= 0 || id <= 0) return false;
        return music.id == id;
    }
}
