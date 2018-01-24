package com.shanghaigm.dms.view.activity.ck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.activity.mm.CenterActivity;
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

/**
 *
 */
public class AreaComAddActivity extends BaseActivity {
    private RelativeLayout rl_back, rl_end;
    private static String TAG = "AreaComAddActivity";
    public static String WHERE_FROM = "WHERE_FROM";
    private TextView title;
    private Boolean isAdd = true;
    private LoadingDialog dialog;
    private EditText edtComNum, edtOrderDate, edtCustomName, edtModelFor, edtModel, edtNum, edtPayMethod,
            edtPrice, edtReason;
    private JSONArray buyForArr = new JSONArray();
    private Button btnAreaComSub;
    private String areaComNum = "", customName;
    private int customId;
    private int areaComId;
    private JSONObject areaComInfo;
    private String carBuyForId = "";
    private ArrayList<EditText> edts;
    private String state;
    private ImageView imgDate, imgSearch, imgDown;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_com_add);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    @Override
    protected void onNewIntent(Intent intent) {   //之后的进入
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.getString(WHERE_FROM).equals("add")) {
                areaComNum = b.getString(CenterActivity.AREA_COM_NUM);
            }
            if (b.getString(WHERE_FROM).equals("modify")) {
                isAdd = false;
                areaComId = b.getInt(ReviewTable.TASK_ID);
                requestAreaComInfo(areaComId);
                state = b.getString(ReviewTable.STATE_ID);
            }
            if (b.getString(WHERE_FROM).equals("custom_info")) {
                flag = 1;
                customId = b.getInt(CustomInfoTable.GET_CUSTOM_ID);
                customName = b.getString(CustomInfoTable.GET_CUSTOM_NAME);
                edtCustomName.setText(customName);
            }
        }
    }

    private void initIntent() {    //第一次进入
        dialog = new LoadingDialog(this,"正在加载");
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getString(WHERE_FROM).equals("add")) {
                areaComNum = b.getString(CenterActivity.AREA_COM_NUM);
            }
            if (b.getString(WHERE_FROM).equals("modify")) {
                isAdd = false;
                areaComId = b.getInt(ReviewTable.TASK_ID);
                state = b.getString(ReviewTable.STATE_ID);
                requestAreaComInfo(areaComId);
            }
            if (b.getString(WHERE_FROM).equals("custom_info")) {
                isAdd = false;
                flag = 1;
                areaComId = b.getInt(ReviewTable.TASK_ID);
                state = b.getString(ReviewTable.STATE_ID);
                requestAreaComInfo(areaComId);
            }
        }
    }

    private void initData() {
        edts = new ArrayList<>();
        edts.add(edtComNum);
        edts.add(edtOrderDate);
        edts.add(edtCustomName);
        edts.add(edtModelFor);
        edts.add(edtModel);
        edts.add(edtNum);
        edts.add(edtPayMethod);
        edts.add(edtPrice);
        edts.add(edtReason);
    }

    private void setUpView() {
        title.setText("区域竞品管理");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(AreaComAddActivity.this);
            }
        });
        if (isAdd) {
            edtComNum.setText(areaComNum);
        }
        pickDate(edtOrderDate);
        edtCustomName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(AreaComCustomSearchPop.class);
            }
        });
        edtModelFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_BUY_CAR_FOR, null, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        buyForArr = (JSONArray) responseObj;
                        ArrayList<PopListInfo> natures = new ArrayList<>();
                        try {
                            natures.add(new PopListInfo(""));
                            for (int i = 0; i < buyForArr.length(); i++) {
                                natures.add(new PopListInfo(buyForArr.getJSONObject(i).getString("date_value")));
                            }
                            MmPopupWindow naturePopup = new MmPopupWindow(AreaComAddActivity.this, edtModelFor, natures, 3);
                            naturePopup.showPopup(edtModelFor);
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
        edtModelFor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyForArr.length() != 0) {
                    for (int i = 0; i < buyForArr.length(); i++) {
                        try {
                            if (s.toString().equals(buyForArr.getJSONObject(i).optString("date_value"))) {
                                carBuyForId = buyForArr.getJSONObject(i).optString("date_key");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        btnAreaComSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String areaComNum = edtComNum.getText().toString();
                String orderDate = edtOrderDate.getText().toString();
                String customName = edtCustomName.getText().toString();
                String carBuyFor = edtModelFor.getText().toString();
                String orderModel = edtModel.getText().toString();
                String num = edtNum.getText().toString();
                String payMethod = edtPayMethod.getText().toString();
                String price = edtPrice.getText().toString();
                final String reason = edtReason.getText().toString();
                JSONObject paramObject = new JSONObject();
                JSONArray paramArr = new JSONArray();
                try {
                    if (!areaComNum.equals("") && !orderDate.equals("") && !customName.equals("") &&
                            !carBuyFor.equals("") && !orderModel.equals("") && !num.equals("") &&
                            !payMethod.equals("") && !price.equals("") && !reason.equals("")) {
                        paramObject.put("competitor_code", areaComNum);
                        paramObject.put("signing_date", orderDate);
                        paramObject.put("company_name", customName);
                        paramObject.put("purpose", carBuyForId);
                        paramObject.put("models_name", orderModel);
                        paramObject.put("num", num);
                        paramObject.put("payment_method", payMethod);
                        paramObject.put("deal_price", price);
                        paramObject.put("lose_order_reason", reason);
                        paramObject.put("custome_id", customId);
                        paramObject.put("purpose_name", carBuyFor);
                        if (!isAdd) {
                            paramObject.put("competitor_id", areaComId);
                        }
                        paramArr.put(paramObject);
                        Map<String, Object> params = new HashMap<>();
                        params.put("crmRegionCompetitor", paramArr.toString());
                        params.put("login_name", app.getAccount());
                        String url = "";
                        if (isAdd) {
                            url = Constant.URL_AREA_COM_SUB;
                        } else {
                            url = Constant.URL_MODIFY_AREA_COM;
                        }
                        Log.i(TAG, "onClick: " + params.toString());
                        OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                try {
                                    if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                        if (isAdd) {
                                            Toast.makeText(AreaComAddActivity.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AreaComAddActivity.this, getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                                        }
                                        goToActivity(CenterActivity.class);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Log.i(TAG, "onFailure: " + reasonObj.toString());
                            }
                        });
                    } else {
                        Toast.makeText(AreaComAddActivity.this, getString(R.string.full_filled), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestAreaComInfo(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("competitor_id", id);
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_AREA_COM_CUSTOM_DETAIL_INFO, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess: " + responseObj.toString());
                areaComInfo = (JSONObject) responseObj;
                setAreaComView();
                dialog.dismissLoadingDlg();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setAreaComView() {
        if (!isAdd) {
            try {
                edtComNum.setText(areaComInfo.getString("competitor_code"));
                edtOrderDate.setText(areaComInfo.getString("signing_date").split(" ")[0]);
                edtCustomName.setText(areaComInfo.getString("company_name"));
                edtModelFor.setText(areaComInfo.getString("purpose_name"));
                edtModel.setText(areaComInfo.getString("models_name"));
                edtNum.setText(areaComInfo.getString("num"));
                edtPayMethod.setText(areaComInfo.getString("payment_method"));
                edtPrice.setText(areaComInfo.getString("deal_price"));
                edtReason.setText(areaComInfo.getString("lose_order_reason"));
                carBuyForId = areaComInfo.getString("purpose");
                customId = areaComInfo.getInt("custome_id");
                if (!state.equals("1") || flag == 1) {
                    imgDate.setVisibility(View.GONE);
                    imgDown.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.GONE);
                    for (int i = 0; i < edts.size(); i++) {
                        edts.get(i).setEnabled(false);
                        btnAreaComSub.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        edtComNum = (EditText) findViewById(R.id.edt_com_num);
        edtOrderDate = (EditText) findViewById(R.id.edt_order_date);
        edtCustomName = (EditText) findViewById(R.id.edt_custom_name);
        edtModelFor = (EditText) findViewById(R.id.edt_model_for);
        edtModel = (EditText) findViewById(R.id.edt_model);
        edtNum = (EditText) findViewById(R.id.edt_order_num);
        edtPayMethod = (EditText) findViewById(R.id.edt_pay_method);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtReason = (EditText) findViewById(R.id.edt_reason);
        btnAreaComSub = (Button) findViewById(R.id.btn_sub);
        imgDate = (ImageView) findViewById(R.id.img_date);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgDown = (ImageView) findViewById(R.id.img_down);

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
                new DatePickerDialog(AreaComAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
}
