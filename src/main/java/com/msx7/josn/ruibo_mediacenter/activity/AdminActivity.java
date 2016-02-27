package com.msx7.josn.ruibo_mediacenter.activity;

import android.os.Bundle;
import android.view.View;

import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.dialog.AdminDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.OpenAccountDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.SyncSongDialog;
import com.msx7.josn.ruibo_mediacenter.dialog.UserManagerDialog;

/**
 * 文件名: LoginManager
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/16
 */
public class AdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 开通账号
     *
     * @param v
     */
    public void onOpen(View v) {
        new OpenAccountDialog(v.getContext()).show();
    }

    /**
     * 管理员
     *
     * @param v
     */
    public void onAdmin(View v) {
        new AdminDialog(v.getContext()).show();
    }

    /**
     * 会员管理
     *
     * @param v
     */
    public void onUser(View v) {
        new UserManagerDialog(v.getContext()).show();
    }

    /**
     * 歌曲管理
     *
     * @param v
     */
    public void onSong(View v) {
        new SyncSongDialog(v.getContext()).show();
    }
}
