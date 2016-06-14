package com.msx7.josn.ruibo_mediacenter.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 文件名: SearchFragment
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/6/2
 */
public class CollectionFragment extends Fragment {

    public CollectionView mCollectionView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCollectionView = new CollectionView(inflater.getContext(), null);

        return mCollectionView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCollectionView.mSongPageView.setFragmentManager(getChildFragmentManager());

        mCollectionView.showData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCollectionView.showData();
    }
}
