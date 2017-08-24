package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.chumi.widget.http.okhttp.RequestParams;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.activity.ck.OrderModifyActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderReview2Fragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderSubFragment extends BaseFragment {
    private static OrderSubFragment fragment;
    private EditText modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt;
    private TextView title;
    private RelativeLayout rl_end, rl_back;
    private Button btnQuery, addBtn;
    private ImageView vpRight, vpLeft;
    private TextView pageNumText;
    private MmPopupWindow modelPopup, statePopup;
    private JSONArray modelArray, stateArray = new JSONArray();
    private static String TAG = "OrderSubFragment";
    private LoadingDialog dialog;

    private ArrayList<TableInfo> tableInfos;
    private ImageView img_first, img_last;
    private ViewPager vp;
    private TablePagerAdapter adapter;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private ArrayList<ReviewTable> tables;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ck_fragment_order, container, false);
        initView(v);
        setUpView();
        return v;
    }

    public void refresh() {
        if (tableInfos != null) {
            tableInfos.clear();
        }
        page = 0;
        requestOrderInfo(1);
    }

    private void setUpView() {
        isQuery = false;
        initViewPager();
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp();
            }
        });
        vpLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > 0 && isQuery) {   //已查询，且不为第一页
                    page--;
                    requestOrderInfo(4);
                    setPages(page + 1, pages);
                }
            }
        });
        vpRight.setOnClickListener(new View.OnClickListener() {
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
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                if (tableInfos != null) {
                    tableInfos.clear();
                }
                requestOrderInfo(1);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setMatchingBeanArrayList(null);
                goToActivity(OrderAddActivity.class);
            }
        });
        modelSelecctEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_GET_MODELS, null), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        try {
                            dialog.dismissLoadingDlg();
                            ArrayList<PopListInfo> models = new ArrayList<>();
                            JSONObject object = new JSONObject(responseObj.toString());
                            modelArray = object.getJSONArray("resultEntity");
                            for (int i = 0; i < modelArray.length(); i++) {
                                models.add(new PopListInfo(modelArray.getJSONObject(i).getString("models_name")));
                            }
                            modelPopup = new MmPopupWindow(getActivity(), modelSelecctEdt, models, 3);
                            modelPopup.showPopup(modelSelecctEdt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                }));
            }
        });
        stateSelectEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                RequestParams params = new RequestParams();
                params.put("filed", "parts_bill_state");
                CommonOkHttpClient.get(CommonRequest.createGetRequest(Constant.URL_GET_STATE, params), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess: state" + responseObj.toString());
                        ArrayList<PopListInfo> states = new ArrayList<PopListInfo>();
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            stateArray = object.getJSONArray("resultEntity");
                            for (int i = 0; i < stateArray.length(); i++) {
                                states.add(new PopListInfo(stateArray.getJSONObject(i).getString("date_value")));
                            }
                            statePopup = new MmPopupWindow(getActivity(), stateSelectEdt, states, 3);
                            statePopup.showPopup(stateSelectEdt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                }));
            }
        });
        reQuery(customerEdt);
        reQuery(numberEdt);
        reQuery(modelSelecctEdt);
        reQuery(stateSelectEdt);
    }

    public static OrderSubFragment getInstance() {
        if (fragment == null) {
            fragment = new OrderSubFragment();
        }
        return fragment;
    }

    /**
     * @param type
     * 判断是否有数据，没有，则请求数据并刷新
     */
    private void requestOrderInfo(final int type) {
//        如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();    //先把tables刷新
            if (tableInfos.get(page).isAdded) {    //满足已有即立即取出显示返回
                for (TableInfo tableInfo : tableInfos) {
                    tables.add((ReviewTable) tableInfo.table);
                }
                adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                vp.setCurrentItem(page);
                return;
            }
        }
        dialog.showLoadingDlg();
        String modelText = modelSelecctEdt.getText().toString();
        String stateText = stateSelectEdt.getText().toString();
        String numberText = numberEdt.getText().toString();
        String customerText = customerEdt.getText().toString();

        Object orgCode = null, modelId = null, stateId = null;

        if (!modelText.equals("")) {
            modelId = getParam(modelArray, modelText, "models_name", "models_Id");
        } else {
            modelId = null;
        }
        if (!stateText.equals("")) {
            stateId = getParam(stateArray, stateText, "date_value", "date_key");
        } else {
            stateId = null;
        }
        JSONObject paramObject = new JSONObject();
        JSONArray paramArray = new JSONArray();
        try {
            paramObject.put("order_number", numberText);
            paramObject.put("customer_name", customerText);
            paramObject.put("models_Id", modelId);
            paramObject.put("state", stateId);
            paramArray.put(paramObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("order", paramArray.toString());
        params.put("page", (page + 1) + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_QUERY_ORDER_SUB_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess:responseObj      " + responseObj.toString());
                dialog.dismissLoadingDlg();
                OrderQueryInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), OrderQueryInfoBean.class);
                List<OrderQueryInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                int total = info.resultEntity.total;

                if (total % 8 == 0) {
                    pages = total / 8;
                } else {
                    pages = total / 8 + 1;
                }
                if (total == 0) {
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    tables.clear();
                    tableInfos.clear();
                    ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 5);
                    tables.add(table);
                }
                if (total != 0) {
                    ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                    for (int i = 0; i < rows.size(); i++) {
                        OrderQueryInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                        String customerName = orderInfo.customer_name;
                        String model = orderInfo.models_name;
                        String orderNumber = orderInfo.order_number;
                        String state = orderInfo.state;
                        int orderId = orderInfo.order_id;
//                    Log.i(TAG, "onSuccess: " + state + "," + model + "," + orderNumber + "," + customerName);
                        paperInfos.add(new PaperInfo(customerName, orderNumber, model, state, true, orderId, getActivity(), 5));
                    }
                    //得到table
                    ReviewTable table = new ReviewTable(getActivity(), paperInfos, 5);
                    table.setLayoutParams(lp);
                    //加入infos,更新数据
                    //加入空的tables占位
                    tables.clear();       //清空
                    if (type == 1) {
                        tableInfos.clear();
                        for (int i = 0; i < pages; i++) {
                            tableInfos.add(new TableInfo(pages, new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 5), false));
                        }
                        isQuery = true;
                    }
                    tableInfos.remove(page);      //除去空的
                    tableInfos.add(page, new TableInfo(page, table, true));   //添加新的
                    for (TableInfo tableInfo : tableInfos) {
                        tables.add((ReviewTable) tableInfo.table);     //整体更新
                    }
                }
                Log.i(TAG, "onSuccess:table.size          " + tables.size() + "     tableinfos       " + tableInfos.size());
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onSuccess:page          " + page);
                vp.setCurrentItem(page);
                //查询时，设置页数
                if (type == 1 && total != 0) {
                    setPages(page + 1, pages);
                }
                if (total == 0) {
                    setPages(0, 0);
                }
            }
            @Override
            public void onFailure(Object reasonObj) {
                Log.i(TAG, "onFailure:       " + reasonObj.toString());
                dialog.dismissLoadingDlg();
            }
        }));
    }

    private Object getParam(JSONArray array, String text, String name, String code) {
        String id = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (text.equals(object.getString(name))) {
                    id = object.getString(code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        //添加空的table
        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 5);
        table.setLayoutParams(lp);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(getActivity(), tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
    }

    private void setPages(int page, int pages) {
        pageNumText.setText("页数:" + page + "/" + pages);
    }

    private void initView(View v) {
        modelSelecctEdt = (EditText) v.findViewById(R.id.mm_model_edt);
        stateSelectEdt = (EditText) v.findViewById(R.id.mm_state_edt);
        numberEdt = (EditText) v.findViewById(R.id.mm_number_edt);
        customerEdt = (EditText) v.findViewById(R.id.mm_client_edt);

        btnQuery = (Button) v.findViewById(R.id.mm_query_button);
        addBtn = (Button) v.findViewById(R.id.mm_add_button);
//        addBtn.setVisibility(View.INVISIBLE);

        vpLeft = (ImageView) v.findViewById(R.id.viewpager_left);
        vpRight = (ImageView) v.findViewById(R.id.viewpager_right);
        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);

        vp = (ViewPager) v.findViewById(R.id.order_review_viewpager);
        dialog = new LoadingDialog(getActivity(), "正在加载");
        app = DmsApplication.getInstance();

        pageNumText = (TextView) v.findViewById(R.id.pages_num);
        title = (TextView) v.findViewById(R.id.title_text);
        title.setText("订单提报");

        rl_end = (RelativeLayout) v.findViewById(R.id.rl_out);
        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
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
