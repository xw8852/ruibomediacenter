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
    public static String URL_BACKUPDATA() {
        return getServerIp() + "/adminapi/admin.backupdata";
    }
    public static String URL_SETTINGMUSIC() {
        return getServerIp() + "/adminapi/admin.settingmusic";
    }
    public static String URL_CLOSEPC() {
        return getServerIp() + "/adminapi/admin.closepc";
    }

    public static String URL_GETUSERLIST() {
        return getServerIp() + "/adminapi/admin.getuserlist";
    }
    public static String URL_UPDATEUSERPHONE() {
        return getServerIp() + "/userapi/user.updateuserphone";
    }

    public static String URL_USERINMONEY() {
        return getServerIp() + "/adminapi/admin.userinmoney";
    }

    public static String URL_CLOSEUSER() {
        return getServerIp() + "/adminapi/admin.closeuser";
    }

    public static String URL_GETMUSICLIST() {
        return getServerIp() + "/userapi/user.searchmusic";
    }

    /**
     * 收藏歌曲
     *
     * @return
     */
    public static String URL_FAVORITEMUSIC() {
        return getServerIp() + "/userapi/user.favoritemusic";
    }

    public static String URL_DOWNLOADMUSICCHECK() {
        return getServerIp() + "/userapi/user.downloadmusiccheck";
    }
    public static String URL_PRINTREPORT() {
        return getServerIp() + "/adminapi/admin.printreport";
    }

    /**
     * 获取音乐类别
     *
     * @return
     */
    public static String URL_GETMUSICTYPE() {
        return getServerIp() + "/adminapi/admin.getmusictypelist";
    }

    /**
     * 设置音乐类别的价格
     * @return
     */
    public static String URL_RESETMUSICTYPE() {
        return getServerIp() + "/adminapi/admin.resetmusictypemoney";
    }

    /**
     * 获取收藏歌曲
     *
     * @return
     */
    public static String URL_GETFAVORITEMUSIC() {
        return getServerIp() + "/userapi/user.getfavoritemusic";
    }

    public static String URL_GETUSERACCOUNTDETAIL() {
        return getServerIp() + "/userapi/user.getuseraccountdetail";
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
        if (ip.matches("http://((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))"))
            return ip + ":88";
        else return ip;
    }

}
