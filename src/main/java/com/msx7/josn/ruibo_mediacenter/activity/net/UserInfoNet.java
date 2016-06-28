package com.msx7.josn.ruibo_mediacenter.activity.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;

/**
 * 文件名: UserInfoNet
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/22
 */
public class UserInfoNet {


    /**
     * 从网络接口获取用户信息
     *
     * @param loginName
     * @param listener
     * @param errorListener
     */
    public static final void getUserInfoNet(String loginName, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = UrlStatic.URL_GETUSERINFO();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginname", loginName.trim());
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST,url, listener, errorListener);
        request.addRequestJson(jsonObject.toString());
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    public static final class UserInfo {

        /**
         * closed : 0
         * consumemoney : 0
         * enddate : null
         * entity : {"DownloadAllMusicPrice":106,"DownloadMusicAmount":200,"DownloadMusicRemainDiskSpace":22,"DownloadMusicSize":1024,"DownloadMusicTotalDiskSpace":99,"DownloadOneMusicPrice":1,"PrintPrice":1,"StoreMusicRemainDiskSpace":22,"StoreMusicTotalDiskSpace":99}
         * id : 1
         * loginid : 0
         * loginname : 123
         * password : 111111
         * remainmoney : 1000000
         * startdate : null
         * totalmoney : 1000000
         * type : 0
         * userstatus : null
         * usertype : null
         */

        @SerializedName("closed")
        public int closed;
        @SerializedName("consumemoney")
        public double consumemoney;
        @SerializedName("enddate")
        public Object enddate;
        /**
         * DownloadAllMusicPrice : 106
         * DownloadMusicAmount : 200
         * DownloadMusicRemainDiskSpace : 22
         * DownloadMusicSize : 1024
         * DownloadMusicTotalDiskSpace : 99
         * DownloadOneMusicPrice : 1
         * PrintPrice : 1
         * StoreMusicRemainDiskSpace : 22
         * StoreMusicTotalDiskSpace : 99
         */

        @SerializedName("entity")
        public EntityEntity entity;
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
        @SerializedName("startdate")
        public Object startdate;
        @SerializedName("totalmoney")
        public double totalmoney;
        @SerializedName("type")
        public int type;
        @SerializedName("userstatus")
        public Object userstatus;
        @SerializedName("usertype")
        public Object usertype;

        public static class EntityEntity {
            /**
             * 单次下载歌曲封顶价格
             */
            @SerializedName("DownloadAllMusicPrice")
            public double DownloadAllMusicPrice;
            /**
             * 单次下载歌曲最大总数量
             */
            @SerializedName("DownloadMusicAmount")
            public long DownloadMusicAmount;
            /**
             * 下载歌曲剩余磁盘空间大小
             */
            @SerializedName("DownloadMusicRemainDiskSpace")
            public double DownloadMusicRemainDiskSpace;

            /**
             * 单次下载歌曲最大容量（单位：M）
             */
            @SerializedName("DownloadMusicSize")
            public double DownloadMusicSize;
            /**
             * 下载歌曲总磁盘空间大小
             */
            @SerializedName("DownloadMusicTotalDiskSpace")
            public double DownloadMusicTotalDiskSpace;
            /**
             * 单首下载歌曲价格
             */
            @SerializedName("DownloadOneMusicPrice")
            public double DownloadOneMusicPrice;
            /**
             * 打印价格
             */
            @SerializedName("PrintPrice")
            public double PrintPrice;
            /**
             * 存放歌曲剩余磁盘空间大小
             */
            @SerializedName("StoreMusicRemainDiskSpace")
            public double StoreMusicRemainDiskSpace;
            /**
             * 存放歌曲总磁盘空间大小
             */
            @SerializedName("StoreMusicTotalDiskSpace")
            public double StoreMusicTotalDiskSpace;
        }
    }
}
