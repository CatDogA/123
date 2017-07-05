package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class ReportSubFragment extends BaseFragment {
    private static ReportSubFragment fragment;
    private Button btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_sub, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ReportAddActivity.class);
            }
        });
    }

    private void initView(View v) {
        btn_add = (Button) v.findViewById(R.id.btn_add);
    }

    public static ReportSubFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportSubFragment();
        }
        return fragment;
    }
}
