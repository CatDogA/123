package com.shanghaigm.dms.view.activity.ck;

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

import static com.shanghaigm.dms.R.id.img_year;

public class WeekReportAddActivity extends BaseActivity {
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private ArrayList<String> datas = new ArrayList<>();
    private ListView left_drawer;
    private DrawerLayout drawerLayout;
    private LinearLayout ll_week_time, ll_week_work, ll_other_work, ll_review_note;
    private EditText edtYear, edtWeek,
            edtSchedule, edtCustom, edtWorkContent, edtEffect, edtRemark,
            edtOtherWorkContent, edtDescribe;
    private Button btnWorkContentSave, btnOtherSave, btnAdd;
    private SwipeMenuListView workContentList, listOtherContent, listReviewNotes;
    private int weekId, customId, nextWeekId, otherId;
    public static String GET_CUSTOM_INFO = "GET_CUSTOM_INFO";
    public static String GET_CUSTOM_INFO_BACK = "GET_CUSTOM_INFO_BACK";
    private ArrayList<CustomManageInfo> workContents, others, reviewNotes;
    private ListAdapter workContentAdapter, otherAdapter, reviewNotesAdapter;
    private LoadingDialog dialog;
    private ImageView imgSchedule, imgSearch, imgYear, imgWeek;
    private int flag = 0;   //0新增 1 修改  2查看
    private PaperInfo info;
    private TextView txtWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_report);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        Bundle b = getIntent().getExtras();
        weekId = -1;
        if (b != null) {
            info = (PaperInfo) b.getSerializable(ReviewTable.TASK_INFO);
            weekId = info.getId();
            Log.i(TAG, "initIntent:weekId        " + weekId);
            if (b.getString(ReviewTable.STATE_ID).equals("1") || b.getString(ReviewTable.STATE_ID).equals("4")) {   //modify
                flag = 1;
            } else if (b.getString(ReviewTable.STATE_ID).equals("-1")) {
                flag = 3;         //review just see
            } else {
                flag = 2;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.getString(GET_CUSTOM_INFO_BACK).equals("custom_info")) {
                customId = b.getInt(CustomInfoTable.GET_CUSTOM_ID);
                edtCustom.setText(b.getString(CustomInfoTable.GET_CUSTOM_NAME));
            }
        } else {
            weekId = -1;
        }
    }

    private void setUpView() {
        allGone();
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR); // 获取当前年份
        edtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> arr = new ArrayList<>();
                arr.add(new PopListInfo(year + ""));
                arr.add(new PopListInfo((year + 1) + ""));
                MmPopupWindow pop = new MmPopupWindow(WeekReportAddActivity.this, edtYear, arr, 5);
                pop.showPopup(edtYear);
            }
        });
        edtYear.addTextChangedListener(new WeekTextWatcher());
        edtWeek.addTextChangedListener(new WeekTextWatcher());
        ll_week_time.setVisibility(View.VISIBLE);
        title.setText("周报管理");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(WeekReportAddActivity.this);
            }
        });
        if (flag != 0) {        //表
            if (info != null) {
                edtYear.setEnabled(false);
                edtWeek.setEnabled(false);
                imgYear.setVisibility(View.GONE);
                imgWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                if (flag == 3) {
                    edtYear.setText(info.getInfo3());
                    edtWeek.setText(info.getInfo5());
                }
                if (flag == 1 || flag == 2) {
                    edtYear.setText(info.getInfo2());
                    edtWeek.setText(info.getInfo3());
                }
                setWeekWorkList();
                setOtherWorkContentList();
            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        if (flag == 2 || flag == 3) {
            setWeekWorkList();
            btnAdd.setVisibility(View.GONE);
            btnWorkContentSave.setVisibility(View.GONE);
            btnOtherSave.setVisibility(View.GONE);
            edtYear.setEnabled(false);
            edtWeek.setEnabled(false);
            edtSchedule.setEnabled(false);
            edtCustom.setEnabled(false);
            edtWorkContent.setFocusable(false);
            edtEffect.setFocusable(false);
            edtRemark.setFocusable(false);
            edtOtherWorkContent.setFocusable(false);
            edtDescribe.setFocusable(false);
            imgWeek.setVisibility(View.GONE);
            imgSchedule.setVisibility(View.GONE);
            imgSearch.setVisibility(View.GONE);

        }

        left_drawer.setAdapter(new SlideMenuAdapter(this, datas));
        left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(left_drawer, true);
                switch (position) {
                    case 0:
                        allGone();
                        ll_week_time.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        allGone();
                        ll_week_work.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        allGone();
                        ll_other_work.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        allGone();
                        ll_review_note.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        workContentList.setAdapter(workContentAdapter);
        listOtherContent.setAdapter(otherAdapter);
        listReviewNotes.setAdapter(reviewNotesAdapter);
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
        if (flag != 2 && flag != 3) {
            workContentList.setMenuCreator(creator);
            listOtherContent.setMenuCreator(creator);
        }
        workContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtSchedule.setText(workContents.get(position).getName1());
                edtCustom.setText(workContents.get(position).getName2());
                edtWorkContent.setText(workContents.get(position).getName3());
                edtEffect.setText(workContents.get(position).getName5());
                edtRemark.setText(workContents.get(position).getName6());
                customId = Integer.parseInt(workContents.get(position).getName4());
                nextWeekId = workContents.get(position).getId();
            }
        });
        listOtherContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtOtherWorkContent.setText(others.get(position).getName1());
                edtDescribe.setText(others.get(position).getName2());
                otherId = others.get(position).getId();
            }
        });
        workContentList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("next_week_id", workContents.get(position).getId());
                Log.i(TAG, "onMenuItemClick: " + params.toString());
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_WEEK_REPORT_WORK_PLAN_DELETE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(WeekReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                setWeekWorkList();
                                edtWorkContent.setText("");
                                edtSchedule.setText("");
                                edtCustom.setText("");
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
        listOtherContent.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, Object> params = new HashMap<>();
                params.put("other_id", others.get(position).getId());
                params.put("type", 2);
                OkhttpRequestCenter.getCommonPostRequest(Constant.URL_DELETE_OTHER_WORK_LIST, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(WeekReportAddActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                edtOtherWorkContent.setText("");
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void onClick(View v) {
        try {
            Map<String, Object> params = new HashMap<>();
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            String url = "";
            switch (v.getId()) {
                case R.id.edt_week:
                    ArrayList<PopListInfo> weeks = new ArrayList<PopListInfo>();
                    for (int i = 1; i <= 53; i++) {
                        weeks.add(new PopListInfo(i + ""));
                    }
                    MmPopupWindow popup2 = new MmPopupWindow(WeekReportAddActivity.this, edtWeek, weeks, 3);
                    popup2.showPopup(edtWeek);
                    break;
                case R.id.edt_schedule:
                    ArrayList<PopListInfo> days = new ArrayList<PopListInfo>();
                    days.add(new PopListInfo("周一"));
                    days.add(new PopListInfo("周二"));
                    days.add(new PopListInfo("周三"));
                    days.add(new PopListInfo("周四"));
                    days.add(new PopListInfo("周五"));
                    days.add(new PopListInfo("周六"));
                    days.add(new PopListInfo("周日"));
                    MmPopupWindow popup3 = new MmPopupWindow(WeekReportAddActivity.this, edtSchedule, days, 3);
                    popup3.showPopup(edtSchedule);
                    break;
                case R.id.edt_custom:
                    Bundle b = new Bundle();
                    b.putString(AreaComCustomSearchPop.GET_CUSTOM_INFO, "week");
                    goToActivity(AreaComCustomSearchPop.class, b);
                    break;
                case R.id.btn_week_add:
                    if (edtYear.getText().toString().equals("") || edtWeek.getText().toString().equals("")) {
                        Toast.makeText(this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    } else {
                        obj.put("creator_year", edtYear.getText().toString());
                        obj.put("creator_week", edtWeek.getText().toString());
                        arr.put(obj);
                        if (flag == 0) {
                            url = Constant.URL_WEEK_REPORT_ADD;
                        }
                        if (flag == 1) {
                            url = Constant.URL_WEEK_REPORT_UPDATE;
                            obj.put("week_id", weekId);
                        }
                        params.put("weekReport", arr.toString());
                        solveRequest(params, url, 1);
                    }
                    break;
                case R.id.btn_week_save:   //next_week:新增和修改
                    if (weekId != -1) {
                        if (nextWeekId == -1) {   //新增
                            url = Constant.URL_WEEK_REPORT_WORK_PLAN_ADD;
                        } else {
                            url = Constant.URL_WEEK_REPORT_WORK_PALN_UPDATE;
                        }
                        if (edtSchedule.getText().toString().equals("") || edtCustom.getText().toString().equals("") ||
                                edtWorkContent.getText().toString().equals("") || edtEffect.getText().toString().equals("")) {
                            Toast.makeText(app, getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
                        } else {
                            obj.put("custome_id", customId);
                            obj.put("creator_week_num", edtSchedule.getText().toString());
                            obj.put("work_content", edtWorkContent.getText().toString());
                            obj.put("work_effect", edtEffect.getText().toString());
                            obj.put("remark", edtRemark.getText().toString());
                            obj.put("next_week_id", nextWeekId);
                            arr.put(obj);
                            params.put("week_id", weekId);
                            params.put("nextWeekWorkContent", arr.toString());
                        }
                        solveRequest(params, url, 2);
                    }
                    break;
                case R.id.btn_other_work_save:
                    if (!edtOtherWorkContent.getText().toString().equals("") && !edtDescribe.getText().toString().equals("")) {
                        params = new HashMap<>();
                        arr = new JSONArray();
                        obj = new JSONObject();
                        try {
                            obj.put("work_content", edtOtherWorkContent.getText().toString());
                            obj.put("work_describe", edtDescribe.getText().toString());
                            arr.put(obj);
                            params.put("otherWork", arr.toString());
                            params.put("relation_id", weekId);
                            params.put("type", 2);
                            params.put("login_name", app.getAccount());
                            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADD_OTHER_LIST, params, new DisposeDataListener() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    try {
                                        if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                            Toast.makeText(WeekReportAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                            setOtherWorkContentList();
                                            edtOtherWorkContent.setText("");
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
                        Toast.makeText(WeekReportAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void solveRequest(Map<String, Object> params, String url, final int type) {
        params.put("loginName", app.getAccount());
        params.put("jobCode", app.getJobCode());
        params.put("roleCode", app.getRoleCode());
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                dialog.dismissLoadingDlg();
                try {
                    JSONObject obj = (JSONObject) responseObj;
                    switch (type) {
                        case 1:
                            if (obj.getString("returnCode").equals("1")) {
                                if (flag == 0) {
                                    Toast.makeText(app, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                    weekId = Integer.parseInt(obj.getString("result"));
                                    edtYear.setEnabled(false);
                                    edtWeek.setEnabled(false);
                                    imgWeek.setVisibility(View.GONE);
                                    imgYear.setVisibility(View.GONE);
                                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                }
                                if (flag == 1) {
                                    Toast.makeText(app, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG, "onSuccess: " + weekId);
                            } else if (obj.getString("result").equals("对不起,您本周已经添加过周报信息,不能重复添加")) {
                                Toast.makeText(WeekReportAddActivity.this, "您本周已经添加过周报信息,不能重复添加", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            if (obj.getString("returnCode").equals("1")) {
                                Toast.makeText(app, getString(R.string.do_success), Toast.LENGTH_SHORT).show();
                                setWeekWorkList();
                                edtWorkContent.setText("");
                                edtSchedule.setText("");
                                edtCustom.setText("");
                                edtEffect.setText("");
                                edtRemark.setText("");
                            }
                            break;
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

    private void initView() {
        //标题
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        //侧边
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //布局
        ll_week_time = (LinearLayout) findViewById(R.id.ll_week_time);
        ll_week_work = (LinearLayout) findViewById(R.id.ll_week_plan);
        ll_other_work = (LinearLayout) findViewById(R.id.ll_other_work);
        ll_review_note = (LinearLayout) findViewById(R.id.ll_review_note);
        //edts
        edtYear = (EditText) findViewById(R.id.edt_year);
        edtWeek = (EditText) findViewById(R.id.edt_week);
        edtSchedule = (EditText) findViewById(R.id.edt_schedule);
        edtCustom = (EditText) findViewById(R.id.edt_custom);
        edtWorkContent = (EditText) findViewById(R.id.edt_work_content);
        edtEffect = (EditText) findViewById(R.id.edt_expect);
        edtOtherWorkContent = (EditText) findViewById(R.id.edt_other_work_content);
        edtDescribe = (EditText) findViewById(R.id.edt_describe);
        edtRemark = (EditText) findViewById(R.id.next_remark);
        //btns
        btnAdd = (Button) findViewById(R.id.btn_week_add);
        btnWorkContentSave = (Button) findViewById(R.id.btn_week_save);
        btnOtherSave = (Button) findViewById(R.id.btn_other_work_save);
        //lists
        workContentList = (SwipeMenuListView) findViewById(R.id.list_work);
        listOtherContent = (SwipeMenuListView) findViewById(R.id.list_other);
        listReviewNotes = (SwipeMenuListView) findViewById(R.id.list_records);

        dialog = new LoadingDialog(this, "正在加载");
        //imgs
        imgSchedule = (ImageView) findViewById(R.id.img_schedule);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgWeek = (ImageView) findViewById(R.id.img_week);
        imgYear = (ImageView) findViewById(img_year);
        //text
        txtWeek = (TextView) findViewById(R.id.txt_week);
    }

    private void setWeekWorkList() {
        Map<String, Object> params = new HashMap<>();
        params.put("week_id", weekId);

        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_WEEK_REPORT_WORK_PLAN_QUERY, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                JSONArray arr = (JSONArray) responseObj;
                workContents.clear();
                if (arr.length() != 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            if (!arr.getJSONObject(i).optString("next_week_id").equals("null")) {
                                workContents.add(new CustomManageInfo(arr.getJSONObject(i).getString("creator_week_num"), arr.getJSONObject(i).getString("company_name"),
                                        arr.getJSONObject(i).getString("work_content"), arr.getJSONObject(i).getString("custome_id") + "", arr.getJSONObject(i).getString("work_effect"),
                                        arr.getJSONObject(i).getString("remark"), arr.getJSONObject(i).optInt("next_week_id")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                workContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i(TAG, "onFailure: " + reasonObj.toString());
            }
        });
    }

    private void setOtherWorkContentList() {
        dialog.showLoadingDlg();
        Map<String, Object> params = new HashMap<>();
        params.put("relation_id", weekId);
        params.put("type", 2);
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

    private void allGone() {
        ll_week_time.setVisibility(View.GONE);
        ll_week_work.setVisibility(View.GONE);
        ll_other_work.setVisibility(View.GONE);
        ll_review_note.setVisibility(View.GONE);
    }

    private void initData() {
        nextWeekId = -1;
        otherId = -1;
        datas.add("周工作时间");
        datas.add("周工作计划");
        datas.add("其他工作汇报");
        reviewNotes = new ArrayList<>();
        if (flag != 0) {
            datas.add("审核记录");
            Map<String, Object> params = new HashMap<>();
            params.put("file_id", weekId);
            params.put("flow_type", info.getId3());
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
        }

        workContents = new ArrayList<>();
        others = new ArrayList<>();
        workContentAdapter = new ListAdapter(WeekReportAddActivity.this, R.layout.item_week_work_content, BR.info, workContents);
        otherAdapter = new ListAdapter(WeekReportAddActivity.this, R.layout.item_month_other, BR.info, others);
        reviewNotesAdapter = new ListAdapter(WeekReportAddActivity.this, R.layout.item_records, BR.info, reviewNotes);
    }

    private class WeekTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!edtWeek.getText().toString().equals("") && !edtYear.getText().toString().equals("")) {
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Calendar.YEAR, Integer.parseInt(edtYear.getText().toString()));
                cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(edtWeek.getText().toString()));
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                //获取月份，0表示1月份
                int month = cal.get(Calendar.MONTH) + 1;
                //获取当前天数
                int day = cal.get(Calendar.DAY_OF_MONTH);
                cal.clear();
                cal.set(Calendar.YEAR, Integer.parseInt(edtYear.getText().toString()));
                cal.set(Calendar.WEEK_OF_YEAR, (Integer.parseInt(edtWeek.getText().toString())+1));
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //获取月份，0表示1月份
                int month2 = cal.get(Calendar.MONTH) + 1;
                //获取当前天数
                int day2 = cal.get(Calendar.DAY_OF_MONTH);
                Log.i(TAG, "afterTextChanged: "+(Integer.parseInt(edtWeek.getText().toString())+1)+"  "+cal.get(Calendar.DAY_OF_MONTH));
                txtWeek.setText("周区间:("+month+"月"+day+"号到"+month2+"月"+day2+"号)");

            }else {
                txtWeek.setText("");
            }
        }
    }
}
