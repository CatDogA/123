package com.shanghaigm.dms.view.activity.ck;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.common.CustomManageInfo;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;
import com.shanghaigm.dms.view.adapter.SlideMenuAdapter;
import com.shanghaigm.dms.view.widget.AreaComCustomSearchPop;
import com.shanghaigm.dms.view.widget.CustomInfoTable;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DayReportAddActivity extends BaseActivity {
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private ArrayList<String> datas = new ArrayList<>();
    private ListView left_drawer;
    private DrawerLayout drawerLayout;
    private LinearLayout ll_other, ll_review_note;
    private ScrollView sv_visit_condition, sv_intent_info;
    private EditText edtVisitDate, edtVisitTime, edtVisitTime2, edtCustomName, edtIntent, edtOtherIntent, edtVisitMan, edtDuty, edtPhone, edtProductNeed, edtRemark, edtResult,//走访客户情况
            edtCustomName2, edtLinkMan, edtPhone2, edtDuty2, edtModel, edtNum, edtPayWay, edtSell, edtAcceptPrice, edtComFactory, edtCustomPosition, edtSellPrice, edtPayWay2, edtRate, //意向订单
            edtOtherCotent, edtDescribe;//其他
    private int reportId = -1, customId = 0, visitInfoId, linkManId, linkManId2, modelId, visitIntentId, flag = 0, customId2 = 0, orderIntentId, otherId;
    private LoadingDialog dialog;
    private JSONArray linkMen, visitIntents, models;
    private SwipeMenuListView visitInfoList, intentOrderList, listOtherContent, listReviewNotes;
    private ArrayList<CustomManageInfo> visitInfos, intentOrderInfos, others, reviewNotes;
    private ListAdapter visitInfoAdapter, orderIntentAdapter, otherAdapter, reviewNotesAdapter;
    private ArrayList<EditText> visitInfoEdts, intentOrderEdts;
    private static String TAG = "DayReportAddActivity";
    private PaperInfo info;
    private Button btnVisitContent, btnIntentContent, btnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report_add);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        reportId = -1;
        if (b != null) {
            info = (PaperInfo) b.getSerializable(ReviewTable.TASK_INFO);
            reportId = info.getId();
            String state = b.getString(ReviewTable.STATE_ID);
            if (state.equals("1") || state.equals("4")) {    //可修改
                flag = 1;
            } else {
                flag = 2;   //scan
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.getString(CustomInfoTable.GET_CUSTOM_INFO_BACK).equals("custom_info")) {
                customId = b.getInt(CustomInfoTable.GET_CUSTOM_ID);
                edtCustomName.setText(b.getString(CustomInfoTable.GET_CUSTOM_NAME));
                edtVisitMan.setText("");
            }
            if (b.getString(CustomInfoTable.GET_CUSTOM_INFO_BACK).equals("custom_info2")) {
                customId2 = b.getInt(CustomInfoTable.GET_CUSTOM_ID);
                edtCustomName2.setText(b.getString(CustomInfoTable.GET_CUSTOM_NAME));
            }
        }
    }

    public void onClick(final View v) {
        Map<String, Object> params = new HashMap<>();
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            switch (v.getId()) {
                case R.id.edt_custom_name:
                    Bundle b = new Bundle();
                    b.putString(AreaComCustomSearchPop.GET_CUSTOM_INFO, "day");
                    goToActivity(AreaComCustomSearchPop.class, b);
                    break;
                case R.id.edt_intent:
                    params.put("filed", "visit_other_value");
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_VISIT_INTENT, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            ArrayList<PopListInfo> infos = new ArrayList<PopListInfo>();
                            JSONArray arr = (JSONArray) responseObj;
                            visitIntents = arr;
                            if (arr.length() != 0) {
                                infos.add(new PopListInfo(""));
                                for (int i = 0; i < arr.length(); i++) {
                                    try {
                                        infos.add(new PopListInfo(arr.getJSONObject(i).optString("date_value")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            MmPopupWindow popupWindow = new MmPopupWindow(DayReportAddActivity.this, edtIntent, infos, 3);
                            popupWindow.showPopup(edtIntent);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                    break;
                case R.id.edt_visit_man:
                    if (customId != 0) {
                        params.put("custome_id", customId);
                        solveLinkMen(params, edtVisitMan);
                    } else {
                        Toast.makeText(this, getText(R.string.need_name2), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.edt_link_man:
                    if (customId2 != 0) {
                        params.put("custome_id", customId2);
                        solveLinkMen(params, edtLinkMan);
                    } else {
                        Toast.makeText(this, getText(R.string.need_name2), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_visit_custom:
                    if (edtVisitDate.getText().toString().equals("") || edtVisitTime.getText().toString().equals("")
                            || edtCustomName.getText().toString().equals("") || edtVisitTime2.toString().equals("")) {
                        Toast.makeText(this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    } else {
                        if (edtIntent.getText().toString().equals("其它")) {
                            if (edtOtherIntent.getText().toString().equals("")) {
                                Toast.makeText(this, "请填写其它意图", Toast.LENGTH_SHORT).show();
                            }
                        }
                        String url = "";
                        obj.put("custome_id", customId);
                        obj.put("visit_start_date", edtVisitTime.getText().toString());
                        obj.put("visit_end_date", edtVisitTime2.getText().toString());
                        obj.put("linkman_id", linkManId);
                        obj.put("phone", edtPhone.getText().toString());
                        obj.put("visit_effect", edtResult.getText().toString());
                        obj.put("product_demand", edtProductNeed.getText().toString());
                        obj.put("remark", edtRemark.getText().toString());
                        obj.put("visit_purpose", visitIntentId);
                        obj.put("visit_other_value", edtOtherIntent.getText().toString());
                        obj.put("visit_date", edtVisitDate.getText().toString());
                        if (reportId == -1) {
                            obj.put("day_report_id", "");
                        } else {
                            obj.put("day_customer_id", visitInfoId);
                            obj.put("day_report_id", reportId);
                            params.put("day_report_id", reportId);
                        }
                        arr.put(obj);
                        params.put("crmDayCustomer", arr.toString());
                        if (visitInfoId == -1) {
                            url = Constant.URL_CUSTOM_ADD;
                            solveRequest(url, params, 1);
                        } else {
                            url = Constant.URL_VISTIT_INFO_UPDATE;
                            solveRequest(url, params, 2);
                        }
                    }
                    break;
                case R.id.btn_order_intent:
                    if (!edtCustomName2.getText().toString().equals("") && !edtLinkMan.getText().toString().equals("") &&
                            !edtModel.getText().toString().equals("") && !edtNum.getText().toString().equals("")) {
                        obj.put("custome_id", customId2);
                        obj.put("linkman_id", linkManId2);
                        obj.put("models_Id", modelId);
                        obj.put("buy_car_num", edtNum.getText().toString());
                        obj.put("accepted_price", edtAcceptPrice.getText().toString());
                        obj.put("kfcp_payment_method", edtPayWay.getText().toString());
                        obj.put("straight_or_sell", edtSell.getText().toString());
                        obj.put("compete_vender", edtComFactory.getText().toString());
                        obj.put("visit_customer_name", edtCustomPosition.getText().toString());
                        obj.put("kccj_payment_method", edtPayWay2.getText().toString());
                        obj.put("sale_price", edtSellPrice.getText().toString());
                        obj.put("divine_turnover_rate", edtRate.getText().toString());
                        String url = Constant.URL_INTENT_ORDER_ADD;
                        if (orderIntentId != -1) {
                            url = Constant.URL_INTENT_ORDER_UPDATE;
                            obj.put("day_customer_id", orderIntentId);
                        }
                        arr.put(obj);
                        params.put("day_report_id", reportId);
                        params.put("crmDayIntentionCustomer", arr.toString());
                        if (orderIntentId == -1) {
                            solveRequest(url, params, 3);
                        } else {
                            solveRequest(url, params, 4);
                        }

                    } else {
                        Toast.makeText(this, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_others:
                    if (!edtOtherCotent.getText().toString().equals("") && !edtDescribe.getText().toString().equals("")) {
                        params = new HashMap<>();
                        arr = new JSONArray();
                        obj = new JSONObject();
                        try {
                            obj.put("work_content", edtOtherCotent.getText().toString());
                            obj.put("work_describe", edtDescribe.getText().toString());
                            arr.put(obj);
                            params.put("otherWork", arr.toString());
                            params.put("relation_id", reportId);
                            params.put("type", 3);
                            params.put("login_name", app.getAccount());
                            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADD_OTHER_LIST, params, new DisposeDataListener() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    try {
                                        if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                            Toast.makeText(DayReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                            setOtherWorkContentList();
                                            edtOtherCotent.setText("");
                                            edtDescribe.setText("");
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
                        Toast.makeText(this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.edt_custom_name2:
                    Bundle b2 = new Bundle();
                    b2.putString(AreaComCustomSearchPop.GET_CUSTOM_INFO, "day2");
                    goToActivity(AreaComCustomSearchPop.class, b2);
                    break;
                case R.id.edt_model:
                    params.put("using_type", 1);
                    params.put("if_effective", 1);
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_MODEL_CHOOSE, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            dialog.dismissLoadingDlg();
                            ArrayList<PopListInfo> infos = new ArrayList<PopListInfo>();
                            JSONArray arr = (JSONArray) responseObj;
                            models = arr;
                            if (arr.length() != 0) {
                                infos.add(new PopListInfo(""));
                                for (int i = 0; i < arr.length(); i++) {
                                    try {
                                        infos.add(new PopListInfo(arr.getJSONObject(i).optString("models_name")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            MmPopupWindow popupWindow = null;
                            popupWindow = new MmPopupWindow(DayReportAddActivity.this, edtModel, infos, 3);
                            popupWindow.showPopup(edtModel);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                    break;
                case R.id.edt_sell:
                    ArrayList<PopListInfo> infos = new ArrayList<PopListInfo>();
                    infos.add(new PopListInfo("直销"));
                    infos.add(new PopListInfo("经销"));
                    MmPopupWindow popupWindow = new MmPopupWindow(DayReportAddActivity.this, edtSell, infos, 3);
                    popupWindow.showPopup(edtSell);
                    break;
                case R.id.edt_rate:
                    ArrayList<PopListInfo> infos2 = new ArrayList<PopListInfo>();
                    infos2.add(new PopListInfo("0-30"));
                    infos2.add(new PopListInfo("30-50"));
                    infos2.add(new PopListInfo("50-70"));
                    infos2.add(new PopListInfo("70-90"));
                    infos2.add(new PopListInfo("90以上"));
                    MmPopupWindow popupWindow2 = new MmPopupWindow(DayReportAddActivity.this, edtRate, infos2, 3);
                    popupWindow2.showPopup(edtRate);
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void solveLinkMen(Map<String, Object> params, final EditText edt) {
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_LINK_MEN, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                ArrayList<PopListInfo> infos = new ArrayList<PopListInfo>();
                JSONArray arr = (JSONArray) responseObj;
                linkMen = arr;
                if (arr.length() != 0) {
                    infos.add(new PopListInfo(""));
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            infos.add(new PopListInfo(arr.getJSONObject(i).optString("linkman_name")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                MmPopupWindow popupWindow = null;
                popupWindow = new MmPopupWindow(DayReportAddActivity.this, edt, infos, 3);
                popupWindow.showPopup(edt);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    //新增或修改或删除
    private void solveRequest(String url, Map<String, Object> params, final int type) {
        params.put("login_name", app.getAccount());
        params.put("job_code", app.getJobCode());
        params.put("role_code", app.getRoleCode());
        dialog.showLoadingDlg();
        Log.i(TAG, "solveRequest: " + params.toString());
        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                JSONObject obj = (JSONObject) responseObj;
                try {
                    if (obj.getString("returnCode").equals("1")) {
                        visitInfoId = -1;
                        switch (type) {
                            case 1:
                                Toast.makeText(app, getText(R.string.add_success), Toast.LENGTH_SHORT).show();
                                reportId = Integer.parseInt(obj.getString("result").split(",")[0]);
                                refreshVisitInfo();
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                break;
                            case 2:
                                Toast.makeText(app, getText(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                refreshVisitInfo();
                                break;
                            case 3:
                                Toast.makeText(app, getText(R.string.add_success), Toast.LENGTH_SHORT).show();
                                refreshOrderIntentInfo();
                                break;
                            case 4:
                                Toast.makeText(app, getText(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                refreshOrderIntentInfo();
                                orderIntentId = -1;
                                break;
                        }
                    } else {
                        if (obj.getString("result").equals("该日期已存在日报！")) {
                            Toast.makeText(app, "该日期已存在日报！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
                Toast.makeText(DayReportAddActivity.this, getText(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshVisitInfo() {
        for (int i = 1; i < visitInfoEdts.size(); i++) {
            visitInfoEdts.get(i).setText("");
        }
        edtVisitDate.setEnabled(false);
        edtOtherIntent.setVisibility(View.GONE);
        setVisitInfoList();
    }

    private void refreshOrderIntentInfo() {
        setIntentOrderList();
        for (int i = 0; i < intentOrderEdts.size(); i++) {
            intentOrderEdts.get(i).setText("");
        }
    }

    private void setUpView() {
        allGone();
        sv_visit_condition.setVisibility(View.VISIBLE);
        title.setText("日报管理");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(DayReportAddActivity.this);
            }
        });
        left_drawer.setAdapter(new SlideMenuAdapter(this, datas));
        left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(left_drawer, true);
                switch (position) {
                    case 0:
                        allGone();
                        sv_visit_condition.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        allGone();
                        sv_intent_info.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        allGone();
                        ll_other.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        allGone();
                        ll_review_note.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
        pickDate(edtVisitDate);
        pickTime(edtVisitTime);
        pickTime(edtVisitTime2);
        edtIntent.addTextChangedListener(new DayReportTextListener(1));
        edtVisitMan.addTextChangedListener(new DayReportTextListener(2));
        edtLinkMan.addTextChangedListener(new DayReportTextListener(3));
        edtModel.addTextChangedListener(new DayReportTextListener(4));
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem((context).getApplicationContext());
                // set item background
                openItem.setBackground(R.color.colorRedText);
                // set item width
                openItem.setWidth(dp2px(60));
                openItem.setTitle("删除");
                openItem.setTitleColor(Color.WHITE);
                openItem.setTitleSize(14);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        if (flag != 2) {
            visitInfoList.setMenuCreator(creator);
            intentOrderList.setMenuCreator(creator);
            listOtherContent.setMenuCreator(creator);
        }
        visitInfoList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("day_customer_id", visitInfos.get(position).getId());
                dialog.showLoadingDlg();
                Log.i(TAG, "onMenuItemClick: " + params.toString());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_VISIT_CUSTOM_INFO_DELETE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(DayReportAddActivity.this, getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                setVisitInfoList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        dialog.dismissLoadingDlg();
                    }
                });
                return true;
            }
        });
        intentOrderList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("day_customer_id", intentOrderInfos.get(position).getId());
                dialog.showLoadingDlg();
                Log.i(TAG, "onMenuItemClick: " + params.toString());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_INTENT_ORDER_DELETE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(DayReportAddActivity.this, getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                setIntentOrderList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        dialog.dismissLoadingDlg();
                    }
                });
                return true;
            }
        });
        listOtherContent.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("other_id", others.get(position).getId());
                params.put("type", 3);
                OkhttpRequestCenter.getCommonPostRequest(Constant.URL_DELETE_OTHER_WORK_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(DayReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                edtOtherCotent.setText("");
                                edtDescribe.setText("");
                            }
                            setOtherWorkContentList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });

                return true;
            }
        });
        visitInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtVisitDate.setText(visitInfos.get(position).getName1());
                edtVisitTime.setText(visitInfos.get(position).getName2());
                edtVisitTime2.setText(visitInfos.get(position).getName3());
                edtCustomName.setText(visitInfos.get(position).getName4());
                edtIntent.setText(visitInfos.get(position).getName5());
                edtOtherIntent.setText(visitInfos.get(position).getName6());
                edtVisitMan.setText(visitInfos.get(position).getName7());
                edtDuty.setText(visitInfos.get(position).getName8());
                edtPhone.setText(visitInfos.get(position).getName9());
                edtProductNeed.setText(visitInfos.get(position).getName10());
                edtRemark.setText(visitInfos.get(position).getName11());
                edtResult.setText(visitInfos.get(position).getName12());
                visitInfoId = visitInfos.get(position).getId();
                customId = visitInfos.get(position).getId2();
                linkManId = visitInfos.get(position).getId3();
                visitIntentId = visitInfos.get(position).getId4();
            }
        });
        intentOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //其实可以用mvvm或遍历处理。。麻烦了。。
                edtCustomName2.setText(intentOrderInfos.get(position).getName1());
                edtLinkMan.setText(intentOrderInfos.get(position).getName2());
                edtPhone2.setText(intentOrderInfos.get(position).getName3());
                edtDuty2.setText(intentOrderInfos.get(position).getName4());
                edtModel.setText(intentOrderInfos.get(position).getName5());
                edtNum.setText(intentOrderInfos.get(position).getName6());
                edtPayWay.setText(intentOrderInfos.get(position).getName7());

                edtSell.setText(intentOrderInfos.get(position).getName8());
                edtAcceptPrice.setText(intentOrderInfos.get(position).getName9());
                edtComFactory.setText(intentOrderInfos.get(position).getName10());
                edtCustomPosition.setText(intentOrderInfos.get(position).getName11());
                edtSellPrice.setText(intentOrderInfos.get(position).getName12());
                edtPayWay2.setText(intentOrderInfos.get(position).getName13());
                edtRate.setText(intentOrderInfos.get(position).getName14());
                orderIntentId = intentOrderInfos.get(position).getId();
                customId2 = intentOrderInfos.get(position).getId2();
                linkManId = intentOrderInfos.get(position).getId3();
                modelId = intentOrderInfos.get(position).getId4();
            }
        });
        listOtherContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtOtherCotent.setText(others.get(position).getName1());
                edtDescribe.setText(others.get(position).getName2());
                otherId = others.get(position).getId();
            }
        });
        if (flag != 0) {
            setVisitInfoList();
            edtVisitDate.setEnabled(false);
            edtVisitDate.setText(info.getInfo2());
            setIntentOrderList();
            setOtherWorkContentList();
            datas.add("审核记录");
            Map<String, Object> params = new HashMap<>();
            params.put("file_id", reportId);
            params.put("flow_type", info.getId2());
            params.put("flow_example_id", info.getId4());
            Log.i(TAG, "initData: " + params.toString());
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_REVIEW_NOTES, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    JSONArray arr = (JSONArray) responseObj;
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            String result = "";
                            switch (arr.getJSONObject(i).getInt("examination_result")) {
                                case 1:
                                    result = "通过";
                                    break;
                                case 2:
                                    result = "驳回";
                                    break;
                            }
                            if (arr.getJSONObject(i).getInt("examination_result") != -1) {
                                reviewNotes.add(new CustomManageInfo(arr.getJSONObject(i).getString("administrator"),
                                        result, arr.getJSONObject(i).getString("remarks"), arr.getJSONObject(i).getString("update_date"),
                                        "", "", -1));
                            }
                            reviewNotesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        }else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        if (flag == 2) {
            btnOther.setVisibility(View.GONE);
            btnIntentContent.setVisibility(View.GONE);
            btnVisitContent.setVisibility(View.GONE);
            edtOtherCotent.setEnabled(false);
            edtDescribe.setEnabled(false);
            for (int i = 0; i < intentOrderEdts.size(); i++) {
                intentOrderEdts.get(i).setEnabled(false);
            }
            for (int i = 0; i < visitInfoEdts.size(); i++) {
                visitInfoEdts.get(i).setEnabled(false);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void setOtherWorkContentList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("relation_id", reportId);
        params.put("type", 3);
        others.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_OTHER_WORK_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess:next       " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            others.add(new CustomManageInfo(arr.getJSONObject(i).getString("work_content"), arr.getJSONObject(i).getString("work_describe"),
                                    "", "", "", "", arr.getJSONObject(i).optInt("other_id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                dialog.dismissLoadingDlg();
                otherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setVisitInfoList() {
        Map<String, Object> params = new HashMap<>();
        params.put("day_report_id", reportId);
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_VISIT_CUSTOM_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                visitInfos.clear();
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            visitInfos.add(new CustomManageInfo(arr.getJSONObject(i).getString("visit_date"), arr.getJSONObject(i).getString("visit_start_date"),
                                    arr.getJSONObject(i).getString("visit_end_date"), arr.getJSONObject(i).getString("company_name"),
                                    arr.getJSONObject(i).getString("visit_purpose_name").equals("null") ? "" : arr.getJSONObject(i).getString("visit_purpose_name"), arr.getJSONObject(i).getString("visit_other_value"),
                                    arr.getJSONObject(i).getString("linkman_name").equals("null") ? "" : arr.getJSONObject(i).getString("linkman_name"), arr.getJSONObject(i).getString("position").equals("null") ? "" : arr.getJSONObject(i).getString("position"),
                                    arr.getJSONObject(i).getString("phone").equals("null") ? "" : arr.getJSONObject(i).getString("phone"), arr.getJSONObject(i).getString("product_demand"),
                                    arr.getJSONObject(i).getString("remark"), arr.getJSONObject(i).getString("visit_effect"), "", "",
                                    arr.getJSONObject(i).getInt("day_customer_id"), arr.getJSONObject(i).getInt("custome_id"),
                                    arr.getJSONObject(i).getInt("linkman_id"), Integer.parseInt(arr.getJSONObject(i).getString("visit_purpose"))
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                visitInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
            }
        });
    }

    private void setIntentOrderList() {
        Map<String, Object> params = new HashMap<>();
        params.put("day_report_id", reportId);
        dialog.showLoadingDlg();
        intentOrderInfos.clear();
        Log.i(TAG, "setIntentOrderList: " + params.toString());
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_INTENT_ORDER_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            JSONObject obj = arr.getJSONObject(i);
                            intentOrderInfos.add(new CustomManageInfo(obj.getString("company_name"), obj.getString("linkman_name"),
                                    obj.getString("phone"), obj.getString("position"),
                                    obj.getString("models_name"), obj.getString("buy_car_num"),
                                    obj.getString("kfcp_payment_method"), obj.getString("straight_or_sell"),
                                    obj.getString("accepted_price"), obj.getString("compete_vender"),
                                    obj.getString("visit_customer_name"), obj.getString("sale_price"),
                                    obj.getString("kccj_payment_method"), obj.getString("divine_turnover_rate"),
                                    obj.getInt("day_customer_id"), obj.getInt("custome_id"),
                                    obj.getInt("linkman_id"), obj.getInt("models_Id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                orderIntentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
            }
        });
    }

    private void initView() {
        //标题
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        //侧边
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //布局
        sv_visit_condition = (ScrollView) findViewById(R.id.sv_visit_condition);
        sv_intent_info = (ScrollView) findViewById(R.id.sv_intent_info);
        ll_other = (LinearLayout) findViewById(R.id.ll_other);
        ll_review_note = (LinearLayout) findViewById(R.id.ll_review_note);
        //btns
        btnVisitContent = (Button) findViewById(R.id.btn_visit_custom);
        btnIntentContent = (Button) findViewById(R.id.btn_order_intent);
        btnOther = (Button) findViewById(R.id.btn_others);
        //edts
        //走访情况
        edtVisitDate = (EditText) findViewById(R.id.edt_visit_date);
        edtVisitTime = (EditText) findViewById(R.id.edt_time);
        edtVisitTime2 = (EditText) findViewById(R.id.edt_time2);
        edtCustomName = (EditText) findViewById(R.id.edt_custom_name);
        edtIntent = (EditText) findViewById(R.id.edt_intent);
        edtOtherIntent = (EditText) findViewById(R.id.edt_intent2);
        edtVisitMan = (EditText) findViewById(R.id.edt_visit_man);
        edtDuty = (EditText) findViewById(R.id.edt_duty);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtProductNeed = (EditText) findViewById(R.id.edt_product_need);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
        edtResult = (EditText) findViewById(R.id.edt_visit_result);
        //意向订单信息
        edtCustomName2 = (EditText) findViewById(R.id.edt_custom_name2);
        edtLinkMan = (EditText) findViewById(R.id.edt_link_man);
        edtPhone2 = (EditText) findViewById(R.id.edt_phone2);
        edtDuty2 = (EditText) findViewById(R.id.edt_position);
        edtModel = (EditText) findViewById(R.id.edt_model);
        edtNum = (EditText) findViewById(R.id.edt_num);
        edtPayWay = (EditText) findViewById(R.id.edt_pay_method);
        edtSell = (EditText) findViewById(R.id.edt_sell);
        edtAcceptPrice = (EditText) findViewById(R.id.edt_accept_price);
        edtComFactory = (EditText) findViewById(R.id.edt_com_factory);
        edtCustomPosition = (EditText) findViewById(R.id.edt_custom_position);
        edtSellPrice = (EditText) findViewById(R.id.edt_price);
        edtPayWay2 = (EditText) findViewById(R.id.edt_pay_way);
        edtRate = (EditText) findViewById(R.id.edt_rate);
        edtCustomName2 = (EditText) findViewById(R.id.edt_custom_name2);

        edtOtherCotent = (EditText) findViewById(R.id.edt_other_work);
        edtDescribe = (EditText) findViewById(R.id.describe);
        dialog = new LoadingDialog(this, "正在加载");
        //SwipeMenuListView
        visitInfoList = (SwipeMenuListView) findViewById(R.id.list_visit_condition);
        intentOrderList = (SwipeMenuListView) findViewById(R.id.list_intent_order);
        listOtherContent = (SwipeMenuListView) findViewById(R.id.list_other_work_condition);
        listReviewNotes = (SwipeMenuListView) findViewById(R.id.list_records);
    }

    private void allGone() {
        sv_visit_condition.setVisibility(View.GONE);
        sv_intent_info.setVisibility(View.GONE);
        ll_other.setVisibility(View.GONE);
        ll_review_note.setVisibility(View.GONE);
    }

    private void initData() {
        datas.add("走访客户情况");
        datas.add("意向订单信息表");
        datas.add("其他工作汇报");
        visitInfoId = -1;   //新增
        orderIntentId = -1;
        otherId = -1;
        linkMen = new JSONArray();
        models = new JSONArray();
        visitIntents = new JSONArray();
        visitInfos = new ArrayList<>();
        intentOrderInfos = new ArrayList<>();
        others = new ArrayList<>();
        reviewNotes = new ArrayList<>();
        visitInfoAdapter = new ListAdapter(this, R.layout.item_day_visit_list, BR.info, visitInfos);
        orderIntentAdapter = new ListAdapter(this, R.layout.item_intent_order, BR.info, intentOrderInfos);
        otherAdapter = new ListAdapter(this, R.layout.item_month_other, com.shanghaigm.dms.BR.info, others);
        reviewNotesAdapter = new ListAdapter(this, R.layout.item_records, com.shanghaigm.dms.BR.info, reviewNotes);
        visitInfoList.setAdapter(visitInfoAdapter);
        intentOrderList.setAdapter(orderIntentAdapter);
        listOtherContent.setAdapter(otherAdapter);
        listReviewNotes.setAdapter(reviewNotesAdapter);
        visitInfoEdts = new ArrayList<>();
        intentOrderEdts = new ArrayList<>();
        visitInfoEdts.add(edtVisitDate);
        visitInfoEdts.add(edtVisitTime);
        visitInfoEdts.add(edtVisitTime2);
        visitInfoEdts.add(edtCustomName);
        visitInfoEdts.add(edtIntent);
        visitInfoEdts.add(edtOtherIntent);
        visitInfoEdts.add(edtVisitMan);
        visitInfoEdts.add(edtDuty);
        visitInfoEdts.add(edtPhone);
        visitInfoEdts.add(edtProductNeed);
        visitInfoEdts.add(edtRemark);
        visitInfoEdts.add(edtResult);

        intentOrderEdts.add(edtCustomName2);
        intentOrderEdts.add(edtLinkMan);
        intentOrderEdts.add(edtPhone2);
        intentOrderEdts.add(edtDuty2);
        intentOrderEdts.add(edtModel);
        intentOrderEdts.add(edtNum);
        intentOrderEdts.add(edtPayWay);
        intentOrderEdts.add(edtSell);
        intentOrderEdts.add(edtAcceptPrice);
        intentOrderEdts.add(edtComFactory);
        intentOrderEdts.add(edtCustomPosition);
        intentOrderEdts.add(edtSellPrice);
        intentOrderEdts.add(edtPayWay2);
        intentOrderEdts.add(edtRate);
    }

    private class DayReportTextListener implements TextWatcher {
        private int type;

        public DayReportTextListener(int type) {
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
            switch (type) {
                case 1:
                    if (!s.toString().equals("") && visitIntents.length() != 0) {
                        visitIntentId = Integer.parseInt(getParam(visitIntents, s.toString(), "date_value", "date_key"));
                    } else {
                        visitIntentId = -1;
                    }
                    if (s.toString().equals("其它")) {
                        edtOtherIntent.setVisibility(View.VISIBLE);
                    } else {
                        edtOtherIntent.setText("");
                        edtOtherIntent.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if (linkMen.length() != 0 && !s.toString().equals("")) {
                        Log.i(TAG, "afterTextChanged: " + getParam(linkMen, s.toString(), "linkman_name", "linkman_id"));
                        linkManId = Integer.parseInt(getParam(linkMen, s.toString(), "linkman_name", "linkman_id"));
                        edtDuty.setText(getParam(linkMen, s.toString(), "linkman_name", "position"));
                        edtPhone.setText(getParam(linkMen, s.toString(), "linkman_name", "phone"));
                    } else {
                        edtDuty.setText("");
                        edtPhone.setText("");
                    }
                    break;
                case 3:
                    if (linkMen.length() != 0 && !s.toString().equals("")) {
                        Log.i(TAG, "afterTextChanged: " + getParam(linkMen, s.toString(), "linkman_name", "linkman_id"));
                        linkManId2 = Integer.parseInt(getParam(linkMen, s.toString(), "linkman_name", "linkman_id"));
                        edtDuty2.setText(getParam(linkMen, s.toString(), "linkman_name", "position"));
                        edtPhone2.setText(getParam(linkMen, s.toString(), "linkman_name", "phone"));
                    } else {
                        edtDuty.setText("");
                        edtPhone.setText("");
                    }
                    break;
                case 4:
                    if (models.length() != 0 && !s.toString().equals("")) {
                        modelId = Integer.parseInt(getParam(models, s.toString(), "models_name", "models_Id"));
                    } else {
                        modelId = -1;
                    }
                    break;
            }

        }
    }

    //点击"日期"按钮布局 设置日期
    private void pickDate(final EditText edt) {
        final int[] mYear = {0};
        final int[] mMonth = {0};
        final int[] mDay = {0};
        final Calendar calendar = Calendar.getInstance();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DayReportAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mYear[0] = year;
                        mMonth[0] = month;
                        mDay[0] = day;
                        edt.setText(new StringBuilder().append(mYear[0]).append("-")
                                .append((mMonth[0] + 1) < 10 ? "0" + (mMonth[0] + 1) : (mMonth[0] + 1))
                                .append("-")
                                .append((mDay[0] < 10) ? "0" + mDay[0] : mDay[0]));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void pickTime(final EditText edt) {
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(DayReportAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edt.setText(new StringBuilder().append(hourOfDay < 10 ? "0" + hourOfDay : hourOfDay).append(":")
                                .append(minute < 10 ? "0" + minute : minute));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    //根据name获取code
    private String getParam(JSONArray array, String text, String name, String code) {
        String id = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (text.equals(object.optString(name))) {
                    Log.i(TAG, "getParam: " + object.get(code).toString());
                    id = object.get(code).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return id;
    }
}
