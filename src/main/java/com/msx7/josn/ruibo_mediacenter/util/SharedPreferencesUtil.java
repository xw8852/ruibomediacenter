package com.msx7.josn.ruibo_mediacenter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BeanAdminInfo;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public static final int getFailCount() {
        return sharedPreferences.getInt("getFailCount", 0);
    }

    public static final void setFailCount(int count) {
        sharedPreferences.edit().putInt("getFailCount", count).commit();
    }

    public static final long getFailTime() {
        return sharedPreferences.getLong("getFailTIME", 0);
    }

    public static final void setFailTIME(long count) {
        sharedPreferences.edit().putLong("getFailTIME", count).commit();
    }


    public static final void addToCollection(BeanMusic music) {
        addToCollection(Arrays.asList(music));
    }

    public static final void addToCollection(List<BeanMusic> music) {
        List<BeanMusic> musics = getCollection();
        musics.addAll(music);
        saveCollection(musics);
    }


    public static final void saveCollection(List<BeanMusic> musics) {
        sharedPreferences.edit().putString("Collection", new Gson().toJson(musics)).commit();
    }

    public static final List<BeanMusic> getCollection() {
        List<BeanMusic> arr = new Gson().fromJson(sharedPreferences.getString("Collection", ""), new TypeToken<List<BeanMusic>>() {
        }.getType());
        if (arr == null) {
            arr = new ArrayList<BeanMusic>();
        }
        return arr;
    }

    public static final void clearCollection() {
        sharedPreferences.edit().putString("Collection", "").commit();
    }


    public static final void saveServerIp(String saveServerIp) {
        sharedPreferences.edit().putString("saveServerIp", saveServerIp).commit();
    }

    public static final String getServerIp() {
        if (TextUtils.isEmpty(sharedPreferences.getString("saveServerIp", "")))
            return UrlStatic.BASE_UR;
        return sharedPreferences.getString("saveServerIp", "");
    }

    /**
     * 列数
     *
     * @param row1
     */
    public static final void saveRow1(int row1) {
        sharedPreferences.edit().putInt("row1", row1).commit();
    }

    /**
     * 列数
     *
     * @return
     */
    public static final int getRow1() {
        return sharedPreferences.getInt("row1", 3);
    }

    /**
     * 行数
     *
     * @param row2
     */
    public static final void saveRow2(int row2) {
        sharedPreferences.edit().putInt("row2", row2).commit();
    }

    /**
     * 行数
     *
     * @return
     */
    public static final int getRow2() {
        return sharedPreferences.getInt("row2", 9);
    }

}
