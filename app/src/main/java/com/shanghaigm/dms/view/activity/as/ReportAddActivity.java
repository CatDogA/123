package com.shanghaigm.dms.view.activity.as;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;
import com.shanghaigm.dms.view.fragment.as.ReportInfoFragment;

import java.util.ArrayList;

public class ReportAddActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private FragmentManager fm;
    private ReportInfoFragment reportInfoFragment;
    private ArrayList<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);
        initView();
        initData();
        setUpView();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(ReportInfoFragment.getInstance());
        fragments.add(ReportAttachSubFragment.getInstance());
    }

    private void setUpView() {
        tablayout.setSelectedTabIndicatorColor(Color.GRAY);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tablayout.addTab(tablayout.newTab().setTag(0).setText(getResources().getText(R.string.report_info)));
        tablayout.addTab(tablayout.newTab().setTag(1).setText(getResources().getText(R.string.attach_sub)));

        //先显示基本信息页
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        reportInfoFragment = ReportInfoFragment.getInstance();
        ft.add(R.id.report_add_content, reportInfoFragment).commit();

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                int tag = (int) tab.getTag();
                if (fragments.get(tag).isAdded()) {
                    ft.show(fragments.get(tag)).commit();
                } else {
                    ft.add(R.id.report_add_content, fragments.get(tag)).commit();
                }
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
        tablayout = (TabLayout) findViewById(R.id.tab_layout_report);
    }
}
