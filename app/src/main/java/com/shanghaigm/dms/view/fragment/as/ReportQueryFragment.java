package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.BaseFragment;


public class ReportQueryFragment extends BaseFragment {
    private static ReportQueryFragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_query, container, false);
    }
    public static ReportQueryFragment getInstance(){
        if(fragment==null){
            fragment = new ReportQueryFragment();
        }
        return  fragment;
    }
}
