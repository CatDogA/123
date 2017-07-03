package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.adapter.ListAdapter;

/**
 * Created by Tom on 2017/6/29.
 */

public class CustomAllocationTable extends LinearLayout{
    private Context context;
    private ListView listView;
    private ListAdapter adapter;
    public CustomAllocationTable(Context context) {
        super(context);
        this.context = context;
        LayoutInflater lf = LayoutInflater.from(context);
        View v= lf.inflate(R.layout.table_custom_allocation, this, true);
        listView = (ListView) v.findViewById(R.id.list_custom_allocation);
//        adapter = new ListAdapter(context,R.layout.include_allocation_add_bar, BR.info,);
    }
}
