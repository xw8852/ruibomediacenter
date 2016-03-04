package com.msx7.josn.ruibo_mediacenter.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.msx7.josn.ruibo_mediacenter.bean.BeanMoneyDetailInfo;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.ToastUtil;
import com.msx7.josn.ruibo_mediacenter.util.VolleyErrorUtils;
import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectView;
import com.msx7.lib.widget.BaseAdapter;

import java.util.List;

/**
 * 文件名: MonthlyStatementDialog
 * 描  述:对账单
 * 作  者：Josn
 * 时  间：2016/2/27
 */
public class MonthlyStatementDialog extends BaseCustomDialog {

    BaseActivity activity;

    public MonthlyStatementDialog(Context context) {
        super(context);
        activity = (BaseActivity) context;
        getLayoutInflater().inflate(R.layout.layout_dialog_admin_record, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        setTitle("对账单");
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportusertoexcel();
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportuserinmoneytoexcel();
            }
        });
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportmoneydetailtoexcel();
            }
        });
        showMoneyDetail();
    }

    void showMoneyDetail() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETMONEYDETAIL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<List<BeanMoneyDetailInfo>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanMoneyDetailInfo>>>() {
                }.getType());
                activity.dismisProgess();
                if ("200".equals(baseBean.code)) {
                    mListView.setAdapter(new MoneyAdapter(activity, baseBean.data));
                } else {
                    ToastUtil.show(baseBean.msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.dismisProgess();
                ToastUtil.show(VolleyErrorUtils.getError(error));
            }
        }));
    }

    class MoneyAdapter extends BaseAdapter<BeanMoneyDetailInfo> {
        public MoneyAdapter(Context context, List<BeanMoneyDetailInfo> data) {
            super(context, data);
        }


        @Override
        public View createView(int position, View convertView, LayoutInflater inflater) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = inflater.inflate(R.layout.item_admin_record, null);
                Inject.inject(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            BeanMoneyDetailInfo info = getItem(position);
            holder.et1.setText(info.yearandmonth + "");
            holder.et2.setText(info.openusercount + "");
            holder.et3.setText(info.inmoney + "");
            holder.et4.setText(info.outmoney + "");
            return convertView;
        }
    }

    class Holder {
        @InjectView(R.id.tv1)
        TextView et1;
        @InjectView(R.id.tv2)
        TextView et2;
        @InjectView(R.id.tv3)
        TextView et3;
        @InjectView(R.id.tv4)
        TextView et4;
    }

    /**
     * 导出会员列表
     */
    void exportusertoexcel() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_EXPORTUSERTOEXCEL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activity.dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if ("200".equals(BaseBean.class)) {
                            ToastUtil.show("导出会员列表成功");
                            activity.dismisProgess();
                        } else {
                            ToastUtil.show(baseBean.msg);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show(VolleyErrorUtils.getError(error));
                activity.dismisProgess();
            }
        }));
    }

    /**
     * 导出充值明细
     */
    void exportuserinmoneytoexcel() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_EXPORTUSERINMONEYTOEXCEL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activity.dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if ("200".equals(BaseBean.class)) {
                            ToastUtil.show("导出充值明细");
                            activity.dismisProgess();
                        } else {
                            ToastUtil.show(baseBean.msg);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show(VolleyErrorUtils.getError(error));
                activity.dismisProgess();
            }
        }));
    }

    /**
     * 导出对账单
     */
    void exportmoneydetailtoexcel() {
        activity.showProgess();
        RuiBoApplication.getApplication().runVolleyRequest(new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_EXPORTMONEYDETAILTOEXCEL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activity.dismisProgess();
                        BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                        if ("200".equals(BaseBean.class)) {
                            ToastUtil.show("导出对账单");
                            activity.dismisProgess();
                        } else {
                            ToastUtil.show(baseBean.msg);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show(VolleyErrorUtils.getError(error));
                activity.dismisProgess();
            }
        }));
    }

    @InjectView(R.id.ListView)
    ListView mListView;

    @InjectView(R.id.btn1)
    View mBtn1;

    @InjectView(R.id.btn2)
    View mBtn2;

    @InjectView(R.id.btn3)
    View mBtn3;
}
