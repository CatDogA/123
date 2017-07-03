package com.shanghaigm.dms.view.activity.mm;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
    private static String TAG = "ContractReviewOrChangeLetterReviewActivity";
    private EditText areaSelectEdt, modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt, ckEdt;
    private Button btnQuery;
    private ImageView vpRight, vpLeft;
    private MmPopupWindow areaPopup, modelPopup, statePopup;
    private JSONArray areaArray, modelArray, stateArray = new JSONArray();
    private RelativeLayout rl_back, rl_end;
    private TextView pageNumText;
    private WrapHeightViewPager vp;
    private ArrayList<PaperInfo> papers;//每页数据
    private ArrayList<PaperInfo> paperInfos;
    private int pages = 0;//页数
    private int page = 1;//第几页
    private ArrayList<ReviewTable> tables = new ArrayList<>();//表集合
    private TablePagerAdapter pagerAdapter;
    private TextView titleText;
    private DmsApplication app = DmsApplication.getInstance();
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean IsQuery = true;//判断是查询还是更多
    private Boolean IsMore = false;//判断可否请求更多
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_review);
        initView();
        setUpView();
    }

    private void setUpView() {
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
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem + 1 == pages) {
                        vpRight.setImageResource(R.mipmap.right_switch_pre);
                    }
                    if (currentItem > 0) {
                        vp.setCurrentItem(--currentItem);
                    }
                    if (currentItem == 0) {
                        vpLeft.setImageResource(R.mipmap.left_switch);
                    }
                    setPages(currentItem + 1, pages);
                }
            }
        });
        vpRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = false;
                if (IsMore) {
                    int currentItem = vp.getCurrentItem();
                    if (currentItem == 0) {
                        vpLeft.setImageResource(R.mipmap.left_switch_pre);
                    }
                    Log.i(TAG, "onClick:     pages   " + pages + "        currentItem     " + currentItem);
                    if (currentItem + 2 == pages) {
                        vpRight.setImageResource(R.mipmap.right_switch);
                    }
                    Log.i(TAG, "onClick: " + "currentItem" + currentItem + "     page" + page);

                    if (currentItem + 2 == page) {
                        if (page > pages) {
                            Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "已是最后一页", Toast.LENGTH_SHORT).show();
                            vpRight.setImageResource(R.mipmap.right_switch);
                            return;
                        }
                        requestOrderInfo(IsQuery);
                    } else {
                        vp.setCurrentItem(++currentItem);
                        setPages(currentItem + 1, pages);
                    }
                    Log.i(TAG, "onClick: " + "currentItem" + currentItem + "     page" + page);
                }
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsQuery = true;
                IsMore = true;
                page = 1;
                requestOrderInfo(IsQuery);
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
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
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
//
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
                            statePopup = new MmPopupWindow(ContractReviewOrChangeLetterReviewActivity.this, stateSelectEdt, states, 3);
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
    }

    private void requestOrderInfo(Boolean isQuery) {
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
            paramObject.put("org_code", orgCode);
            paramObject.put("user_name", ckText);
            paramObject.put("state", stateId);
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
            params.put("cls", paramArray.toString());
        }
        params.put("page", page + "");
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
                    int total;
                    if (flag == 2) {
                        ContractInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ContractInfoBean.class);
                        List<ContractInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                        total = info.resultEntity.total;
                        if (total == 0) {
                            Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                        if (total % 7 == 0) {
                            pages = total / 7;
                        } else {
                            pages = total / 7 + 1;
                        }
                        for (int i = 0; i < rows.size(); i++) {
                            ContractInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                            String customerName = orderInfo.customer_name;
                            String model = orderInfo.models_name;
                            String orderNumber = orderInfo.order_number;
                            int orderId = orderInfo.order_id;
                            int flow_details_id = 0;
                            if (!rowArray.getJSONObject(i).get("flowdetails").equals("")) {
                                JSONArray flow_details = rowArray.getJSONObject(i).getJSONArray("flowdetails");
                                flow_details_id = flow_details.getJSONObject(flow_details.length() - 1).getInt("flow_details_id");
                            }
                            String examination_result = orderInfo.examination_resul;
                            Log.i(TAG, "onSuccess: " + examination_result);
                            ContractDetailInfo contractDetailInfo = new ContractDetailInfo(orderInfo.contract_id, orderInfo.company_name, orderInfo.creator_by, orderInfo.delivery_time, orderInfo.deposit, orderInfo.models_name, orderInfo.battery_manufacturer, orderInfo.battery_system, orderInfo.vehicle_number, orderInfo.number, orderInfo.delivery_mode, orderInfo.purpose, orderInfo.seat_number);
                            paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, ContractReviewOrChangeLetterReviewActivity.this, flag, contractDetailInfo, examination_result, flow_details_id));
                        }
                    }
                    if (flag == 3) {
                        ChangeLetterInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), ChangeLetterInfoBean.class);
                        List<ChangeLetterInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                        total = info.resultEntity.total;
                        if (total == 0) {
                            if (total == 0) {
                                Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (total % 7 == 0) {
                            pages = total / 7;
                        } else {
                            pages = total / 7 + 1;
                        }
                        for (int i = 0; i < rows.size(); i++) {
                            ChangeLetterInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                            String customerName = orderInfo.customer_name;
                            String model = orderInfo.models_name;
                            String orderNumber = orderInfo.order_number;
                            int orderId = orderInfo.order_id;
                            String examination_result = orderInfo.examination_resul;
                            int flow_details_id = 0;
                            if (!rowArray.getJSONObject(i).get("flowdetails").equals("")) {
                                JSONArray flow_details = rowArray.getJSONObject(i).getJSONArray("flowdetails");
                                flow_details_id = flow_details.getJSONObject(flow_details.length() - 1).getInt("flow_details_id");
                            }
                            Log.i(TAG, "onSuccess: examination_result   " + examination_result + "     flow_details_id      " + flow_details_id);
                            ChangeLetterDetailInfo changeLetterDetailInfo = new ChangeLetterDetailInfo(orderInfo.contract_id, orderInfo.contract_price, orderInfo.number, orderInfo.models_name, orderInfo.company_name, orderInfo.change_contract_price, orderInfo.config_change_date, orderInfo.contract_delivery_date, orderInfo.config_chang_delivery_date);
                            paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, ContractReviewOrChangeLetterReviewActivity.this, flag, changeLetterDetailInfo, examination_result, flow_details_id));
                        }
                    }
                    Log.i(TAG, "onSuccess: " + "???");
                    ReviewTable table = new ReviewTable(ContractReviewOrChangeLetterReviewActivity.this, paperInfos, 1);
                    table.setLayoutParams(lp);
                    if (IsQuery) {
                        tables.clear();
                        tables.add(table);
                        setPages(1, pages);
                        page++;
                    } else if (IsMore && !IsQuery) {
                        if (vp.getCurrentItem() + 1 >= pages) {
                            Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "已是最后一页", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tables.add(table);
                        setPages(tables.size(), pages);
                        page++;
                    } else {
                        Toast.makeText(ContractReviewOrChangeLetterReviewActivity.this, "请先查询", Toast.LENGTH_SHORT).show();
                    }
                    pagerAdapter.notifyDataSetChanged();
                    vp.setAdapter(pagerAdapter);
                    vp.setCurrentItem(page);
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
        papers = new ArrayList<>();
        ReviewTable table = new ReviewTable(this, papers, 1);
        table.setLayoutParams(lp);
        tables.add(table);
        pagerAdapter = new TablePagerAdapter(this, tables);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
        vp.setOnClickListener(null);
        vp.setAdapter(pagerAdapter);
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
    }

}
