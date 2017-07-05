package com.shanghaigm.dms.view.fragment.as;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class ReportAttachSubFragment extends BaseFragment {
    private static ReportAttachSubFragment reportAttachSubFragment;
    private Button btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_attach_sub, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
//        btn_add = v.findViewById(R.id.)
    }

    public static ReportAttachSubFragment getInstance() {
        if (reportAttachSubFragment == null) {
            reportAttachSubFragment = new ReportAttachSubFragment();
        }
        return reportAttachSubFragment;
    }
}
