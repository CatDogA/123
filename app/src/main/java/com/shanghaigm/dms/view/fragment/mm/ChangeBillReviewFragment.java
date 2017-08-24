package com.shanghaigm.dms.view.fragment.mm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.shanghaigm.dms.model.entity.mm.ChangeBillDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeBillInfoBean;
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.mm.OrderDetailActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.design.R.id.info;


public class ChangeBillReviewFragment extends BaseFragment {
    private static String TAG = "ChangeBillReview";
    private EditText areaSelectEdt, modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt, ckEdt;
    private Button btnQuery;
    private ImageView vpRight, vpLeft;
    private MmPopupWindow areaPopup, modelPopup, statePopup;
    private JSONArray areaArray, modelArray = new JSONArray();

    private TextView pageNumText;
    private RelativeLayout rl_end, rl_back;
    private TextView titleText;
    private LoadingDialog dialog;

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
    private ArrayList<ReviewTable> tables;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mm_fragment_modify, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        pageNumText.setText("页数:" + "0" + "/" + pages);
        titleText.setText(String.format(getActivity().getString(R.string.mm_allocation_change_bill_review)));
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
        initViewPager();
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


        areaSelectEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_AREA, null), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        ArrayList<PopListInfo> areas = new ArrayList<PopListInfo>();
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            areaArray = object.getJSONArray("resultEntity");
                            areas.add(new PopListInfo(""));
                            for (int i = 0; i < areaArray.length(); i++) {
                                areas.add(new PopListInfo(areaArray.getJSONObject(i).getString("org_name")));
                            }
                            areaPopup = new MmPopupWindow(getActivity(), areaSelectEdt, areas, 3);
                            areaPopup.showPopup(areaSelectEdt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj);
                    }
                }));
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
                            models.add(new PopListInfo(""));
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
//
            }
        });
        stateSelectEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {"", "待审核", "已通过", "驳回"};
                ArrayList<PopListInfo> states = new ArrayList<PopListInfo>();
                for (int i = 0; i < strings.length; i++) {
                    states.add(new PopListInfo(strings[i]));
                }
                statePopup = new MmPopupWindow(getActivity(), stateSelectEdt, states, 3);
                statePopup.showPopup(stateSelectEdt);
            }
        });
        reQuery(ckEdt);
        reQuery(customerEdt);
        reQuery(numberEdt);
        reQuery(areaSelectEdt);
        reQuery(modelSelecctEdt);
        reQuery(stateSelectEdt);
    }

    private void requestOrderInfo(final int type) {

        //如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();
            if (tableInfos.get(page).isAdded) {    //满足即取出显示返回
                for (TableInfo tableInfo : tableInfos) {
                    tables.add((ReviewTable) tableInfo.table);
                }
                adapter.notifyDataSetChanged();     //刷新完毕就无需再走下一步
                vp.setAdapter(adapter);
                vp.setCurrentItem(page);
                return;
            }
        }
        dialog.showLoadingDlg();
        String areaText = areaSelectEdt.getText().toString();
        String modelText = modelSelecctEdt.getText().toString();
        String stateText = stateSelectEdt.getText().toString();

        String numberText = numberEdt.getText().toString();
        String ckText = ckEdt.getText().toString();
        String customerText = customerEdt.getText().toString();

        Object orgCode = null, modelId = null, stateId = null;
        if (!areaText.equals("")) {
            orgCode = getParam(areaArray, areaText, "org_name", "org_code");
        } else {
            orgCode = null;
        }
        if (!modelText.equals("")) {
            modelId = getParam(modelArray, modelText, "models_name", "models_Id");
        } else {
            modelId = null;
        }
        JSONObject paramObject = new JSONObject();
        JSONArray paramArray = new JSONArray();
        try {
            paramObject.put("order_number", numberText);
            paramObject.put("customer_name", customerText);
            paramObject.put("models_Id", modelId);
            paramObject.put("org_code", orgCode);
            paramObject.put("user_name", ckText);
            paramObject.put("audit_status", getState(stateText) + "");
            paramArray.put(paramObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "requestOrderInfo: params    " + orgCode + "   " + modelId + "      " + stateId);
        Map<String, Object> params = new HashMap<>();
        params.put("cls", paramArray.toString());
        params.put("page", page + 1 + "");
        params.put("rows", "7");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_QUERY_CHANGE_BILL_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONObject object = (JSONObject) responseObj;
                JSONArray jsonRows = null;
                try {
                    JSONObject resultEntity = object.getJSONObject("resultEntity");
                    jsonRows = resultEntity.getJSONArray("rows");

                    ChangeBillInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ChangeBillInfoBean.class);
                    List<ChangeBillInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                    int total = info.resultEntity.total;
                    if (total == 0) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                        tables.clear();
                        tableInfos.clear();
                        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 4);
                        tables.add(table);
                    }
                    if (total % 7 == 0) {
                        pages = total / 7;
                    } else {
                        pages = total / 7 + 1;
                    }
                    ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                    for (int i = 0; i < rows.size(); i++) {
                        ChangeBillInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                        String customerName = orderInfo.customer_name;
                        String model = orderInfo.models_name;
                        String orderNumber = orderInfo.config_change_sheet_number;
                        String state = orderInfo.state;
                        int orderId = orderInfo.order_id;
                        JSONArray flow_details = jsonRows.getJSONObject(i).getJSONArray("flowdetails");
                        int flow_details_id = flow_details.getJSONObject(flow_details.length() - 1).getInt("flow_details_id");
                        String examination_result = orderInfo.examination_resul;
                        Log.i(TAG, "onSuccess: " + state + "," + model + "," + orderNumber + "," + customerName);
                        ChangeBillDetailInfo changeBillDetailInfo = new ChangeBillDetailInfo(orderInfo);
                        paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, getActivity(), 4, changeBillDetailInfo, examination_result, flow_details_id));

                    }
                    ReviewTable table = new ReviewTable(getActivity(), paperInfos, 4);
                    table.setLayoutParams(lp);
                    //加入infos,更新数据
                    if (total != 0) {
                        tables.clear();
                        //加入空的tables占位
                        if (type == 1) {
                            tableInfos.clear();
                            for (int i = 0; i < pages; i++) {
                                tableInfos.add(new TableInfo(pages, new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 4), false));
                            }
                            isQuery = true;
                        }
                        tableInfos.remove(page);
                        tableInfos.add(page, new TableInfo(page, table, true));
                        for (TableInfo tableInfo : tableInfos) {
                            tables.add((ReviewTable) tableInfo.table);
                        }
                    }
                    Log.i(TAG, "onSuccess:size          " + tables.size() + "     tableinfos       " + tableInfos.size());
                    adapter.notifyDataSetChanged();
                    vp.setAdapter(adapter);
                    Log.i(TAG, "onSuccess:page          " + page);
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
                Log.i(TAG, "onFailure: " + reasonObj.toString());
            }
        }));
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

    private int getState(String state) {
        int audit_status = 0;
        if (state.equals("待审核")) {
            audit_status = -1;
        }
        if (state.equals("已通过")) {
            audit_status = 1;
        }
        if (state.equals("驳回")) {
            audit_status = 2;
        }
        return audit_status;
    }

    private void setPages(int page, int pages) {
        pageNumText.setText("页数:" + page + "/" + pages);
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        //添加空的table
        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 4);
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

    private void initView(View v) {
        pageNumText = (TextView) v.findViewById(R.id.pages_num);
        vp = (WrapHeightViewPager) v.findViewById(R.id.order_review_viewpager);
        btnQuery = (Button) v.findViewById(R.id.mm_query_button);
        vpLeft = (ImageView) v.findViewById(R.id.viewpager_left);
        vpRight = (ImageView) v.findViewById(R.id.viewpager_right);
        areaSelectEdt = (EditText) v.findViewById(R.id.mm_area_edt);
        modelSelecctEdt = (EditText) v.findViewById(R.id.mm_model_edt);
        stateSelectEdt = (EditText) v.findViewById(R.id.mm_state_edt);
        numberEdt = (EditText) v.findViewById(R.id.mm_number_edt);
        customerEdt = (EditText) v.findViewById(R.id.mm_client_edt);
        ckEdt = (EditText) v.findViewById(R.id.mm_ck_edt);
        titleText = (TextView) v.findViewById(R.id.title_text);
        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) v.findViewById(R.id.rl_out);
        dialog = new LoadingDialog(getActivity(), "正在加载");
        app = DmsApplication.getInstance();
        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);
    }
}
