package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAddInfo;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChangeLetterSearchTable extends LinearLayout {
    private ListView lv;
    private Context context;
    private ArrayList<ChangeLetterAddInfo> infos;
    private ListAdapter adapter;
    private OrderAddSearchInfo info = new OrderAddSearchInfo();

    public ChangeLetterSearchTable(Context context, ArrayList<ChangeLetterAddInfo> infos) {
        super(context);
        this.context = context;
        this.infos = infos;
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_change_letter_search, this, true);
        initView(view);
        setUpView();
    }

    private void setUpView() {
        adapter = new ListAdapter(context, R.layout.list_item_change_letter_search, BR.info, infos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChangeLetterAddActivity.changeLetterAddInfo = infos.get(position);
            }
        });
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.pop_search_list);
    }
}
