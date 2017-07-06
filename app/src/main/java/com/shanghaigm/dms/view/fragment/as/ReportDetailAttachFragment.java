package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;

public class ReportDetailAttachFragment extends BaseFragment {
    private static ReportDetailAttachFragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail_attach, container, false);
    }

    public static ReportDetailAttachFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportDetailAttachFragment();
        }
        return fragment;
    }
}
