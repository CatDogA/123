package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/5/16.
 * 自定义的table，代码中实现，应用时要放到布局中，需要setLayoutParams
 */

public class ReviewTable extends LinearLayout {
    private ListView lv;
    private Context context;
    private ArrayList<PaperInfo> papers;
    private ListAdapter adapter;
    private int flag;       //判断提交和审核
    private boolean type;       //判断是否为更改函查询

    public ReviewTable(Context context, ArrayList<PaperInfo> papers, int flag) {
        super(context);
        this.context = context;
        this.papers = papers;
        this.flag = flag;
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.table_order_review_layout, this, true);
        initView(view);
        setUpView();
    }
    public ReviewTable(Context context, ArrayList<PaperInfo> papers, int flag, boolean type) {
        super(context);
        this.context = context;
        this.papers = papers;
        this.flag = flag;
        this.type = type;
        LayoutInflater lf = LayoutInflater.from(context);
        View view;
        if (type) {
            view = lf.inflate(R.layout.table_change_letter_layout, this, true);

        } else {
            view = lf.inflate(R.layout.table_order_review_layout, this, true);
        }
        initView(view);
        setUpView();
    }

//    public void setInfo(ArrayList<PaperInfo> papers) {
//        this.papers = papers;
//    }

    private void setUpView() {
        if (flag == 1) {
            adapter = new ListAdapter(context, R.layout.list_item_review, BR.paper, papers);
        }
        if (flag == 2) {
            adapter = new ListAdapter(context, R.layout.list_item_sub, BR.paper, papers);
        }
        if (flag == 3) {
            adapter = new ListAdapter(context, R.layout.list_item_change_letter_sub, BR.paper, papers);
        }
        lv.setAdapter(adapter);
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.review_list_view);
    }

}
