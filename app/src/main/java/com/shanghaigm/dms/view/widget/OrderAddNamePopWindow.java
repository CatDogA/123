package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.OrderAddSearchInfo;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/6/26.
 * 查询并添加客户基础信息
 */

public class OrderAddNamePopWindow extends PopupWindow {
    public static String GET_INFO = "get_info";
    private OrderAddSearchInfo selectedInfo = new OrderAddSearchInfo();
    private static String TAG = "OrderAddNamePopWindow";
    private TextView txt_page_num;
    private LoadingDialog dialog;
    private EditText edt_name, edt_tel;

    private Context context;
    private Button btnSearch, btnSure;
    private ImageView vp_right, vp_left;
    private Handler mHandler;
    private Handler handler;

    private ArrayList<TableInfo> tableInfos;
    private ImageView img_first, img_last;
    private WrapHeightViewPager vp;
    private TablePagerAdapter adapter;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private ArrayList<SearchTable> tables;

    public OrderAddNamePopWindow(Context context, Handler handler) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.pop_search_info, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.handler = handler;
        this.context = context;
        initView(v);
        setUpView();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                selectedInfo = (OrderAddSearchInfo) bundle.getSerializable(SearchTable.GET_ORDER_ADD_INFO);
            }
        };
    }

    private void setUpView() {
        txt_page_num.setText("页数:" + "0" + "/" + pages);
        initViewPager();
        vp_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 0 && isQuery) {   //已查询，且不为第一页
                    page--;
                    requestOrderInfo(4);
                    setPages(page + 1, pages);
                }
            }
        });
        vp_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < pages - 1 && isQuery) {
                    page++;
                    requestOrderInfo(5);
                    setPages(page + 1, pages);
                }
            }
        });
        img_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = 0;
                    requestOrderInfo(2);
                    setPages(page + 1, pages);
                }
            }
        });
        img_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQuery) {
                    page = pages - 1;
                    requestOrderInfo(3);
                    setPages(page + 1, pages);
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                if (tableInfos != null) {
                    tableInfos.clear();
                }
                requestOrderInfo(1);
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (selectedInfo.getCompany() != null) {
                    bundle.putSerializable(OrderAddNamePopWindow.GET_INFO, selectedInfo);
                    msg.setData(bundle);
                    msg.sendToTarget();
                } else {
                    bundle.putSerializable(OrderAddNamePopWindow.GET_INFO, new OrderAddSearchInfo("", "", "", "", "", -1, "", ""));
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
                hidePopup();
            }
        });
        reQuery(edt_name);
        reQuery(edt_tel);
    }

    private void initView(View v) {
        vp = (WrapHeightViewPager) v.findViewById(R.id.vp_search_order_add);
        btnSearch = (Button) v.findViewById(R.id.btn_search);
        btnSure = (Button) v.findViewById(R.id.btn_sure);
        vp_right = (ImageView) v.findViewById(R.id.viewpager_right);
        vp_left = (ImageView) v.findViewById(R.id.viewpager_left);
        txt_page_num = (TextView) v.findViewById(R.id.pages_num);
        dialog = new LoadingDialog(context, "正在加载");
        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_tel = (EditText) v.findViewById(R.id.edt_tel);
        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);
        app = DmsApplication.getInstance();
    }

    private void requestOrderInfo(final int type) {
        //如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();
            if(tableInfos.size()>0){
                if (tableInfos.get(page).isAdded) {    //满足即取出显示返回
                    for (TableInfo tableInfo : tableInfos) {
                        tables.add((SearchTable) tableInfo.table);
                    }
                    adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                    vp.setCurrentItem(page);
                    return;
                }
            }
        }

        dialog.showLoadingDlg();
        String name = edt_name.getText().toString();
        String tel = edt_tel.getText().toString();

        JSONObject paramObject = new JSONObject();
        JSONArray paramArray = new JSONArray();
        try {
            paramObject.put("mobile_phone", tel);
            paramObject.put("customer_name", name);
            paramArray.put(paramObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer", paramArray.toString());
        Log.i(TAG, "requestOrderInfo: " + paramArray.toString());
        params.put("page", page + 1);
        params.put("rows", 10);
        params.put("loginName", app.getAccount());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode() + "        " + page);
        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ADD_ORDER_NAME, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                JSONObject object = (JSONObject) responseObj;
                Log.i(TAG, "onSuccess: " + object.toString());
                try {
                    JSONObject resultEntity = object.getJSONObject("resultEntity");
                    int total = resultEntity.getInt("total");
                    JSONArray rows = resultEntity.getJSONArray("rows");
                    if (total == 0) {
                        Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                        tables.clear();
                        tableInfos.clear();
                        SearchTable table = new SearchTable(context, new ArrayList<OrderAddSearchInfo>(), mHandler);
                        tables.add(table);
                    }
                    if (total % 10 == 0) {
                        pages = total / 10;
                    } else {
                        pages = total / 10 + 1;
                    }

                    ArrayList<OrderAddSearchInfo> searchInfos = new ArrayList<>();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject searchInfo = rows.getJSONObject(i);
                        searchInfos.add(new OrderAddSearchInfo(searchInfo.getString("customer_name"), searchInfo.getString("mobile_phone"), searchInfo.getString("company_name"), searchInfo.getString("sex"), searchInfo.getString("detailed_address"), searchInfo.getInt("customer_code"), searchInfo.getString("address"), searchInfo.getString("fixed_telephone")));
                    }

                    SearchTable table = new SearchTable(context, searchInfos, mHandler);
                    table.setLayoutParams(lp);

                    //加入infos,更新数据
                    if (total != 0) {
                        tables.clear();
                        //加入空的tables占位
                        if (type == 1) {
                            tableInfos.clear();
                            for (int i = 0; i < pages; i++) {
                                tableInfos.add(new TableInfo(pages, new SearchTable(context, new ArrayList<OrderAddSearchInfo>(), mHandler), false));
                            }
                            isQuery = true;
                        }
                        tableInfos.remove(page);
                        tableInfos.add(page, new TableInfo(page, table, true));
                        for (TableInfo tableInfo : tableInfos) {
                            tables.add((SearchTable) tableInfo.table);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    vp.setAdapter(adapter);
                    vp.setCurrentItem(page);
//查询时，设置页数
                    if (type == 1 && total != 0) {
                        setPages(page + 1, pages);
                    }
                    if (total == 0) {
                        setPages(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
                Toast.makeText(context, "无数据", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        //添加空的table
        SearchTable table = new SearchTable(context, new ArrayList<OrderAddSearchInfo>(), mHandler);
        table.setLayoutParams(lp);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(context, tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
    }

    private void setPages(int page, int pages) {
        txt_page_num.setText("页数:" + page + "/" + pages);
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void hidePopup(MmPopupWindow popupWindow) {
        popupWindow.dismiss();
    }

    public void hidePopup() {
        this.dismiss();
    }

    /**
     * Created by Tom on 2017/6/26
     */

    public class TablePagerAdapter extends PagerAdapter {
        private ArrayList<SearchTable> tables;
        private int mChildCount = 0;
        private Context context;

        public TablePagerAdapter(Context context, ArrayList<SearchTable> tables) {
            this.context = context;
            this.tables = tables;
        }

        @Override
        public int getCount() {
            if (tables != null && tables.size() > 0) {
                return tables.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(tables.get(position));
            return tables.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private void reQuery(EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isQuery = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
