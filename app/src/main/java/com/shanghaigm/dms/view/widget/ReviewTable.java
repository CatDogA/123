package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.activity.ck.AreaComAddActivity;
import com.shanghaigm.dms.view.activity.ck.CustomFileAddActivity;
import com.shanghaigm.dms.view.activity.ck.DayReportAddActivity;
import com.shanghaigm.dms.view.activity.ck.MonthReportAddActivity;
import com.shanghaigm.dms.view.activity.ck.WeekReportAddActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.adapter.NotSlideMenuAdpter;
import com.shanghaigm.dms.view.adapter.SlideMenuAdapter;

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
    public static String TASK_TYPE = "task_type";
    public static String TASK_ID = "task_id";
    public static String TASK_INFO = "task_info";
    public static String STATE_ID = "state_id";
    private int flag;       //1,订单审核，2，合同审核，3，更改函，4，更改单，5，订单提交，6，更改函提交,7.日报查询,8.日报提交,9.客户文档
    //10.月提，11.周提，12.日提,13.区域竞品提，14.月审，15.周审，16.日审，17.区域竞品审核
    private RelativeLayout rl_order, rl_contract, rl_change_letter, rl_change_bill, rl_change_letter_sub, rl_report_sub,
            rl_report, rl_custom_file, rl_area_com_sub, rl_month, rl_week, rl_day, rl_month_review, rl_week_review, rl_day_review, rl_area_com_review;
    private SwipeMenuListView swipeMenuListView;
    private RelativeLayout rl_swipe_list;
    private NotSlideMenuAdpter notSlideMenuAdpter;

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
        //侧滑删除
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            /**
             * @param menu
             */
            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        Log.i("review", "create:getViewType      " + menu.getViewType());
                        switch (flag) {
                            case 10:                       //提交
                            case 11:
                            case 12:
                            case 13:
                                // create "open" item
                                SwipeMenuItem subItem = new SwipeMenuItem((context).getApplicationContext());
                                subItem.setBackground(R.color.titleBackColor);
                                // set item width
                                subItem.setWidth(dp2px(50));
                                subItem.setTitle("提交");
                                subItem.setTitleColor(R.color.colorWhite);
                                subItem.setTitleColor(R.color.colorWhite);
                                subItem.setTitleSize(14);
                                // add to menu
                                menu.addMenuItem(subItem);
                                // set item background
                                SwipeMenuItem deleteItem = new SwipeMenuItem((context).getApplicationContext());
                                deleteItem.setBackground(R.color.colorRedText);
                                // set item width
                                deleteItem.setWidth(dp2px(50));
                                deleteItem.setTitle("删除");
                                deleteItem.setTitleColor(R.color.colorWhite);
                                deleteItem.setTitleColor(R.color.colorWhite);
                                deleteItem.setTitleSize(14);
                                // add to menu
                                menu.addMenuItem(deleteItem);
                                break;
                            case 14:           //审核
                            case 15:
                            case 16:
                                // create "open" item
                                SwipeMenuItem passItem = new SwipeMenuItem((context).getApplicationContext());
                                passItem.setBackground(R.color.colorGreen);
                                // set item width
                                passItem.setWidth(dp2px(50));
                                passItem.setTitle("通过");
                                passItem.setTitleColor(R.color.colorWhite);
                                passItem.setTitleColor(R.color.colorWhite);
                                passItem.setTitleSize(14);
                                // add to menu
                                menu.addMenuItem(passItem);
                                // create "open" item
                                SwipeMenuItem rejectItem = new SwipeMenuItem((context).getApplicationContext());
                                rejectItem.setBackground(R.color.colorRedText);
                                // set item width
                                rejectItem.setWidth(dp2px(50));
                                rejectItem.setTitle("驳回");
                                rejectItem.setTitleColor(R.color.colorWhite);
                                rejectItem.setTitleColor(R.color.colorWhite);
                                rejectItem.setTitleSize(14);
                                // add to menu
                                menu.addMenuItem(rejectItem);
                                break;
                            case 17:
                                // create "open" item
                                SwipeMenuItem remove = new SwipeMenuItem((context).getApplicationContext());
                                remove.setBackground(R.color.text);
                                // set item width
                                remove.setWidth(dp2px(50));
                                remove.setTitle("作废");
                                remove.setTitleColor(Color.WHITE);
                                remove.setTitleSize(14);
                                // add to menu
                                menu.addMenuItem(remove);
                                break;
                        }
                        break;
                }
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {   //scan
                Intent intent = null;
                Bundle bundle = new Bundle();
                switch (flag) {
                    case 9:  //custom_file
                        intent = new Intent(context, CustomFileAddActivity.class);
                        bundle.putInt(TASK_TYPE, 9);
                        bundle.putInt(TASK_ID, papers.get(position).getId());
                        break;
                    case 10:   // month
                        intent = new Intent(context, MonthReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID,papers.get(position).getInfo4());
                        break;
                    case 11:   // week update
                        intent = new Intent(context, WeekReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID, papers.get(position).getInfo4());
                        break;
                    case 12:
                        intent = new Intent(context, DayReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID, papers.get(position).getInfo4());
                        break;
                    case 13:
                        intent = new Intent(context, AreaComAddActivity.class);
                        bundle.putString(AreaComAddActivity.WHERE_FROM, "modify");
                        bundle.putInt(TASK_ID, papers.get(position).getId());
                        bundle.putString(STATE_ID, papers.get(position).getInfo4());
                        break;
                    case 14:   // month review
                        intent = new Intent(context, MonthReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID, "-1");
                        break;
                    case 15:
                        intent = new Intent(context, WeekReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID, -1 + "");
                        break;
                    case 16:
                        intent = new Intent(context, DayReportAddActivity.class);
                        bundle.putSerializable(TASK_INFO, papers.get(position));
                        bundle.putString(STATE_ID, -1 + "");
                        break;
                    case 17:     //area_com
                        intent = new Intent(context, AreaComAddActivity.class);
                        bundle.putString(AreaComAddActivity.WHERE_FROM, "custom_info");
                        bundle.putInt(TASK_ID, papers.get(position).getId());
                        bundle.putString(STATE_ID, papers.get(position).getInfo4());    //to know is modify or review
                        break;
                }
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            /**
             * @param position
             * @param menu
             * @param index
             * @return
             */
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                EnsureDeletePopUpWindow pop = null;
                switch (flag) {
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        switch (index) {
                            case 0:        //month sub
                                Log.i("swipeMenuListView", "onMenuItemClick: " + "提交");
                                switch (flag) {
                                    case 10:
                                        pop = new EnsureDeletePopUpWindow(context, 7, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 11:
                                        pop = new EnsureDeletePopUpWindow(context, 12, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 12:
                                        pop = new EnsureDeletePopUpWindow(context, 15, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 13:
                                        pop = new EnsureDeletePopUpWindow(context, 4, papers.get(position).getId(), -1, -1);
                                        break;
                                }
                                break;
                            case 1:      //month delete
                                Log.i("swipeMenuListView", "onMenuItemClick: " + "删除");
                                switch (flag) {
                                    case 10:
                                        pop = new EnsureDeletePopUpWindow(context, 8, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 11:
                                        pop = new EnsureDeletePopUpWindow(context, 11, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 12:
                                        pop = new EnsureDeletePopUpWindow(context, 16, papers.get(position).getId(), -1, -1);
                                        break;
                                    case 13:
                                        pop = new EnsureDeletePopUpWindow(context, 5, papers.get(position).getId(), -1, -1);
                                        break;
                                }
                                break;
                        }
                        break;
                    case 14:
                    case 15:
                    case 16:
                        switch (index) {
                            case 0:
                                Log.i("swipeMenuListView", "onMenuItemClick: " + "通过");
                                switch (flag) {
                                    case 14:
                                        pop = new EnsureDeletePopUpWindow(context, 9, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                    case 15:
                                        pop = new EnsureDeletePopUpWindow(context, 13, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                    case 16:
                                        pop = new EnsureDeletePopUpWindow(context, 17, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                }
                                break;
                            case 1:
                                Log.i("swipeMenuListView", "onMenuItemClick: " + "驳回");
                                switch (flag) {
                                    case 14:
                                        pop = new EnsureDeletePopUpWindow(context, 10, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                    case 15:
                                        pop = new EnsureDeletePopUpWindow(context, 14, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                    case 16:
                                        pop = new EnsureDeletePopUpWindow(context, 18, papers.get(position).getId(), papers.get(position).getId2(), papers.get(position).getId3());
                                        break;
                                }
                                break;
                        }
                        break;
                    case 17:
                        switch (index) {
                            case 0:
                                Log.i("swipeMenuListView", "onMenuItemClick: " + "作废");
                                switch (flag) {
                                    case 17:
                                        pop = new EnsureDeletePopUpWindow(context, 6, papers.get(position).getId(), -1, -1);
                                        break;
                                }

                                break;
                        }
                        break;
                }
                pop.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                View layout = LayoutInflater.from(context).inflate(R.layout.activity_center, null, false);
                pop.showPopup(layout, pop.getContentView().getMeasuredWidth());
                return true;
            }
        });
        if (flag == 1 || flag == 2 || flag == 3 || flag == 4 || flag == 5 || flag == 6 || flag == 7 || flag == 8) {
            lv.setVisibility(View.VISIBLE);
        }
        //测试
        ArrayList<PaperInfo> data = new ArrayList<>();
        data.add(new PaperInfo());
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
            //日报审核
            case 7:
                rl_report.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_report_review, BR.paper, papers);
                break;
            //日报提交
            case 8:
                rl_report_sub.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_report_sub, BR.paper, papers);
                break;
            //客户文档
            case 9:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_custom_file.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(context, R.layout.list_item_custom_file, BR.paper, papers);
                break;
            //月报管理
            case 10:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_month.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_month_report, BR.paper, papers);
                break;
            //周报管理
            case 11:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_week.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_month_report, BR.paper, papers);
                break;
            //日报管理
            case 12:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_day.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_day_report, BR.paper, papers);
                break;
            //区域竞品管理
            case 13:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_area_com_sub.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_area_com, BR.paper, papers);
                break;
            //月报审核
            case 14:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_month_review.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_month_report_review, BR.paper, papers);
                break;
            //周报审核
            case 15:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_week_review.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_month_report_review, BR.paper, papers);
                break;
            //日报审核
            case 16:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_day_review.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_day_review, BR.paper, papers);
                break;
            //区域竞品审核
            case 17:
                rl_swipe_list.setVisibility(View.VISIBLE);
                rl_area_com_review.setVisibility(View.VISIBLE);
                notSlideMenuAdpter = new NotSlideMenuAdpter(context, R.layout.list_item_area_com_review, BR.paper, papers);
                break;
        }
        lv.setAdapter(adapter);
        if (flag == 1 || flag == 2 || flag == 3 || flag == 4 || flag == 5 || flag == 6 || flag == 7 || flag == 8 || flag == 9) {
            swipeMenuListView.setAdapter(adapter);
        } else {
            swipeMenuListView.setAdapter(notSlideMenuAdpter);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.review_list_view);
        rl_order = (RelativeLayout) view.findViewById(R.id.order_table_title);
        rl_contract = (RelativeLayout) view.findViewById(R.id.contract_table_title);
        rl_change_bill = (RelativeLayout) view.findViewById(R.id.change_bill_table_title);
        rl_change_letter = (RelativeLayout) view.findViewById(R.id.change_letter_table_title);
        rl_change_letter_sub = (RelativeLayout) view.findViewById(R.id.change_letter_sub_table_title);
        rl_report = (RelativeLayout) view.findViewById(R.id.report_review_table_title);
        rl_report_sub = (RelativeLayout) view.findViewById(R.id.report_sub_table_title);
        rl_custom_file = (RelativeLayout) view.findViewById(R.id.custom_file_title);
        swipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.swipe_list);
        rl_swipe_list = (RelativeLayout) view.findViewById(R.id.rl_swipe_list);
        rl_month = (RelativeLayout) view.findViewById(R.id.rl_month_title);
        rl_week = (RelativeLayout) view.findViewById(R.id.rl_week_title);
        rl_day = (RelativeLayout) view.findViewById(R.id.rl_day_title);
        rl_month_review = (RelativeLayout) view.findViewById(R.id.rl_month_review_title);
        rl_week_review = (RelativeLayout) view.findViewById(R.id.rl_week_review_title);
        rl_day_review = (RelativeLayout) view.findViewById(R.id.rl_day_review_title);
        rl_area_com_review = (RelativeLayout) view.findViewById(R.id.rl_area_com_title);
        rl_area_com_sub = (RelativeLayout) view.findViewById(R.id.rl_area_sub_title);
    }

    private void AllGone() {
        rl_order.setVisibility(View.GONE);
        rl_contract.setVisibility(View.GONE);
        rl_change_letter.setVisibility(View.GONE);
        rl_change_bill.setVisibility(View.GONE);
        rl_change_letter_sub.setVisibility(View.GONE);
        rl_report.setVisibility(View.GONE);
        rl_report_sub.setVisibility(View.GONE);
        rl_custom_file.setVisibility(View.GONE);
        rl_month.setVisibility(View.GONE);
        rl_week.setVisibility(View.GONE);
        rl_day.setVisibility(View.GONE);
        rl_month_review.setVisibility(View.GONE);
        rl_week_review.setVisibility(View.GONE);
        rl_day_review.setVisibility(View.GONE);
        rl_area_com_review.setVisibility(View.GONE);
        rl_area_com_sub.setVisibility(View.GONE);
    }
}
