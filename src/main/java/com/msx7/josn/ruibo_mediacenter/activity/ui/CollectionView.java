package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.net.OkHttpManager;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SDUtils;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SearchView
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/28
 */
public class CollectionView extends BeanView {

    public CollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_collection, this);
        Inject.inject(this, this);
        initSearch();
    }


    @InjectView(R.id.SongPageView)
    public SongPageView mSongPageView;


    @InjectView(R.id.down2)
    View mDownBtn;
    //
    @InjectView(R.id.clear2)
    View mdelete;

    @InjectView(R.id.selectAll2)
    CheckBox mSelectAll;

    @InjectView(R.id.tip)
    TextView mTip;


    void initSearch() {
        mSelectAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mdelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BeanMusic> musics = SharedPreferencesUtil.getCollection();
                List<BeanMusic> delMusics = mSongPageView.getAllSelectedMusic();
                if (delMusics != null) {
                    musics.removeAll(delMusics);
                }
                mSelectAll.setOnCheckedChangeListener(null);
                mSelectAll.setChecked(false);
                mSelectAll.setOnCheckedChangeListener(onCheckedChangeListener);
                SharedPreferencesUtil.saveCollection(musics);
                if (musics.isEmpty()) {
                    mdelete.setEnabled(false);
                    mDownBtn.setEnabled(false);
                }
                showData();
            }
        });
        mDownBtn.setEnabled(false);
        mSongPageView.setDoSelect(doSelect);
        mDownBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSongPageView.setSelectedAll(isChecked);
            doALlSelect(mSongPageView.getAllSelectedMusic());
            mSongPageView.setTips();
        }
    };
    public SingleFragment.IDoSelect doSelect = new SingleFragment.IDoSelect() {
        @Override
        public void doSelect(List<BeanMusic> musics) {
            if (musics.size() == mSongPageView.getAllMusic().size()) {
                if (!mSelectAll.isChecked()) {
                    mSelectAll.setChecked(true);
                }
            } else if (musics.size() == 0) {
                if (mSelectAll.isChecked()) {
                    mSelectAll.setChecked(false);
                }
            }
            doALlSelect(musics);
        }
    };

    void doALlSelect(List<BeanMusic> musics) {
        if (musics == null || musics.isEmpty()) {
            mDownBtn.setEnabled(false);
            mdelete.setEnabled(false);
            return;
        }
        mDownBtn.setEnabled(true);
        mdelete.setEnabled(true);
        BeanUserInfo beanUserInfo = SharedPreferencesUtil.getUserInfo();
        if (beanUserInfo == null) {
            mDownBtn.setEnabled(false);
            return;
        }
        double size = 0;
        for (BeanMusic music : mSongPageView.getAllSelectedMusic()) {
            size += music.size;
        }
        if (musics.size() > beanUserInfo.entity.DownloadMusicAmount) {
            mDownBtn.setEnabled(false);
            return;
        }
        if (size > beanUserInfo.entity.DownloadMusicSize) {
            mDownBtn.setEnabled(false);
            return;
        }
    }

    public void showData() {
        mSongPageView.setSelectedAll(false);
        mdelete.setEnabled(false);
        mDownBtn.setEnabled(false);
        mSelectAll.setOnCheckedChangeListener(null);
        mSelectAll.setChecked(false);
        mSelectAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mSongPageView.showData(SharedPreferencesUtil.getCollection());
    }

    void download() {
        download(mSongPageView);
    }

    public void clear() {
        SharedPreferencesUtil.saveCollection(new ArrayList<BeanMusic>());
        showData();
    }


}
