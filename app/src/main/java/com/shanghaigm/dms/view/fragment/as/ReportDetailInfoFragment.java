package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class ReportDetailInfoFragment extends BaseFragment {
    private static ReportDetailInfoFragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_detail_info, container, false);
        return v;
    }

    public static ReportDetailInfoFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportDetailInfoFragment();
        }
        return fragment;
    }
}
