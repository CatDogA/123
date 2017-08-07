package com.shanghaigm.dms.view.activity.as;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.HomeFragment;
import com.shanghaigm.dms.view.fragment.as.ReportQueryFragment;
import com.shanghaigm.dms.view.fragment.as.ReportSubFragment;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private FragmentManager fm;
    private HomeFragment homeFragment;
    private ReportSubFragment reportSubFragment;
    private ReportQueryFragment reportQueryFragment;
    private LinearLayout tab_home, tab_sub, tab_query;
    private RelativeLayout back, end;
    private DmsApplication app;
    private TextView title;
    private View line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_activity_home);
        initView();
        setUpView();
        showHome();
    }

    private void setUpView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp();
            }
        });
    }

    private void showHome() {
        showHomeTab();
    }

    private void initView() {
        tab_home = (LinearLayout) findViewById(R.id.home_tab);
        tab_home.setOnClickListener(this);
        tab_sub = (LinearLayout) findViewById(R.id.sub_tab);
        tab_sub.setOnClickListener(this);
        tab_query = (LinearLayout) findViewById(R.id.query_tab);
        tab_query.setOnClickListener(this);

        back = (RelativeLayout) findViewById(R.id.rl_back);
        end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
        line = findViewById(R.id.line);

        app = DmsApplication.getInstance();

        fm = getFragmentManager();
    }

    private void hideFragment(BaseFragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }
    //远程实现刷新操控！
    public static void refresh() {
        ReportSubFragment f = ReportSubFragment.getInstance();
        f.refreshTable();
    }
    public static void refresh2(){
        ReportQueryFragment f = ReportQueryFragment.getInstance();
        f.refreshTable();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_tab:
                showHomeTab();
                break;
            case R.id.sub_tab:
                if(app.getRoleCode().equals("out_service")){
                    showSubTab();
                }
                break;
            case R.id.query_tab:
                showQueryTab();
                break;
        }
    }

    private void setUnSelected() {
        tab_home.setSelected(false);
        tab_sub.setSelected(false);
        tab_query.setSelected(false);

    }

    public void selectFragment(int flag) {
        switch (flag) {
            case 1:
                showSubTab();
                break;
            case 2:
                showQueryTab();
                break;
        }
    }

    private void showHomeTab() {
        FragmentTransaction ft = fm.beginTransaction();
        setUnSelected();
        tab_home.setSelected(true);
        line.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        hideFragment(reportSubFragment, ft);
        hideFragment(reportQueryFragment, ft);
        if (homeFragment == null) {
            homeFragment = HomeFragment.getInstance();
            ft.add(R.id.fragment_content, homeFragment).commit();
        } else {
            ft.show(homeFragment).commit();
        }
    }

    private void showSubTab() {
        FragmentTransaction ft = fm.beginTransaction();
        setUnSelected();
        tab_sub.setSelected(true);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(getResources().getText(R.string.after_report_sub));
        hideFragment(homeFragment, ft);
        hideFragment(reportQueryFragment, ft);
        if (reportSubFragment == null) {
            reportSubFragment = ReportSubFragment.getInstance();
            ft.add(R.id.fragment_content, reportSubFragment).commit();
        } else {
            ft.show(reportSubFragment).commit();
        }
    }

    private void showQueryTab() {
        FragmentTransaction ft = fm.beginTransaction();
        setUnSelected();
        tab_query.setSelected(true);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(getResources().getText(R.string.after_report_query));
        hideFragment(homeFragment, ft);
        hideFragment(reportSubFragment, ft);
        if (reportQueryFragment == null) {
            reportQueryFragment = ReportQueryFragment.getInstance();
            ft.add(R.id.fragment_content, reportQueryFragment).commit();
        } else {
            ft.show(reportQueryFragment).commit();
        }
    }
}
