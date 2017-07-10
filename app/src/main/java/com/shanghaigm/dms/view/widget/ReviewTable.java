package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
    private int flag;       //1,订单审核，2，合同审核，3，更改函，4，更改单，5，订单提交，6，更改函提交
    private RelativeLayout rl_order, rl_contract, rl_change_letter, rl_change_bill, rl_change_letter_sub;

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

    private void setUpView() {
        AllGone();
        switch (flag) {
            //订单审核
            case 1:
                rl_order.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_review, BR.paper, papers);
                break;
            //合同审核
            case 2:
                rl_contract.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_review, BR.paper, papers);
                break;
            //更改函审核
            case 3:
                rl_change_letter.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_review, BR.paper, papers);
                break;
            //更改单审核
            case 4:
                rl_change_bill.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_review, BR.paper, papers);
                break;
            //订单提交
            case 5:
                rl_order.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_sub, BR.paper, papers);
                break;
            //更改函提交
            case 6:
                rl_change_letter_sub.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_change_letter_sub, BR.paper, papers);
                break;
        }

        lv.setAdapter(adapter);
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.review_list_view);
        rl_order = (RelativeLayout) view.findViewById(R.id.order_table_title);
        rl_contract = (RelativeLayout) view.findViewById(R.id.contract_table_title);
        rl_change_bill = (RelativeLayout) view.findViewById(R.id.change_bill_table_title);
        rl_change_letter = (RelativeLayout) view.findViewById(R.id.change_letter_table_title);
        rl_change_letter_sub = (RelativeLayout) view.findViewById(R.id.change_letter_sub_table_title);
    }

    private void AllGone() {
        rl_order.setVisibility(View.GONE);
        rl_contract.setVisibility(View.GONE);
        rl_change_letter.setVisibility(View.GONE);
        rl_change_bill.setVisibility(View.GONE);
        rl_change_letter_sub.setVisibility(View.GONE);
    }

}
