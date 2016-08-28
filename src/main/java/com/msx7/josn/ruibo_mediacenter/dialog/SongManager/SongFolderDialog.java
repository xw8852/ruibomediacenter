package com.msx7.josn.ruibo_mediacenter.dialog.SongManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.msx7.josn.ruibo_mediacenter.R;
import com.msx7.josn.ruibo_mediacenter.RuiBoApplication;
import com.msx7.josn.ruibo_mediacenter.activity.BaseActivity;
import com.msx7.josn.ruibo_mediacenter.bean.BaseBean;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名: SongFolderDialog
 * 描  述:
 * 作  者：Josn@憬承
 * 时  间：2016/8/27
 */

public class SongFolderDialog extends Dialog {

    BaseActivity activity;

    @InjectView(R.id.radioButton)
    RadioGroup mTabs;

    @InjectView(R.id.print)
    View print;

    @InjectView(R.id.select)
    View select;

    @InjectView(R.id.delete)
    View delete;

    @InjectView(R.id.tip)
    TextView tip;

    @InjectView(R.id.SongFolder2)
    SongFolderView SongFolder2;

    @InjectView(R.id.SongFolder1)
    SongFolderView SongFolder1;

    public SongFolderDialog(Context context) {
        super(context, R.style.Translucent_Dialog);
        if (context instanceof Activity) {
            activity = (BaseActivity) context;
        }
        setContentView(R.layout.layout_admin_song);
        View root = findViewById(R.id.Root);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 4 / 5;
        root.setLayoutParams(params);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Inject.inject(this, findViewById(R.id.Root));
        SongFolder2.setVisibility(View.GONE);
        SongFolder2.setFolderStatus(folderStatus2);
        SongFolder1.setFolderStatus(folderStatus1);
        mTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn1:
                        select.setEnabled(false);
                        SongFolder2.setVisibility(View.GONE);
                        SongFolder1.setVisibility(View.VISIBLE);
                        netAllFolder();
                        break;

                    case R.id.btn2:
                        netNoPrintFolder();
                        select.setEnabled(true);
                        SongFolder2.setVisibility(View.VISIBLE);
                        SongFolder1.setVisibility(View.GONE);
                        break;
                }
            }
        });
        delete.setOnClickListener(delListener);
        print.setOnClickListener(printListener);
        select.setOnClickListener(selectListener);
        netAllFolder();
        select.setEnabled(false);
    }

    View.OnClickListener selectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!v.isSelected()) {
                SongFolder2.resetSelect(true);
                v.setSelected(true);
            } else {
                v.setSelected(false);
                SongFolder2.resetSelect(false);
            }
        }
    };

    View.OnClickListener delListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getSelect().isEmpty()) return;
            List<Long> ids = new ArrayList<>();
            for (BeanSongFolder folder : getSelect()) {
                ids.add(folder.id);
            }
            BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_deleteMusicCategory(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            refresh();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    refresh();
                }
            });
            request.addRequestJson(new Gson().toJson(ids));
            RuiBoApplication.getApplication().runVolleyRequest(request);
        }
    };

    View.OnClickListener printListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getSelect().isEmpty()) return;
            PrintPost printPost = new PrintPost();
            for (BeanSongFolder folder : getSelect()) {
                printPost.TypeIds.add(folder.id + "");
            }
            BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_printMusicCategoryList(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            refresh();
                            ToastUtil.show("请确保打印机正常连接，开始打印");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    refresh();
                }
            });
            request.addRequestJson(new Gson().toJson(printPost));
            RuiBoApplication.getApplication().runVolleyRequest(request);
        }
    };

    void refresh() {
        if (mTabs.getCheckedRadioButtonId() == R.id.btn1) {
            netAllFolder();
        } else netNoPrintFolder();
        ;
    }

    List<BeanSongFolder> getSelect() {
        if (mTabs.getCheckedRadioButtonId() == R.id.btn1) {
            return SongFolder1.getAllSelected();
        } else return SongFolder2.getAllSelected();
    }

    SongFolderView.IFolderStatus folderStatus1 = new SongFolderView.IFolderStatus() {
        @Override
        public void change() {
            tip.setText((SongFolder1.getCurrentItem() + 1) + "/" + SongFolder1.getAdapter().getCount()
                    + " 页 \t 共" + SongFolder1.getAllFolder().size() + "个目录");
        }
    };
    SongFolderView.IFolderStatus folderStatus2 = new SongFolderView.IFolderStatus() {
        @Override
        public void change() {

            tip.setText((SongFolder2.getCurrentItem() + 1) + "/" + SongFolder2.getAdapter().getCount()
                    + " 页 \t 共" + SongFolder2.getAllFolder().size() + "个目录");

            if (SongFolder2.getAllFolder().size() > SongFolder2.getAllSelected().size()) {
                select.setSelected(false);
            }
        }
    };

    public void showProgess() {
        activity.showProgess();
    }


    public void dismisProgess() {
        activity.dismisProgess();
    }

    class PrintPost {

        /**
         * Number : 1
         * TypeIds : ["1012"]
         */

        @SerializedName("Number")
        public int Number = 1;

        public PrintPost() {
            Number = 1;
            TypeIds = new ArrayList<>();
        }

        @SerializedName("TypeIds")
        public List<String> TypeIds;
    }

    void netAllFolder() {
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_getMusicCategoryList(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseBean<List<BeanSongFolder>> baseBean =
                                new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanSongFolder>>>() {
                                }.getType());
                        if ("200".equals(baseBean.code)) {
                            SongFolder1.setData(baseBean.data);
                        }
                        if (mTabs.getCheckedRadioButtonId() == R.id.btn1) {
                            tip.setText((SongFolder1.getCurrentItem() + 1) + "/" + SongFolder1.getAdapter().getCount()
                                    + " 页 \t 共" + SongFolder1.getAllFolder().size() + "个目录");
                        }
                    }
                }, null));
    }

    void netNoPrintFolder() {
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_getNoPrintMusicCategoryList(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<List<BeanSongFolder>> baseBean =
                        new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanSongFolder>>>() {
                        }.getType());
                if ("200".equals(baseBean.code)) {
                    SongFolder2.setData(baseBean.data);
                }
                if (mTabs.getCheckedRadioButtonId() == R.id.btn2) {
                    tip.setText((SongFolder2.getCurrentItem() + 1) + "/" + SongFolder2.getAdapter().getCount()
                            + " 页 \t 共" + SongFolder2.getAllFolder().size() + "个目录");
                }
            }
        }, null));
    }
}
