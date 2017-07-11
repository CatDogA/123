package com.shanghaigm.dms.view.fragment.as;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.databinding.FragmentReportDetailInfoBinding;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.view.activity.as.ReportDetailActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class ReportDetailInfoFragment extends BaseFragment {
    private static ReportDetailInfoFragment fragment;
    private static String TAG = "ReportDetailInfo";
    private FragmentReportDetailInfoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_detail_info, container, false);
        binding = DataBindingUtil.bind(v);
        initBundle();
        return v;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        ReportQueryDetailInfoBean reportInfo = (ReportQueryDetailInfoBean) bundle.getSerializable(ReportDetailActivity.REPORT_DETAIL_INFO);
        ReportQueryDetailInfo info = new ReportQueryDetailInfo(reportInfo);
        binding.setInfo(info);
    }

    public static ReportDetailInfoFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportDetailInfoFragment();
        }
        return fragment;
    }
}
