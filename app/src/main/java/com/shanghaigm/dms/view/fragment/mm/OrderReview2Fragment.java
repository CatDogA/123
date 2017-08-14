package com.shanghaigm.dms.view.fragment.mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class OrderReview2Fragment extends BaseFragment {
    private ArrayList<TableInfo> tableInfos;
    private Button btn_query;
    private ImageView img_first, img_last, img_left, img_right;
    private WrapHeightViewPager vp;
    private TablePagerAdapter adapter;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private LoadingDialog dialog;
    private static String TAG = "OrderReview2Fragment";
    private ArrayList<ReviewTable> tables;
    public static OrderReview2Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_review2, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        initViewPager();
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                requestTable(1);
            }
        });
        img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 0 && isQuery) {   //已查询，且不为第一页
                    page--;
                    requestTable(4);
                }
            }
        });
        img_right.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (page < pages-1 && isQuery) {
                    page++;
                    requestTable(5);
                }
            }
        });
        img_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = 0;
                    requestTable(2);
                }
            }
        });
        img_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = pages - 1;
                    requestTable(3);
                }
            }
        });
    }

    /**
     * @param type 1：查询，2，最左，3，最右，4，向左，5，向右
     */
    private void requestTable(final int type) {
        tables.clear();
        if (type != 1) {       //已经查询过
            if (tableInfos.get(page).isAdded) {    //满足即取出显示返回
                for (TableInfo tableInfo : tableInfos) {
                    tables.add((ReviewTable) tableInfo.table);
                }
                adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                vp.setAdapter(adapter);
                Log.i(TAG, "requestTable: "+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                vp.setCurrentItem(page);
                return;
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page + 1) + "");
        params.put("rows", "7");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_QUERY_ORDER_REVIEW_INFO, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                OrderQueryInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), OrderQueryInfoBean.class);
                List<OrderQueryInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                //获取总页数
                int total = info.resultEntity.total;
                if (total == 0) {
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                //加入空的tables占位
                if (type == 1) {
                    for (int i = 0; i < pages; i++) {
                        tableInfos.add(new TableInfo(pages, new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 1), false));
                    }
                    isQuery = true;
                }
                //获取本页信息
                ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    OrderQueryInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                    String customerName = orderInfo.customer_name;
                    String model = orderInfo.models_name;
                    String orderNumber = orderInfo.order_number;
                    String state = orderInfo.state;
                    int orderId = orderInfo.order_id;
                    String examination_result = orderInfo.examination_result;
                    Log.i(TAG, "onSuccess: " + state + "," + model + "," + orderNumber + "," + customerName);
                    paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, getActivity(), 1, examination_result));
                }
                //得到table
                ReviewTable table = new ReviewTable(getActivity(), paperInfos, 1);
                table.setLayoutParams(lp);
                //加入infos,更新数据
                tableInfos.remove(page);
                tableInfos.add(page, new TableInfo(page, table, true));
                for (TableInfo tableInfo : tableInfos) {
                    tables.add((ReviewTable) tableInfo.table);
                }
                tables.add(table);
                Log.i(TAG, "onSuccess:          " + tables.size());
                adapter.notifyDataSetChanged();
                vp.setAdapter(adapter);
                vp.setCurrentItem(page);
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
            }
        });
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        //添加空的table
        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 1);
        table.setLayoutParams(lp);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(getActivity(), tables);
        vp.setAdapter(adapter);
    }

    private void initView(View v) {
        btn_query = (Button) v.findViewById(R.id.btn_query);
        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_first.setVisibility(View.VISIBLE);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);
        img_last.setVisibility(View.VISIBLE);
        img_left = (ImageView) v.findViewById(R.id.viewpager_left);
        img_right = (ImageView) v.findViewById(R.id.viewpager_right);
        vp = (WrapHeightViewPager) v.findViewById(R.id.order_review_viewpager);
        isQuery = false;
        app = DmsApplication.getInstance();
        dialog = new LoadingDialog(getActivity(), getText(R.string.loading).toString());
    }

    public static OrderReview2Fragment getInstance() {
        if (fragment == null) {
            fragment = new OrderReview2Fragment();
        }
        return fragment;
    }
}
