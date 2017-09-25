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
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityChangeBillReviewBinding;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class ChangeBillDetailActivity extends BaseActivity {
    private ActivityChangeBillReviewBinding binding;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_pass, btn_return;
    private EditText remarks;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_bill_review);
        initView();
        setUpView();
        initData();
    }

    private void setUpView() {
        title.setText("更改单审核明细");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(ChangeBillDetailActivity.this);
            }
        });

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new HashMap();
                params.put("remarks",remarks.getText().toString());
                params.put("examination_result", 1);
                params.put("loginName", app.getAccount());
                params.put("flow_details_id", app.getFlow_detail_id());
                params.put("jobCode",app.getJobCode());
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CHANGE_BILL_REVIEW, params), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj);
                        dialog.dismissLoadingDlg();
                        Toast.makeText(ChangeBillDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                        btn_return.setEnabled(false);
                        btn_pass.setEnabled(false);
                        Bundle b = new Bundle();
                        b.putInt(HomeActivity.ORDER_BILL_REFRESH,2);
                        goToActivity(HomeActivity.class,b);
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                }));
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new HashMap();
                if(!remarks.getText().toString().equals("")){
                    params.put("examination_result", 2);
                    params.put("remarks",remarks.getText().toString());
                    params.put("loginName", app.getAccount());
                    params.put("flow_details_id", app.getFlow_detail_id());
                    params.put("jobCode",app.getJobCode());
                    dialog.showLoadingDlg();
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CHANGE_BILL_REVIEW, params), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: " + responseObj);
                            dialog.dismissLoadingDlg();
                            Toast.makeText(ChangeBillDetailActivity.this, "驳回成功", Toast.LENGTH_SHORT).show();
                            btn_return.setEnabled(false);
                            btn_pass.setEnabled(false);
                            Bundle b = new Bundle();
                            b.putInt(HomeActivity.ORDER_BILL_REFRESH,2);
                            goToActivity(HomeActivity.class,b);
                            finish();
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    }));
                }else {
                    Toast.makeText(ChangeBillDetailActivity.this, "添加备注", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_text);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        btn_pass = (Button) findViewById(R.id.order_pass_button);
        btn_return = (Button) findViewById(R.id.order_return_back_button);

        remarks = (EditText) findViewById(R.id.edt_review_remarks);

        dialog = new LoadingDialog(this,"正在加载");
    }

    private void initData() {
        binding.setInfo(app.getChangeBillDetailInfo());
    }
}
