package com.shanghaigm.dms.view.activity.mm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.shanghaigm.dms.model.entity.mm.ChangeLetterDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeLetterInfoBean;
import com.shanghaigm.dms.model.entity.mm.ContractDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ContractInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.mm.HomeFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContractReviewOrChangeLetterReviewActivity extends BaseActivity {
    private int flag = 0;
    private String url;
    public static String CONTRACT_REFRESH = "contract_refresh";
    private static String TAG = "ContractReview";
    private EditText areaSelectEdt, modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt, ckEdt;
    private Button btnQuery;
    private ImageView vpRight, vpLeft;
    private MmPopupWindow areaPopup, modelPopup, statePopup;
    private JSONArray areaArray, modelArray;
    private RelativeLayout rl_back, rl_end;
    private TextView pageNumText;
    private TextView titleText;
    private LoadingDialog dialog;

    private ArrayList<TableInfo> tableInfos;
    private ImageView img_first, img_last;
    private WrapHeightViewPager vp;
    private TablePagerAdapter adapter;
    private RelativeLayout.LayoutParams lp;
    private Boolean isQuery = false;        //是否已经查询
    private int page, pages;       //显示页数,总页数
    private DmsApplication app;
    private ArrayList<ReviewTable> tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_review);
        initView();
        setUpView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refresh();
    }

    private void setUpView() {
        areaArray = new JSONArray();
        modelArray = new JSONArray();
        lp = new RelativeLayout.
                LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        pageNumText.setText("页数:" + "0" + "/" + pages);
        if (flag == 2) {
            titleText.setText(String.format(ContractReviewOrChangeLetterReviewActivity.this.getString(R.string.mm_contract_sheet_review)));
        }
        if (flag == 3) {
            titleText.setText(String.format(ContractReviewOrChangeLetterReviewActivity.this.getString(R.string.mm_change_letter_review)));
        }

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
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp();
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
                            areaPopup = new MmPopupWindow(ContractReviewOrChangeLetterReviewActivity.this, areaSelectEdt, areas, 3);
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
                            modelPopup = new MmPopupWindow(ContractReviewOrChangeLetterReviewActivity.this, modelSelecctEdt, models, 3);
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
                String[] strings = {"", "待审核", "已审核", "驳回"};
                ArrayList<PopListInfo> states = new ArrayList<PopListInfo>();
                for (int i = 0; i < strings.length; i++) {
                    states.add(new PopListInfo(strings[i]));
                }
                statePopup = new MmPopupWindow(ContractReviewOrChangeLetterReviewActivity.this, stateSelectEdt, states, 3);
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

    private void refresh() {
        page = 0;
        if (tableInfos != null) {
            tableInfos.clear();
        }
        requestOrderInfo(1);
    }

    private void requestOrderInfo(final int type) {
        Log.i(TAG, "requestOrderInfo:        " + isSoftShowing());
        //如果有，直接显示
        if (type != 1) {       //已经查询过
            tables.clear();
            if (tableInfos.size() > 0) {
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
        RequestParams params = new RequestParams();
        if (flag == 2) {
            params.put("conts", paramArray.toString());
            url = Constant.URL_QUERY_CONTRACT_REVIEW_INFO;
        }
        if (flag == 3) {
            url = Constant.URL_QUERY_CHANGE_LETTER_INFO;
            params.put("cle", paramArray.toString());
        }
        params.put("page", page + 1 + "");
        params.put("rows", "7");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(url, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                JSONObject response = (JSONObject) responseObj;
                try {
                    JSONObject result = response.getJSONObject("resultEntity");
                    JSONArray rowArray = result.getJSONArray("rows");
                    int total = 0;
                    if (flag == 2) {
                        ContractInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ContractInfoBean.class);
                        List<ContractInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                        total = info.resultEntity.total;
                        if (total % 7 == 0) {
                            pages = total / 7;
                        } else {
                            pages = total / 7 + 1;
                        }
                        for (int i = 0; i < rows.size(); i++) {
                            ContractInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                            String customerName = orderInfo.customer_name;
                            String model = orderInfo.models_name;
                            String orderNumber = orderInfo.contract_id;
                            int orderId = orderInfo.order_id;
                            int flow_details_id = 0;
                            if (!rowArray.getJSONObject(i).get("flowdetails").equals("")) {
                                JSONArray flow_details = rowArray.getJSONObject(i).getJSONArray("flowdetails");
                                flow_details_id = flow_details.getJSONObject(flow_details.length() - 1).getInt("flow_details_id");
                            }
                            String examination_result = orderInfo.examination_resul;
                            ContractDetailInfo contractDetailInfo = new ContractDetailInfo(orderInfo.contract_id, orderInfo.company_name, orderInfo.creator_by, orderInfo.delivery_time, orderInfo.deposit, orderInfo.models_name, orderInfo.battery_manufacturer, orderInfo.battery_system, orderInfo.vehicle_number, orderInfo.number, orderInfo.delivery_mode, orderInfo.purpose, orderInfo.seat_number);
                            paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, ContractReviewOrChangeLetterReviewActivity.this, flag, contractDetailInfo, examination_result, flow_details_id));
                        }
                    }
                    if (flag == 3) {
                        ChangeLetterInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ChangeLetterInfoBean.class);
                        List<ChangeLetterInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                        total = info.resultEntity.total;

                        if (total % 7 == 0) {
                            pages = total / 7;
                        } else {
                            pages = total / 7 + 1;
                        }
                        for (int i = 0; i < rows.size(); i++) {
                            ChangeLetterInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                            String customerName = orderInfo.customer_name;
                            String model = orderInfo.models_name;
                            String orderNumber = orderInfo.change_letter_number;
                            int orderId = orderInfo.order_id;
                            String examination_result = orderInfo.examination_resul;
                            int flow_details_id = 0;
                            if (!rowArray.getJSONObject(i).get("flowdetails").equals("")) {
                                JSONArray flow_details = rowArray.getJSONObject(i).getJSONArray("flowdetails");
                                flow_details_id = flow_details.getJSONObject(flow_details.length() - 1).getInt("flow_details_id");
                            }
                            Log.i(TAG, "onSuccess: examination_result   " + examination_result + "     flow_details_id      " + flow_details_id);
                            ChangeLetterDetailInfo changeLetterDetailInfo = new ChangeLetterDetailInfo(orderInfo.contract_id, orderInfo.contract_price, orderInfo.number, orderInfo.models_name, orderInfo.company_name, orderInfo.change_contract_price, orderInfo.config_change_date, orderInfo.contract_delivery_date, orderInfo.config_chang_delivery_date, orderInfo.letter_id);
                            paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, ContractReviewOrChangeLetterReviewActivity.this, flag, changeLetterDetailInfo, examination_result, flow_details_id, orderInfo.letter_id));
                        }
                    }
                    if (total == 0) {
                        Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        tables.clear();
                        tableInfos.clear();
                        ReviewTable table = null;
                        if (flag == 2) {
                            table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 2);
                        }
                        if (flag == 3) {
                            table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 3);
                        }
                        tables.add(table);
                    }
                    ReviewTable table = null;
                    if (flag == 2) {
                        table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, paperInfos, 2);
                        table.setLayoutParams(lp);
                    }
                    if (flag == 3) {
                        table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, paperInfos, 3);
                        table.setLayoutParams(lp);
                    }

                    //加入infos,更新数据
                    if (total != 0) {
                        tables.clear();
                        //加入空的tables占位
                        if (type == 1) {
                            tableInfos.clear();
                            for (int i = 0; i < pages; i++) {
                                if (flag == 2) {
                                    tableInfos.add(new TableInfo(pages, new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 2), false));
                                }
                                if (flag == 3) {
                                    tableInfos.add(new TableInfo(pages, new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 3), false));
                                }
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
                Log.i("luo", "onFailure: " + reasonObj.toString());
            }
        }));
    }


    private int getState(String state) {
        int audit_status = 0;
        if (state.equals("待审核")) {
            audit_status = -1;
        }
        if (state.equals("已审核")) {
            audit_status = 1;
        }
        if (state.equals("驳回")) {
            audit_status = 2;
        }
        return audit_status;
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

    private void setPages(int page, int pages) {
        pageNumText.setText("页数:" + page + "/" + pages);
    }

    private void initViewPager() {
        tableInfos = new ArrayList<>();
        ReviewTable table = null;
        Log.i(TAG, "initViewPager:flag           " + flag);
        //添加空的table
        if (flag == 2) {
            table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 2);
        }
        if (flag == 3) {
            table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, new ArrayList<PaperInfo>(), 3);
        }
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.
                LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        table.setLayoutParams(lp1);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(ContractReviewOrChangeLetterReviewActivity.this, tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
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

    private void initView() {
        pageNumText = (TextView) findViewById(R.id.pages_num);
        vp = (WrapHeightViewPager) findViewById(R.id.order_review_viewpager);
        btnQuery = (Button) findViewById(R.id.mm_query_button);
        vpLeft = (ImageView) findViewById(R.id.viewpager_left);
        vpRight = (ImageView) findViewById(R.id.viewpager_right);
        areaSelectEdt = (EditText) findViewById(R.id.mm_area_edt);
        modelSelecctEdt = (EditText) findViewById(R.id.mm_model_edt);
        stateSelectEdt = (EditText) findViewById(R.id.mm_state_edt);
        numberEdt = (EditText) findViewById(R.id.mm_number_edt);
        customerEdt = (EditText) findViewById(R.id.mm_client_edt);
        ckEdt = (EditText) findViewById(R.id.mm_ck_edt);
        titleText = (TextView) findViewById(R.id.title_text);
        dialog = new LoadingDialog(this, "正在加载");
        flag = getIntent().getIntExtra(HomeFragment.CONTRACT_OR_LETTER, 0);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        img_first = (ImageView) findViewById(R.id.viewpager_first);
        img_last = (ImageView) findViewById(R.id.viewpager_last);
        app = DmsApplication.getInstance();
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    //事件分发，点击其他地方，隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //是否隐藏
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 判断是否应该隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
