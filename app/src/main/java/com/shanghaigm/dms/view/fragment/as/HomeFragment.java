package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ReportQueryButton;
import com.shanghaigm.dms.view.widget.ReportSubButton;
import com.shanghaigm.dms.view.widget.VerticalLine;


public class HomeFragment extends BaseFragment {
    private static HomeFragment homeFragment;
    private ReportSubButton reportSubButton;
    private ReportQueryButton reportQueryButton;
    private LinearLayout button_ll;
    private VerticalLine line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        reportSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).selectFragment(1);
            }
        });
        reportQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).selectFragment(2);
            }
        });
    }

    private void initView(View v) {
        button_ll = (LinearLayout) v.findViewById(R.id.button_ll);
        reportSubButton = new ReportSubButton(getActivity());
        reportQueryButton = new ReportQueryButton(getActivity());
        //分割线
        line = new VerticalLine(getActivity());

        //公用
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        //提交
        LinearLayout rl_sub = new LinearLayout(getActivity());
        rl_sub.setLayoutParams(lp);
        rl_sub.addView(reportSubButton);
        //查询
        LinearLayout rl_query = new LinearLayout(getActivity());
        rl_query.setLayoutParams(lp);
        rl_query.addView(reportQueryButton);
        //添加
        button_ll.addView(rl_sub);
        button_ll.addView(line);
        button_ll.addView(rl_query);
    }

    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }
}
