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
import com.msx7.josn.ruibo_mediacenter.bean.BeanUserRecord;
import com.msx7.josn.ruibo_mediacenter.common.UrlStatic;
import com.msx7.josn.ruibo_mediacenter.net.BaseJsonRequest;
import com.msx7.josn.ruibo_mediacenter.util.SharedPreferencesUtil;
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
public class RecordListDialog extends BaseCustomDialog {

    BaseActivity activity;
    @InjectView(R.id.ListView)
    ListView mListView;

    public RecordListDialog(Context context) {
        super(context);
        activity = (BaseActivity) context;
        getLayoutInflater().inflate(R.layout.layout_dialog_record, (ViewGroup) findViewById(R.id.content));
        Inject.inject(this, findViewById(R.id.content));
        setTitle("交易记录查询");
        showMoneyDetail();
    }

    void showMoneyDetail() {
        activity.showProgess();
        BaseJsonRequest request = new BaseJsonRequest(Request.Method.POST, UrlStatic.URL_GETUSERACCOUNTDETAIL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BaseBean<List<BeanUserRecord>> baseBean = new Gson().fromJson(response, new TypeToken<BaseBean<List<BeanUserRecord>>>() {
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
        });
        request.addRequestJson(getPostBody());
        RuiBoApplication.getApplication().runVolleyRequest(request);
    }

    String getPostBody() {
        return new Gson().toJson(SharedPreferencesUtil.getUserInfo());
    }

    class MoneyAdapter extends BaseAdapter<BeanUserRecord> {
        public MoneyAdapter(Context context, List<BeanUserRecord> data) {
            super(context, data);
        }


        @Override
        public View createView(int position, View convertView, LayoutInflater inflater) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = inflater.inflate(R.layout.item_user_record, null);
                Inject.inject(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            BeanUserRecord info = getItem(position);
            holder.et1.setText(info.getFormatTime());
            holder.et2.setText(info.type);
            if (info.money > 0) {
                holder.et3.setTextColor(0xFF1bb947);
            } else {
                holder.et3.setTextColor(0xFFff971e);
            }
            holder.et3.setText(info.money + "");
            holder.et4.setText(info.remainmoney + "");
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


}
