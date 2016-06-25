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
    public long id;
    @SerializedName("loginid")
    public int loginid;
    @SerializedName("loginname")
    public String loginname;
    @SerializedName("password")
    public String password;
    @SerializedName("phone")
    public String phone;
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
    /**
     * closed : 0
     * entity : {"DownloadAllMusicPrice":10,"DownloadMusicAmount":200,"DownloadMusicRemainDiskSpace":20795,"DownloadMusicSize":1024,"DownloadMusicTotalDiskSpace":102400,"DownloadOneMusicPrice":1,"PrintPrice":1,"StoreMusicRemainDiskSpace":20795,"StoreMusicTotalDiskSpace":102400}
     * userstatus : null
     * usertype : null
     */

    @SerializedName("closed")
    public int closed;
    /**
     * DownloadAllMusicPrice : 10
     * DownloadMusicAmount : 200
     * DownloadMusicRemainDiskSpace : 20795
     * DownloadMusicSize : 1024
     * DownloadMusicTotalDiskSpace : 102400
     * DownloadOneMusicPrice : 1
     * PrintPrice : 1
     * StoreMusicRemainDiskSpace : 20795
     * StoreMusicTotalDiskSpace : 102400
     */

    @SerializedName("entity")
    public EntityEntity entity;
    @SerializedName("userstatus")
    public Object userstatus;
    @SerializedName("usertype")
    public Object usertype;

    public static class EntityEntity {
        @SerializedName("DownloadAllMusicPrice")
        public double DownloadAllMusicPrice;
        @SerializedName("DownloadMusicAmount")
        public long DownloadMusicAmount;
        /**
         * 下载歌曲剩余磁盘空间大小
         */
        @SerializedName("DownloadMusicRemainDiskSpace")
        public double DownloadMusicRemainDiskSpace;
        /**
         * DownloadMusicSize
         */
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
