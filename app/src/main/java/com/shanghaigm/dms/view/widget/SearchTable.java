package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.model.entity.common.CustomManageInfo;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/26.
 */

public class SearchTable extends LinearLayout {
    public static String GET_ORDER_ADD_INFO = "get_order_add_info";
    private ListView lv;
    private Context context;
    private ArrayList<OrderAddSearchInfo> infos;
    private ListAdapter adapter;
    private OrderAddSearchInfo info = new OrderAddSearchInfo();
    private Handler handler;
    public SearchTable(Context context, ArrayList<OrderAddSearchInfo> infos, Handler handler) {
        super(context);
        this.context = context;
        this.infos = infos;
        this.handler = handler;
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_order_add_name, this, true);
        initView(view);
        setUpView();
    }

    private void setUpView() {
        adapter = new ListAdapter(context, R.layout.list_item_pop_search, BR.info, infos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                info = infos.get(position);
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SearchTable.GET_ORDER_ADD_INFO,info);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        });
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.pop_search_list);
    }
}