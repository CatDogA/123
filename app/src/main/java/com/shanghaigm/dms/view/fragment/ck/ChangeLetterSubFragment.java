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
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailBean;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailInfo;
import com.shanghaigm.dms.model.entity.mm.ChangeLetterDetailInfo;
import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;
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
    private WrapHeightViewPager vp;
    private TextView pageNumText;
    private MmPopupWindow modelPopup, statePopup;
    private JSONArray modelArray, stateArray = new JSONArray();

    private TablePagerAdapter pagerAdapter;
    private static String TAG = "OrderReviewFragment";
    public static LoadingDialog dialog;
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

    public void refresh() {
        IsQuery = true;
        IsMore = true;
        page = 1;
        requestOrderInfo(IsQuery);
    }

    /**
     * @param isQuery
     */
    //查询
    private void requestOrderInfo(Boolean isQuery) {
        dialog.showLoadingDlg();
        String stateText = "";
        String contractId = "";
        String changeLetterId = "";
        String customerText = "";
        if (stateSelectEdt != null) {
            stateText = stateSelectEdt.getText().toString();
        }
        if (customerEdt != null) {
            stateText = customerEdt.getText().toString();
        }
        if (changeLetterIdEdt != null) {
            stateText = changeLetterIdEdt.getText().toString();
        }
        Object stateId = null;
        if (!stateText.equals("")) {
            stateId = getParam(stateArray, stateText, "date_value", "date_key");
        } else {
            stateId = null;
        }
        JSONObject paramObject = new JSONObject();
        JSONArray paramArray = new JSONArray();
        try {
            paramObject.put("contract_id", contractId);
            paramObject.put("customer_name", customerText);
//            paramObject.put("letter_id", changeLetterId);
            paramObject.put("state", stateId);
            paramArray.put(paramObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
//        params.put("cls", paramArray.toString());
        params.put("page", page + "");
        params.put("rows", "8");
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        Log.i(TAG, "requestOrderInfo: " + app.getJobCode());
        CommonOkHttpClient.get(new CommonRequest().createGetRequest(Constant.URL_QUREY_CHANGE_LETTER_SUB_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                //            //合同号
//
                Log.i("luo", "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                JSONObject result = (JSONObject) responseObj;
                ArrayList<PaperInfo> paperInfos = new ArrayList<PaperInfo>();
                try {
                    JSONObject resultEntity = result.getJSONObject("resultEntity");
                    JSONArray rows = resultEntity.getJSONArray("rows");
                    int total = resultEntity.getInt("total");
                    if (total == 0) {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
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
                        int order_id = item.getInt("order_id");
                        String change_letter_number = item.getString("change_letter_number");
                        ChangeLetterSubDetailInfo changeLetterSubDetailInfo = new ChangeLetterSubDetailInfo(item);
                        paperInfos.add(new PaperInfo(customerName, state, contract_id, change_letter_number, getActivity(), 6, changeLetterSubDetailInfo, item.getInt("letter_id"),order_id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ReviewTable table = new ReviewTable(getActivity(), paperInfos, 6);
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
        ReviewTable table = new ReviewTable(getActivity(), papers, 6);
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
        //由于布局复用，名字会对不上
        stateSelectEdt = (EditText) v.findViewById(R.id.mm_state_edt);
        contractIdEdt = (EditText) v.findViewById(R.id.mm_number_edt);
        changeLetterIdEdt = (EditText) v.findViewById(R.id.mm_client_edt);
        customerEdt = (EditText) v.findViewById(R.id.mm_model_edt);

        btnQuery = (Button) v.findViewById(R.id.mm_query_button);
        addBtn = (Button) v.findViewById(R.id.mm_add_button);

        vpLeft = (ImageView) v.findViewById(R.id.viewpager_left);
        vpRight = (ImageView) v.findViewById(R.id.viewpager_right);

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
