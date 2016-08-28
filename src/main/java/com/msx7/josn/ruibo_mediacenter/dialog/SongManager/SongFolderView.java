package com.msx7.josn.ruibo_mediacenter.dialog.SongManager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.activity.ui.FullGridView;
import com.msx7.josn.ruibo_mediacenter.activity.ui.SongPageView;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;
import com.msx7.lib.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SongFolderView
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/8/27
 */

public class SongFolderView extends ViewPager {
    public static int EVG_PAGE_SONG = 27;

    public SongFolderView(Context context) {
        super(context);
        EVG_PAGE_SONG = SharedPreferencesUtil.getRow1() * SharedPreferencesUtil.getRow2();
    }

    public SongFolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        EVG_PAGE_SONG = SharedPreferencesUtil.getRow1() * SharedPreferencesUtil.getRow2();
    }

    List<BeanSongFolder> folders = new ArrayList<>();
    List<BeanSongFolder> selected = new ArrayList<>();

    /**
     * set data
     *
     * @param data
     */
    public void setData(List<BeanSongFolder> data) {
        this.folders = data;
        selected.clear();
        int count = data.size() / EVG_PAGE_SONG;
        if (data.size() % EVG_PAGE_SONG > 0)
            count++;
        setAdapter(new SongFolderAdapter(count));
    }

    /**
     * @return 获取所有的文件夹
     */
    public List<BeanSongFolder> getAllFolder() {
        return folders;
    }

    public void notifyChange() {
        post(new Runnable() {
            @Override
            public void run() {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    ((SinglePage) getChildAt(i)).notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取选中的文件夹
     *
     * @return
     */
    public List<BeanSongFolder> getAllSelected() {
        return selected;
    }

    public void resetSelect(boolean flag) {
        selected.clear();
        if (flag) {
            selected.addAll(folders);
        }
        notifyChange();
    }

    class SongFolderAdapter extends PagerAdapter {
        int count;

        public SongFolderAdapter(int count) {
            this.count = count;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            SinglePage singlePage = new SinglePage(container.getContext());
            int start = position * EVG_PAGE_SONG;
            int end = Math.min(folders.size(), start + EVG_PAGE_SONG);
            singlePage.showData(getAllFolder().subList(start, end));
            container.addView(singlePage, new ViewPager.LayoutParams());//添加页卡
            return singlePage;
        }

        @Override
        public int getCount() {
            return count;//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
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

        public void showData(List<BeanSongFolder> musics) {
            while (musics.size() < EVG_PAGE_SONG) {
                musics.add(new BeanSongFolder());
            }
            adapter = new MusicAdapter(getContext(), musics);
            gridView.setAdapter(adapter);
        }

    }

    public class MusicAdapter extends BaseAdapter<BeanSongFolder> {
        public MusicAdapter(Context context, List<BeanSongFolder> data) {
            super(context, data);
        }

        public MusicAdapter(Context context, BeanSongFolder... data) {
            super(context, data);
        }

        @Override
        public View createView(int position, View convertView, LayoutInflater inflater) {
            MusicViewHolder holder = null;
            if (convertView != null) {
                holder = (MusicViewHolder) convertView.getTag();
            } else {
                holder = new MusicViewHolder(inflater.inflate(R.layout.layout_item_list2, null));
                holder.itemView.setTag(holder);
            }
            onBindViewHolder(holder, position);
            return holder.itemView;
        }

        @Override
        public int getCount() {
            return EVG_PAGE_SONG;
        }


        /**
         * 实现从水平排序转换为垂直排序
         *
         * @param position
         * @return
         */
        @Override
        public BeanSongFolder getItem(int position) {
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
            final BeanSongFolder music = getItem(position);
            if (music == null || TextUtils.isEmpty(music.name)) {
                holder.box.setVisibility(View.INVISIBLE);
                holder.name.setVisibility(View.INVISIBLE);
                return;
            } else {
                holder.box.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
            }

            holder.box.setChecked(getAllSelected().contains(music));
//            holder.num.setText("歌曲编码:" + music.code);
            holder.name.setText(String.valueOf(music.id) + "." + music.name);
//            holder.money.setText(music.money + "元");
            holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (music == null || holder.box.getVisibility() != View.VISIBLE) return;
                    if (isChecked) {
                        getAllSelected().add(music);
                    } else {
                        getAllSelected().remove(music);
                    }
                    if (getFolderStatus() != null) getFolderStatus().change();
                }
            });
        }

    }

    IFolderStatus folderStatus;

    public IFolderStatus getFolderStatus() {
        return folderStatus;
    }

    public void setFolderStatus(IFolderStatus folderStatus) {
        this.folderStatus = folderStatus;
    }

    public static interface IFolderStatus {
        void change();
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
