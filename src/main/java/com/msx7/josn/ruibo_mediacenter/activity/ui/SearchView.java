package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.msx7.josn.ruibo_mediacenter.common.SyncUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.dialog.CheckDownDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.Keyboard1;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件名: SearchView
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/2/28
 */
public class SearchView extends BeanView {

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_search, this);
        Inject.inject(this, this);
        initSearch();
        findViewById(R.id.tmp).setVisibility(GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doSearch();
            }
        }, 100);
    }


    @InjectView(R.id.search_content1)
    EditText mSearchContent;

    @InjectView(R.id.search_btn1)
    View mSearchBtn;

    @InjectView(R.id.codeBg)
    View mCodeBg;


    @InjectView(R.id.SongPageView)
    public SongPageView mSongPageView;


    @InjectView(R.id.down1)
    View mDownBtn;

    @InjectView(R.id.collection1)
    View mCollection;

    @InjectView(R.id.selectAll)
    CheckBox mSelectAll;

    @InjectView(R.id.tip)
    TextView mTip;


    public void showEnable(boolean enable) {
        mDownBtn.setEnabled(false);
        mCollection.setEnabled(false);
        mSelectAll.setEnabled(enable);
        if (!enable) {
            mSongPageView.setSelectedAll(false);
        }
        if (enable)
            mSongPageView.notifyLoginStatusChange();
    }

    public void clear() {
        mSearchContent.setText("");
        mSongPageView.showData(new ArrayList<BeanMusic>());
        mDownBtn.setEnabled(false);
        mCollection.setEnabled(false);
        mSearchContent.setInputType(InputType.TYPE_NULL);
        mSearchContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContent.setText("");
                mSearchContent.setTag("mSearchContent");
                new Keyboard1(v, mSearchContent).getPopupWindow().showAsDropDown(v, 0, getResources().getDimensionPixelSize(R.dimen.dp10));
            }
        });
        findViewById(R.id.tmp).setVisibility(GONE);
        mCodeBg.setVisibility(VISIBLE);

        initSearch();
    }

    public void initSearch() {
//        mSearchContent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
//                    doSearch();
//                }
//                return false;
//            }
//        });
        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSongPageView.setSelectedAll(isChecked);
                doSelected(mSongPageView.getAllSelectedMusic());
                mSongPageView.setTips();
            }
        });
        mSongPageView.setDoSelect(doSelect);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });


        mCollection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.getUserInfo() == null) {
                    ToastUtil.show("请先登录");
                    return;
                }
                List<BeanMusic> musics = mSongPageView.getAllSelectedMusic();
                if (musics.size() <= 0) return;
                List<BeanMusic> _musics = SharedPreferencesUtil.getCollection();
                BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();

                for (int i = 0; i < musics.size(); i++) {
                    BeanMusic music = musics.get(i);
                    if (music == null || TextUtils.isEmpty(music.name)) continue;
                    music.loginid = userInfo.id;
                    if (!_musics.contains(music)) _musics.add(music);
                }
                Collections.sort(_musics, new Comparator<BeanMusic>() {
                    @Override
                    public int compare(BeanMusic lhs, BeanMusic rhs) {
                        return (int) (lhs.id - rhs.id);
                    }
                });
                SharedPreferencesUtil.saveCollection(_musics);
                mSongPageView.clearSelected();
                ToastUtil.show("歌曲收藏成功");
//                clear();
            }
        });
        mDownBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.getUserInfo() == null) {
                    ToastUtil.show("请先登录");
                    return;
                }
                download(mSongPageView);
            }
        });
    }

  public   SingleFragment.IDoSelect doSelect = new SingleFragment.IDoSelect() {
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
            doSelected(musics);
            mSongPageView.setTips();
        }
    };

    void doSelected(List<BeanMusic> musics) {
        if (musics != null && musics.size() > 0) {
            mDownBtn.setEnabled(true);
            mCollection.setEnabled(true);
        } else {
            mDownBtn.setEnabled(false);
            mCollection.setEnabled(false);
            return;
        }
        BeanUserInfo beanUserInfo = SharedPreferencesUtil.getUserInfo();
        if (beanUserInfo == null) {
            mDownBtn.setEnabled(false);
            return;
        }
        if (musics.size() > beanUserInfo.entity.DownloadMusicAmount) {
            mDownBtn.setEnabled(false);
            return;
        }
        if (musics.size() > 0) {
            mDownBtn.setEnabled(true);
            return;
        }
        long size = 0;
        for (BeanMusic music : mSongPageView.getAllSelectedMusic()) {
            size += music.size;
        }
        if (size / 1.0 > beanUserInfo.entity.DownloadMusicSize) {
            mDownBtn.setEnabled(false);
            return;
        }
    }


    void doSearch() {
        if (TextUtils.isEmpty(mSearchContent.getText().toString().trim())) {
            clear();
            return;
        }
        SyncUserInfo.SyncUserInfo();
        ((BaseActivity) getContext()).showProgess();
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETMUSICLIST(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        L.d(response);
                        ((BaseActivity) getContext()).dismisProgess();
                        BaseBean<List<BeanMusic>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanMusic>>>() {
                        }.getType());
                        if ("200".equals(baseBean.code)) {
                            clear();
                            mDownBtn.setEnabled(false);
                            mSongPageView.setSelectedAll(false);
                            mSongPageView.showData(baseBean.data);
                            mSongPageView.setDoSelect(doSelect);
                            if (baseBean.data == null || baseBean.data.isEmpty()) {
                                mCodeBg.setVisibility(VISIBLE);
                                findViewById(R.id.tmp).setVisibility(GONE);
                            } else {
                                findViewById(R.id.tmp).setVisibility(VISIBLE);
                                mCodeBg.setVisibility(GONE);
                            }
                        } else {
                            ToastUtil.show(baseBean.msg);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getContext()).dismisProgess();
                ToastUtil.show(VolleyErrorUtils.getError(error));
            }
        });
//        if (!TextUtils.isEmpty(mSearchContent.getText().toString().trim()))
        request.addRequestJson(mSearchContent.getText().toString().trim());
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    public static final List<BeanMusic> sort(List<BeanMusic> baseban) {
        int i = 0;
        int count = baseban.size();
        BeanMusic[] musics = new BeanMusic[count];
        while (i * 2 < count) {
            musics[i * 2] = baseban.get(i);
            i++;
        }
        if (count % 2 > 0 && musics[count - 1] == null) {
            musics[count - 1] = baseban.get(i);
            i++;
        }
        int j = 0;
        while (j < count && i < count) {
            if (musics[j] != null) {
                j++;
                continue;
            }
            musics[j] = baseban.get(i);
            i++;
        }
        return Arrays.asList(musics);
    }


}
