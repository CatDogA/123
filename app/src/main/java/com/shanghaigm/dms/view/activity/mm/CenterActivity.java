package com.shanghaigm.dms.view.activity.mm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
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
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.ck.AreaComAddActivity;
import com.shanghaigm.dms.view.activity.ck.CustomFileAddActivity;
import com.shanghaigm.dms.view.activity.ck.DayReportAddActivity;
import com.shanghaigm.dms.view.activity.ck.MonthReportAddActivity;
import com.shanghaigm.dms.view.activity.ck.WeekReportAddActivity;
import com.shanghaigm.dms.view.adapter.TablePagerAdapter;
import com.shanghaigm.dms.view.fragment.common.HomeFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;
import com.shanghaigm.dms.view.widget.ReviewTable;
import com.shanghaigm.dms.view.widget.WrapHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shanghaigm.dms.R.id.edt_day_sub_man;
import static com.shanghaigm.dms.R.id.edt_month_state;
import static com.shanghaigm.dms.R.id.edt_week;

public class CenterActivity extends BaseActivity {
    private int flag = 0;
    private String url;
    public static String CONTRACT_REFRESH = "contract_refresh";
    public static String AREA_COM_NUM = "area_com_num";
    private static String TAG = "center";
    private EditText areaSelectEdt, modelSelecctEdt, stateSelectEdt, numberEdt, customerEdt, ckEdt,   //审核
            customNatureEdt, customTypeEdt, customLevel1, customLevel2, customNameEdt,  //客户文档
            edtAreaComSubDate, edtAreaComToDate, edtAreaComNum, edtAreaComState,   //区域竞品提交
            dateEdt, stateEdt, edtMonthSubMan, //月报管理/提/审
            yearEdt, weekEdt, weekStateEdt, edtWeekSubMan,    //周提/审
            dayEdt, toDayEdt, subDayEdt, toSubDayEdt, dayStateEdt, edtDaySubMan;//日提/审核
    private Button btnQuery, btnAdd, btnQuery2, btnQuery3;     //2:客户档案查询
    private ImageView vpRight, vpLeft;
    private MmPopupWindow areaPopup, modelPopup, statePopup;
    private JSONArray areaArray, modelArray, natureArray, typeArray;
    private RelativeLayout rl_back, rl_end, rlReview, rlSub, rlReportSub;
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
    private LinearLayout ll_review, ll_custom_file, ll_btn, ll_btn2, ll_month, ll_week, ll_day, ll_area_com,
            ll_day_review, llDaySubMan, llMonthSubMan, llWeekSubMan;
    private TextView txtReject, txtSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        initView();
        setUpView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refresh();
    }

    private void setUpView() {
        switch (flag) {
            case 2:
            case 3:
                ll_review.setVisibility(View.VISIBLE);
                rlReview.setVisibility(View.VISIBLE);
                break;
            case 9:
                ll_custom_file.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(CustomFileAddActivity.class);
                    }
                });
                break;
            case 10:
                rlReportSub.setVisibility(View.VISIBLE);
                ll_month.setVisibility(View.VISIBLE);
                llMonthSubMan.setVisibility(View.GONE);
                ll_btn.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(MonthReportAddActivity.class);
                    }
                });
                break;
            case 11:
                rlReportSub.setVisibility(View.VISIBLE);
                ll_week.setVisibility(View.VISIBLE);
                llWeekSubMan.setVisibility(View.INVISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(WeekReportAddActivity.class);
                    }
                });
                break;
            case 12:
                rlReportSub.setVisibility(View.VISIBLE);
                ll_day.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(DayReportAddActivity.class);
                    }
                });
                break;
            case 13:
                rlSub.setVisibility(View.VISIBLE);
                ll_area_com.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.showLoadingDlg();
                        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_AREA_COM_GET_CODE, null, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Bundle bundle = new Bundle();
                                bundle.putString(AreaComAddActivity.WHERE_FROM, "add");
                                bundle.putString(AREA_COM_NUM, ((JSONObject) responseObj).optString("result"));
                                goToActivity(AreaComAddActivity.class, bundle);
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Toast.makeText(CenterActivity.this, getString(R.string.add_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case 14:
                rlReview.setVisibility(View.VISIBLE);
                ll_month.setVisibility(View.VISIBLE);    //月审核
                ll_btn2.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                break;
            case 15:
                rlReview.setVisibility(View.VISIBLE);
                ll_week.setVisibility(View.VISIBLE);
                ll_btn2.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(MonthReportReviewActivity.class);
                    }
                });
                break;
            case 16:
                ll_day.setVisibility(View.VISIBLE);
                ll_btn2.setVisibility(View.VISIBLE);
                break;
            case 17:
                rlSub.setVisibility(View.VISIBLE);
                txtSub.setVisibility(View.GONE);
                ll_area_com.setVisibility(View.VISIBLE);
                ll_btn2.setVisibility(View.VISIBLE);
                break;
        }
        areaArray = new JSONArray();
        modelArray = new JSONArray();
        lp = new RelativeLayout.
                LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        pageNumText.setText("页数:" + "0" + "/" + pages);
        if (flag == 2) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.mm_contract_sheet_review)));
        }
        if (flag == 3) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.mm_change_letter_review)));
        }
        if (flag == 9) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.custom_file)));
        }
        if (flag == 10) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.month_manage)));
        }
        if (flag == 11) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.week_manage)));
        }
        if (flag == 12) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.day_manage)));
        }
        if (flag == 13) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.area_com)));
        }
        if (flag == 14) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.month_review)));
        }
        if (flag == 15) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.week_review)));
        }
        if (flag == 16) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.day_review)));
            llDaySubMan.setVisibility(View.VISIBLE);
        }
        if (flag == 17) {
            titleText.setText(String.format(CenterActivity.this.getString(R.string.area_com)));
            txtReject.setVisibility(View.GONE);
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
        btnQuery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                if (tableInfos != null) {
                    tableInfos.clear();
                }
                requestOrderInfo(1);
            }
        });
        btnQuery3.setOnClickListener(new View.OnClickListener() {
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
                app.endApp(CenterActivity.this);
            }
        });
        pickDate(edtAreaComSubDate);
        pickDate(edtAreaComToDate);
        pickDate(dayEdt);
        pickDate(toDayEdt);
        pickDate(subDayEdt);
        pickDate(toSubDayEdt);
        pickDate3(dateEdt);
        weekStateEdt.setOnClickListener(new OnClickListener(weekStateEdt));
        dayStateEdt.setOnClickListener(new OnClickListener(dayStateEdt));
        yearEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> infos = new ArrayList<>();
                infos.add(new PopListInfo(""));
                for (int i = 2017; i <= 2030; i++) {
                    infos.add(new PopListInfo(i + ""));
                }
                MmPopupWindow pop = new MmPopupWindow(CenterActivity.this, yearEdt, infos, 3);
                pop.showPopup(yearEdt);
            }
        });
        weekEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> infos = new ArrayList<>();
                infos.add(new PopListInfo(""));
                for (int i = 1; i <= 53; i++) {
                    infos.add(new PopListInfo(i + ""));
                }
                MmPopupWindow pop = new MmPopupWindow(CenterActivity.this, weekEdt, infos, 3);
                pop.showPopup(weekEdt);
            }
        });
        edtAreaComState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> states = new ArrayList<>();
                states.add(new PopListInfo("全部"));
                if (flag == 13) {
                    states.add(new PopListInfo("未提交"));
                }
                states.add(new PopListInfo("审核中"));
                states.add(new PopListInfo("已作废"));
                MmPopupWindow naturePopup = new MmPopupWindow(CenterActivity.this, edtAreaComState, states, 3);
                naturePopup.showPopup(edtAreaComState);
            }
        });
        customNatureEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_nature");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        natureArray = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < natureArray.length(); i++) {
                                natures.add(new PopListInfo(natureArray.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(CenterActivity.this, customNatureEdt, natures, 3);
                            naturePopup.showPopup(customNatureEdt);
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
        customTypeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> param = new HashMap<>();
                param.put("filed", "crm_company_type");
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_CUSTOM_INFO, param, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        typeArray = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < typeArray.length(); i++) {
                                natures.add(new PopListInfo(typeArray.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow typePopup = new MmPopupWindow(CenterActivity.this, customTypeEdt, natures, 3);
                            typePopup.showPopup(customTypeEdt);
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
        customLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo(""));
                level1s.add(new PopListInfo("1"));
                level1s.add(new PopListInfo("2"));
                level1s.add(new PopListInfo("3"));
                level1s.add(new PopListInfo("4"));
                MmPopupWindow levelPopup = new MmPopupWindow(CenterActivity.this, customLevel1, level1s, 3);
                levelPopup.showPopup(customLevel1);
            }
        });
        customLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> level1s = new ArrayList<PopListInfo>();
                level1s.add(new PopListInfo(""));
                level1s.add(new PopListInfo("A"));
                level1s.add(new PopListInfo("B"));
                level1s.add(new PopListInfo("C"));
                level1s.add(new PopListInfo("D"));
                MmPopupWindow levelPopup = new MmPopupWindow(CenterActivity.this, customLevel2, level1s, 3);
                levelPopup.showPopup(customLevel2);
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
                            areaPopup = new MmPopupWindow(CenterActivity.this, areaSelectEdt, areas, 3);
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
                            modelPopup = new MmPopupWindow(CenterActivity.this, modelSelecctEdt, models, 3);
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
                statePopup = new MmPopupWindow(CenterActivity.this, stateSelectEdt, states, 3);
                statePopup.showPopup(stateSelectEdt);
            }
        });
        stateEdt.setOnClickListener(new OnClickListener(stateEdt));
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
//        dialog.showLoadingDlg();
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

        //审核
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
        String customName = customNameEdt.getText().toString();
        String sCustomLevel1 = customLevel1.getText().toString();
        String sCustomLevel2 = customLevel2.getText().toString();
        String customNature = customNatureEdt.getText().toString();
        String customType = customTypeEdt.getText().toString();
        Object natureCode = null;
        Object typeCode = null;
        if (!customNature.equals("")) {
            natureCode = getParam(natureArray, customNature, "date_value", "date_key");
        }
        if (!customType.equals("")) {
            typeCode = getParam(typeArray, customType, "date_value", "date_key");
        }
        Map<String, Object> params = new HashMap<>();
        if (flag == 2) {
            params.put("conts", paramArray.toString());
            url = Constant.URL_QUERY_CONTRACT_REVIEW_INFO;
        }
        if (flag == 3) {
            url = Constant.URL_QUERY_CHANGE_LETTER_INFO;
            params.put("cle", paramArray.toString());
        }
        if (flag == 9) {
            url = Constant.URL_CUSTOM_LIST;
            params.put("company_name", customName);
            if (typeCode != null) {
                params.put("company_type", typeCode.toString());
            }
            if (natureCode != null) {
                params.put("company_nature", natureCode.toString());
            }
            params.put("level_num", sCustomLevel1);
            params.put("level_letter", sCustomLevel2);
        }
        if (flag == 10) {
            url = Constant.URL_MONTH_REPORT_LIST;
            params.put("creator_date", dateEdt.getText().toString());
            params.put("state", getReportState(stateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReportState(stateEdt.getText().toString())));
        }
        if (flag == 11) {
            url = Constant.URL_WEEK_REPORT_DLR_QUERY;
            params.put("creator_year", yearEdt.getText().toString().equals("") ? "" : Integer.parseInt(yearEdt.getText().toString()));
            params.put("state", getReportState(weekStateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReportState(weekStateEdt.getText().toString())));
            params.put("creator_week", weekEdt.getText().toString());
        }
        if (flag == 12) {
            url = Constant.URL_DAY_REPORT_SUB_LIST;
            params.put("state", getReportState(dayStateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReportState(dayStateEdt.getText().toString())));
            params.put("creator_date_stare", dayEdt.getText().toString());
            params.put("creator_date_end", toDayEdt.getText().toString());
            params.put("creator_date_ti_stare", subDayEdt.getText().toString());
            params.put("creator_date_ti_end", toSubDayEdt.getText().toString());
        }
        if (flag == 13) {
            url = Constant.URL_AREA_COM_LIST;
            params.put("creator_date_stare", edtAreaComSubDate.getText().toString());
            params.put("creator_date_end", edtAreaComToDate.getText().toString());
            params.put("state", getComAreaState(edtAreaComState.getText().toString()));
            params.put("competitor_code", edtAreaComNum.getText().toString());
        }
        if (flag == 14) {
            url = Constant.URL_REVIEW_SCAN;
            params.put("type", 1);
            params.put("creator_date", dateEdt.getText().toString());
            params.put("examination_result", getReporReviewtState(stateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReporReviewtState(stateEdt.getText().toString())));
            params.put("user_name", edtMonthSubMan.getText().toString());
        }
        if (flag == 15) {
            url = Constant.URL_WEEK_REPORT_OEM_QUERY;
            params.put("creator_year", yearEdt.getText().toString().equals("") ? "" : Integer.parseInt(yearEdt.getText().toString()));
            params.put("examination_result", getReporReviewtState(weekStateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReporReviewtState(weekStateEdt.getText().toString())));
            params.put("creator_week", weekEdt.getText().toString());
            params.put("user_name", edtWeekSubMan.getText().toString());
        }
        if (flag == 16) {
            url = Constant.URL_DAY_REPORT_REVIEW_LIST;
            params.put("examination_result", getReporReviewtState(dayStateEdt.getText().toString()).equals("") ? "" :
                    Integer.parseInt(getReporReviewtState(dayStateEdt.getText().toString())));
            params.put("creator_date_stare", dayEdt.getText().toString());
            params.put("creator_date_end", toDayEdt.getText().toString());
            params.put("creator_date_ti_stare", subDayEdt.getText().toString());
            params.put("creator_date_ti_end", toSubDayEdt.getText().toString());
            params.put("user_name", edtDaySubMan.getText().toString());
        }
        if (flag == 17) {
            url = Constant.URL_AREA_COM_REVIEW;
            params.put("creator_date_stare", edtAreaComSubDate.getText().toString());
            params.put("creator_date_end", edtAreaComToDate.getText().toString());
            params.put("state", getComAreaState(edtAreaComState.getText().toString()));
            params.put("competitor_code", edtAreaComNum.getText().toString());
        }
        params.put("page", page + 1 + "");
        params.put("rows", "7");
        params.put("loginName", app.getAccount());
        params.put("login_name", app.getAccount());
        params.put("jobCode", app.getJobCode());
        params.put("job_code",app.getJobCode());
        params.put("roleCode", app.getRoleCode());
        params.put("role_code", app.getRoleCode());
        dialog.showLoadingDlg();
        Log.i(TAG, "requestOrderInfo: " + params.toString());
        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess:custom_info " + responseObj.toString());
                solveResult(responseObj, type);
            }

            @Override
            public void onFailure(Object reasonObj) {
                dialog.dismissLoadingDlg();
                Log.i("luo", "onFailure: " + reasonObj.toString());
            }
        });
    }

    class OnClickListener implements View.OnClickListener {
        private EditText edt;

        public OnClickListener(EditText edt) {
            this.edt = edt;
        }

        @Override
        public void onClick(View v) {
            ArrayList<PopListInfo> infos = new ArrayList<>();
            infos.add(new PopListInfo(""));
            if (flag == 10 || flag == 11 || flag == 12) {
                infos.add(new PopListInfo("未提交"));
                infos.add(new PopListInfo("审核中"));
                infos.add(new PopListInfo("已驳回"));
                infos.add(new PopListInfo("已通过"));
            } else {
                infos.add(new PopListInfo("审核中"));
                infos.add(new PopListInfo("已驳回"));
                infos.add(new PopListInfo("已通过"));
            }
            statePopup = new MmPopupWindow(CenterActivity.this, edt, infos, 3);
            statePopup.showPopup(edt);
        }
    }

    private String getReportState(String stateName) {
        String stateCode = "";
        switch (stateName) {
            case "":
                stateCode = "";
                break;
            case "未提交":
                stateCode = "1";
                break;
            case "审核中":
                stateCode = "2";
                break;
            case "已驳回":
                stateCode = "4";
                break;
            case "已通过":
                stateCode = "3";
                break;
        }
        return stateCode;
    }

    private String getReporReviewtState(String stateName) {
        String stateCode = "";
        switch (stateName) {
            case "":
                stateCode = "";
                break;
            case "审核中":
                stateCode = "-1";
                break;
            case "已驳回":
                stateCode = "2";
                break;
            case "已通过":
                stateCode = "1";
                break;
        }
        return stateCode;
    }

    private String getComAreaState(String stateName) {
        String stateCode = "";
        switch (stateName) {
            case "全部":
                break;
            case "未提交":
                stateCode = "1";
                break;
            case "审核中":
                stateCode = "2";
                break;
            case "已作废":
                stateCode = "3";
                break;
        }
        return stateCode;
    }

    private void solveResult(Object responseObj, int type) {
        ArrayList<PaperInfo> paperInfos = new ArrayList<>();
        JSONObject response = (JSONObject) responseObj;
        try {
            JSONObject result = null;
            if (flag == 2 || flag == 3) {
                result = response.getJSONObject("resultEntity");
            } else {
                result = response;
            }
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
                    paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, CenterActivity.this, flag, contractDetailInfo, examination_result, flow_details_id));
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
                    ChangeLetterDetailInfo changeLetterDetailInfo = new ChangeLetterDetailInfo(orderInfo.contract_id, orderInfo.contract_price, orderInfo.number, orderInfo.models_name, orderInfo.company_name, orderInfo.change_contract_price, orderInfo.config_change_date, orderInfo.contract_delivery_date, orderInfo.config_chang_delivery_date, orderInfo.letter_id);
                    paperInfos.add(new PaperInfo(customerName, orderNumber, model, true, orderId, CenterActivity.this, flag, changeLetterDetailInfo, examination_result, flow_details_id, orderInfo.letter_id));
                }
            }
            if (flag == 9) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Log.i(TAG, "solveResult: " + rowArray.getJSONObject(i).getString("company_name"));
                    String companyName = "";
                    String companyNature = "";
                    String companyType = "";
                    String companyLevel = "";
                    String companyLeve2 = "";
                    int custom_id = -1;
                    companyName = getJsonData("company_name", rowArray.getJSONObject(i));
                    companyNature = getJsonData("company_nature_value", rowArray.getJSONObject(i));
                    companyType = getJsonData("company_type_value", rowArray.getJSONObject(i));
                    companyLevel = getJsonData("level_num", rowArray.getJSONObject(i));
                    companyLeve2 = getJsonData("level_letter", rowArray.getJSONObject(i));
                    custom_id = rowArray.getJSONObject(i).optInt("custome_id");
                    paperInfos.add(new PaperInfo(companyName, companyNature, companyType, companyLevel + companyLeve2, 9, custom_id, false));
                }
            }
            if (flag == 10) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Boolean isSlide = (rowArray.getJSONObject(i).getInt("state") == 1 || rowArray.getJSONObject(i).getInt("state") == 4);
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("monthly_code"), rowArray.getJSONObject(i).getString("creator_year"),
                            rowArray.getJSONObject(i).getString("creator_month"), rowArray.getJSONObject(i).getInt("state") + "", "", 10,
                            rowArray.getJSONObject(i).getInt("monthly_id"), rowArray.getJSONObject(i).optInt("flow_details_id"),
                            rowArray.getJSONObject(i).optInt("flow_type"), rowArray.getJSONObject(i).optInt("flow_example_id"), isSlide));
                }
            }
            if (flag == 11) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Boolean isSlide = (rowArray.getJSONObject(i).getInt("state") == 1 || rowArray.getJSONObject(i).getInt("state") == 4);
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("week_code"), rowArray.getJSONObject(i).getString("creator_year"),
                            rowArray.getJSONObject(i).getString("creator_week"), rowArray.getJSONObject(i).getInt("state") + "", "", 11,
                            rowArray.getJSONObject(i).getInt("week_id"), rowArray.getJSONObject(i).optInt("flow_details_id"),
                            rowArray.getJSONObject(i).optInt("flow_type"), rowArray.getJSONObject(i).optInt("flow_example_id"), isSlide));
                }
            }
            if (flag == 12) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Boolean isSlide = (rowArray.getJSONObject(i).getInt("state") == 1 || rowArray.getJSONObject(i).getInt("state") == 4);
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("day_report_code"), rowArray.getJSONObject(i).getString("visit_date"),
                            rowArray.getJSONObject(i).getString("creator_date_ti").endsWith("null") ? "" : rowArray.getJSONObject(i).getString("creator_date_ti"),
                            rowArray.getJSONObject(i).getInt("state") + "", "", 12,
                            rowArray.getJSONObject(i).getInt("day_report_id"), rowArray.getJSONObject(i).optInt("flow_type"),
                            rowArray.getJSONObject(i).optInt("flow_details_id"), rowArray.getJSONObject(i).optInt("flow_example_id"), isSlide));
                }
            }

            if (flag == 13) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Log.i(TAG, "solveResult: " + rowArray.getJSONObject(i).getString("company_name"));
                    String competitor_code = "";
                    String company_name = "";
                    String creator_date = "";
                    String state = "";
                    int competitor_id = -1;
                    state = getJsonData("state", rowArray.getJSONObject(i));
                    Boolean isSlide = (state.equals("1"));
                    competitor_code = getJsonData("competitor_code", rowArray.getJSONObject(i));
                    company_name = getJsonData("company_name", rowArray.getJSONObject(i));
                    creator_date = getJsonData("submit_date", rowArray.getJSONObject(i));
                    competitor_id = rowArray.getJSONObject(i).optInt("competitor_id");
                    paperInfos.add(new PaperInfo(competitor_code, company_name, creator_date.equals("null") ? "" : creator_date, state, 13, competitor_id, isSlide));
                }
            }
            if (flag == 14) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("monthly_code"), rowArray.getJSONObject(i).getString("user_name"), rowArray.getJSONObject(i).getString("creator_year"),
                            rowArray.getJSONObject(i).getInt("examination_result") + "", rowArray.getJSONObject(i).getString("creator_month"), 14, rowArray.getJSONObject(i).getInt("monthly_id"),
                            rowArray.getJSONObject(i).getInt("flow_details_id"), rowArray.getJSONObject(i).getInt("flow_type"),
                            rowArray.getJSONObject(i).getInt("flow_example_id"), rowArray.getJSONObject(i).getInt("examination_result") == -1));
                }
            }
            if (flag == 15) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Boolean isSlide = (rowArray.getJSONObject(i).optInt("examination_result") == -1);
                    String suber = "";
                    if (!rowArray.getJSONObject(i).optString("user_name").toString().equals("null")) {
                        suber = rowArray.getJSONObject(i).optString("user_name");
                    }
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("week_code"), suber, rowArray.getJSONObject(i).getString("creator_year"),
                            rowArray.getJSONObject(i).optInt("examination_result") + "", rowArray.getJSONObject(i).getString("creator_week"), 11,
                            rowArray.getJSONObject(i).getInt("week_id"), rowArray.getJSONObject(i).optInt("flow_details_id"),
                            rowArray.getJSONObject(i).optInt("flow_type"), rowArray.getJSONObject(i).getInt("flow_example_id"), isSlide));
                }
            }
            if (flag == 16) {
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    Boolean isSlide = (rowArray.getJSONObject(i).optInt("examination_result") == -1);
                    paperInfos.add(new PaperInfo(rowArray.getJSONObject(i).getString("day_report_code"), rowArray.getJSONObject(i).getString("user_name"),
                            rowArray.getJSONObject(i).getString("visit_date").endsWith("null") ? "" : rowArray.getJSONObject(i).getString("visit_date"),
                            rowArray.getJSONObject(i).getInt("examination_result") + "", "", 16,
                            rowArray.getJSONObject(i).getInt("day_report_id"), rowArray.getJSONObject(i).getInt("flow_type"),
                            rowArray.getJSONObject(i).getInt("flow_details_id"), rowArray.getJSONObject(i).getInt("flow_example_id"), isSlide));
                }
            }
            if (flag == 17) {     //区域竞品审核
                total = response.getInt("total");
                if (total % 7 == 0) {
                    pages = total / 7;
                } else {
                    pages = total / 7 + 1;
                }
                for (int i = 0; i < rowArray.length(); i++) {
                    String competitor_code = "";
                    String company_name = "";
                    String creator_date = "";
                    String state = "";
                    int competitor_id = -1;
                    state = rowArray.getJSONObject(i).optInt("state") + "";
                    Boolean isSlide = (state.equals("2"));
                    competitor_code = getJsonData("competitor_code", rowArray.getJSONObject(i));
                    company_name = getJsonData("company_name", rowArray.getJSONObject(i));
                    creator_date = getJsonData("user_name", rowArray.getJSONObject(i));
                    competitor_id = rowArray.getJSONObject(i).optInt("competitor_id");
                    Log.i(TAG, "solveResult:state        " + state);
                    paperInfos.add(new PaperInfo(competitor_code, company_name, creator_date, state, 17, competitor_id, isSlide));
                }
            }
            if (total == 0) {
                Toast.makeText(CenterActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                tables.clear();
                tableInfos.clear();
                ReviewTable table = null;
                table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), flag);
                tables.add(table);
            }
            ReviewTable table = null;
            table = new ReviewTable(CenterActivity.this, paperInfos, flag);
            table.setLayoutParams(lp);

            //加入infos,更新数据
            if (total != 0) {
                tables.clear();
                //加入空的tables占位
                if (type == 1) {
                    tableInfos.clear();
                    for (int i = 0; i < pages; i++) {
                        tableInfos.add(new TableInfo(pages, new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), flag), false));
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
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 2);
        }
        if (flag == 3) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 3);
        }
        if (flag == 9) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 9);
        }
        if (flag == 10) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 10);
        }
        if (flag == 11) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 11);
        }
        if (flag == 12) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 12);
        }
        if (flag == 13) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 13);
        }
        if (flag == 14) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 14);
        }
        if (flag == 15) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 15);
        }
        if (flag == 16) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 16);
        }
        if (flag == 17) {
            table = new ReviewTable(CenterActivity.this, new ArrayList<PaperInfo>(), 17);
        }
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.
                LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        table.setLayoutParams(lp1);
        tables = new ArrayList<>();
        tables.add(table);
        adapter = new TablePagerAdapter(CenterActivity.this, tables);
        vp.setAdapter(adapter);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
    }

    private String getJsonData(String key, JSONObject obj) throws JSONException {
        String result = "";
        result = obj.optString(key);
        return result + "";
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
        natureArray = new JSONArray();
        typeArray = new JSONArray();
        pageNumText = (TextView) findViewById(R.id.pages_num);
        vp = (WrapHeightViewPager) findViewById(R.id.order_review_viewpager);
        btnQuery = (Button) findViewById(R.id.mm_query_button);
        btnQuery2 = (Button) findViewById(R.id.query_button);
        btnAdd = (Button) findViewById(R.id.mm_add_button);
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
        ll_review = (LinearLayout) findViewById(R.id.ll_order_review);
        ll_custom_file = (LinearLayout) findViewById(R.id.ll_custom_file);
        ll_btn = (LinearLayout) findViewById(R.id.ll_query_add);
        ll_month = (LinearLayout) findViewById(R.id.ll_month);
        ll_week = (LinearLayout) findViewById(R.id.ll_week);
        ll_day = (LinearLayout) findViewById(R.id.ll_day);
        ll_area_com = (LinearLayout) findViewById(R.id.ll_area_com);
        ll_day_review = (LinearLayout) findViewById(R.id.ll_day_review);
        ll_btn2 = (LinearLayout) findViewById(R.id.ll_query);
        btnQuery3 = (Button) findViewById(R.id.query_button2);
        customTypeEdt = (EditText) findViewById(R.id.edt_custom_type);
        customLevel1 = (EditText) findViewById(R.id.edt_custom_level);
        customLevel2 = (EditText) findViewById(R.id.edt_custom_level2);
        customNatureEdt = (EditText) findViewById(R.id.edt_custom_nature);
        customNameEdt = (EditText) findViewById(R.id.edt_custom_name);
        edtAreaComNum = (EditText) findViewById(R.id.edt_area_com_num);
        edtAreaComSubDate = (EditText) findViewById(R.id.edt_area_com_sub_date);
        edtAreaComToDate = (EditText) findViewById(R.id.edt_area_com_to_date);
        edtAreaComState = (EditText) findViewById(R.id.edt_order_state);
        rlReview = (RelativeLayout) findViewById(R.id.rl_review);
        rlSub = (RelativeLayout) findViewById(R.id.rl_sub);
        rlReportSub = (RelativeLayout) findViewById(R.id.rl_report_sub_state);
        //月报管理
        dateEdt = (EditText) findViewById(R.id.edt_year_month);
        stateEdt = (EditText) findViewById(edt_month_state);
        //周提
        yearEdt = (EditText) findViewById(R.id.edt_year);
        weekEdt = (EditText) findViewById(edt_week);
        weekStateEdt = (EditText) findViewById(R.id.edt_state);
        //日提
        dayEdt = (EditText) findViewById(R.id.edt_day_date);
        toDayEdt = (EditText) findViewById(R.id.edt_day_to_date);
        subDayEdt = (EditText) findViewById(R.id.edt_day_sub_date);
        toSubDayEdt = (EditText) findViewById(R.id.edt_day_to_sub_date);
        dayStateEdt = (EditText) findViewById(R.id.edt_day_state);
        //驳回标识
        txtReject = (TextView) findViewById(R.id.txt_reject);
        //审核
        llDaySubMan = (LinearLayout) findViewById(R.id.ll_day_sub_man);
        edtDaySubMan = (EditText) findViewById(edt_day_sub_man);
        llMonthSubMan = (LinearLayout) findViewById(R.id.ll_month_sub_man);
        edtMonthSubMan = (EditText) findViewById(R.id.edt_month_sub_man);
        llWeekSubMan = (LinearLayout) findViewById(R.id.ll_week_sub_man);
        edtWeekSubMan = (EditText) findViewById(R.id.edt_week_sub_man);
        txtSub = (TextView) findViewById(R.id.txt_sub);
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    //点击"日期"按钮布局 设置日期
    private void pickDate(final EditText edt) {
        final Calendar calendar = Calendar.getInstance();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CenterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        edt.setText(new StringBuilder().append(year).append("-")
                                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                                .append("-")
                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt.setText("");
                    }
                });
                datePickerDialog.show();
            }
        });
    }

    private void pickDate2(final EditText edt) {
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前日期
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                //创建并显示DatePickerDialog
                DatePickerDialog dialog = new DatePickerDialog(CenterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edt.setText(new StringBuilder().append(year).append("-")
                                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1)));
                    }
                }, year, month, day);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt.setText("");
                    }
                });
                dialog.show();

                //只显示年月，隐藏掉日
                DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
                if (dp != null) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                            .getChildAt(2).setVisibility(View.GONE);
                    //如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
                }
            }
        });
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    final Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);

    private void pickDate3(final EditText edt){
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(CenterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edt.setText(new StringBuilder().append(year).append("-")
                                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1)));
                    }
                }, yy, mm, dd) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                        if (mSpinners != null) {
                            NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                            NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                            mSpinners.removeAllViews();
                            if (mYearSpinner != null) {
                                mSpinners.addView(mYearSpinner);
                            }
                            if (mMonthSpinner != null) {
                                mSpinners.addView(mMonthSpinner);
                            }
                        }
                        View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                        if(dayPickerView != null){
                            dayPickerView.setVisibility(View.GONE);
                        }
                    }
                };
                dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt.setText("");
                    }
                });
                dlg.show();
            }
        });
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
