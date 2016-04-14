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


    @InjectView(R.id.search_content)
    EditText mSearchContent;

    @InjectView(R.id.search_btn)
    View mSearchBtn;

    @InjectView(R.id.searchRoot)
    View mSearchRoot;

    @InjectView(R.id.RecyclerView)
    RecyclerView mRecyclerView;


    @InjectView(R.id.down)
    View mDownBtn;

    @InjectView(R.id.clear)
    View mclear;

    @InjectView(R.id.selectAll)
    CheckBox mSelectAll;

    @InjectView(R.id.tip)
    TextView mTip;

    MusicAdapter mMusicAdapter;

    void initSearch() {
        mSearchRoot.setVisibility(View.GONE);
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
//        mSearchBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSearch();
//            }
//        });
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MarginDecoration(getContext()));
        mMusicAdapter = new MusicAdapter(new ArrayList<BeanMusic>());
        mRecyclerView.setAdapter(mMusicAdapter);
        mMusicAdapter.setData(SharedPreferencesUtil.getCollection());

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
        mclear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.clearCollection();
                mMusicAdapter.clear();
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

    public void showData() {
        mMusicAdapter.setData(SearchView.sort(SharedPreferencesUtil.getCollection()));
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
        SharedPreferencesUtil.clearCollection();
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
            if (musics == null) musics = new ArrayList<BeanMusic>();
            this.beanMusics = musics;
        }

        public void clearCheck() {
            if (mChecked.size() != beanMusics.size()) return;
            mChecked = new ArrayList<String>();
            onCheckedItem();
            notifyDataSetChanged();
        }

        public void clear() {
            beanMusics.clear();
            notifyDataSetChanged();
            mChecked.clear();
            onCheckedItem();
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
            if (musics == null) musics = new ArrayList<BeanMusic>();
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

            holder.itemView.setOnClickListener(new OnClickListener() {
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
