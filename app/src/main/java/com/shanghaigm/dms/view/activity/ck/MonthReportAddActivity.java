package com.shanghaigm.dms.view.activity.ck;

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
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/*
* 月报的新增和修改
* */
public class MonthReportAddActivity extends BaseActivity {
    private TextView title;
    private static String TAG = "MonthReportAddActivity";
    private RelativeLayout rl_back, rl_end;
    private ArrayList<String> datas = new ArrayList<>();
    private ListView left_drawer;
    private DrawerLayout drawerLayout;
    private LoadingDialog dialog;
    private LinearLayout ll_sale_target, ll_month, ll_next_month, ll_other, ll_review_note;
    private EditText edtYear, edtMonth, edtNextMonthLeast, edtNextMonthFight, edtSaleds, edtYearSaleds, edtPreMonthLeast, edtPreMonthFight,//销售指标
            edtContent, edtEffect, edtRemark,  //本月情况
            edtContent2, edtEffect2, edtRemark2,   //下本月情况
            edtOtherContent, edtDescribe;   //其他
    private Button btnSave, btnSave2, btnSave3, btnSave4;
    private String monthId = "-1";
    private ImageView imgYear,imgMonth;
    private ArrayList<CustomManageInfo> workContents, nextWorkContents, others, reviewNotes;
    private SwipeMenuListView listWorkContent, listNextWorkContent, listOtherContent, listReviewNotes;
    private ListAdapter workContentAdapter, nextWorkAdapter, otherAdapter, reviewNotesAdapter;
    private int workContentId, nextWorkContentId, otherId;
    private int flag = 0;    //新增0、修改1、查询2
    private int year;
    private PaperInfo info;
    private int count;
    private int c = 0;
    private String lastMonthId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report_add);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        monthId = "";
        if (b != null) {
            info = (PaperInfo) b.getSerializable(ReviewTable.TASK_INFO);
            monthId = info.getId() + "";
            if (b.getString(ReviewTable.STATE_ID).equals("1") || b.getString(ReviewTable.STATE_ID).equals("4")) {   //modify
                flag = 1;
            } else {
                flag = 2;         //just see
            }
        }
    }

    private void setUpView() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR); // 获取当前年份
//        edtYear.setText(String.valueOf(year));
//        edtYear.setEnabled(false);
        edtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> arr = new ArrayList<>();
                arr.add(new PopListInfo(year + ""));
                arr.add(new PopListInfo((year + 1) + ""));
                MmPopupWindow pop = new MmPopupWindow(MonthReportAddActivity.this,edtYear,arr,5);
                pop.showPopup(edtYear);
            }
        });
        final int month = c.get(Calendar.MONTH) + 1; //月份
        if (flag != 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("monthly_id", monthId);
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_MAIN_INFO, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    JSONObject response = (JSONObject) responseObj;
                    try {
                        if (response.getString("returnCode").equals("1")) {
                            JSONObject result = response.getJSONObject("result");
                            edtNextMonthLeast.setText(result.getString("base_index"));
                            edtNextMonthFight.setText(result.getString("struggle_index"));
                            edtSaleds.setText(result.getString("sale_num"));
                            edtMonth.setText(result.getString("creator_month"));
                            edtYear.setText(result.getString("creator_year"));
                            edtYear.setEnabled(false);
                            edtMonth.setEnabled(false);
                            imgMonth.setVisibility(View.GONE);
                            imgYear.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
            setWorkContentList();
            setNextWorkContentList();
            setOtherWorkContentList();
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        if (flag == 2) {
            edtContent.setEnabled(false);
            edtEffect.setEnabled(false);
            edtRemark.setEnabled(false);
            edtNextMonthLeast.setEnabled(false);
            edtNextMonthFight.setEnabled(false);
            edtSaleds.setEnabled(false);
            edtContent2.setEnabled(false);
            edtEffect2.setEnabled(false);
            edtRemark2.setEnabled(false);
            edtOtherContent.setEnabled(false);
            edtContent2.setEnabled(false);
            edtDescribe.setEnabled(false);
            btnSave.setVisibility(View.GONE);
            btnSave2.setVisibility(View.GONE);
            btnSave3.setVisibility(View.GONE);
            btnSave4.setVisibility(View.GONE);
        }
        allGone();
        ll_sale_target.setVisibility(View.VISIBLE);
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(MonthReportAddActivity.this);
            }
        });
        title.setText("月报管理");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        ll_sale_target.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        allGone();
                        ll_month.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        allGone();
                        ll_next_month.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        allGone();
                        ll_other.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        allGone();
                        ll_review_note.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        Map<String, Object> params = new HashMap<>();
        params.put("year", edtYear.getText().toString());
        params.put("month", edtMonth.getText().toString() + "");
        params.put("login_name", app.getAccount());
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_YEAR_SOLDS, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                try {
                    Log.i(TAG, "onSuccess: last_month" + responseObj.toString());
                    edtYearSaleds.setText(((JSONObject) responseObj).getInt("result") + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });

        edtMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Map<String, Object> params2 = new HashMap<>();
                params2.put("year", edtYear.getText().toString());
                params2.put("month", s.toString());
                params2.put("login_name", app.getAccount());
                dialog.showLoadingDlg();
                Log.i(TAG, "afterTextChanged: " + params2.toString());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_LAST_MONTH_TARGET, params2, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        JSONObject response = (JSONObject) responseObj;
                        if (response.optJSONObject("result") != null) {
                            try {
                                edtPreMonthLeast.setText(response.optJSONObject("result").getString("last_base_index"));
                                edtPreMonthFight.setText(response.optJSONObject("result").getString("last_struggle_index"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            edtPreMonthLeast.setText("");
                            edtPreMonthFight.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });

            }
        });
        edtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> months = new ArrayList<PopListInfo>();
                for (int i = 1; i <= 12; i++) {
                    months.add(new PopListInfo(i + ""));
                }
                MmPopupWindow levelPopup = new MmPopupWindow(MonthReportAddActivity.this, edtMonth, months, 3);
                levelPopup.showPopup(edtMonth);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject paramObj = new JSONObject();
                JSONArray paramArr = new JSONArray();
                Map<String, Object> params = new HashMap<>();
                String url = "";
                try {
                    if (!edtMonth.getText().toString().equals("") && !edtNextMonthLeast.getText().toString().equals("") &&
                            !edtNextMonthFight.getText().toString().equals("") && !edtSaleds.getText().toString().equals("")) {
                        paramObj.put("creator_year", edtYear.getText().toString());
                        paramObj.put("creator_month", edtMonth.getText().toString());
                        paramObj.put("base_index", edtNextMonthLeast.getText().toString());
                        paramObj.put("struggle_index", edtNextMonthFight.getText().toString());
                        paramObj.put("sale_num", edtSaleds.getText().toString());
                        paramObj.put("monthly_id", "");
                        url = Constant.URL_MONTH_REPORT_ADD;
                        if (flag != 0) {
                            paramObj.put("monthly_id", monthId);
                            url = Constant.URL_MAIN_INFO_MODIFY;
                        }
                        paramArr.put(paramObj);
                        params.put("monthlyReport", paramArr.toString());
                        params.put("login_name", app.getAccount());
                        params.put("job_code", app.getJobCode());
                        params.put("role_code", app.getRoleCode());
                        dialog.showLoadingDlg();
                        Log.i(TAG, "onClick:params       " + params.toString());
                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                try {
                                    if (flag == 0) {
                                        if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                            monthId = ((JSONObject) responseObj).getString("result");
                                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                            getLastWorkContents();
                                            flag = 1;
                                            edtMonth.setEnabled(false);
                                            edtYear.setEnabled(false);
                                            imgMonth.setVisibility(View.GONE);
                                            imgYear.setVisibility(View.GONE);
                                        } else {
                                            if (((JSONObject) responseObj).getString("result").equals("对不起,您本月已经保存月报信息,不能重复保存")) {
                                                Toast.makeText(MonthReportAddActivity.this, "您本月已经保存月报信息,不能重复保存", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else if (flag == 1) {
                                        if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                        }
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
                        Toast.makeText(MonthReportAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray arr = new JSONArray();
                JSONObject obj = new JSONObject();
                try {
                    String url = "";
                    if (!edtContent.getText().toString().equals("") && !edtEffect.getText().toString().equals("")) {
                        obj.put("work_content", edtContent.getText().toString());
                        obj.put("work_effect", edtEffect.getText().toString());
                        obj.put("remark", edtRemark.getText().toString());
                        url = Constant.URL_MONTH_SITUATION;
                        if (workContentId != -1) {         //修改
                            obj.put("this_month_id", workContentId);
                            obj.put("last_month_id", lastMonthId.equals("0") ? "" : Integer.parseInt(lastMonthId));
                            url = Constant.URL_MONTH_SITUATION_MODIFY;
                        }
                        arr.put(obj);
                        Map<String, Object> params = new HashMap<>();
                        params.put("monthly_id", Integer.parseInt(monthId));
                        if (workContentId != -1) {
                            params.put("monthWorkContentlist", arr.toString());
                        } else {
                            params.put("monthWorkContent", arr.toString());
                        }
                        params.put("login_name", app.getAccount());
                        Log.i(TAG, "onClick: " + params.toString());
                        dialog.showLoadingDlg();
                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                try {
                                    if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                        if (workContentId == -1) {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                            setWorkContentList();
                                        } else {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                            workContentId = -1;
                                            setWorkContentList();
                                        }
                                        edtContent.setText("");
                                        edtEffect.setText("");
                                        edtRemark.setText("");
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
                        Toast.makeText(MonthReportAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btnSave3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray arr = new JSONArray();
                JSONObject obj = new JSONObject();
                String url = "";
                try {
                    if (!edtContent2.getText().toString().equals("") && !edtEffect2.getText().toString().equals("")) {

                        obj.put("work_content", edtContent2.getText().toString());
                        obj.put("work_effect", edtEffect2.getText().toString());
                        obj.put("remark", edtRemark2.getText().toString());
                        url = Constant.URL_NEXT_MONTH_SITUATION;
                        if (nextWorkContentId != -1) {
                            obj.put("this_month_id", nextWorkContentId);
                            url = Constant.URL_NEXT_MONTH_SITUATION_MODIFY;
                        } else {
                            obj.put("last_month_id", "");
                        }
                        arr.put(obj);
                        Map<String, Object> params = new HashMap<>();
                        params.put("monthly_id", Integer.parseInt(monthId));
                        if (nextWorkContentId == -1) {
                            params.put("nextMonthWorkContent", arr.toString());
                        } else {
                            params.put("nextMonthWorkContentlist", arr.toString());
                        }
                        params.put("login_name", app.getAccount());
                        Log.i(TAG, "onClick: " + params.toString());
                        dialog.showLoadingDlg();
                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                try {
                                    if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                        if (nextWorkContentId == -1) {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                            setNextWorkContentList();
                                        } else {
                                            Toast.makeText(MonthReportAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                            setNextWorkContentList();
                                            nextWorkContentId = -1;
                                        }
                                        edtContent2.setText("");
                                        edtEffect2.setText("");
                                        edtRemark2.setText("");
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
                        Toast.makeText(MonthReportAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btnSave4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtOtherContent.getText().toString().equals("") && !edtDescribe.getText().toString().equals("")) {
                    Map<String, Object> params = new HashMap<>();
                    JSONArray arr = new JSONArray();
                    JSONObject obj = new JSONObject();
                    String url = "";
                    try {
                        obj.put("work_content", edtOtherContent.getText().toString());
                        obj.put("work_describe", edtDescribe.getText().toString());
                        arr.put(obj);
                        params.put("otherWork", arr.toString());
                        params.put("relation_id", Integer.parseInt(monthId));
                        params.put("type", 1);
                        params.put("login_name", app.getAccount());
                        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADD_OTHER_LIST, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                try {
                                    if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                        Toast.makeText(MonthReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                        setOtherWorkContentList();
                                        edtOtherContent.setText("");
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
                    Toast.makeText(MonthReportAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
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
                openItem.setTitleColor(Color.WHITE);
                openItem.setTitleSize(14);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        if (flag != 2) {
            listWorkContent.setMenuCreator(creator);
            listNextWorkContent.setMenuCreator(creator);
            listOtherContent.setMenuCreator(creator);
        }
        listWorkContent.setAdapter(workContentAdapter);
        listNextWorkContent.setAdapter(nextWorkAdapter);
        listOtherContent.setAdapter(otherAdapter);
        listReviewNotes.setAdapter(reviewNotesAdapter);
        listWorkContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtContent.setText(workContents.get(position).getName1());
                edtEffect.setText(workContents.get(position).getName2());
                edtRemark.setText(workContents.get(position).getName3());
                workContentId = workContents.get(position).getId();
                lastMonthId = "0";
            }
        });
        listNextWorkContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtContent2.setText(nextWorkContents.get(position).getName1());
                edtEffect2.setText(nextWorkContents.get(position).getName2());
                edtRemark2.setText(nextWorkContents.get(position).getName3());
                nextWorkContentId = nextWorkContents.get(position).getId();
            }
        });
        listOtherContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtOtherContent.setText(others.get(position).getName1());
                edtDescribe.setText(others.get(position).getName2());
                otherId = others.get(position).getId();
            }
        });
        listWorkContent.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("this_month_id", workContents.get(position).getId());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_THIS_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(MonthReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                setWorkContentList();
                                edtContent.setText("");
                                edtEffect.setText("");
                                edtRemark.setText("");
                            }
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
        listNextWorkContent.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("this_month_id", nextWorkContents.get(position).getId());
                OkhttpRequestCenter.getCommonPostRequest(Constant.URL_DELETE_NEXT_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:delete_next " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(MonthReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                setNextWorkContentList();
                                edtContent2.setText("");
                                edtEffect2.setText("");
                                edtRemark2.setText("");
                            }
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
        listOtherContent.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("other_id", others.get(position).getId());
                params.put("type", 1);
                OkhttpRequestCenter.getCommonPostRequest(Constant.URL_DELETE_OTHER_WORK_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(MonthReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                edtOtherContent.setText("");
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
    }

    private void getLastWorkContents() {
        Map<String, Object> params3 = new HashMap<>();
        params3.put("year", edtYear.getText().toString());
        params3.put("month", edtMonth.getText().toString());
        params3.put("login_name", app.getAccount());
        dialog.showLoadingDlg();
        Log.i(TAG, "getLastWorkContents: " + params3.toString());
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_LAST_MONTH_PLAN, params3, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                JSONArray response = (JSONArray) responseObj;
                if (response != null && response.length() != 0) {
                    count = response.length();
                    for (int i = 0; i < response.length(); i++) {
                        try {
//                            lastWorkContents.add(new CustomManageInfo(response.getJSONObject(i).optString("work_content"), response.getJSONObject(i).optString("work_effect"),
//                                    response.getJSONObject(i).optString("remark"), "-1", "", "", response.getJSONObject(i).optInt("last_month_id")));//-1  标识上月
                            JSONArray arr = new JSONArray();
                            JSONObject obj = new JSONObject();
                            String url = "";
                            obj.put("work_content", response.getJSONObject(i).optString("work_content"));
                            obj.put("work_effect", response.getJSONObject(i).optString("work_effect"));
                            obj.put("remark", response.getJSONObject(i).optString("remark"));
                            url = Constant.URL_MONTH_SITUATION;
                            obj.put("last_month_id", response.getJSONObject(i).optInt("last_month_id"));
                            obj.put("this_month_id", "");
                            arr.put(obj);
                            Map<String, Object> params = new HashMap<>();
                            params.put("monthly_id", Integer.parseInt(monthId));
                            params.put("monthWorkContent", arr.toString());
                            params.put("login_name", app.getAccount());
                            Log.i(TAG, "onClick: " + params.toString());
                            dialog.showLoadingDlg();
                            OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    dialog.dismissLoadingDlg();
                                    Log.i(TAG, "onSuccess: " + responseObj.toString());
                                    try {
                                        if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                            c++;   //计数   逐条添加
                                            if (c == count) {
                                                setWorkContentList();
                                            }
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
                    }

                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i(TAG, "onFailure: " + reasonObj.toString());
                dialog.dismissLoadingDlg();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void setWorkContentList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("monthly_id", Integer.parseInt(monthId));
        workContents.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_WORK_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            workContents.add(new CustomManageInfo(arr.getJSONObject(i).getString("work_content"), arr.getJSONObject(i).getString("work_effect"),
                                    arr.getJSONObject(i).getString("remark"), arr.getJSONObject(i).optInt("is_last_month") + "", arr.getJSONObject(i).optInt("last_month_id") + "",
                                    "", arr.getJSONObject(i).optInt("this_month_id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                workContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setNextWorkContentList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("monthly_id", Integer.parseInt(monthId));
        nextWorkContents.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_NEXT_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess:next       " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            nextWorkContents.add(new CustomManageInfo(arr.getJSONObject(i).getString("work_content"), arr.getJSONObject(i).getString("work_effect"),
                                    arr.getJSONObject(i).getString("remark"), "", "", "", arr.getJSONObject(i).optInt("this_month_id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                nextWorkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setOtherWorkContentList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("relation_id", Integer.parseInt(monthId));
        params.put("type", 1);
        others.clear();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_OTHER_WORK_LIST, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
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
                otherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void initView() {
        dialog = new LoadingDialog(this, "正在加载");
        //标题
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        //侧边
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //布局
        ll_sale_target = (LinearLayout) findViewById(R.id.ll_sale_target);
        ll_month = (LinearLayout) findViewById(R.id.ll_month);
        ll_next_month = (LinearLayout) findViewById(R.id.ll_next_month);
        ll_other = (LinearLayout) findViewById(R.id.ll_other);

        ll_review_note = (LinearLayout) findViewById(R.id.ll_review_note);
        //销售指标
        edtYear = (EditText) findViewById(R.id.edt_year);
        edtMonth = (EditText) findViewById(R.id.edt_month);
        edtNextMonthLeast = (EditText) findViewById(R.id.edt_next_least);
        edtNextMonthFight = (EditText) findViewById(R.id.edt_next_fight);
        edtSaleds = (EditText) findViewById(R.id.edt_solds);
        edtYearSaleds = (EditText) findViewById(R.id.edt_year_solds);
        edtPreMonthFight = (EditText) findViewById(R.id.edt_pre_fight);
        edtPreMonthLeast = (EditText) findViewById(R.id.edt_pre_least);
        btnSave = (Button) findViewById(R.id.btn_save);
        imgMonth = (ImageView) findViewById(R.id.img_month);
        imgYear = (ImageView) findViewById(R.id.img_year);
        //本月
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtRemark = (EditText) findViewById(R.id.edt_month_remark);
        edtEffect = (EditText) findViewById(R.id.edt_effect);
        btnSave2 = (Button) findViewById(R.id.btn_save2);
        listWorkContent = (SwipeMenuListView) findViewById(R.id.list_work_condition);
        //下月
        edtContent2 = (EditText) findViewById(R.id.work_content2);
        edtRemark2 = (EditText) findViewById(R.id.next_remark);
        edtEffect2 = (EditText) findViewById(R.id.edt_effect2);
        btnSave3 = (Button) findViewById(R.id.btn_next_month_save);
        listNextWorkContent = (SwipeMenuListView) findViewById(R.id.list_next_month_work_condition);
        //其他
        listOtherContent = (SwipeMenuListView) findViewById(R.id.list_other_work_condition);
        edtOtherContent = (EditText) findViewById(R.id.edt_other_work);
        edtDescribe = (EditText) findViewById(R.id.edt_describe);
        btnSave4 = (Button) findViewById(R.id.btn_other);
        //审核
        listReviewNotes = (SwipeMenuListView) findViewById(R.id.list_reviews);
    }

    private void allGone() {

        ll_sale_target.setVisibility(View.GONE);
        ll_month.setVisibility(View.GONE);
        ll_next_month.setVisibility(View.GONE);
        ll_other.setVisibility(View.GONE);
        ll_review_note.setVisibility(View.GONE);
    }

    /**
     *
     */
    private void initData() {
        workContentId = -1;
        nextWorkContentId = -1;
        otherId = -1;
        workContents = new ArrayList<>();
        nextWorkContents = new ArrayList<>();
        others = new ArrayList<>();
        reviewNotes = new ArrayList<>();
        workContentAdapter = new ListAdapter(MonthReportAddActivity.this, R.layout.item_month_work_content, BR.info, workContents);
        nextWorkAdapter = new ListAdapter(MonthReportAddActivity.this, R.layout.item_month_work_content, BR.info, nextWorkContents);
        otherAdapter = new ListAdapter(MonthReportAddActivity.this, R.layout.item_month_other, BR.info, others);
        reviewNotesAdapter = new ListAdapter(MonthReportAddActivity.this, R.layout.item_records, BR.info, reviewNotes);
        datas.add("销售指标管理");
        datas.add("本月工作完成情况");
        datas.add("下月重点工作内容");
        datas.add("其他工作汇报");
        if (flag != 0) {
            datas.add("审核记录");
            Map<String, Object> params = new HashMap<>();
            params.put("file_id", monthId);
            params.put("flow_type", info.getId3());
            params.put("flow_example_id", info.getId4());
            Log.i(TAG, "initData: " + params.toString());
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_REVIEW_NOTES, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    Log.i(TAG, "onSuccess: 12121212" + responseObj.toString());
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
        }
    }
}
