package com.shanghaigm.dms.view.activity.ck;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityChangeLetterAddBinding;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAddInfo;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAllocationInfo;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.widget.ChangeLetterAllocationTable;
import com.shanghaigm.dms.view.widget.ChangeletterAddPopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeLetterAddActivity extends BaseActivity {
    private final String TAG = "ChangeLetterAddActivity";
    public static Boolean isAdded;   //判断是否是新增
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_save, btn_sub;
    public static EditText edt_contract_id, edt_company_name, edt_model, edt_num, edt_contract_price, edt_change_contract_price,
            edt_config_change_date, edt_contract_delivery_date, edt_config_chang_delivery_date;
    private ImageView img_contract_id;
    private LoadingDialog dialog;
    public static ChangeLetterAddInfo changeLetterAddInfo;
    private ArrayList<ChangeLetterAllocationInfo> allocationInfos = new ArrayList<>();
    private LinearLayout llChangeLetter;
    private JSONArray dataList;
    private JSONObject letterObj;
    private int letterId;
    private int flag = 0;   //判断是否为修改
    private int type = 0;   //判断是否再次选择contract_id
    private ActivityChangeLetterAddBinding binding;
    private DmsApplication app;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_letter_add);
        initView();
        setUpView();
        initIntent();
    }

    private void initIntent() {
        LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            allocationInfos = (ArrayList<ChangeLetterAllocationInfo>) b.getSerializable(PaperInfo.CHANGE_LETTER_INFO);
            flag = 1;
            ChangeLetterAllocationTable table = new ChangeLetterAllocationTable(ChangeLetterAddActivity.this, allocationInfos, scrollView);
            table.setLayoutParams(tableParams);
            llChangeLetter.addView(table);
        } else {
            allocationInfos.add(new ChangeLetterAllocationInfo("", "", "", ""));
            ChangeLetterAllocationTable table = new ChangeLetterAllocationTable(ChangeLetterAddActivity.this, allocationInfos, scrollView);
            table.setLayoutParams(tableParams);
            llChangeLetter.addView(table);
        }
        scrollView.smoothScrollTo(0, 0);
        scrollView.setFocusable(true);
    }

    private void setUpView() {
        if (app.getChangeLetterSubDetailInfo() != null) {
            binding.setInfo(app.getChangeLetterSubDetailInfo());
        }
        title.setText("变更函填写");
        pickDate(edt_config_change_date);
        edt_config_change_date.setText(getDate());
        pickDate(edt_config_chang_delivery_date);
        pickDate(edt_contract_delivery_date);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp();
            }
        });
        img_contract_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                ChangeletterAddPopWindow pop = new ChangeletterAddPopWindow(ChangeLetterAddActivity.this);
                pop.showPopup(img_contract_id);
            }
        });
        if (isAdded) {
            btn_sub.setEnabled(false);
        }
        //保存
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                if (edt_contract_id.getText().toString().equals("")) {
                    count++;
                }
                if (edt_contract_delivery_date.getText().toString().equals("")) {
                    count++;
                }
                if (edt_config_chang_delivery_date.getText().toString().equals("")) {
                    count++;
                }
                if (edt_config_change_date.getText().toString().equals("")) {
                    count++;
                }
                if (count > 0) {
                    Toast.makeText(ChangeLetterAddActivity.this, getText(R.string.finish_info), Toast.LENGTH_SHORT).show();
                } else {
                    if (edt_contract_id.getText().toString().equals("")) {
                        Toast.makeText(ChangeLetterAddActivity.this, getText(R.string.fill_contract), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray letterArray = new JSONArray();
                        try {
                            for (ChangeLetterAllocationInfo info : allocationInfos) {
                                JSONObject infoObj = new JSONObject();
                                infoObj.put("config_item", info.getConfig_item());
                                infoObj.put("change_content", info.getChange_content());
                                infoObj.put("price_change", info.getPrice());
                                infoObj.put("man_hour", info.getMan_hour());
                                dataList.put(infoObj);
                            }
                            if (flag == 0 || type == 1) {
                                letterObj.put("contract_id", changeLetterAddInfo.getContract_id());
                                letterObj.put("order_id", changeLetterAddInfo.getOrder_id());
                                letterObj.put("letter_id", "");
                                letterObj.put("change_letter_number", "");
                                letterObj.put("state", "1");
                            }
                            letterObj.put("change_contract_price", edt_change_contract_price.getText().toString());
                            letterObj.put("config_change_date", edt_config_change_date.getText().toString());
                            letterObj.put("contract_delivery_date", edt_contract_delivery_date.getText().toString());
                            letterObj.put("config_chang_delivery_date", edt_config_chang_delivery_date.getText().toString());
                            letterObj.put("creator_by", app.getAccount());
                            letterObj.put("creator_date", getDate());
                            letterObj.put("update_by ", app.getAccount());
                            letterObj.put("update_date", getDate());
                            if (flag == 1) {
                                if (type == 0) {
                                    letterObj.put("contract_id", app.getChangeLetterSubDetailInfo().getContract_id());
                                    letterObj.put("order_id", app.getChangeLetterSubDetailInfo().getOrder_id());
                                    letterObj.put("letter_id", app.getChangeLetterSubDetailInfo().getLetter_id() + "");
                                    letterObj.put("change_letter_number", app.getChangeLetterSubDetailInfo().getChange_letter_number());
                                    letterObj.put("state", "2");
                                }
                            }
                            letterArray.put(letterObj);
                            Map<String, Object> params = new HashMap<>();
                            params.put("ents", letterArray.toString());
                            params.put("letterId", "");
                            params.put("dataList", dataList.toString());
                            params.put("loginName", app.getAccount());
                            if (flag == 1) {
                                params.put("letterId", app.getChangeLetterSubDetailInfo().getLetter_id() + "");
                            }
                            dialog.showLoadingDlg();
                            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADD_CHANGE_LETTER, params, new DisposeDataListener() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    dialog.dismissLoadingDlg();
//                                    {"message":"加载成功","resultCode":"true","resultEntity":93}
                                    Toast.makeText(ChangeLetterAddActivity.this, getText(R.string.save_info_success), Toast.LENGTH_SHORT).show();
                                    JSONObject result = (JSONObject) responseObj;
                                    try {
                                        letterId = result.getInt("resultEntity");
                                        btn_save.setEnabled(false);
                                        btn_sub.setEnabled(true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Object reasonObj) {
                                    dialog.dismissLoadingDlg();
                                    Log.i(TAG, "onFailure:reasonObj         " + reasonObj.toString());
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", letterId);
                if (flag == 1) {
                    if (type == 0) {
                        params.put("id", app.getChangeLetterSubDetailInfo().getLetter_id());
                    }
                }
                params.put("loginName", app.getAccount());
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_SUB_CHANGE_LETTER, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onSuccess:      " + responseObj.toString());
                        Toast.makeText(ChangeLetterAddActivity.this, getText(R.string.sub_success), Toast.LENGTH_SHORT).show();
                        btn_sub.setEnabled(false);
                        Bundle b = new Bundle();
                        b.putInt(HomeActivity.ORDER_LETTER_SUB,2);
                        goToActivity(HomeActivity.class,b);
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        dialog.dismissLoadingDlg();
                        Log.i(TAG, "onFailure:reasonObj         " + reasonObj.toString());
                    }
                });
            }
        });
    }

    public static void setView() {
        if (changeLetterAddInfo != null) {
            edt_contract_id.setText(changeLetterAddInfo.getContract_id());
            edt_company_name.setText(changeLetterAddInfo.getCompany_name());
            edt_model.setText(changeLetterAddInfo.getModels_name());
            edt_num.setText(changeLetterAddInfo.getNumber());
            edt_contract_price.setText(changeLetterAddInfo.getContract_price());
            edt_change_contract_price.setText(changeLetterAddInfo.getContract_price());
        }
    }

    //获取系统时间
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String date = sdf.format(new java.util.Date());
        return date;
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
                new DatePickerDialog(ChangeLetterAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void initView() {
//        清空数据
        changeLetterAddInfo = null;
        letterObj = new JSONObject();
        dataList = new JSONArray();
        letterId = -1;

        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_sub = (Button) findViewById(R.id.btn_sub);

        app = DmsApplication.getInstance();
        dialog = new LoadingDialog(ChangeLetterAddActivity.this, getResources().getText(R.string.loading).toString());
        img_contract_id = (ImageView) findViewById(R.id.img_contract_id);

        edt_contract_id = (EditText) findViewById(R.id.edt_contract_id);
        edt_company_name = (EditText) findViewById(R.id.edt_company_name);
        edt_model = (EditText) findViewById(R.id.edt_model);
        edt_num = (EditText) findViewById(R.id.edt_num);
        edt_contract_price = (EditText) findViewById(R.id.edt_contract_price);
        edt_change_contract_price = (EditText) findViewById(R.id.edt_change_contract_price);
        edt_config_change_date = (EditText) findViewById(R.id.edt_config_change_date);
        edt_contract_delivery_date = (EditText) findViewById(R.id.edt_contract_delivery_date);
        edt_config_chang_delivery_date = (EditText) findViewById(R.id.edt_config_chang_delivery_date);
        llChangeLetter = (LinearLayout) findViewById(R.id.ll_change_letter);
        app = DmsApplication.getInstance();
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
    }
}
