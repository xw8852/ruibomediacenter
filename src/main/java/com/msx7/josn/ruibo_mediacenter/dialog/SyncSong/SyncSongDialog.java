package com.msx7.josn.ruibo_mediacenter.dialog.SyncSong;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.BaseCustomDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.UserPrintDownDialog;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.CheckURequest;
import com.msx7.josn.ruibo_mediacenter.net.SyncSongRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.text.DecimalFormat;

import static com.android.volley.Request.Method.POST;

/**
 * 文件名: SyncSongDialog
 * 描  述: 管理员页面-歌曲库item
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class SyncSongDialog extends BaseCustomDialog {

    @InjectView(R.id.sure)
    TextView mSure;


    @InjectView(R.id.cancel)
    TextView mCancel;
    @InjectView(R.id.tip)
    TextView mTip;

    BaseActivity activity;

    public SyncSongDialog(Context context) {
        super(context);
        getLayoutInflater().inflate(R.layout.layout_dialog_song, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        activity = (BaseActivity) context;
        setTitle("歌曲库");
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RuiBoApplication.getApplication().runVolleyRequest(new SyncSongRequest(
                        SharedPreferencesUtil.getAdminUserInfo().id, responseListener, errorListener));
                mTip.setText("歌曲正在上传中，请耐心等待...");
                mSure.setText("查看执行情况");
                setCancelable(false);
                mSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkDownProgress();
                    }
                });
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCancel.setVisibility(View.GONE);
        showContent();
    }

    void checkDownProgress() {

        BaseJsonRequest request = new BaseJsonRequest(POST, UrlStatic.URL_GETUPLOADPROGRESS(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if (!"200".equals(baseBean.code)) {
                            ToastUtil.show("获取下载进度失败，请稍后重新尝试");
                            return;
                        }
                        String progress = baseBean.data.toString();
                        if (progress.contains("100")) {
                            dismiss();
                            ToastUtil.show("歌曲上传成功");
                            return;
                        }
                        mTip.setText("已下载 " + progress + "，请耐心等待...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.show("获取下载进度失败，请稍后重新尝试");
                    }
                });
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    void showContent() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new CheckURequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                activity.dismisProgess();
                BaseBean<CheckUBean> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<CheckUBean>>() {
                }.getType());
                if (!"200".equals(baseBean.code)) {
                    ToastUtil.show(baseBean.msg);
                    dismiss();
                    return;
                }
                show();
                mTip.setText("U盘中有发现 " + baseBean.data.FolderCount + " 个目录" +
                        "\n" +
                        "总共有 " + new DecimalFormat("0.##").format(baseBean.data.UsedSize / 1024.0) + "G 的资源需要上");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show("请稍后重新尝试");
                dismiss();
                activity.dismisProgess();
            }
        }));
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            activity.dismisProgess();
            L.d(response);
            BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
            if ("200".equals(baseBean.code)) {
//                dismiss();
            }
//            ToastUtil.show(baseBean.msg);

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
//            activity.dismisProgess();
            ToastUtil.show(VolleyErrorUtils.getError(error));
        }
    };


    public static class CheckUBean {
        /**
         * 文件夹数
         */
        @SerializedName("FolderCount")
        public long FolderCount;
        /**
         * 文件数
         */
        @SerializedName("FileCount")
        public long FileCount;
        /**
         * 待上传资源容量(MB)
         */
        @SerializedName("UsedSize")
        public long UsedSize;
        /**
         * U盘总容量
         */
        @SerializedName("TotalSize")
        public long TotalSize;
    }
}
