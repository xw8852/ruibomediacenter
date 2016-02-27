package com.msx7.josn.ruibo_mediacenter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BaseBean
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/27
 */
public class BaseBean<T> {

    /**
     * code : 200
     * msg : 登录成功
     * data : null
     */

    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public T data;
}
