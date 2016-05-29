package com.msx7.josn.ruibo_mediacenter.activity.ui;

import com.msx7.josn.ruibo_mediacenter.bean.BeanMusic;

import java.util.List;

/**
 * 文件名: IMusic
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/5/28
 */
public interface IMusicSelcted {
    public List<BeanMusic> getAllMusic();

    public List<BeanMusic> getAllSelectedMusic();

    public void addSelected(BeanMusic music);

    public void removeSelected(BeanMusic music);
}
