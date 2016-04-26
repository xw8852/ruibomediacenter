package com.msx7.josn.ruibo_mediacenter.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件名: DateUtil
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/24
 */
public class DateUtil {

    /**
     * @param date "\/Date(1464013976850+0800)\/"
     * @return yyyy-MM-dd
     */
    public static final String converTime(String date) {
        if (TextUtils.isEmpty(date)) return date;
        Matcher matcher = Pattern.compile("[0-9]{1,}").matcher(date);
        matcher.find();
        date = matcher.group();
        if (!TextUtils.isEmpty(date))
            return new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(date));
        return date;
    }
}
