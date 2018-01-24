package com.shanghaigm.dms.view.activity.mm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.model.util.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.BR;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.ActivityContractReviewDetailBinding;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.mm.MatchingBean;
import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.adapter.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContractReviewDetailActivity extends BaseActivity {
    private static String TAG = "ContractReviewDeta";
    private ActivityContractReviewDetailBinding binding;
    private TextView title;
    private RelativeLayout rl_back, rl_end;
    private Button btn_pass, btn_return;
    private EditText remarks;
    private LoadingDialog dialog;
    public static ArrayList<MatchingBean> matchings;
    private static String ISREFRESH = "refresh";
    private ListView listview;
    private ListAdapter adapter;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contract_review_detail);
        initView();
        setUpView();
        initData();
        initIntent();
    }

    private void initIntent() {
        if (matchings != null) {
            matchings.clear();
        }
        Bundle b = getIntent().getExtras();
        ArrayList<MatchingBean> matchings_get = (ArrayList<MatchingBean>) b.getSerializable(PaperInfo.CONTRACT_MATCHING_INFOS);
        if (matchings_get != null) {
            matchings = matchings_get;
        }
        ArrayList<OrderDetailInfoAllocation> allocations = new ArrayList<>();
        if (matchings != null) {
            for (int i = 0; i < matchings.size(); i++) {
                MatchingBean bean = matchings.get(i);
                OrderDetailInfoAllocation allocation = new OrderDetailInfoAllocation(bean.assembly, bean.entry_name, bean.config_information, bean.num + "", bean.remarks, bean.isdefault);
                allocations.add(allocation);
            }
        }
        adapter = new ListAdapter(this, R.layout.list_item_allocation, BR.info, allocations);
        listview.setAdapter(adapter);
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                scrollView.requestDisallowInterceptTouchEvent(true); 
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

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
        listview = (ListView) findViewById(R.id.list_allocation);
        dialog = new LoadingDialog(this, "正在加载");
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
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
                    map.put("jobCode", app.getJobCode());
                    dialog.showLoadingDlg();
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_CONTRACT_REVIEW, map), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj);
                            Toast.makeText(ContractReviewDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                            btn_return.setEnabled(false);
                            btn_pass.setEnabled(false);

                            goToActivity(CenterActivity.class);
                            finish();
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
                        map.put("jobCode", app.getJobCode());
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

                                goToActivity(CenterActivity.class);
                                finish();
                            }

                            @Override
                            public void onFailure(Object reasonObj) {

                            }
                        }));
                    } else {
                        Toast.makeText(ContractReviewDetailActivity.this, getText(R.string.need_remark), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
