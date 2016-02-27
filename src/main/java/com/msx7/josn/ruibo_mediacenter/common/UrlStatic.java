package com.msx7.josn.ruibo_mediacenter.common;

/**
 * 文件名: UrlStatic
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/27
 */
public class UrlStatic {
    private static final String BASE_UR = "http://xw8852.xicp.net";

    public static final String URL_LOGIN = BASE_UR + "/passportapi/passport.userlogin";
    public static final String URL_CHANGEUSERPASSWORD = BASE_UR + "/passportapi/passport.changeuserpassword";
    public static final String URL_CHANGEADMINPASSWORD = BASE_UR + "/passportapi/passport.changeadminpassword";
    public static final String URL_ADMINLOGIN = BASE_UR + "/passportapi/passport.adminlogin";
    public static final String URL_UPLOADMUSIC = BASE_UR + "/adminapi/admin.uploadmusic";
    public static final String URL_GETUSERLIST = BASE_UR + "/adminapi/admin.getuserlist";

    public static final String URL_USERINMONEY = BASE_UR + "/adminapi/admin.userinmoney";
    public static final String URL_CLOSEUSER = BASE_UR + "/adminapi/admin.closeuser";

    /**
     * 导出会员列表
     */
    public static final String URL_EXPORTUSERTOEXCEL = BASE_UR + "/adminapi/admin.exportusertoexcel";
    /**
     * 导出充值明细
     */
    public static final String URL_EXPORTUSERINMONEYTOEXCEL = BASE_UR + "/adminapi/admin.exportuserinmoneytoexcel";
    /**
     * 导出对账单
     */
    public static final String URL_EXPORTMONEYDETAILTOEXCEL = BASE_UR + "/adminapi/admin.exportmoneydetailtoexcel";
    /**
     * 获取对账单
     */
    public static final String URL_GETMONEYDETAIL = BASE_UR + "/adminapi/admin.getmoneydetail";


}
