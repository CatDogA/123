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

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailAttachFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailInfoFragment;

import java.util.ArrayList;

public class ReportDetailActivity extends AppCompatActivity {
    private ArrayList<BaseFragment> fragments;
    private TabLayout tabLayout;
    private RelativeLayout rl_back, rl_end;
    private TextView title;
    private FragmentManager fm;
    private DmsApplication app;
    private static String TAG = "ReportDetailActivity";
    public static String REPORT_DETAIL_INFO = "report_detail_info_one";
    public static ArrayList<ArrayList<PathInfo>> allPaths;

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
        ReportQueryDetailInfoBean reportDetailInfo = (ReportQueryDetailInfoBean) bundle.getSerializable(PaperInfo.REPORT_DETAI_INFO);
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
    }
}
