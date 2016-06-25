package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.activity.HomeActivity;
import com.msx7.josn.ruibo_mediacenter.activity.ui.SingleFragment.IDoSelect;
import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserInfo;
import com.msx7.josn.ruibo_mediacenter.common.SyncUserInfo;
import com.msx7.josn.ruibo_mediacenter.util.L;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;
import com.msx7.lib.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SongPageView
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/22
 */
public class SongPageView extends LinearLayout implements IMusicSelcted {

    public static int EVG_PAGE_SONG = 27;

    @InjectView(R.id.ViewPager)
    ViewPager mViewPager;


    @InjectView(R.id.tips)
    TextView mTips;

    @InjectView(R.id.down_tip)
    TextView mDownTips;

    HomeActivity activity;

    public SongPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (HomeActivity) context;
        inflate(context, R.layout.layout_song_page_view, this);
        Inject.inject(this, this);

        EVG_PAGE_SONG = SharedPreferencesUtil.getRow1() * SharedPreferencesUtil.getRow2();
        mTips.setText("");
        mDownTips.setText("");

        mViewPager.addOnPageChangeListener(pageChangeListener);

        selecters = new ArrayList<>();
        musics = new ArrayList<>();
    }


    void next() {
        int page = Math.min(mViewPager.getCurrentItem() + 1, pageCount);
        mViewPager.setCurrentItem(page, true);
        setTip(page);
    }

    void pre() {
        int page = Math.max(mViewPager.getCurrentItem() - 1, 0);
        mViewPager.setCurrentItem(page, true);
        setTip(page);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            post(new Runnable() {
                @Override
                public void run() {
                    setTip(position);
                }
            });
            SyncUserInfo.SyncUserInfo();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    IDoSelect select;


    public void setDoSelect(IDoSelect select) {
        this.select = select;
    }

    void doSelect2(List<BeanMusic> musics) {
        if (HomeActivity.curFragment == activity.searchFragment) {
            activity.searchFragment.mSearchView.doSelect.doSelect(musics);
        } else
        if (select != null) select.doSelect(musics);
    }

    public void notifyLoginStatusChange() {
        setTip(mViewPager.getCurrentItem());
    }

    public void setTips() {
        setTip(mViewPager.getCurrentItem());
    }

    void setTip(int position) {
        if (musics.size() <= 0) {
            mTips.setText("");
            mDownTips.setText("");
            return;
        }
        String tips = "共" + origin.size() + "首，当前页";
        tips += "<font color=\"#ff971e\">";
        tips += (position + 1);
        tips += "</font>";
        tips += "/" + pageCount;
        mTips.setText(Html.fromHtml(tips));
        StringBuffer buffer = new StringBuffer();
        BeanUserInfo beanUserInfo = SharedPreferencesUtil.getUserInfo();
        buffer.append("<big>" + getAllMusic().get(0).typename + "</big>  \t   ");
        if (beanUserInfo != null && getAllMusic().size() > 0) {
            buffer.append("已选歌曲");
            buffer.append("<font color=\"#ff971e\">");
            buffer.append(getAllSelectedMusic().size() + "/" + (long)beanUserInfo.entity.DownloadMusicAmount);
            buffer.append("</font>");
            buffer.append(",下载需支付");
            buffer.append("<font color=\"#ff971e\">");

            int size = getAllSelectedMusic().size();
            double money = beanUserInfo.entity.DownloadOneMusicPrice * size;
            buffer.append("" + Math.min(money, beanUserInfo.entity.DownloadAllMusicPrice));

            buffer.append("</font>");

            buffer.append("元，下载负荷");
            buffer.append("<font color=\"#ff971e\">");
            buffer.append(getFuhe());
            buffer.append("</font>");
        }
        mDownTips.setText(Html.fromHtml(buffer.toString()));
    }


    public String getFuhe() {
        BeanUserInfo beanUserInfo = SharedPreferencesUtil.getUserInfo();
        double size = 0;
        for (BeanMusic music : getAllSelectedMusic()) {
            size += music.size;
        }

        return Math.round(100 * size / (1.0 * beanUserInfo.entity.DownloadMusicSize)) + "%";
    }

    List<BeanMusic> musics;
    List<BeanMusic> origin;
    List<BeanMusic> selecters = new ArrayList<>();
    int pageCount = 0;
    FragmentManager fm;

    public void setFragmentManager(FragmentManager fm) {
        this.fm = fm;
    }

    public void showData(List<BeanMusic> musics) {
        if (musics == null) musics = new ArrayList<>();
        int position = Math.max(0, mViewPager.getCurrentItem());
        this.musics = musics;
        origin = new ArrayList<>();
        origin.addAll(musics);
        selecters.clear();
        mViewPager.removeAllViews();
        int size = this.musics.size();
        pageCount = size / EVG_PAGE_SONG;
        if (pageCount * EVG_PAGE_SONG < size) pageCount++;

        SingleFragmentPagerAdapter singleFragmentPagerAdapter = new SingleFragmentPagerAdapter(
                fm
        );
        mViewPager.setAdapter(singleFragmentPagerAdapter);
        position = Math.min(position, pageCount);
        mViewPager.setCurrentItem(position, true);
        setTip(position);
    }


    public class SingleFragmentPagerAdapter extends FragmentPagerAdapter {

        public SingleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SingleFragment fragment = new SingleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("num", position);
            fragment.setArguments(bundle);
            fragment.setMusic(SongPageView.this);
            fragment.setDoSelect(doSelect);
            return fragment;
        }

        @Override
        public int getCount() {
            return pageCount;
        }
    }

    IDoSelect doSelect = new IDoSelect() {
        @Override
        public void doSelect(List<BeanMusic> musics) {
            setTip(mViewPager.getCurrentItem());
            doSelect2(musics);
        }
    };


    public void clearSelected() {
        selecters.clear();
        notifyLoginStatusChange();
        if (mViewPager.findViewWithTag(mViewPager.getCurrentItem()) != null) {
            ((SingleFragment.SinglePage) mViewPager.findViewWithTag(mViewPager.getCurrentItem())).notifyDataSetChanged();
        }
        post(new Runnable() {
            @Override
            public void run() {
                int count = mViewPager.getChildCount();
                for (int i = 0; i < count; i++) {
                    ((SingleFragment.SinglePage) mViewPager.getChildAt(i)).notifyDataSetChanged();
                }
            }
        });
    }

    public void setSelectedAll(boolean isChecked) {
        if (!isChecked && selecters.size() == musics.size()) {
            selecters.clear();
            notifyLoginStatusChange();
        } else if (isChecked) {
            selecters.clear();
            selecters.addAll(musics);
        }
        if (mViewPager.findViewWithTag(mViewPager.getCurrentItem()) != null) {
            ((SingleFragment.SinglePage) mViewPager.findViewWithTag(mViewPager.getCurrentItem())).notifyDataSetChanged();
        }
        post(new Runnable() {
            @Override
            public void run() {
                int count = mViewPager.getChildCount();
                for (int i = 0; i < count; i++) {
                    ((SingleFragment.SinglePage) mViewPager.getChildAt(i)).notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void addSelected(BeanMusic music) {
        if (music == null) return;
        if (selecters == null) selecters = new ArrayList<>();
        if (selecters.contains(music)) return;
        selecters.add(music);
    }

    @Override
    public List<BeanMusic> getAllMusic() {
        return musics;
    }

    @Override
    public List<BeanMusic> getAllSelectedMusic() {
        return selecters;
    }

    @Override
    public void removeSelected(BeanMusic music) {
        if (music == null) return;
        if (selecters == null) selecters = new ArrayList<>();
        selecters.remove(music);
    }

//    class SongPageAdapter extends PagerAdapter {
//        //viewpager中的组件数量
//        @Override
//        public int getCount() {
//            return pageCount;
//        }
//
//        //滑动切换的时候销毁当前的组件
//        @Override
//        public void destroyItem(ViewGroup container, int position,
//                                Object object) {
//            container.removeView((View) object);
//        }
//
//        //每次滑动的时候生成的组件
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            SinglePage singlePage = new SinglePage(getContext());
//            singlePage.setTag(String.valueOf(position));
//            singlePage.showData(musics.subList(position * 12, Math.min((position + 1) * 12, musics.size())));
//            ((ViewPager) container).addView(singlePage);
//            return singlePage;
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return super.getItemPosition(object);
//        }
//    }
}
