package com.shanghaigm.dms.view.activity.mm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderDetailAllocationInfoFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderDetailInfoOneFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderDetailInfoTwoFragment;
import com.shanghaigm.dms.view.fragment.mm.OrderReviewFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity {
    private static String TAG = "OrderDetailActivity";
    private LinearLayout remarkLayout;
    private TextView titleText;
    private TabLayout tabLayout;
    private ArrayList<BaseFragment> fragments;
    private FrameLayout fragmentContent;
    private FragmentManager manager;
    private Button pass, returnBack;
    private LoadingDialog dialog;
    private DmsApplication app;
    private RelativeLayout rl_back, rl_end;
    private EditText remarks;
    private RelativeLayout rl_remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
        setUpView();
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.title_text);
        tabLayout = (TabLayout) findViewById(R.id.mm_order_detail_tab);
        fragmentContent = (FrameLayout) findViewById(R.id.mm_order_info_fragment_content);

        pass = (Button) findViewById(R.id.order_pass_button);
        returnBack = (Button) findViewById(R.id.order_return_back_button);
        rl_remarks = (RelativeLayout) findViewById(R.id.rl_remarks);
        dialog = new LoadingDialog(this, "正在加载");
        app = DmsApplication.getInstance();

        remarks = (EditText) findViewById(R.id.edt_review_remarks);
    }

    private void initData() {
        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addTab(tabLayout.newTab().setText("基本信息").setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText("付款方式").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("选配").setTag(2));

        fragments = new ArrayList<>();
        fragments.add(OrderDetailInfoOneFragment.getInstance());
        fragments.add(OrderDetailInfoTwoFragment.getInstance());
        fragments.add(OrderDetailAllocationInfoFragment.getInstance());

        manager = getFragmentManager();
        manager.beginTransaction().add(R.id.mm_order_info_fragment_content, fragments.get(0)).commit();

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
    }

    private void setUpView() {
        titleText.setText(String.format(getString(R.string.mm_order_detail)));
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
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = Integer.parseInt(tab.getTag().toString());

                if (!fragments.get(index).isAdded()) {
                    manager.beginTransaction().add(R.id.mm_order_info_fragment_content, fragments.get(index)).commit();
                } else {
                    manager.beginTransaction().show(fragments.get(index)).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int poi = Integer.parseInt(tab.getTag().toString());
                manager.beginTransaction().hide(fragments.get(poi)).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString(PaperInfo.ORDER_TYPE);
        if (type.equals(PaperInfo.ORDER_REVIEW_DETIAL_INFO)) {
            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> params = new HashMap();
                    params.put("remarks", remarks.getText().toString());
                    params.put("order_id", app.getOrderDetailInfoBean().resultEntity.order_id);
                    params.put("flow_details_id", app.getFlow_detail_id());
                    params.put("examination_result", 1);
                    params.put("loginName", app.getAccount());
                    params.put("jobCode", app.getJobCode());
                    Log.i(TAG, "onClick:  flow_detail_id     " + app.getFlow_detail_id() + "    loginName   " + app.getAccount() + "    orderId     " + app.getOrderDetailInfoBean().resultEntity.order_id);
                    dialog.showLoadingDlg();
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_REVIEW, params), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            JSONObject object = (JSONObject) responseObj;
                            try {
                                int result = object.getInt("resultEntity");
                                if (result == 1) {
                                    Toast.makeText(OrderDetailActivity.this, "通过成功", Toast.LENGTH_SHORT).show();
                                    returnBack.setEnabled(false);
                                    pass.setEnabled(false);
                                    finish();
//                                    OrderReviewFragment fragment = OrderReviewFragment.getInstance();
//                                    fragment.refresh();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            Toast.makeText(OrderDetailActivity.this, "通过失败", Toast.LENGTH_SHORT).show();
                        }
                    }));
                }
            });

            returnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map params = new HashMap();
                    if (!remarks.getText().toString().equals("")) {
                        params.put("remarks", remarks.getText().toString());
                        params.put("order_id", app.getOrderDetailInfoBean().resultEntity.order_id);
                        params.put("flow_details_id", app.getFlow_detail_id());
                        params.put("examination_result", 2);
                        params.put("loginName", app.getAccount());
                        params.put("jobCode", app.getJobCode());
                        Log.i(TAG, "onClick:  flow_detail_id     " + app.getFlow_detail_id() + "    loginName   " + app.getAccount() + "    orderId     " + app.getOrderDetailInfoBean().resultEntity.order_id);
                        dialog.showLoadingDlg();
                        CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_REVIEW, params), new DisposeDataHandle(new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                dialog.dismissLoadingDlg();
                                Log.i(TAG, "onSuccess: " + responseObj.toString());
                                JSONObject object = (JSONObject) responseObj;
                                try {
                                    int result = object.getInt("resultEntity");
                                    if (result == 1) {
                                        Toast.makeText(OrderDetailActivity.this, "驳回成功", Toast.LENGTH_SHORT).show();
                                        returnBack.setEnabled(false);
                                        pass.setEnabled(false);
                                        finish();
                                        OrderReviewFragment fragment = OrderReviewFragment.getInstance();
                                        fragment.refresh();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Object reasonObj) {
                                Toast.makeText(OrderDetailActivity.this, "驳回失败", Toast.LENGTH_SHORT).show();
                            }
                        }));
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "需要添加备注", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (type.equals(PaperInfo.ORDER_SUB_DETAIL_INFO)) {
            rl_remarks.setVisibility(View.GONE);
        }
    }
}
