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


    protected void download(final SongPageView songPageView) {
        int songs = songPageView.getAllSelectedMusic().size();
        double money = SharedPreferencesUtil.getUserInfo().entity.DownloadOneMusicPrice * songs;
        money = Math.min(money, SharedPreferencesUtil.getUserInfo().entity.DownloadAllMusicPrice);

        final BaseActivity activity = (BaseActivity) getContext();
        activity.showProgess();

        String url = UrlStatic.URL_DOWNLOADMUSICCHECK();
        final CheckPost post = new CheckPost(SharedPreferencesUtil.getUserInfo().id, money, songPageView.getAllSelectedMusic());
        BaseJsonRequest
                jsonRequest = new BaseJsonRequest(POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.d(response);
                activity.dismisProgess();
                BaseBean<UserInfoNet.UserInfo.EntityEntity> baseBean = new Gson().fromJson(
                        response, new TypeToken<BaseBean<UserInfoNet.UserInfo.EntityEntity>>() {
                        }.getType()
                );
                if (!"200".equals(baseBean.code)) {
                    ToastUtil.show(baseBean.msg);
                    return;
                }
                SyncUserInfo.SyncUserInfo();
                double size = 0;
                for (BeanMusic music : songPageView.getAllSelectedMusic()) {
                    size += music.size;
                }
                DecimalFormat a = new DecimalFormat(".##");
                if (baseBean.data.DownloadMusicRemainDiskSpace <= 0) {
                    ToastUtil.show("您还没有插入U盘");
                    return;
                }
                if (size > baseBean.data.DownloadMusicRemainDiskSpace) {
                    ToastUtil.show("U盘空间不足，还需" + a.format(baseBean.data.DownloadMusicRemainDiskSpace - size) + "M");
                    return;
                }
                double money = baseBean.data.DownloadOneMusicPrice * songPageView.getAllSelectedMusic().size();
                money = Math.min(money, SharedPreferencesUtil.getUserInfo().entity.DownloadAllMusicPrice);
                post.money = money;
                CheckDownDialog dialog = new CheckDownDialog(songPageView.getContext());
                dialog.show();
                double maxSize = Math.max(size, baseBean.data.DownloadMusicRemainDiskSpace);


                dialog.show(size,
                        (int)Math.round(100 * size / maxSize),
                        baseBean.data.DownloadMusicRemainDiskSpace,
                        (int)Math.round (100 * baseBean.data.DownloadMusicRemainDiskSpace / maxSize),
                        baseBean.data.PrintPrice);
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


    public static class CheckPost {
        @SerializedName("musiclist")
        public List<BeanMusic> musiclist;
        @SerializedName("loginid")
        public long loginId;
        @SerializedName("money")
        public double money;
        @SerializedName("printnumber")
        public int printnumber=1;

        @SerializedName("needprint")
        public int needprint;

        public CheckPost(long loginId, double money, List<BeanMusic> musiclist) {
            this.loginId = loginId;
            this.money = money;
            this.musiclist = musiclist;
        }

    }


}
