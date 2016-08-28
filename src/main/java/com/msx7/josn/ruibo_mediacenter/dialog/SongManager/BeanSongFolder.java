package com.msx7.josn.ruibo_mediacenter.dialog.SongManager;

import com.google.gson.annotations.SerializedName;

/**
 * 文件名: BeanSongFolder
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/8/27
 */

public class BeanSongFolder {

    /**
     * id : 1012
     * loginid : 0
     * money : 0
     * name : 音乐
     */

    @SerializedName("id")
    public long id;

    @SerializedName("loginid")
    public long loginid;

    @SerializedName("money")
    public double money;

    @SerializedName("name")
    public String name;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof BeanSongFolder) {
            BeanSongFolder obj = (BeanSongFolder) o;
            return obj.id == id;
        }
        return false;
    }
}
