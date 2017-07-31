package com.shanghaigm.dms.view.activity.as;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailAttachFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailInfoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportDetailActivity extends AppCompatActivity {
    private ArrayList<BaseFragment> fragments;
    private TabLayout tabLayout;
    private RelativeLayout rl_back, rl_end;
    private TextView title;
    private FragmentManager fm;
    private DmsApplication app;
    private static String TAG = "ReportDetailActivity";
    public static String REPORT_DETAIL_INFO = "report_detail_info_one";
    public static ArrayList<ArrayList<PathInfo>> allPaths = null;
    private Button btn_return_back;
    private ReportQueryDetailInfoBean reportDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = getIntent().getExtras();
        reportDetailInfo = (ReportQueryDetailInfoBean) bundle.getSerializable(PaperInfo.REPORT_DETAI_INFO);
        if (allPaths != null) {
            allPaths.clear();
        }
        allPaths = (ArrayList<ArrayList<PathInfo>>) bundle.getSerializable(PaperInfo.REPORT_FILE_INFO);

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(ReportDetailActivity.REPORT_DETAIL_INFO, reportDetailInfo);
        ReportDetailInfoFragment fragment = ReportDetailInfoFragment.getInstance();
        fragment.setArguments(fragmentBundle);
        ft.add(R.id.fragment_content, fragment).commit();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(ReportDetailInfoFragment.getInstance());
        fragments.add(ReportDetailAttachFragment.getInstance());
        app = DmsApplication.getInstance();
    }

    private void setUpView() {
        if (app.getRoleCode().equals("out_service")) {
            btn_return_back.setVisibility(View.GONE);
        }
        btn_return_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("state", 3);
                    obj.put("daily_id", reportDetailInfo.daily_id);
                    array.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, Object> params = new HashMap<>();
                params.put("loginName", app.getAccount());
                params.put("dailyStr", array.toString());
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_SUB_REPORT, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:      " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            JSONObject result = obj.getJSONObject("resultEntity");
                            String code = result.getString("returnCode");
                            if (code.equals("1")) {
                                Toast.makeText(ReportDetailActivity.this, getResources().getText(R.string.return_success), Toast.LENGTH_SHORT).show();
                                finish();
                                //刷新页面2
                                HomeActivity.refresh2();
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
        });

        title.setText(getResources().getText(R.string.report_detail));
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.OutOfApp();
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.report_info)).setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.attacn_preview)).setTag(1));

//        fm.beginTransaction().add(R.id.fragment_content, fragments.get(0)).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                int tag = (int) tab.getTag();
                if (!fragments.get(tag).isAdded()) {
                    ft.add(R.id.fragment_content, fragments.get(tag));
                } else {
                    ft.show(fragments.get(tag));
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tag = (int) tab.getTag();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(fragments.get(tag)).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_report);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
        btn_return_back = (Button) findViewById(R.id.btn_return_back);
    }
}
