package com.shanghaigm.dms.view.fragment.ck;

import android.os.Bundle;
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
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailBean;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailInfo;
import com.shanghaigm.dms.model.entity.ck.FragmentInfo;
import com.shanghaigm.dms.model.entity.common.TableInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeLetterDetailInfo;
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;
import com.shanghaigm.dms.view.activity.ck.HomeActivity;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
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

public class ChangeLetterSubFragment extends BaseFragment {
    private static ChangeLetterSubFragment fragment;
    private EditText contractIdEdt, stateSelectEdt, changeLetterIdEdt, customerEdt;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btnQuery, addBtn;
    private ImageView vpRight, vpLeft;
    //    private WrapHeightViewPager vp;
    private TextView pageNumText;
    private MmPopupWindow statePopup;
    private JSONArray stateArray = new JSONArray();
    private static String TAG = "ChangeLetterSubFragment";
    public static LoadingDialog dialog;

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
    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ck_fragment_change_letter, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        initViewPager();
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).back();
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
                //清除遗留数据
                app.setChangeLetterSubDetailInfo(null);
                ChangeLetterAddActivity.isAdded = true;
                goToActivity(ChangeLetterAddActivity.class);
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
                            states.add(new PopListInfo(""));
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
        reQuery(contractIdEdt);
        reQuery(changeLetterIdEdt);
        reQuery(stateSelectEdt);
    }

    public void refresh() {
        page = 0;
        if (tableInfos != null) {
            tableInfos.clear();
        }
        requestOrderInfo(1);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.i("homefragment", "onHiddenChanged:show      " + "3333333333333333333333333333333333333333333333");
//            ((HomeActivity) getActivity()).setButton(3);
        }
        if (hidden) {
            if(!((HomeActivity)getActivity()).isBackClick){
                ((HomeActivity)getActivity()).fragmentInfos.add(new FragmentInfo(3));
            }
            ((HomeActivity)getActivity()).isBackClick = false;
            Log.i("homefragment", "onHiddenChanged:hide         " + "333333333333333333333333333333");
        }
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        ((HomeActivity)getActivity()).setButton(3);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.i("homefragment", "onStart: " + "start            3");
//        ((HomeActivity) getActivity()).fragmentInfos.add(new FragmentInfo(3));
//    }

    //查询
    private void requestOrderInfo(final int type) {
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
        JSONObject paramObject = new JSONObject();
        JSONArray paramArray = new JSONArray();
        String stateText = stateSelectEdt.getText().toString();
        String contractId = contractIdEdt.getText().toString();
        String changeLetterId = changeLetterIdEdt.getText().toString();
        String customerText = customerEdt.getText().toString();
        Object stateId = null;
        if (!stateText.equals("")) {
            stateId = getParam(stateArray, stateText, "date_value", "date_key");
        } else {
            stateId = null;
        }
        try {
            paramObject.put("contract_id", contractId);
            paramObject.put("customer_name", customerText);
            paramObject.put("change_letter_number", changeLetterId);
            paramObject.put("state", stateId);
            paramArray.put(paramObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();

        params.put("cls", paramArray.toString());
        params.put("page", page + 1 + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode() + app.getAccount());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_QUREY_CHANGE_LETTER_SUB_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                //            //合同号
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                JSONObject result = (JSONObject) responseObj;
                ArrayList<PaperInfo> paperInfos = new ArrayList<PaperInfo>();
                try {
                    JSONObject resultEntity = result.getJSONObject("resultEntity");
                    JSONArray rows = resultEntity.getJSONArray("rows");
                    int total = resultEntity.getInt("total");
                    if (total == 0) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                        tables.clear();
                        tableInfos.clear();
                        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 6);
                        tables.add(table);
                    }
                    if (total % 8 == 0) {
                        pages = total / 8;
                    } else {
                        pages = total / 8 + 1;
                    }
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject item = rows.getJSONObject(i);
                        String customerName = item.getString("customer_name");
                        String orderNumber = item.getString("order_number");
                        String state = item.getInt("state") + "";
                        String contract_id = item.getString("contract_id");
                        String change_letter_number = item.getString("change_letter_number");
                        ChangeLetterSubDetailInfo changeLetterSubDetailInfo = new ChangeLetterSubDetailInfo(item);
                        paperInfos.add(new PaperInfo(customerName, state, contract_id, change_letter_number, getActivity(), 6, changeLetterSubDetailInfo, item.getInt("letter_id"), item.getString("models_name")));//order_id没用
                    }
                    //得到table
                    ReviewTable table = new ReviewTable(getActivity(), paperInfos, 6);
                    table.setLayoutParams(lp);
                    if (total != 0) {
                        tables.clear();
                        //加入空的tables占位
                        if (type == 1) {
                            tableInfos.clear();
                            for (int i = 0; i < pages; i++) {
                                tableInfos.add(new TableInfo(pages, new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 6), false));
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
        ReviewTable table = new ReviewTable(getActivity(), new ArrayList<PaperInfo>(), 6);
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

    private void initView(View v) {
        //由于布局复用，名字会对不上
        stateSelectEdt = (EditText) v.findViewById(R.id.mm_state_edt);
        contractIdEdt = (EditText) v.findViewById(R.id.mm_number_edt);
        changeLetterIdEdt = (EditText) v.findViewById(R.id.mm_client_edt);
        customerEdt = (EditText) v.findViewById(R.id.mm_model_edt);

        btnQuery = (Button) v.findViewById(R.id.mm_query_button);
        addBtn = (Button) v.findViewById(R.id.mm_add_button);

        vpLeft = (ImageView) v.findViewById(R.id.viewpager_left);
        vpRight = (ImageView) v.findViewById(R.id.viewpager_right);
        img_first = (ImageView) v.findViewById(R.id.viewpager_first);
        img_last = (ImageView) v.findViewById(R.id.viewpager_last);

        vp = (WrapHeightViewPager) v.findViewById(R.id.order_review_viewpager);
        dialog = new LoadingDialog(getActivity(), "正在加载");
        app = DmsApplication.getInstance();

        pageNumText = (TextView) v.findViewById(R.id.pages_num);
        title = (TextView) v.findViewById(R.id.title_text);
        title.setText("变更函提报");

        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) v.findViewById(R.id.rl_out);
    }

    public static ChangeLetterSubFragment getInstance() {
        if (fragment == null) {
            fragment = new ChangeLetterSubFragment();
        }
        return fragment;
    }
}
