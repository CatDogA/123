package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.ReportDetailActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class ReportQueryFragment extends BaseFragment {
    private static ReportQueryFragment fragment;
    private Button btn_add,btn_query;
    private TextView mid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_query, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        btn_add.setVisibility(View.GONE);
        mid.setVisibility(View.GONE);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ReportDetailActivity.class);
            }
        });
    }

    private void initView(View v) {
        btn_add = (Button) v.findViewById(R.id.btn_add);
        mid = (TextView) v.findViewById(R.id.view_mid);
        btn_query = (Button) v.findViewById(R.id.btn_query);
    }

    public static ReportQueryFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportQueryFragment();
        }
        return fragment;
    }
}
