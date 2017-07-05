package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class ReportInfoFragment extends BaseFragment {
    private static ReportInfoFragment reportInfoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_info, container, false);
    }

    public static ReportInfoFragment getInstance() {
        if (reportInfoFragment == null) {
            reportInfoFragment = new ReportInfoFragment();
        }
        return reportInfoFragment;
    }
}
