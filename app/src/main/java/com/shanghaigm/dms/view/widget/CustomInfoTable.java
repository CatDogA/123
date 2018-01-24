package com.shanghaigm.dms.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.model.entity.common.CustomManageInfo;
import com.shanghaigm.dms.view.activity.ck.AreaComAddActivity;
import com.shanghaigm.dms.view.activity.ck.DayReportAddActivity;
import com.shanghaigm.dms.view.activity.ck.WeekReportAddActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/28.
 */

public class CustomInfoTable extends LinearLayout {
    public static String GET_CUSTOM_ID = "get_custom_id";
    public static String GET_CUSTOM_NAME = "get_custom_name";
    private ListView lv;
    private Context context;
    private ArrayList<CustomManageInfo> infos;
    private ListAdapter adapter;
    private int flag;
    public static String GET_CUSTOM_INFO_BACK = "GET_CUSTOM_INFO_BACK";
    public CustomInfoTable(Context context, ArrayList<CustomManageInfo> infos, int flag) {
        super(context);
        this.context = context;
        this.infos = infos;
        this.flag = flag;
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_custom_info, this, true);
        initView(view);
        setUpView();
    }

    private void setUpView() {
        adapter = new ListAdapter(context, R.layout.item_custom_info, BR.info, infos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent infoIntent = null;
                Bundle bundle = new Bundle();
                if (flag == 0) {
                    infoIntent = new Intent(context, AreaComAddActivity.class);
                    bundle.putString(AreaComAddActivity.WHERE_FROM, "custom_info");
                }
                if (flag == 1) {
                    infoIntent = new Intent(context, WeekReportAddActivity.class);
                    bundle.putString(WeekReportAddActivity.GET_CUSTOM_INFO_BACK, "custom_info");
                }
                if (flag == 2) {
                    infoIntent = new Intent(context, DayReportAddActivity.class);
                    bundle.putString(GET_CUSTOM_INFO_BACK, "custom_info");
                }
                if (flag == 3) {
                    infoIntent = new Intent(context, DayReportAddActivity.class);
                    bundle.putString(GET_CUSTOM_INFO_BACK, "custom_info2");
                }
                bundle.putInt(GET_CUSTOM_ID, infos.get(position).getId());
                bundle.putString(GET_CUSTOM_NAME, infos.get(position).getName1());
                infoIntent.putExtras(bundle);
                ((Activity) context).finish();
                context.startActivity(infoIntent);
            }
        });
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.pop_search_list);
    }
}
