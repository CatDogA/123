package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
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
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.activity.ck.OrderModifyActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
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
    private WrapHeightViewPager vp;
    private TextView pageNumText;
    private MmPopupWindow modelPopup, statePopup;
    private JSONArray modelArray, stateArray = new JSONArray();

    private TablePagerAdapter pagerAdapter;
    private static String TAG = "OrderReviewFragment";
    private LoadingDialog dialog;
    private int pages = 0;//页数
    private int page = 1;//第几页
    private DmsApplication app;
    private RelativeLayout.LayoutParams lp = new RelativeLayout.
            LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private Boolean IsQuery = true;//判断是查询还是更多
    private Boolean IsMore = false;//判断可否请求更多
    private ArrayList<PaperInfo> papers;//每页数据
    private ArrayList<ReviewTable> tables = new ArrayList<>();//表集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ck_fragment_order, container, false);
        initView(v);
        setUpView();
        return v;
    }
    public void refresh(){
        IsQuery = true;
        IsMore = true;
        page = 1;
        requestOrderInfo(IsQuery);
    }
    private void setUpView() {
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
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
                app.OutOfApp();
            }
        });
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
                    if (currentItem + 2 == pages) {
                        vpRight.setImageResource(R.mipmap.right_switch);
                    }
                    Log.i(TAG, "onClick: " + "currentItem" + currentItem + "     page" + page);

                    if (currentItem + 2 == page) {
                        if (page > pages) {
                            Toast.makeText(getActivity(), "已是最后一页", Toast.LENGTH_SHORT).show();
                            vpRight.setImageResource(R.mipmap.right_switch);
                            return;
                        }
                        requestOrderInfo(IsQuery);
                    } else {
                        vp.setCurrentItem(++currentItem);
                        setPages(currentItem + 1, pages);
                    }
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
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    public static OrderSubFragment getInstance(){
        if(fragment==null){
            fragment = new OrderSubFragment();
        }
        return fragment;
    }
    private void requestOrderInfo(Boolean isQuery) {
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
        params.put("page", page + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_QUERY_ORDER_SUB_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                OrderQueryInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), OrderQueryInfoBean.class);
                Log.i(TAG, "onSuccess: " + info);
                List<OrderQueryInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                int total = info.resultEntity.total;
                if (total == 0) {
                    if (total == 0) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    }
                }
                if (total % 8 == 0) {
                    pages = total / 8;
                } else {
                    pages = total / 8 + 1;
                }

                ArrayList<PaperInfo> paperInfos = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    OrderQueryInfoBean.ResultEntity.OrderInfo orderInfo = rows.get(i);
                    String customerName = orderInfo.customer_name;
                    String model = orderInfo.models_name;
                    String orderNumber = orderInfo.order_number;
                    String state = orderInfo.state;
                    int orderId = orderInfo.order_id;
                    Log.i(TAG, "onSuccess: " + state + "," + model + "," + orderNumber + "," + customerName);
                    paperInfos.add(new PaperInfo(customerName, orderNumber, model, state, true, orderId, getActivity(), 5));
                }
                ReviewTable table = new ReviewTable(getActivity(), paperInfos, 5);
                table.setLayoutParams(lp);
                if (IsQuery) {
                    tables.clear();
                    tables.add(table);
                    setPages(1, pages);
                    page++;
                } else if (IsMore && !IsQuery) {
                    if (vp.getCurrentItem() + 1 >= pages) {
                        Toast.makeText(getActivity(), "已是最后一页", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tables.add(table);
                    setPages(tables.size(), pages);
                    page++;
                } else {
                    Toast.makeText(getActivity(), "请先查询", Toast.LENGTH_SHORT).show();
                }
                pagerAdapter.notifyDataSetChanged();
                vp.setAdapter(pagerAdapter);
                vp.setCurrentItem(page);
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

    private void initViewPager() {
        papers = new ArrayList<>();
        ReviewTable table = new ReviewTable(getActivity(), papers, 5);
        table.setLayoutParams(lp);
        tables.add(table);
        pagerAdapter = new TablePagerAdapter(getActivity(), tables);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
        vp.setOnClickListener(null);
        vp.setAdapter(pagerAdapter);
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

        vp = (WrapHeightViewPager) v.findViewById(R.id.order_review_viewpager);
        dialog = new LoadingDialog(getActivity(), "正在加载");
        app = DmsApplication.getInstance();

        pageNumText = (TextView) v.findViewById(R.id.pages_num);
        title = (TextView) v.findViewById(R.id.title_text);
        title.setText("订单提报");

        rl_end = (RelativeLayout) v.findViewById(R.id.rl_out);
        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
    }
}
