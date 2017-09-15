package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
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
import com.shanghaigm.dms.view.widget.ShowPictureLayout2;

import java.util.ArrayList;

public class ReportDetailAttachFragment extends BaseFragment {
    private static ReportDetailAttachFragment fragment;
    private ArrayList<ArrayList<PathInfo>> allPaths;
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
        allPaths = ReportDetailActivity.allPaths;    //获取数据
    }

    private void setUpView() {
        ArrayList<PathInfo> carPlatePaths = new ArrayList<>();    //整车铭牌
        ArrayList<PathInfo> troublePaths = new ArrayList<>();     //故障
        ArrayList<PathInfo> repairPaths = new ArrayList<>();      //维修
        ArrayList<PathInfo> otherPaths = new ArrayList<>();       //其他
        ArrayList<PathInfo> videoPath = new ArrayList<>();       //视频图片地址
        Log.i(TAG, "setUpView:    " + allPaths.size());
        for (ArrayList<PathInfo> paths : allPaths) {
            Log.i(TAG, "setUpView:   " + paths.size());
            if (paths.get(0).type == 15) {
                for (int j = 0; j < paths.size(); j++) {
                    carPlatePaths.add(paths.get(j));
                }
            }
            if (paths.get(0).type == 16) {
                for (int j = 0; j < paths.size(); j++) {
                    troublePaths.add(paths.get(j));
                }
            }
            if (paths.get(0).type == 18) {
                for (int j = 0; j < paths.size(); j++) {
                    repairPaths.add(paths.get(j));
                }
            }
            if (paths.get(0).type == 19) {
                for (int j = 0; j < paths.size(); j++) {
                    otherPaths.add(paths.get(j));
                }
            }
            if (paths.get(0).type == 20) {
                videoPath.add(paths.get(0));
            }
        }
        //allPtahs,15,16..作用于修改，对于查询没有作用，isUpDate:判断是否为修改
        ShowPictureLayout2 pictureLayout = new ShowPictureLayout2(getActivity(), carPlatePaths, "整车铭牌", R.mipmap.all_car, true, allPaths, 15, true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ShowPictureLayout2 pictureLayout2 = new ShowPictureLayout2(getActivity(), troublePaths, "故障", R.mipmap.trouble, true, allPaths, 16, true);
        ShowPictureLayout2 pictureLayout3 = new ShowPictureLayout2(getActivity(), repairPaths, "维修", R.mipmap.repair, true, allPaths, 18, true);
        ShowPictureLayout2 pictureLayout4 = new ShowPictureLayout2(getActivity(), otherPaths, "其他", R.mipmap.other, true, allPaths, 19, true);
        ShowPictureLayout2 videoLayout = new ShowPictureLayout2(getActivity(), videoPath, "视频", R.mipmap.video, false, allPaths, 20, true);
        pictureLayout.setLayoutParams(layoutParams);
        pictureLayout2.setLayoutParams(layoutParams);
        pictureLayout3.setLayoutParams(layoutParams);
        pictureLayout4.setLayoutParams(layoutParams);
        videoLayout.setLayoutParams(layoutParams);
        ll_content.addView(pictureLayout);
        ll_content.addView(pictureLayout2);
        ll_content.addView(pictureLayout3);
        ll_content.addView(pictureLayout4);
        ll_content.addView(videoLayout);
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
