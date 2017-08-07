package com.shanghaigm.dms.view.activity.ck;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.ModelInfo;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAddInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.widget.ChangeletterAddPopWindow;
import com.shanghaigm.dms.view.widget.MmPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeLetterAddActivity extends AppCompatActivity {
    private final String TAG = "ChangeLetterAddActivity";
    private DmsApplication app;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_save, btn_sub;
    public static EditText edt_contract_id, edt_company_name, edt_model, edt_num, edt_contract_price, edt_change_contract_price,
            edt_config_change_date, edt_contract_delivery_date, edt_config_chang_delivery_date;
    private ImageView img_contract_id;
    private LoadingDialog dialog;
    public static ChangeLetterAddInfo changeLetterAddInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_letter_add);
        initView();
        setUpView();
    }

    private void setUpView() {
        title.setText("变更函填写");
        pickDate(edt_config_change_date);
        pickDate(edt_config_chang_delivery_date);
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
                ChangeletterAddPopWindow pop = new ChangeletterAddPopWindow(ChangeLetterAddActivity.this);
                pop.showPopup(img_contract_id);
            }
        });
//            //配置更改函编号
//            private Integer letter_id;
//            //更改内容
//            private String change_content;
//            //价格变更
//            private String price_change;
//            //配置项
//            private String config_item;
//            //工时费
//            private String man_hour;
//            //创建人
//            private String creator_by;
//            //创建日期
//            private String creator_date;
//            //更新人
//            private String update_by ;
//            //更新日期
//            private String update_date;
        //保存
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_contract_id.getText().toString().equals("")) {
                    Toast.makeText(ChangeLetterAddActivity.this, getText(R.string.ck_order_add), Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray letterArray = new JSONArray();
                    JSONObject letterObj = new JSONObject();
                    try {
                        letterObj.put("contract_id", changeLetterAddInfo.getContract_id());
                        letterObj.put("order_id", changeLetterAddInfo.getOrder_id());
                        letterObj.put("letter_id", 0);
                        letterObj.put("change_letter_number", "");
                        letterObj.put("change_contract_price", edt_change_contract_price.getText().toString());
                        letterObj.put("config_change_date", edt_config_change_date.getText().toString());
                        letterObj.put("contract_delivery_date", edt_contract_delivery_date.getText().toString());
                        letterObj.put("config_chang_delivery_date", edt_config_chang_delivery_date.getText().toString());
                        letterObj.put("state", "1");
                        letterObj.put("creator_by", changeLetterAddInfo.getCreator_by());
                        letterObj.put("creator_date", changeLetterAddInfo.getCreator_date());
                        letterObj.put("update_by ", "");
                        letterObj.put("update_date", "");
                        letterArray.put(letterObj);
                        Map<String, Object> params = new HashMap<>();
                        params.put("ents", letterArray.toString());
                        params.put("letterId", "");
                        params.put("loginName", app.getAccount());
                        dialog.showLoadingDlg();
                        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ADD_CHANGE_LETTER, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess:reasonObj       " + responseObj.toString());
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
        });
    }

    public static void setView() {
        Log.i("what", "setView: " + changeLetterAddInfo.getCompany_name());
        edt_contract_id.setText(changeLetterAddInfo.getContract_id());
        edt_company_name.setText(changeLetterAddInfo.getCompany_name());
        edt_model.setText(changeLetterAddInfo.getModels_name());
        edt_num.setText(changeLetterAddInfo.getNumber());
        edt_contract_price.setText(changeLetterAddInfo.getContract_price());
        edt_contract_delivery_date.setText(changeLetterAddInfo.getDelivery_time());
    }

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
    }
}
