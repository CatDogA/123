package com.shanghaigm.dms.view.fragment.mm;

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
import java.util.List;

public class OrderReviewFragment extends BaseFragment {
    private static String TAG = "OrderReviewFragment";
    private static OrderReviewFragment fragment;
    private EditText areaSelectEdt, modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt, ckEdt;
    private Button btnQuery;
    private ImageView vpRight, vpLeft;
    private MmPopupWindow areaPopup, modelPopup, statePopup;
    private JSONArray areaArray, modelArray, stateArray = new JSONArray();

    private TextView pageNumText;
    private RelativeLayout rl_end, rl_back;
    private WrapHeightViewPager vp;
    private ArrayList<PaperInfo> papers;//每页数据
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mm_fragment_order, container, false);
        initView(view);
        setUpView();
        return view;
    }

    private void setUpView() {
        pageNumText.setText("页数:" + "0" + "/" + pages);
        titleText.setText(String.format(getActivity().getString(R.string.mm_order_title)));
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
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
                    Log.i(TAG, "onClick: " + "  currentItem   " + currentItem + "  page   " + page + "   ismore " + IsMore);
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
                String[] strings = {"待审核", "已审核", "驳回"};
                ArrayList<PopListInfo> states = new ArrayList<PopListInfo>();
                for (int i = 0; i < strings.length; i++) {
                    states.add(new PopListInfo(strings[i]));
                }
                statePopup = new MmPopupWindow(getActivity(), stateSelectEdt, states, 3);
                statePopup.showPopup(stateSelectEdt);
//                dialog.showLoadingDlg();
//                RequestParams params = new RequestParams();
//                params.put("filed", "parts_bill_state");
//                CommonOkHttpClient.get(CommonRequest.createGetRequest(Constant.URL_GET_STATE, params), new DisposeDataHandle(new DisposeDataListener() {
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        dialog.dismissLoadingDlg();
//                        Log.i(TAG, "onSuccess: state" + responseObj.toString());
//                        ArrayList<PopListInfo> states = new ArrayList<PopListInfo>();
//                        JSONObject object = (JSONObject) responseObj;
//                        try {
//                            stateArray = object.getJSONArray("resultEntity");
//                            for (int i = 0; i < stateArray.length(); i++) {
//                                states.add(new PopListInfo(stateArray.getJSONObject(i).getString("date_value")));
//                            }
//                            statePopup = new MmPopupWindow(getActivity(), stateSelectEdt, states, 3);
//                            statePopup.showPopup(stateSelectEdt);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Object reasonObj) {
//
//                    }
//                }));
            }
        });
    }

    public void refresh() {
        IsQuery = true;
        IsMore = true;
        page = 1;
        requestOrderInfo(IsQuery);
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
//        if (!stateText.equals("")) {
//            stateId = getParam(stateArray, stateText, "date_value", "date_key");
//        } else {
//            stateId = null;
//        }
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
        params.put("order", paramArray.toString());
        params.put("page", page + "");
        params.put("rows", "7");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_QUERY_ORDER_REVIEW_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i("luo", "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                OrderQueryInfoBean info = GsonUtil.GsonToBean(responseObj.toString(), OrderQueryInfoBean.class);
                Log.i(TAG, "onSuccess: " + info);
                List<OrderQueryInfoBean.ResultEntity.OrderInfo> rows = info.resultEntity.rows;
                int total = info.resultEntity.total;
                if (total == 0) {
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
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
                ReviewTable table = new ReviewTable(getActivity(), paperInfos, 1);
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
                Log.i(TAG, "onSuccess:page " + page);
                Log.i(TAG, "onSuccess:tables " + tables.size());
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
        papers = new ArrayList<>();
        ReviewTable table = new ReviewTable(getActivity(), papers, 1);
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
        dialog = new LoadingDialog(getActivity(), "正在加载");

        rl_end = (RelativeLayout) v.findViewById(R.id.rl_out);
        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
    }

    public static OrderReviewFragment getInstance() {
        if (fragment == null) {
            fragment = new OrderReviewFragment();
        }
        return fragment;
    }
}
