package com.shanghaigm.dms.view.activity.mm;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
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
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityChangeLetterDetailBinding;
import com.shanghaigm.dms.model.Constant;

import java.util.HashMap;
import java.util.Map;

public class ChangeLetterDetailActivity extends AppCompatActivity {
    private static String TAG = "ChangeLetterDetailActivity";
    private DmsApplication app = DmsApplication.getInstance();
    private ActivityChangeLetterDetailBinding binding;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_pass, btn_return;
    private EditText remarks;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_letter_detail);
        initView();
        setUpView();
        initData();
    }

    private void setUpView() {
        title.setText("更改函审核明细");

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
                Map<String, Object> map = new HashMap<>();
                map.put("remarks",remarks.getText().toString());
                map.put("examination_result", 1);
                map.put("loginName", app.getAccount());
                map.put("flow_details_id", app.getFlow_detail_id());
                dialog.showLoadingDlg();
                CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CHANGE_LETTER_REVIEW, map), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj);
                        dialog.dismissLoadingDlg();
                        Toast.makeText(ChangeLetterDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                        btn_return.setEnabled(false);
                        btn_pass.setEnabled(false);
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
                Map<String, Object> map = new HashMap<>();
                if(!remarks.getText().toString().equals("")){
                    map.put("remarks",remarks.getText().toString());
                    map.put("examination_result", 2);
                    map.put("loginName", app.getAccount());
                    map.put("flow_details_id", app.getFlow_detail_id());
                    dialog.showLoadingDlg();
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CHANGE_LETTER_REVIEW, map), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: " + responseObj);
                            dialog.dismissLoadingDlg();
                            Toast.makeText(ChangeLetterDetailActivity.this, "驳回成功", Toast.LENGTH_SHORT).show();
                            btn_return.setEnabled(false);
                            btn_pass.setEnabled(false);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    }));
                }
            }
        });
    }

    private void initData() {
        Log.i(TAG, "initData: " + app.getChangeLetterDetailInfo().getCompany_name());
        binding.setInfo(app.getChangeLetterDetailInfo());
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
}
