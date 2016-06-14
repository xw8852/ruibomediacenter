package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;

/**
 * 文件名: SearchFragment
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/6/2
 */
public class SearchFragment extends Fragment {

    public SearchView mSearchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSearchView = new SearchView(inflater.getContext(), null);
        return mSearchView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView.showEnable(false);
        mSearchView.mSongPageView.setFragmentManager(getChildFragmentManager());
        if (SharedPreferencesUtil.getUserInfo() != null)
            mSearchView.setEnabled(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
            mSearchView.setVisibility(View.GONE);
        else mSearchView.setVisibility(View.VISIBLE);
    }
}
