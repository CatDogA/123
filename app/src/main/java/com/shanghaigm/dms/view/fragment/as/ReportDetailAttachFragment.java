package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.view.activity.as.ReportDetailActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

import java.util.ArrayList;

public class ReportDetailAttachFragment extends BaseFragment {
    private static ReportDetailAttachFragment fragment;
    private ArrayList<PathInfo> paths;
    private LinearLayout ll_content;
    private static String TAG = "ReportDetail";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_detail_attach, container, false);
        initView(v);
        initData();
        setUpView();
        return v;
    }

    private void initData() {
        paths = ReportDetailActivity.paths;    //获取数据
    }

    private void setUpView() {
        ArrayList<PathInfo> carPlatePaths = new ArrayList<>();    //整车铭牌
        for (PathInfo path : paths) {
            if (path.type == 15) {
                carPlatePaths.add(path);
            }
        }
        Log.i(TAG, "setUpView: carPlatePaths        " + carPlatePaths.size());
        ShowPictureLayout pictureLayout = new ShowPictureLayout(getActivity(), carPlatePaths, "整车铭牌");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pictureLayout.setLayoutParams(layoutParams);
        ll_content.addView(pictureLayout);
    }

    private void initView(View v) {
        ll_content = (LinearLayout) v.findViewById(R.id.ll_content);
    }

    public static ReportDetailAttachFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportDetailAttachFragment();
        }
        return fragment;
    }
}
