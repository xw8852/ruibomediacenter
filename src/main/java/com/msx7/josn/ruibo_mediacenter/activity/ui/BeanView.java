package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.activity.net.UserInfoNet;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.SyncUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.CheckDownDialog;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.android.volley.Request.Method.POST;


/**
 * 文件名: BeanView
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/3/2
 */
public class BeanView extends LinearLayout {
    public BeanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    protected void download(final SongPageView songPageView, final double money) {
        int songs = songPageView.getAllSelectedMusic().size();
        if (songs <= 0) {
            ToastUtil.show("请选择需要下载的歌曲");
            return;
        }

        final BaseActivity activity = (BaseActivity) getContext();
        activity.showProgess();

        String url = UrlStatic.URL_DOWNLOADCHECK();
        final CheckPost post = new CheckPost(SharedPreferencesUtil.getUserInfo().id, money, songPageView.getAllSelectedMusic());
        BaseJsonRequest
                jsonRequest = new BaseJsonRequest(POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.d(response);
                activity.dismisProgess();
                BaseBean<CheckDown> baseBean = new Gson().fromJson(
                        response, new TypeToken<BaseBean<CheckDown>>() {
                        }.getType()
                );
                if (!"200".equals(baseBean.code)) {
                    ToastUtil.show(baseBean.msg);
                    return;
                }
                SyncUserInfo.SyncUserInfo();


                CheckDownDialog dialog = new CheckDownDialog(songPageView.getContext());
                dialog.show();
                double maxSize = Math.max(baseBean.data.DownSize, baseBean.data.DiskSize);


                dialog.show(baseBean.data.DownSize,
                        (int) Math.round(100 * baseBean.data.DownSize / maxSize),
                        baseBean.data.DiskSize,
                        (int) Math.round(100 * baseBean.data.DiskSize / maxSize),
                        SharedPreferencesUtil.getUserInfo().entity.PrintPrice);
                dialog.setPostData(post);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show(R.string.error);
                activity.dismisProgess();
            }
        }
        );
        jsonRequest.addRequestJson(new Gson().toJson(post));
        RuiBoApplication.getApplication().runVolleyRequest(jsonRequest);
    }

    public static class CheckDown {
        @SerializedName("DownSize")
        public int DownSize;
        @SerializedName("DiskSize")
        public int DiskSize;
    }

    public static class CheckPost {
        @SerializedName("musiclist")
        public List<BeanMusic> musiclist;
        @SerializedName("loginid")
        public long loginId;
        @SerializedName("money")
        public double money;
        @SerializedName("printnumber")
        public int printnumber = 0;

        @SerializedName("needprint")
        public int needprint;

        public CheckPost(long loginId, double money, List<BeanMusic> musiclist) {
            this.loginId = loginId;
            this.money = money;
            this.musiclist = musiclist;
        }

    }


}
