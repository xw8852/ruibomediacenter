package com.msx7.josn.ruibo_mediacenter.common;

import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: UrlStatic
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/27
 */
public class UrlStatic {
    public static String BASE_UR = "http://xw8852.xicp.net";

    public static String URL_LOGIN() {
        return getServerIp() + "/passportapi/passport.userlogin";
    }

    public static String URL_USERREGISTER() {
        return getServerIp() + "/passportapi/passport.userregister";
    }

    public static String URL_GETUSERINFO() {
        return getServerIp() + "/userapi/user.getuserinfo";
    }

    public static String URL_DOWNLOADMUSIC() {
        return getServerIp() + "/userapi/user.downloadmusic";
    }

    public static String URL_CHANGEUSERPASSWORD() {
        return getServerIp() + "/passportapi/passport.changeuserpassword";
    }

    public static String URL_CHANGEADMINPASSWORD() {
        return getServerIp() + "/passportapi/passport.changeadminpassword";
    }

    public static String URL_ADMINLOGIN() {
        return getServerIp() + "/passportapi/passport.adminlogin";
    }

    public static String URL_UPLOADMUSIC() {
        return getServerIp() + "/adminapi/admin.uploadmusic";
    }

    public static String URL_GETUSERLIST() {
        return getServerIp() + "/adminapi/admin.getuserlist";
    }

    public static String URL_USERINMONEY() {
        return getServerIp() + "/adminapi/admin.userinmoney";
    }

    public static String URL_CLOSEUSER() {
        return getServerIp() + "/adminapi/admin.closeuser";
    }

    public static String URL_GETMUSICLIST() {
        return getServerIp() + "/adminapi/admin.getmusiclist";
    }

    public static String URL_GETUSERACCOUNTDETAIL() {
        return getServerIp() + "/userapi/admin.getuseraccountdetail";
    }

    /**
     * 导出会员列表
     */
    public static String URL_EXPORTUSERTOEXCEL() {
        return getServerIp() + "/adminapi/admin.exportusertoexcel";
    }

    /**
     * 导出充值明细
     */
    public static String URL_EXPORTUSERINMONEYTOEXCEL() {
        return getServerIp() + "/adminapi/admin.exportuserinmoneytoexcel";
    }

    /**
     * 导出对账单
     */
    public static String URL_EXPORTMONEYDETAILTOEXCEL() {
        return getServerIp() + "/adminapi/admin.exportmoneydetailtoexcel";
    }

    /**
     * 获取对账单
     */
    public static String URL_GETMONEYDETAIL() {
        return getServerIp() + "/adminapi/admin.getmoneydetail";
    }

    private static final String getServerIp() {
        String ip = SharedPreferencesUtil.getServerIp();
        if (ip.matches("http://((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))")) return ip + ":88";
        else return ip;
    }

}
