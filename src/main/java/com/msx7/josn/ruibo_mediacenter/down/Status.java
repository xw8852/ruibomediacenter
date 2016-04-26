package com.msx7.josn.ruibo_mediacenter.down;

/**
 * 文件名: Statis
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/4/24
 */
public class Status {
    public String url;
    public String name;
    public String toPath;
    public boolean isSuccess;

    public Status() {
    }

    public Status(String url, String toPath) {
        this.url = url;
        this.toPath = toPath;
    }

    public Status(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Status(String url, String toPath, boolean isSuccess) {
        this.url = url;
        this.toPath = toPath;
        this.isSuccess = isSuccess;
    }
}
