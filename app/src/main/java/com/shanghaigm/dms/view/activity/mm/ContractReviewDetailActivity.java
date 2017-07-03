package com.shanghaigm.dms.view.activity.mm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityContractReviewDetailBinding;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContractReviewDetailActivity extends BaseActivity {
    private static String TAG = "ContractReviewDetailActivity";
    private ActivityContractReviewDetailBinding binding;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_pass, btn_return;
    private EditText remarks;
    private LoadingDialog dialog;
    private static String ISREFRESH = "refresh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contract_review_detail);
        initView();
        setUpView();
        initData();
    }

    private void initData() {
        binding.setInfo(app.getContractDetailInfo());
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        btn_pass = (Button) findViewById(R.id.order_pass_button);
        btn_return = (Button) findViewById(R.id.order_return_back_button);
        remarks = (EditText) findViewById(R.id.edt_review_remarks);
        dialog = new LoadingDialog(this, "正在加载");
    }

    private void setUpView() {
        title.setText("合同审核明细");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Map<String, Object> map = new HashMap<>();
                    Log.i(TAG, "onClick: remarks        " + remarks.getText().toString());
                    map.put("remarks", remarks.getText().toString());

                    JSONObject object = new JSONObject();
                    object.put("contract_id", app.getContract_id());
                    JSONArray array = new JSONArray();
                    array.put(object);

                    map.put("conts", array.toString());

                    map.put("examination_result", 1);
                    map.put("loginName", app.getAccount());
                    map.put("flow_details_id", app.getFlow_detail_id());
                    dialog.showLoadingDlg();
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CONTRACT_REVIEW, map), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj);
                            Toast.makeText(ContractReviewDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                            btn_return.setEnabled(false);
                            btn_pass.setEnabled(false);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    }));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v
             */
            @Override
            public void onClick(View v) {
                try {
                    Map<String, Object> map = new HashMap<>();
                    if (!remarks.getText().toString().equals("")) {
                        map.put("remarks", remarks.getText().toString());

                        JSONObject object = new JSONObject();
                        object.put("contract_id", app.getContract_id());
                        JSONArray array = new JSONArray();
                        array.put(object);

                        map.put("conts", array.toString());

                        map.put("examination_result", 2);
                        map.put("loginName", app.getAccount());
                        map.put("flow_details_id", app.getFlow_detail_id());
                        dialog.showLoadingDlg();
                        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CONTRACT_REVIEW, map), new DisposeDataHandle(new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess: " + responseObj);
                                Toast.makeText(ContractReviewDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                                btn_return.setEnabled(false);
                                btn_pass.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Object reasonObj) {

                            }
                        }));
                    } else {
                        Toast.makeText(ContractReviewDetailActivity.this, "备注不可为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
