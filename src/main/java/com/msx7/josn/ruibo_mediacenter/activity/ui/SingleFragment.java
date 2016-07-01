package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.common.SyncUserInfo;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;
import com.msx7.lib.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SingleFragmentPagerAdapter
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/28
 */
public class SingleFragment extends Fragment implements IMusicSelcted {
    SinglePage singlePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        singlePage = new SinglePage(inflater.getContext());
        return singlePage;
    }

    IMusicSelcted musicSelcted;

    public void setMusic(IMusicSelcted music) {
        musicSelcted = music;
    }

    @Override
    public void addSelected(BeanMusic music) {
        if (musicSelcted == null){
            return;
        }
        musicSelcted.addSelected(music);
    }

    @Override
    public List<BeanMusic> getAllMusic() {
        if (musicSelcted == null) {
            return new ArrayList<>();
        }
        return musicSelcted.getAllMusic();
    }

    @Override
    public List<BeanMusic> getAllSelectedMusic() {
        if (musicSelcted == null) {
            return new ArrayList<>();
        }
        return musicSelcted.getAllSelectedMusic();
    }

    @Override
    public void removeSelected(BeanMusic music) {
        if (musicSelcted == null){
            return;
        }
        musicSelcted.removeSelected(music);
    }

    int start = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = getArguments().getInt("num");
        start = position * SongPageView.EVG_PAGE_SONG;
        int count = SongPageView.EVG_PAGE_SONG;
        List<BeanMusic> data = new ArrayList<>();
        int _pos = start;
        while (count > 0) {
            if (_pos >= getAllMusic().size()) break;
            data.add(getAllMusic().get(_pos));
            _pos++;
            count--;
        }
        singlePage.showData(data);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                singlePage.notifyDataSetChanged();
            }
        }, 200);
        singlePage.setTag(position);
    }


    public class SinglePage extends LinearLayout {
        public FullGridView gridView;
        public MusicAdapter adapter;

        public SinglePage(Context context) {
            super(context);
            inflate(context, R.layout.item_single_page_song, this);
            gridView = (FullGridView) getChildAt(0);
            gridView.setNumColumns(SharedPreferencesUtil.getRow1());
        }

        public void notifyDataSetChanged() {
            adapter.notifyDataSetChanged();
        }

        public void showData(List<BeanMusic> musics) {
            while (musics.size() < SongPageView.EVG_PAGE_SONG) {
                musics.add(new BeanMusic());
            }
            adapter = new MusicAdapter(getContext(), musics);
            gridView.setAdapter(adapter);
        }

    }

    public class MusicAdapter extends BaseAdapter<BeanMusic> {
        public MusicAdapter(Context context, List<BeanMusic> data) {
            super(context, data);
        }

        public MusicAdapter(Context context, BeanMusic... data) {
            super(context, data);
        }

        @Override
        public View createView(int position, View convertView, LayoutInflater inflater) {
            MusicViewHolder holder = null;
            if (convertView != null) {
                holder = (MusicViewHolder) convertView.getTag();
            } else {
                holder = new MusicViewHolder(inflater.inflate(R.layout.layout_item_list, null));
                holder.itemView.setTag(holder);
            }
            onBindViewHolder(holder, position);
            return holder.itemView;
        }

        @Override
        public int getCount() {
            return SongPageView.EVG_PAGE_SONG;
        }


        /**
         * 实现从水平排序转换为垂直排序
         *
         * @param position
         * @return
         */
        @Override
        public BeanMusic getItem(int position) {
//            int tmp = vertalPosition[position] - 1;
//            if (tmp >= data.size()) {
//                return null;
//            }
            //列数
            int tmp = getRealPosition(position);
            if (tmp >= data.size()) {
                return null;
            }
            return super.getItem(tmp);
        }

        public int getRealPosition(int position) {
            int row = SharedPreferencesUtil.getRow1();
            //行数
            int line = SharedPreferencesUtil.getRow2();

            int _row = position % row;

            int _line = position / row;
            return _row * line + _line;
        }

        public void onBindViewHolder(final MusicViewHolder holder, int position) {
            holder.box.setOnCheckedChangeListener(null);
            holder.box.setChecked(false);
            final BeanMusic music = getItem(position);
            if (music == null || TextUtils.isEmpty(music.path)) {
                holder.box.setVisibility(View.INVISIBLE);
                holder.name.setVisibility(View.INVISIBLE);
                return;
            } else {
                holder.box.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
            }
            holder.box.setChecked(isSelected(music));
//            holder.num.setText("歌曲编码:" + music.code);
            holder.name.setText(String.valueOf(start + getRealPosition(position) + 1) + "." + music.path);
//            holder.money.setText(music.money + "元");
            holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (music == null || holder.box.getVisibility() != View.VISIBLE) return;
                    if (isChecked) {
                        if (SharedPreferencesUtil.getUserInfo() == null) {
                            holder.box.setChecked(false);
                            getAllSelectedMusic().clear();
                            return;
                        }
                        addSelected(music);
                        if (select != null) select.doSelect(getAllSelectedMusic());
                    } else {
                        removeSelected(music);
                        if (select != null) select.doSelect(getAllSelectedMusic());
                    }
                    SyncUserInfo.SyncUserInfo();
                }
            });
        }

    }

    boolean isSelected(BeanMusic music) {
        for (BeanMusic _music : getAllSelectedMusic()) {
            if (_music != null && _music.code == music.code) {
                return true;
            }
        }
        return false;
    }

    public interface IDoSelect {
        void doSelect(List<BeanMusic> musics);
    }

    static IDoSelect select;

    public void setDoSelect(IDoSelect select) {
        this.select = select;
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.num)
        TextView num;
        @InjectView(R.id.box)
        CheckBox box;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.money)
        TextView money;

        public MusicViewHolder(View itemView) {
            super(itemView);
            Inject.inject(this, itemView);
        }
    }

}
