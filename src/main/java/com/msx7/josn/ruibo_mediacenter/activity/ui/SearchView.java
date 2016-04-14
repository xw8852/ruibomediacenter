package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
import com.msx7.josn.ruibo_mediacenter.dialog.Keyboard1;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
    }


    @InjectView(R.id.search_content)
    EditText mSearchContent;

    @InjectView(R.id.search_btn)
    View mSearchBtn;

    @InjectView(R.id.RecyclerView)
    RecyclerView mRecyclerView;

    @InjectView(R.id.swip)
    SwipeRefreshLayout mSwip;

    @InjectView(R.id.down)
    View mDownBtn;

    @InjectView(R.id.collection)
    View mCollection;

    @InjectView(R.id.selectAll)
    CheckBox mSelectAll;

    @InjectView(R.id.tip)
    TextView mTip;

    MusicAdapter mMusicAdapter;

    public void clear() {
        mMusicAdapter.setData(new ArrayList<BeanMusic>());
        mSearchContent.setText("");
        mSearchContent.setInputType(InputType.TYPE_NULL);
        mSearchContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContent.setText("");
                mSearchContent.setTag("mSearchContent");
                new Keyboard1(v, mSearchContent).getPopupWindow().showAsDropDown(v, 0, getResources().getDimensionPixelSize(R.dimen.dp10));
            }
        });
    }

    void initSearch() {
        mSearchContent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    doSearch();
                }
                return false;
            }
        });
//        mSearchContent.
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MarginDecoration(getContext()));
        mMusicAdapter = new MusicAdapter(new ArrayList<BeanMusic>());
        mRecyclerView.setAdapter(mMusicAdapter);
        mSwip.setColorSchemeColors(0xFFFF971E);
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSearch();
            }
        });
        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMusicAdapter.clearCheck();
                } else {
                    mMusicAdapter.checkAll();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwip.setRefreshing(true);
                doSearch();
            }
        }, 100);

        mCollection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.getUserInfo() == null) {
                    ToastUtil.show("请先登录");
                    return;
                }
                if (mMusicAdapter.mChecked.size() <= 0) return;
                List<BeanMusic> musics = SharedPreferencesUtil.getCollection();
                BeanUserInfo userInfo = SharedPreferencesUtil.getUserInfo();
                for (int i = 0; i < mMusicAdapter.mChecked.size(); i++) {
                    int position = Integer.parseInt(mMusicAdapter.mChecked.get(i));
                    BeanMusic music = mMusicAdapter.beanMusics.get(position);
                    music.loginid = userInfo.id;
                    if (!musics.contains(music)) musics.add(music);
                }
                Collections.sort(musics, new Comparator<BeanMusic>() {
                    @Override
                    public int compare(BeanMusic lhs, BeanMusic rhs) {
                        return (int) (lhs.code - rhs.code);
                    }
                });
                SharedPreferencesUtil.saveCollection(musics);
                ToastUtil.show("歌曲收藏成功");

            }
        });
        mDownBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.getUserInfo() == null) {
                    ToastUtil.show("请先登录");
                    return;
                }
                download();
            }
        });
    }

    void download() {
        double money = 0.0;
        List<BeanMusic> urls = new ArrayList<BeanMusic>();
        BeanUserInfo info = SharedPreferencesUtil.getUserInfo();
        //下载大小
        long size = 0;
        for (String str : mMusicAdapter.mChecked) {
            BeanMusic beanMusic = mMusicAdapter.beanMusics.get(Integer.parseInt(str));
            beanMusic.loginid = info.loginid;
            size = +beanMusic.size;
            urls.add(beanMusic);
            money += beanMusic.money;
        }

        if (urls == null || urls.isEmpty()) {
            ToastUtil.show("请选择歌曲");
            return;
        }
        if (money > SharedPreferencesUtil.getUserInfo().totalmoney) {
            ToastUtil.show("余额不足,请充值");
            return;
        }

        if (!SDUtils.isExist()) {
            ToastUtil.show("请将u盘插入usb1接口处");
            return;
        }


        if (SDUtils.getRemainSize() < size) {
            ToastUtil.show("U盘存储空间不足");
            return;
        }


        download(urls);

    }

    void doSearch() {
        if (TextUtils.isEmpty(mSearchContent.getText().toString().trim())) {
            mSwip.setRefreshing(false);
            return;
        }
        mMusicAdapter.clear();
        onCheckedItem();
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETMUSICLIST(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        L.d(response);
                        mSwip.setRefreshing(false);
                        BaseBean<List<BeanMusic>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanMusic>>>() {
                        }.getType());
                        if ("200".equals(baseBean.code)) {
                            mMusicAdapter.addMore(sort(baseBean.data));
                        } else {
                            ToastUtil.show(baseBean.msg);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwip.setRefreshing(false);
                ToastUtil.show(VolleyErrorUtils.getError(error));
            }
        });
//        if (!TextUtils.isEmpty(mSearchContent.getText().toString().trim()))
        request.addRequestJson(mSearchContent.getText().toString().trim());
        mSwip.setRefreshing(true);
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

    void onCheckedItem() {
        int count = 0;
        double money = 0.0;
        for (String str : mMusicAdapter.mChecked) {
            count++;
            money += mMusicAdapter.beanMusics.get(Integer.parseInt(str)).money;
        }
        if (count > 0) {
            mTip.setText("已选中" + count + "首歌曲，下载需支付" + money + "元");
        } else {
            mTip.setText("");
        }
    }

    class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {
        List<BeanMusic> beanMusics;
        List<String> mChecked = new ArrayList<String>();

        public MusicAdapter(List<BeanMusic> musics) {
            this.beanMusics = musics;
        }

        public void clear() {
            beanMusics.clear();
            notifyDataSetChanged();
            mChecked.clear();
            onCheckedItem();
        }

        public void clearCheck() {
            if (mChecked.size() != beanMusics.size()) return;
            mChecked = new ArrayList<String>();
            onCheckedItem();
            notifyDataSetChanged();
        }

        public void checkAll() {
            if (mChecked.size() == beanMusics.size()) return;
            for (int i = 0; i < beanMusics.size(); i++) {
                if (!mChecked.contains("" + i)) mChecked.add("" + i);
            }
            onCheckedItem();
            notifyDataSetChanged();
        }

        public void setData(List<BeanMusic> musics) {
            this.beanMusics = musics;
            notifyDataSetChanged();
        }

        public void addMore(List<BeanMusic> musics) {
            this.beanMusics.addAll(musics);
            notifyDataSetChanged();
        }

        @Override
        public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MusicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.layout_item_list, null));
        }

        @Override
        public void onBindViewHolder(final MusicViewHolder holder, final int position) {

            BeanMusic music = beanMusics.get(position);

            if (mChecked.contains("" + position)) {
                holder.itemView.setSelected(true);
            } else holder.itemView.setSelected(false);

            holder.num.setText("歌曲编码:" + music.code);
            holder.name.setText(music.name);
            holder.money.setText(music.money + "元");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChecked.contains(position + "")) {
                        mChecked.remove(position + "");
                        mSelectAll.setChecked(true);
                        holder.itemView.setSelected(false);
                    } else {
                        mChecked.add("" + position);
                        if (mChecked.size() == beanMusics.size()) {
                            mSelectAll.setChecked(false);
                        }
                        holder.itemView.setSelected(true);
                    }
                    onCheckedItem();
                }
            });
        }

        @Override
        public int getItemCount() {
            return beanMusics.size();
        }
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.money)
        TextView money;

        public MusicViewHolder(View itemView) {
            super(itemView);
            Inject.inject(this, itemView);
        }
    }

    public class MarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public MarginDecoration(Context context) {
            margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(margin, margin, margin, margin);
        }
    }
}
