package com.msx7.josn.ruibo_mediacenter.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;

/**
 * 文件名: ShareUtil
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/27
 */
public class SharedPreferencesUtil {
    static String NAME = "userInfo";
    static SharedPreferences sharedPreferences;

    static {
        sharedPreferences = RuiBoApplication.getApplication().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static final void saveUserInfo(BeanUserInfo userInfo) {
        sharedPreferences.edit().putString("BeanUserInfo", new Gson().toJson(userInfo)).commit();
    }

    public static final BeanUserInfo getUserInfo() {
        return new Gson().fromJson(sharedPreferences.getString("BeanUserInfo", ""), BeanUserInfo.class);
    }

    public static final void clearUserInfo() {
        sharedPreferences.edit().putString("BeanUserInfo", "").commit();
    }

    public static final void saveAdminUserInfo(BeanAdminInfo userInfo) {
        sharedPreferences.edit().putString("BeanAdminInfo", new Gson().toJson(userInfo)).commit();
    }

    public static final BeanAdminInfo getAdminUserInfo() {
        return new Gson().fromJson(sharedPreferences.getString("BeanAdminInfo", ""), BeanAdminInfo.class);
    }

    public static final void clearAdminUserInfo() {
        sharedPreferences.edit().putString("BeanAdminInfo", "").commit();
    }
}
