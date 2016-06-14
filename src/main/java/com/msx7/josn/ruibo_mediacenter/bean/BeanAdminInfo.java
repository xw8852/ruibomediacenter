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


    /**
     * DownloadAllMusicPrice : 10
     * DownloadMusicAmount : 200
     * DownloadMusicRemainDiskSpace : 20429
     * DownloadMusicSize : 1024
     * DownloadMusicTotalDiskSpace : 102400
     * DownloadOneMusicPrice : 1
     * PrintPrice : 1
     * StoreMusicRemainDiskSpace : 20429
     * StoreMusicTotalDiskSpace : 102400
     */

    @SerializedName("entity")
    public EntityEntity entity;

    public static class EntityEntity {
        @SerializedName("DownloadAllMusicPrice")
        public double DownloadAllMusicPrice;
        @SerializedName("DownloadMusicAmount")
        public double DownloadMusicAmount;
        @SerializedName("DownloadMusicRemainDiskSpace")
        public double DownloadMusicRemainDiskSpace;
        @SerializedName("DownloadMusicSize")
        public double DownloadMusicSize;
        @SerializedName("DownloadMusicTotalDiskSpace")
        public double DownloadMusicTotalDiskSpace;
        @SerializedName("DownloadOneMusicPrice")
        public double DownloadOneMusicPrice;
        @SerializedName("PrintPrice")
        public double PrintPrice;
        @SerializedName("StoreMusicRemainDiskSpace")
        public double StoreMusicRemainDiskSpace;
        @SerializedName("StoreMusicTotalDiskSpace")
        public double StoreMusicTotalDiskSpace;
    }
}
