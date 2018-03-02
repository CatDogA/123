package com.shanghaigm.dms.view.activity.ck;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.common.CustomManageInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.adapter.SlideMenuAdapter;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//客户档案的新增和修改
public class CustomFileAddActivity extends BaseActivity {
    private ListView left_drawer;
    private SwipeMenuListView list_linkman, list_holdings, list_years, list_coaches;
    private ListAdapter linkmanAdapter, holdingsAdapter, coachesAdapter, yearsAdapter;
    private ArrayList<String> datas = new ArrayList<>();
    private ArrayList<CustomManageInfo> linkmans, holdings, coaches, years;
    private RelativeLayout rl_back, rl_end;
    private TextView title;
    private static String TAG = "CustomFileAddActivity";
    private LinearLayout ll_custom_info, ll_linkman, ll_holdings, ll_product, ll_buy_history, ll_county;
    private DrawerLayout drawerLayout;
    private EditText edtCustomName, edtNature, edtNature2, edtCustomPhone, edtType, edtType2, edtLevel1, edtLevel2,
            edtCustomProvince, edtCustomCity, edtCounty, edtAddress,
            edtLinkManType, edtLinkName, edtLinkPhone, edtDuty, edtHobby,
            edtCarType, edtModel, edtBrand, edtHoldings,
            edtCoachType2, edtCoachLength, edtCoachLength2,
            edtBuyYear, edtRemark;
    private LinearLayout llNatureOther,llTypeOther;
    private LoadingDialog dialog;
    private Button btnCustomInfo, btnSaveLinkMan, btnSaveHoldings, btnHistory, btnProduct;
    private ImageView imgNature, imgCustomType;
    private JSONArray provinces, citys, countys, natureArr, typeArr, linkTypeArr, carTypeArr, carLengthArr;
    private int provinceId, cityId, countyId;
    private String customId;
    private ArrayList<EditText> linkManEdts, holdingEdts, produectEdts, historyEdts;
    private Boolean haveCounty, isAdd = true, isFirstLinkInfo = true, isFirstAdd = true,
            isFirstHoldingInfo = true, isFirstProductInfo = true,
            isFirstYearInfo = true;
    private String customNatureId, customTypeId, levelId1, levelId2, linkManTypeId, carTypeId, lengthId;
    private int linkManId, holdingId, productId, yearId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_file_add);
        intentGet();
        initView();
        initData();
        setUpView();
    }

    private void intentGet() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int flag = bundle.getInt(ReviewTable.TASK_TYPE);
            customId = bundle.getInt(ReviewTable.TASK_ID) + "";
            if (flag == 9) {
                isAdd = false;
            }
        }
    }

    private void setEdtText(EditText edt, String text) {
        edt.setText(text);
    }

    private void initData() {
        haveCounty = true;
        linkManId = -1;
        holdingId = -1;
        productId = -1;
        yearId = -1;
        countyId = -1;
        datas.add("客户信息");
        datas.add("单位联系人");
        datas.add("保有量");
        datas.add("产品需求");
        datas.add("购买历史");
        linkmans = new ArrayList<>();
        holdings = new ArrayList<>();
        years = new ArrayList<>();
        coaches = new ArrayList<>();
        provinces = new JSONArray();
        citys = new JSONArray();
        countys = new JSONArray();
        natureArr = new JSONArray();
        typeArr = new JSONArray();
        carTypeArr = new JSONArray();
        carLengthArr = new JSONArray();
        linkTypeArr = new JSONArray();
        linkmanAdapter = new ListAdapter(this, R.layout.item_list_linkman, BR.info, linkmans);
        holdingsAdapter = new ListAdapter(this, R.layout.item_holdings, BR.info, holdings);
        coachesAdapter = new ListAdapter(this, R.layout.items_coaches, BR.info, coaches);
        yearsAdapter = new ListAdapter(this, R.layout.item_years, BR.info, years);
        linkManEdts = new ArrayList<>();
        linkManEdts.add(edtLinkManType);
        linkManEdts.add(edtLinkName);
        linkManEdts.add(edtLinkPhone);
        linkManEdts.add(edtDuty);
        linkManEdts.add(edtHobby);
        holdingEdts = new ArrayList<>();
        holdingEdts.add(edtCarType);
        holdingEdts.add(edtModel);
        holdingEdts.add(edtBrand);
        holdingEdts.add(edtHoldings);
        produectEdts = new ArrayList<>();
        produectEdts.add(edtCoachType2);
        produectEdts.add(edtCoachLength);
        produectEdts.add(edtCoachLength2);
        historyEdts = new ArrayList<>();
        historyEdts.add(edtBuyYear);
        historyEdts.add(edtRemark);
        if (!isAdd) {
            //请求修改信息
            Map<String, Object> params = new HashMap<>();
            params.put("customer_id", Integer.parseInt(customId));
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_CUSTOM_INFO, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.i(TAG, "onSuccess: " + responseObj.toString());
                    dialog.dismissLoadingDlg();
                    JSONObject result = null;
                    try {
                        result = ((JSONObject) responseObj).getJSONObject("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setEdtText(edtCustomName, result.optString("company_name"));
                    setEdtText(edtNature, result.optString("company_nature_value"));
                    if (result.optString("company_nature_value").equals("null")) {
                        setEdtText(edtNature, "");
                    }
                    setEdtText(edtType, result.optString("company_type_value"));
                    if (result.optString("company_type_value").equals("null")) {
                        setEdtText(edtType, "");
                    }
                    setEdtText(edtLevel1, result.optString("level_num"));
                    if (result.optString("level_num").equals("null")) {
                        setEdtText(edtLevel1, "");
                    }
                    setEdtText(edtLevel2, result.optString("level_letter"));
                    if (result.optString("level_letter").equals("null")) {
                        setEdtText(edtLevel2, "");
                    }
                    setEdtText(edtCustomPhone, result.optString("fixed_line"));
                    String[] areaIds = result.optString("address").split(",");    //先配cityId
                    provinceId = Integer.parseInt(areaIds[0]);
                    cityId = Integer.parseInt(areaIds[1]);
                    if (areaIds.length == 3) {
                        countyId = Integer.parseInt(areaIds[2]);
                    }
                    String[] area = result.optString("address_value").split(",");
                    if (area.length == 3) {
                        setEdtText(edtCustomProvince, area[0]);
                        setEdtText(edtCustomCity, area[1]);
                        setEdtText(edtCounty, area[2]);
                    }
                    if (area.length == 2) {
                        setEdtText(edtCustomProvince, area[0]);
                        setEdtText(edtCustomCity, area[1]);
                        haveCounty = false;
                        ll_county.setVisibility(View.INVISIBLE);
                    }
                    setEdtText(edtAddress, result.optString("detailed_address"));
                    customNatureId = result.optString("company_nature");
                    customTypeId = result.optString("company_type");

                    if (result.optString("company_nature_value").equals("其它")) {
                        setEdtText(edtNature2, result.optString("nature_other_value"));
                    }
                    if (result.optString("company_type_value").equals("其他")) {
                        setEdtText(edtType2, result.optString("type_other_value"));
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {
                    Log.i(TAG, "onFailure: " + reasonObj.toString());
                }
            });
        }
    }

    private void setUpView() {
        title.setText("客户管理");
        ll_custom_info.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        left_drawer.setAdapter(new SlideMenuAdapter(this, datas));
        if (isAdd) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        edtCoachLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("filed", "chang_du");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CAR_LENGTH_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        carLengthArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> types = new ArrayList<>();
                        try {
                            types.add(new PopListInfo(""));
                            for (int i = 0; i < carLengthArr.length(); i++) {
                                types.add(new PopListInfo(carLengthArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCoachLength, types, 3);
                            naturePopup.showPopup(edtCoachLength);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        edtCoachLength.addTextChangedListener(new CustomInfoTextListener(4));
        edtCoachLength2.setEnabled(false);
        edtCarType.addTextChangedListener(new CustomInfoTextListener(6));
        edtCarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("filed", "car_type");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CAR_TYPE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        carTypeArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> types = new ArrayList<>();
                        try {
                            types.add(new PopListInfo(""));
                            for (int i = 0; i < carTypeArr.length(); i++) {
                                types.add(new PopListInfo(carTypeArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCarType, types, 3);
                            naturePopup.showPopup(edtCarType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        edtCoachType2.addTextChangedListener(new CustomInfoTextListener(7));
        edtCoachType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("filed", "car_type");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CAR_TYPE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        carTypeArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> types = new ArrayList<>();
                        try {
                            types.add(new PopListInfo(""));
                            for (int i = 0; i < carTypeArr.length(); i++) {
                                types.add(new PopListInfo(carTypeArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCoachType2, types, 3);
                            naturePopup.showPopup(edtCoachType2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        edtNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_nature");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        natureArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < natureArr.length(); i++) {
                                natures.add(new PopListInfo(natureArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(CustomFileAddActivity.this, edtNature, natures, 3);
                            naturePopup.showPopup(edtNature);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        edtNature.addTextChangedListener(new CustomInfoTextListener(1));
        edtType.addTextChangedListener(new CustomInfoTextListener(2));
        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_type");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        typeArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> types = new ArrayList<>();
                        try {
                            types.add(new PopListInfo(""));
                            for (int i = 0; i < typeArr.length(); i++) {
                                types.add(new PopListInfo(typeArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow typePopup = new MmPopupWindow(CustomFileAddActivity.this, edtType, types, 3);
                            typePopup.showPopup(edtType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.i(TAG, "onFailure: " + reasonObj.toString());
                    }
                });
            }
        });
        edtLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo("1"));
                level1s.add(new PopListInfo("2"));
                level1s.add(new PopListInfo("3"));
                level1s.add(new PopListInfo("4"));
                MmPopupWindow levelPopup = new MmPopupWindow(CustomFileAddActivity.this, edtLevel1, level1s, 3);
                levelPopup.showPopup(edtLevel1);
            }
        });
        edtLevel1.addTextChangedListener(new CustomInfoTextListener(9));
        edtLevel2.addTextChangedListener(new CustomInfoTextListener(9));
        edtLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo("A"));
                level1s.add(new PopListInfo("B"));
                level1s.add(new PopListInfo("C"));
                level1s.add(new PopListInfo("D"));
                MmPopupWindow levelPopup = new MmPopupWindow(CustomFileAddActivity.this, edtLevel2, level1s, 3);
                levelPopup.showPopup(edtLevel2);
            }
        });
        edtCustomProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("Pid", 0);
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADDRESS, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        try {
                            JSONArray result = ((JSONObject) responseObj).getJSONArray("result");
                            provinces = result;
                            ArrayList<PopListInfo> provinceInfos = new ArrayList<PopListInfo>();
                            for (int i = 1; i < result.length(); i++) {
                                provinceInfos.add(new PopListInfo(result.getJSONObject(i).getString("name")));
                            }
                            MmPopupWindow provincePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCustomProvince, provinceInfos, 2);
                            provincePopup.showPopup(edtCustomProvince);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        edtCustomProvince.addTextChangedListener(new CustomInfoTextListener(3));
        edtCustomCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = edtCustomProvince.getText().toString();
                for (int i = 0; i < provinces.length(); i++) {
                    if (province.equals("")) {
                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.need_province), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (province.equals(provinces.getJSONObject(i).getString("name"))) {
                                provinceId = provinces.getJSONObject(i).getInt("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Map<String, Object> params = new HashMap<>();
                params.put("Pid", provinceId);
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADDRESS, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        try {
                            JSONArray result = ((JSONObject) responseObj).getJSONArray("result");
                            citys = result;
                            ArrayList<PopListInfo> provinceInfos = new ArrayList<PopListInfo>();
                            for (int i = 0; i < result.length(); i++) {
                                provinceInfos.add(new PopListInfo(result.getJSONObject(i).getString("name")));
                            }
                            MmPopupWindow provincePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCustomCity, provinceInfos, 2);
                            provincePopup.showPopup(edtCustomCity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        edtCustomCity.addTextChangedListener(new CustomInfoTextListener(8));
        edtCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtCustomCity.getText().toString();
                String province = edtCustomProvince.getText().toString();
                if (province.equals("")) {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.need_province), Toast.LENGTH_SHORT).show();
                } else if (city.equals("")) {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.need_city), Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < citys.length(); i++) {
                    try {
                        if (city.equals(citys.getJSONObject(i).getString("name"))) {
                            cityId = citys.getJSONObject(i).getInt("id");
                            Map<String, Object> params = new HashMap<>();
                            params.put("Pid", cityId);
                            dialog.showLoadingDlg();
                            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADDRESS, params, new DisposeDataListener() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    dialog.dismissLoadingDlg();
                                    Log.i(TAG, "onSuccess: " + responseObj.toString());
                                    try {
                                        JSONArray result = ((JSONObject) responseObj).getJSONArray("result");
                                        countys = result;
                                        if (countys.length() == 0) {
                                            ll_county.setVisibility(View.INVISIBLE);
                                            haveCounty = false;
                                        } else {
                                            ArrayList<PopListInfo> countyInfos = new ArrayList<PopListInfo>();
                                            for (int i = 0; i < result.length(); i++) {
                                                countyInfos.add(new PopListInfo(result.getJSONObject(i).getString("name")));
                                            }
                                            MmPopupWindow provincePopup = new MmPopupWindow(CustomFileAddActivity.this, edtCounty, countyInfos, 2);
                                            provincePopup.showPopup(edtCounty);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Object reasonObj) {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        edtLinkManType.addTextChangedListener(new CustomInfoTextListener(5));
        edtLinkManType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> params = new HashMap<>();
                params.put("filed", "linkman_type");
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_LINK_MAN_TYEP, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        ArrayList<PopListInfo> types = new ArrayList<PopListInfo>();
                        linkTypeArr = (JSONArray) responseObj;
                        types.add(new PopListInfo(""));
                        for (int i = 0; i < linkTypeArr.length(); i++) {
                            try {
                                types.add(new PopListInfo(linkTypeArr.getJSONObject(i).getString("date_value")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        MmPopupWindow typePopup = new MmPopupWindow(CustomFileAddActivity.this, edtLinkManType, types, 3);
                        typePopup.showPopup(edtLinkManType);
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        edtBuyYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> years = new ArrayList<PopListInfo>();
                years.add(new PopListInfo(""));
                for (int i = 1990; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
                    years.add(new PopListInfo(i + ""));
                }
                MmPopupWindow levelPopup = new MmPopupWindow(CustomFileAddActivity.this, edtBuyYear, years, 3);
                levelPopup.showPopup(edtBuyYear);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("customer_id", Integer.parseInt(customId));
                JSONArray arr = new JSONArray();
                JSONObject obj = new JSONObject();
                String year = edtBuyYear.getText().toString();
                final String remark = edtRemark.getText().toString();
                if (!year.equals("")) {
                    String url = "";
                    try {
                        obj.put("buy_year", year);
                        obj.put("remark", remark);
                        url = Constant.URL_ADD_HISTORY;
                        if (yearId != -1) {
                            obj.put("buy_id", yearId);
                            url = Constant.URL_MODIFY_HISTORY;
                        }
                        yearId = -1;
                        arr.put(obj);
                        params.put("crmBuyHistoryStr", arr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            try {
                                if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                    if (yearId == -1) {
                                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                    } else {
                                        yearId = -1;
                                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                    }
                                    setYearList();
                                    clearEdts(historyEdts);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                } else {
                    Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coachType = edtCoachType2.getText().toString();
                String length1 = edtCoachLength.getText().toString();
                String length2 = edtCoachLength2.getText().toString();
                Map<String, Object> params = new HashMap<>();
                JSONArray arr = new JSONArray();
                JSONObject obj = new JSONObject();
                String url = "";
                try {
                    if (productId == -1) {
                        obj.put("car_type", getParam(carTypeArr, coachType, "date_value", "date_key"));
                        url = Constant.URL_ADD_PRODUCT;
                    } else {
                        obj.put("car_type", carTypeId);
                        obj.put("tenure_id", productId);
                        url = Constant.URL_MODIFY_PRODUCT;
                    }
                    if (carLengthArr.length() == 0) {
                        obj.put("length", lengthId);
                    } else {
                        obj.put("length", getParam(carLengthArr, length1, "date_value", "date_key"));
                    }
                    obj.put("length_other_value", length2);
                    arr.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("customer_id", Integer.parseInt(customId));
                params.put("crmProductDemandStr", arr.toString());
                params.put("login_name", app.getAccount());
                if (coachType.equals("")) {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (length1.equals("")) {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (length1.equals("其它") && length2.equals("")) {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    return;
                }
                OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        try {
                            if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                if (productId == -1) {
                                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                }
                                productId = -1;
                                setProductList();
                                clearEdts(produectEdts);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        btnSaveHoldings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coachType = edtCarType.getText().toString();
                String model = edtModel.getText().toString();
                String brand = edtBrand.getText().toString();
                final String holdings = edtHoldings.getText().toString();
                Map<String, Object> params = new HashMap<>();
                JSONArray arr = new JSONArray();
                JSONObject obj = new JSONObject();
                if (!coachType.equals("") && !model.equals("") && !brand.equals("") && !holdings.equals("")) {
                    String url = "";
                    try {
                        if (holdingId == -1) {     //添加
                            obj.put("car_type", getParam(carTypeArr, coachType, "date_value", "date_key"));
                            url = Constant.URL_ADD_HOLDINGS;
                        } else {
                            obj.put("car_type", carTypeId);
                            obj.put("tenure_id", holdingId);
                            url = Constant.URL_MODIFY_HOLDINGS;
                        }
                        obj.put("brand", brand);
                        obj.put("tenure_amount", holdings);
                        obj.put("models_name", model);
                        arr.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put("customer_id", Integer.parseInt(customId));
                    params.put("crmTenureAmountStr", arr.toString());
                    params.put("login_name", app.getAccount());
                    dialog.showLoadingDlg();
                    Log.i(TAG, "onClick: " + params.toString());
                    OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
//                            {"returnCode":"1","result":"添加成功"}
                            try {
                                if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                    if (holdingId == -1) {
                                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                    }
                                    holdingId = -1;
                                    clearEdts(holdingEdts);
                                    setHoldingsTable();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                        }
                    });
                } else {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSaveLinkMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linkName = edtLinkName.getText().toString();
                String linkPhone = edtLinkPhone.getText().toString();
                String linkDuty = edtDuty.getText().toString();
                String linkHobby = edtHobby.getText().toString();
                String linkType = edtLinkManType.getText().toString();
                //需要客户ID
                if (!linkName.equals("") && !linkDuty.equals("") && !linkType.equals("")) {
                    Map<String, Object> params = new HashMap<>();
                    JSONArray linkArr = new JSONArray();
                    JSONObject linkObj = new JSONObject();
                    try {
                        linkObj.put("linkman_name", linkName);
                        linkObj.put("phone", linkPhone);
                        linkObj.put("position", linkDuty);
                        linkObj.put("linkman_like", linkHobby);
                        String url = "";
                        if (linkManId == -1) {
                            linkObj.put("linkman_id", "");
                            url = Constant.URL_LINK_MAN_ADD;
                            if (!linkType.equals("")) {
                                linkObj.put("linkman_type", getParam(linkTypeArr, linkType, "date_value", "date_key"));
                            } else {
                                linkObj.put("linkman_type", "");
                            }
                        } else {
                            url = Constant.URL_LINK_MAN_UPDATE;
                            linkObj.put("linkman_id", linkManId);
                            linkObj.put("linkman_type", linkManTypeId);
                        }
                        linkArr.put(linkObj);
                        params.put("customer_id", customId);
                        params.put("login_name", app.getAccount());
                        params.put("job_code", app.getJobCode());
                        params.put("role_code", app.getRoleCode());
                        params.put("crmCustomerLinkmanStr", linkArr.toString());
                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                JSONObject result = (JSONObject) responseObj;
                                try {
                                    if (result.getString("returnCode").equals("1")) {
                                        if (linkManId == -1) {
                                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                        }
                                        clearEdts(linkManEdts);
                                        setLinkTable();
                                        linkManId = -1;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Object reasonObj) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCustomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customName = edtCustomName.getText().toString();
                String customNature = edtNature.getText().toString();
                String customNature2 = edtNature2.getText().toString();
                String customType = edtType.getText().toString();
                String customType2 = edtType2.getText().toString();
                String customLevel1 = edtLevel1.getText().toString();
                String customLevel2 = edtLevel2.getText().toString();
                String customPhone = edtCustomPhone.getText().toString();
                String customProvince = edtCustomProvince.getText().toString();
                String customCity = edtCustomCity.getText().toString();
                String customCounty = edtCounty.getText().toString();
                String customAddress = edtAddress.getText().toString();
                if (!customName.equals("")) {
                    if (!customNature.equals("")) {
                        if (!customProvince.equals("")) {
                            if (!customCity.equals("")) {
                                if (!customAddress.equals("")) {
                                    if (customNature.equals("其它")) {
                                        if (customNature2.equals("")) {
                                            Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    if (customType.equals("其他")) {
                                        if (customType2.equals("")) {
                                            Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    if (haveCounty) {
                                        if (customCounty.equals("")) {
                                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    if (!edtLevel1.getText().toString().equals("") && edtLevel2.getText().toString().equals("")) {
                                        Toast.makeText(CustomFileAddActivity.this, "等级须填写完整", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (edtLevel1.getText().toString().equals("") && !edtLevel2.getText().toString().equals("")) {
                                        Toast.makeText(CustomFileAddActivity.this, "等级须填写完整", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (countys.length() != 0) {
                                        for (int i = 0; i < countys.length(); i++) {
                                            try {
                                                if (customCounty.equals(countys.getJSONObject(i).getString("name"))) {
                                                    countyId = countys.getJSONObject(i).getInt("id");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        countyId = -1;
                                    }
                                    Map<String, Object> params = new HashMap<>();
                                    JSONObject paramObj = new JSONObject();
                                    JSONArray paramArr = new JSONArray();
                                    try {
                                        paramObj.put("company_name", customName);
                                        paramObj.put("company_nature", customNatureId);
                                        paramObj.put("nature_other_value", customNature2);
                                        paramObj.put("company_type", customTypeId);
                                        paramObj.put("type_other_value", customType2);
                                        paramObj.put("level_num", customLevel1);
                                        paramObj.put("level_letter", customLevel2);
                                        paramObj.put("fixed_line", customPhone);
                                        paramObj.put("address", provinceId + "," + cityId + "," + countyId);
                                        if (!isAdd || !isFirstAdd) {
                                            paramObj.put("custome_id", customId);
                                        }
                                        paramObj.put("detailed_address", customAddress);
                                        paramArr.put(paramObj);
                                        params.put("crmCustomerInfoStr", paramArr.toString());
                                        params.put("login_name", app.getAccount());
                                        params.put("role_code", app.getRoleCode());
                                        params.put("job_code", app.getJobCode());
                                        Log.i(TAG, "onClick: " + params.toString());
                                        String url = Constant.URL_ADD_CUTOM;
                                        if (!isAdd || !isFirstAdd) {
                                            url = Constant.URL_MODIFY_CUSTOM_INFO;
                                        }
                                        Log.i(TAG, "onClick:params   " + params.toString());
                                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                                            @Override
                                            public void onSuccess(Object responseObj) {
                                                Log.i(TAG, "onSuccess: " + responseObj.toString() + isAdd + isFirstAdd);
                                                try {
                                                    if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                                        if (isAdd && isFirstAdd) {
                                                            customId = ((JSONObject) responseObj).getString("result");
                                                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                                            isFirstAdd = false;
                                                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else if (((JSONObject) responseObj).getString("returnCode").equals("-1")) {
                                                        Toast.makeText(CustomFileAddActivity.this, ((JSONObject) responseObj).getString("result"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Object reasonObj) {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CustomFileAddActivity.this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CustomFileAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                }
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem((context).getApplicationContext());
                // set item background
                openItem.setBackground(R.color.colorRedText);
                // set item width
                openItem.setWidth(dp2px(40));
                openItem.setTitle("删除");
                openItem.setTitleColor(R.color.colorWhite);
                openItem.setTitleSize(14);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        list_linkman.setMenuCreator(creator);
        list_holdings.setMenuCreator(creator);
        list_coaches.setMenuCreator(creator);
        list_years.setMenuCreator(creator);
        list_linkman.setAdapter(linkmanAdapter);
        list_holdings.setAdapter(holdingsAdapter);
        list_coaches.setAdapter(coachesAdapter);
        list_years.setAdapter(yearsAdapter);
        list_years.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEdtText(edtBuyYear, years.get(position).getName1());
                setEdtText(edtRemark, years.get(position).getName2());
                yearId = years.get(position).getId();
            }
        });
        list_years.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("buy_id", years.get(position).getId());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_HISTORY, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        if (((JSONObject) responseObj).optString("returnCode").equals("1")) {
                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                            setYearList();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
                return true;
            }
        });
        list_coaches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEdtText(edtCoachType2, coaches.get(position).getName1());
                setEdtText(edtCoachLength, coaches.get(position).getName4());
                setEdtText(edtCoachLength2, coaches.get(position).getName5().
                        equals("null") ? "" : coaches.get(position).getName5());
                carTypeId = coaches.get(position).getName3();
                lengthId = coaches.get(position).getName6();
                productId = coaches.get(position).getId();
            }
        });
        list_coaches.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("tenure_id", coaches.get(position).getId());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_PRODUCTS, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        if (((JSONObject) responseObj).optString("returnCode").equals("1")) {
                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                            setProductList();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
                return true;
            }
        });
        list_linkman.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEdtText(edtLinkName, linkmans.get(position).getName1());
                setEdtText(edtLinkPhone, linkmans.get(position).getName2());
                setEdtText(edtDuty, linkmans.get(position).getName3());
                setEdtText(edtLinkManType, linkmans.get(position).getName4());
                setEdtText(edtHobby, linkmans.get(position).getName5());
                linkManId = linkmans.get(position).getId();
                linkManTypeId = linkmans.get(position).getName6();
            }
        });
        list_linkman.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("linkman_id", linkmans.get(position).getId());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_LINK_MAN, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {

                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        if (((JSONObject) responseObj).optString("returnCode").equals("1")) {
                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                            setLinkTable();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
                return true;
            }
        });
        list_holdings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEdtText(edtCarType, holdings.get(position).getName1());
                setEdtText(edtModel, holdings.get(position).getName2());
                setEdtText(edtBrand, holdings.get(position).getName3());
                setEdtText(edtHoldings, holdings.get(position).getName4());
                carTypeId = holdings.get(position).getName6();
                holdingId = holdings.get(position).getId();
            }
        });
        list_holdings.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("tenure_id", holdings.get(position).getId());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_HOLDINGS, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        if (((JSONObject) responseObj).optString("returnCode").equals("1")) {
                            Toast.makeText(CustomFileAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                            setHoldingsTable();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
                return true;
            }
        });
        left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(left_drawer, true);
                switch (position) {
                    case 0:
                        allGone();
                        ll_custom_info.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        allGone();
                        ll_linkman.setVisibility(View.VISIBLE);
                        if (isFirstLinkInfo && !isAdd) {
                            setLinkTable();
                        }
                        break;
                    case 2:
                        allGone();
                        ll_holdings.setVisibility(View.VISIBLE);
                        if (isFirstHoldingInfo && !isAdd) {
                            isFirstHoldingInfo = false;
                            setHoldingsTable();
                        }
                        break;
                    case 3:
                        allGone();
                        ll_product.setVisibility(View.VISIBLE);
                        if (isFirstProductInfo && !isAdd) {
                            isFirstProductInfo = false;
                            setProductList();
                        }
                        break;
                    case 4:
                        allGone();
                        ll_buy_history.setVisibility(View.VISIBLE);
                        if (isFirstYearInfo && !isAdd) {
                            isFirstYearInfo = false;
                            setYearList();
                        }
                        break;
                }
            }
        });
    }

    private void clearEdts(ArrayList<EditText> edts) {
        for (int i = 0; i < edts.size(); i++) {
            edts.get(i).setText("");
        }
    }

    private void setProductList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", Integer.parseInt(customId));
        coaches.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_PRODUCT_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray holdArr = (JSONArray) responseObj;
                for (int i = 0; i < holdArr.length(); i++) {
                    try {
                        String len = "";
                        if (!holdArr.getJSONObject(i).getString("length_value").equals("其它")) {
                            len = holdArr.getJSONObject(i).getString("length_value");
                        } else {
                            len = holdArr.getJSONObject(i).getString("length_other_value");
                        }
                        coaches.add(new CustomManageInfo(holdArr.getJSONObject(i).getString("car_type_value"), len,
                                holdArr.getJSONObject(i).getString("car_type"),
                                holdArr.getJSONObject(i).getString("length_value"),
                                holdArr.getJSONObject(i).getString("length_other_value"),
                                holdArr.getJSONObject(i).getString("length")
                                , holdArr.getJSONObject(i).getInt("tenure_id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                coachesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setYearList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", Integer.parseInt(customId));
        years.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_HISTORY_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray holdArr = (JSONArray) responseObj;
                for (int i = 0; i < holdArr.length(); i++) {
                    try {
                        years.add(new CustomManageInfo(holdArr.getJSONObject(i).getString("buy_year"), holdArr.getJSONObject(i).getString("remark"),
                                "", "", "", "", holdArr.getJSONObject(i).getInt("buy_id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                yearsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setHoldingsTable() {
        dialog.showLoadingDlg();
        holdings.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", Integer.parseInt(customId));
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_HOLDINGS_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray holdArr = (JSONArray) responseObj;
                for (int i = 0; i < holdArr.length(); i++) {
                    try {
                        holdings.add(new CustomManageInfo(holdArr.getJSONObject(i).getString("car_type_value"), holdArr.getJSONObject(i).getString("models_name"),
                                holdArr.getJSONObject(i).getString("brand"), holdArr.getJSONObject(i).getInt("tenure_amount") + "", ""
                                , holdArr.getJSONObject(i).getString("car_type"), holdArr.getJSONObject(i).getInt("tenure_id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                holdingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setLinkTable() {
        dialog.showLoadingDlg();
        linkmans.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", Integer.parseInt(customId));
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_LINK_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                JSONArray links = (JSONArray) responseObj;
                for (int i = 0; i < links.length(); i++) {
                    try {
                        linkmans.add(new CustomManageInfo(links.getJSONObject(i).getString("linkman_name"), links.getJSONObject(i).getString("phone"),
                                links.getJSONObject(i).getString("position"), links.getJSONObject(i).getString("linkman_type_value"), links.getJSONObject(i).getString("linkman_like"),
                                links.getJSONObject(i).getString("linkman_type"), links.getJSONObject(i).getInt("linkman_id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                linkmanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void initView() {
        dialog = new LoadingDialog(this, getString(R.string.loading));
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_linkman = (SwipeMenuListView) findViewById(R.id.list_linkman);
        list_holdings = (SwipeMenuListView) findViewById(R.id.list_holdings);
        list_coaches = (SwipeMenuListView) findViewById(R.id.list_coaches);
        list_years = (SwipeMenuListView) findViewById(R.id.list_years);
        ll_custom_info = (LinearLayout) findViewById(R.id.ll_custom_info);
        ll_linkman = (LinearLayout) findViewById(R.id.ll_linkman);
        ll_holdings = (LinearLayout) findViewById(R.id.ll_holdings);
        ll_product = (LinearLayout) findViewById(R.id.ll_product);
        ll_buy_history = (LinearLayout) findViewById(R.id.ll_buy_history);
        edtNature = (EditText) findViewById(R.id.edt_nature);
        edtNature2 = (EditText) findViewById(R.id.edt_nature_other);
        edtType = (EditText) findViewById(R.id.edt_custom_type);
        edtType2 = (EditText) findViewById(R.id.edt_other_type);
        edtCustomName = (EditText) findViewById(R.id.edt_custom_name);
        edtLevel1 = (EditText) findViewById(R.id.edt_level1);
        edtLevel2 = (EditText) findViewById(R.id.edt_level2);
        edtLinkManType = (EditText) findViewById(R.id.edt_type);
        btnSaveLinkMan = (Button) findViewById(R.id.btn_linkman);
        btnSaveHoldings = (Button) findViewById(R.id.btn_holdings);
        btnHistory = (Button) findViewById(R.id.btn_history);
        edtLinkName = (EditText) findViewById(R.id.edt_link_name);
        edtLinkPhone = (EditText) findViewById(R.id.edt_phone2);
        edtHobby = (EditText) findViewById(R.id.edt_hobby);
        edtDuty = (EditText) findViewById(R.id.edt_duty);
        btnCustomInfo = (Button) findViewById(R.id.btn_custom_info);
        edtCustomPhone = (EditText) findViewById(R.id.edt_phone);
        edtCustomProvince = (EditText) findViewById(R.id.province);
        edtCustomCity = (EditText) findViewById(R.id.city);
        edtCounty = (EditText) findViewById(R.id.county);
        edtAddress = (EditText) findViewById(R.id.detail_address);
        imgCustomType = (ImageView) findViewById(R.id.img_custom_type);
        imgNature = (ImageView) findViewById(R.id.img_custom_nature);
        edtCarType = (EditText) findViewById(R.id.edt_coach_type);
        edtModel = (EditText) findViewById(R.id.edt_model);
        edtHoldings = (EditText) findViewById(R.id.edt_holdings);
        edtBrand = (EditText) findViewById(R.id.edt_brand);
        edtCoachType2 = (EditText) findViewById(R.id.edt_coach_type2);
        edtCoachLength = (EditText) findViewById(R.id.edt_coach_length);
        edtCoachLength2 = (EditText) findViewById(R.id.edt_coach_length_other);
        edtBuyYear = (EditText) findViewById(R.id.edt_buy_year);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
        btnProduct = (Button) findViewById(R.id.btn_product);
        ll_county = (LinearLayout) findViewById(R.id.ll_county);

        llNatureOther = (LinearLayout) findViewById(R.id.ll_nature_other);
        llTypeOther = (LinearLayout) findViewById(R.id.ll_type_other);
    }

    private void allGone() {
        ll_custom_info.setVisibility(View.GONE);
        ll_linkman.setVisibility(View.GONE);
        ll_holdings.setVisibility(View.GONE);
        ll_product.setVisibility(View.GONE);
        ll_buy_history.setVisibility(View.GONE);
    }

    private String getParam(JSONArray array, String text, String name, String code) {
        String id = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (text.equals(object.optString(name))) {
                    id = object.optString(code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return id;
    }

    class CustomInfoTextListener implements TextWatcher {
        private int type;    //1.客户性质，2.客户类型

        public CustomInfoTextListener(int type) {
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String result = s.toString();
            switch (type) {
                case 1:
                    if (result.equals("其他") || result.equals("其它")) {
                        edtNature2.setVisibility(View.VISIBLE);
                        llNatureOther.setVisibility(View.VISIBLE);
                    } else {
                        edtNature2.setVisibility(View.GONE);
                        llNatureOther.setVisibility(View.GONE);
                    }
                    if (natureArr.length() != 0) {
                        customNatureId = getParam(natureArr, result, "date_value", "date_key");
                    }
                    break;
                case 2:
                    if (result.equals("其他") || result.equals("其它")) {
                        edtType2.setVisibility(View.VISIBLE);
                        llTypeOther.setVisibility(View.VISIBLE);
                    } else {
                        edtType2.setVisibility(View.GONE);
                        llTypeOther.setVisibility(View.GONE);
                    }
                    if (typeArr.length() != 0) {
                        customTypeId = getParam(typeArr, result, "date_value", "date_key");
                    }
                    break;
                case 3:
                    edtCustomCity.setText("");
                    edtCounty.setText("");
                    break;
                case 4:
                    if (result.equals("其它")) {
                        edtCoachLength2.setEnabled(true);
                        edtCoachLength2.setText("");
                    } else {
                        edtCoachLength2.setEnabled(false);
                        edtCoachLength2.setText("");
                    }
                    break;
                case 5:
                    if (linkTypeArr.length() != 0) {
                        linkManTypeId = getParam(linkTypeArr, edtLinkManType.getText().toString(), "date_value", "date_key");
                    }
                    break;
                case 6:
                    if (carTypeArr.length() != 0) {
                        carTypeId = getParam(carTypeArr, edtCarType.getText().toString(), "date_value", "date_key");
                    }
                    break;
                case 7:
                    if (carTypeArr.length() != 0) {
                        carTypeId = getParam(carTypeArr, edtCoachType2.getText().toString(), "date_value", "date_key");
                    }
                    break;
                case 8:
                    edtCounty.setText("");
                    if (citys.length() != 0) {               //获取cistyId
                        for (int i = 0; i < citys.length(); i++) {
                            try {
                                if (edtCustomCity.getText().toString().equals(citys.getJSONObject(i).getString("name"))) {
                                    cityId = citys.getJSONObject(i).getInt("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Map<String, Object> params = new HashMap<>();
                    params.put("Pid", cityId);
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADDRESS, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            try {
                                JSONArray result = ((JSONObject) responseObj).getJSONArray("result");
                                countys = result;
                                if (countys.length() == 0) {
                                    ll_county.setVisibility(View.INVISIBLE);
                                    haveCounty = false;
                                } else {
                                    ll_county.setVisibility(View.VISIBLE);
                                    haveCounty = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                    break;
                case 9:
                    if (!edtLevel1.getText().toString().equals("") && !edtLevel2.getText().toString().equals("")) {
                        String levelText = "";
                        switch (edtLevel1.getText().toString() + edtLevel2.getText().toString()) {
                            case "1A":
                                levelText = "购买过南龙车（300台以上），愿买南金车";
                                break;
                            case "1B":
                                levelText = "购买过南龙车（100台到300台），愿买南金车";
                                break;
                            case "1C":
                                levelText = "购买过南龙车（15到100台），愿买南龙车";
                                break;
                            case "1D":
                                levelText = "购买过南龙车（小于15台），愿买南龙车";
                                break;
                            case "2A":
                                levelText = "购买过南龙车（300台以上），不愿买南金车";
                                break;
                            case "2B":
                                levelText = "购买过南龙车（100台到300台），不愿买南金车";
                                break;
                            case "2C":
                                levelText = "购买过南龙车（15到100台），不愿买南龙车";
                                break;
                            case "2D":
                                levelText = "购买过南龙车（小于15台），不愿买南龙车";
                                break;
                            case "3A":
                                levelText = "未购买过南龙车；保有量大于300台，愿买南金车";
                                break;
                            case "3B":
                                levelText = "未购买过南龙车；保有量100台到300台，愿买南金车";
                                break;
                            case "3C":
                                levelText = "未购买过南龙车：保有量15到100台；愿买南龙车";
                                break;
                            case "3D":
                                levelText = "未购买过南龙车：保有量小于15台；愿买南龙车";
                                break;
                            case "4A":
                                levelText = "未购买过南龙车；保有量大于300台，不愿买南金车";
                                break;
                            case "4B":
                                levelText = "未购买过南龙车；保有量100台到300台，不愿买南金车";
                                break;
                            case "4C":
                                levelText = "未购买过南龙车：保有量15到100台；不愿买南龙车";
                                break;
                            case "4D":
                                levelText = "未购买过南龙车：保有量小于15台；不愿买南龙车";
                                break;
                        }
                        ((TextView) findViewById(R.id.txt_level)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.txt_level)).setText(levelText);
                    } else {
                        ((TextView) findViewById(R.id.txt_level)).setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }
}
