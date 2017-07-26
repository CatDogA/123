package com.shanghaigm.dms.view.fragment.as;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportUpdateAttachFragment extends BaseFragment {
    private static ReportUpdateAttachFragment fragment = null;
    private ArrayList<ArrayList<PathInfo>> allPaths = new ArrayList<>();
    private LinearLayout ll_content;
    private Button btn_sub;
    private static String TAG = "ReportUpdate";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_update_attach, container, false);
        initView(v);
        initData();
        setUpView();
        return v;
    }

    private void initData() {
        allPaths.clear();
        allPaths = ReportUpdateActivity.allPaths;    //获取数据
        Log.i(TAG, "initData:       " + allPaths.size());
    }

    public void setPaths(ArrayList<ArrayList<PathInfo>> allPaths) {
        this.allPaths = allPaths;
        ll_content.removeAllViews();
        setUpView();
    }

    private void setUpView() {
        final ArrayList<PathInfo> carPlatePaths = new ArrayList<>();    //整车铭牌
        ArrayList<PathInfo> troublePaths = new ArrayList<>();     //故障
        final ArrayList<PathInfo> repairPaths = new ArrayList<>();      //维修
        ArrayList<PathInfo> otherPaths = new ArrayList<>();       //其他
        ArrayList<PathInfo> videoPath = new ArrayList<>();       //视频图片地址
        for (ArrayList<PathInfo> paths : allPaths) {
            if (paths.size() > 0) {
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
        }
        ShowPictureLayout pictureLayout = new ShowPictureLayout(getActivity(), carPlatePaths, "整车铭牌", true, allPaths, 15, true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ShowPictureLayout pictureLayout2 = new ShowPictureLayout(getActivity(), troublePaths, "故障", true, allPaths, 16, true);
        ShowPictureLayout pictureLayout3 = new ShowPictureLayout(getActivity(), repairPaths, "维修", true, allPaths, 18, true);
        ShowPictureLayout pictureLayout4 = new ShowPictureLayout(getActivity(), otherPaths, "其他", true, allPaths, 19, true);
        ShowPictureLayout videoLayout = new ShowPictureLayout(getActivity(), videoPath, "视频", false, allPaths, 20, true);
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

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //先删后存
                if (ShowPictureLayout.pathsDelete != null) {
                    //取出用来删除的数据
                    ArrayList<PathInfo> idDelete = new ArrayList<>();
                    for (PathInfo info : ShowPictureLayout.pathsDelete) {
                        if (info.file_id != 0) {
                            idDelete.add(info);
                        }
                    }
                    //遍历删除
                    for (PathInfo info : idDelete) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", info.file_id + "");
                        params.put("type", info.type + "");
                        OkhttpRequestCenter.getCommonRequest(Constant.URL_REPORT_DELETE_FILE, params, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                Log.i(TAG, "onSuccess:delete     " + responseObj.toString());
                            }

                            @Override
                            public void onFailure(Object reasonObj) {

                            }
                        });
                    }
                }
                ArrayList<Integer> ids = new ArrayList<Integer>();    //原来的和新增的id
                ArrayList<PathInfo> paths = new ArrayList<PathInfo>();   //新增的paths
                for (int i = 0; i < allPaths.size(); i++) {
                    Log.i(TAG, "onClick:allPaths.get(0).size     " + allPaths.get(0).size());
                    for (PathInfo info : allPaths.get(i)) {
                        if (info.file_id == 0) {
                            paths.add(info);
                        }
                        if (info.file_id != 0) {
                            ids.add(info.file_id);
                        }
                    }
                }
                //添加新增的
                for (int i = 0; i < paths.size(); i++) {
                    File file = new File(paths.get(i).cp_path);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", paths.get(i).type + "");
                    map.put("is_compress", 1 + "");
                    //先存压缩的
                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, map);
                    if (!result.equals("")) {
                        //添加id
                        int id = addFileId(ids, result);
                        //TODO-
                        //根据result获取文件名或路径
                        String name = "";
                        //遍历找对应
                        for (int j = 0; j < paths.size(); j++) {
                            File file1 = new File(paths.get(j).path);
                            //判断
                            if (name.equals(file1.getName())) {
                                Map<String, String> map1 = new HashMap<String, String>();
                                map1.put("type", paths.get(i).type + "");
                                map1.put("is_compress", 1 + "");
                                map1.put("id", id + "");
                                //存入
                                String result1 = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, map1);
                                Log.i(TAG, "onClick: result     " + result.toString());
                            }
                        }
                    }
                }

                //总体存入？
                JSONArray array = new JSONArray();
                for (int id : ids) {
                    JSONObject idObj = new JSONObject();
                    try {
                        idObj.put("id", id);
                        idObj.put("file_id", ((ReportAddActivity) getActivity()).getReport_id());
                        array.put(idObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Map<String, Object> params = new HashMap<>();
                params.put("fileList", array.toString());
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_SUB_PIC_VIDEO, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:       " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            String s = obj.getString("returnCode");
                            if (s.equals("1")) {
                                Toast.makeText(getActivity(), getResources().getText(R.string.sub_file_success), Toast.LENGTH_SHORT).show();
                                btn_sub.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
            //先把id取出
//                ArrayList<Integer> ids = new ArrayList<Integer>();
//                for (ArrayList<PathInfo> paths : allPaths) {
//                    for (PathInfo pathInfo : paths) {
//                        ids.add(pathInfo.file_id);
//                    }
//                }
//                JSONObject object = new JSONObject();
//                JSONArray array = new JSONArray();
//                for (int id : ids) {
//                    try {
//                        object.put("file_id", ReportUpdateActivity.daily_id);
//                        object.put("id", id);
//                        array.put(object);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Map<String, Object> params = new HashMap<>();
//                params.put("fileList", array.toString());
//                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_SUB_PIC_VIDEO, params, new DisposeDataListener() {
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        Log.i(TAG, "onSuccess: " + responseObj);
//                    }
//
//                    @Override
//                    public void onFailure(Object reasonObj) {
//
//                    }
//                });

    });
}

    private int addFileId(ArrayList<Integer> ids, String result) {
        int id = 0;
        try {
            JSONObject resultObj = new JSONObject(result);
            JSONObject resultObj2 = resultObj.getJSONObject("result");
            id = resultObj2.getInt("id");
            ids.add(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    private void initView(View v) {
        ll_content = (LinearLayout) v.findViewById(R.id.ll_content);
        btn_sub = (Button) v.findViewById(R.id.btn_sub);
    }

    public static ReportUpdateAttachFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportUpdateAttachFragment();
        }
        return fragment;
    }
}
