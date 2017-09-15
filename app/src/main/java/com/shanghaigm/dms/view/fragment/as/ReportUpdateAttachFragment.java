package com.shanghaigm.dms.view.fragment.as;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;
import com.shanghaigm.dms.view.widget.ShowPictureLayout2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportUpdateAttachFragment extends BaseFragment {
    private static ReportUpdateAttachFragment fragment = null;
    private ArrayList<ArrayList<PathInfo>> allPaths;
    private LinearLayout ll_content;
    private static String TAG = "ReportUpdateAttach";
    private int c = 0;     //计数，删除和新增是否完毕
    private LoadingDialog dialog;
    private Boolean isSub;

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
        ReportUpdateActivity.isAttachShow = true;
        if (ReportUpdateActivity.allPaths != null) {
            if (allPaths != null) {
                allPaths.clear();
                allPaths = ReportUpdateActivity.allPaths;    //获取数据
            } else {
                allPaths = ReportUpdateActivity.allPaths;    //获取数据
            }
        } else {
            allPaths = new ArrayList<>();
        }

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
            if (paths != null) {
                if (paths.size() > 0) {
                    if (paths.get(0) != null) {
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
            }
        }
        ShowPictureLayout2 pictureLayout = new ShowPictureLayout2(getActivity(), carPlatePaths, "整车铭牌", R.mipmap.all_car, true, allPaths, 15, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ShowPictureLayout2 pictureLayout2 = new ShowPictureLayout2(getActivity(), troublePaths, "故障", R.mipmap.trouble, true, allPaths, 16, false);
        ShowPictureLayout2 pictureLayout3 = new ShowPictureLayout2(getActivity(), repairPaths, "维修", R.mipmap.repair, true, allPaths, 18, false);
        ShowPictureLayout2 pictureLayout4 = new ShowPictureLayout2(getActivity(), otherPaths, "其他", R.mipmap.other, true, allPaths, 19, false);
        ShowPictureLayout2 videoLayout = new ShowPictureLayout2(getActivity(), videoPath, "视频", R.mipmap.video, false, allPaths, 20, false);
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

    public void saveAttachInfo(Boolean isSub) {
        this.isSub = isSub;
        c = 0;
        dialog.showLoadingDlg();
//                //先删后存
        final ArrayList<Integer> ids = new ArrayList<Integer>();    //新增的id
        final ArrayList<PathInfo> paths = new ArrayList<PathInfo>();   //新增的paths
        if (ShowPictureLayout2.pathsDelete != null) {
            Log.i(TAG, "onClick:pathsDelete        " + ShowPictureLayout2.pathsDelete.size());
            //取出用来删除的数据
            final ArrayList<PathInfo> idDelete = new ArrayList<>();
            for (PathInfo info : ShowPictureLayout2.pathsDelete) {
                if (info.file_id != 0) {
                    idDelete.add(info);
                }
            }
            //遍历删除
            final int[] deleteCount = {0};
            for (PathInfo info : idDelete) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", info.file_id + "");
                params.put("type", info.type + "");
                params.put("isById", 1 + "");
                Log.i(TAG, "onClick: " + idDelete.size() + "     " + info.file_id + "     " + info.type);
                OkhttpRequestCenter.getCommonRequest(Constant.URL_REPORT_DELETE_FILE, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        deleteCount[0]++;
                        if (deleteCount[0] == idDelete.size()) {
                            c++;
                            Log.i(TAG, "onSuccess:delete     " + responseObj.toString() + "   删除c:  " + c);
                            if (c == 2) {
                                subInfo(ids);
                                Toast.makeText(getActivity(), getResources().getText(R.string.modify_success), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        } else {
            c++;
            if (c == 2) {
                subInfo(ids);
            }
        }
        for (int i = 0; i < allPaths.size(); i++) {
            Log.i(TAG, "onClick:allPaths.get(0).size     " + allPaths.get(0).size());
            for (PathInfo info : allPaths.get(i)) {
                if (info.file_id == 0) {
                    paths.add(info);
                }
            }
        }
        //添加新增的
        new Thread(new Runnable() {
            /**
             *
             */
            @Override
            public void run() {
                int count = 0;   //判断是否修改完
                if (paths.size() > 0) {
                    for (int i = 0; i < paths.size(); i++) {
                        File file = new File(paths.get(i).cp_path);
                        if (file.exists()) {
                            Log.i(TAG, "onClick:cp_path      " + paths.get(i).cp_path + "       " + paths.size());
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("type", paths.get(i).type + "");
                            map.put("is_compress", 1 + "");
                            //先存压缩的
                            String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, map);
                            if (result != null) {
                                if (!result.equals("")) {
                                    //添加id
                                    String[] ss = addFileId(ids, result);    //添加id并获取name
                                    String id = ss[1];
                                    Log.i(TAG, "onClick:result压缩图片       " + result);
                                    //根据result获取文件名或路径
                                    String name = ss[0];
                                    //遍历找对应
                                    for (int j = 0; j < paths.size(); j++) {
                                        File file1 = new File(paths.get(j).path);
                                        //判断
                                        if (name.equals(paths.get(j).name)) {
                                            Map<String, String> map1 = new HashMap<String, String>();
                                            map1.put("type", paths.get(i).type + "");
                                            map1.put("is_compress", 2 + "");
                                            map1.put("id", id);
                                            //存入
                                            String result1 = HttpUpLoad.uploadFile(file1, Constant.URL_GET_PICTURE_VIDEO_FILE, map1);
                                            Log.i(TAG, "onClick: result原图     " + result.toString());
                                            if (!result1.equals("")) {
                                                count++;
                                                if (count == paths.size()) {
                                                    c++;
                                                    if (c == 2) {
                                                        subInfo(ids);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    c++;
                    if (c == 2) {
                        subInfo(ids);
                    }
                }
            }
        }).start();
    }

    private void finishSub() {
        getActivity().finish();
        ReportSubFragment fragment = ReportSubFragment.getInstance();
        fragment.refreshTable();
    }

    //关联id
    private void subInfo(ArrayList<Integer> ids) {
        Log.i(TAG, "subInfo: " + ids.size());
        //存入
        if (ids.size() > 0) {
            JSONArray array = new JSONArray();
            for (int id : ids) {
                JSONObject idObj = new JSONObject();
                try {
                    idObj.put("id", id);
                    idObj.put("file_id", ReportUpdateActivity.daily_id);
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
                    dialog.dismissLoadingDlg();
                    ((ReportUpdateActivity) getActivity()).setButton();
                    Log.i(TAG, "onSuccess:       " + responseObj.toString());
                    JSONObject obj = (JSONObject) responseObj;
                    try {
                        String s = obj.getString("returnCode");
                        if (s.equals("1")) {
                            if (!isSub) {
                                Intent intent = new Intent(getActivity(), ReportUpdateActivity.class);
                                getActivity().startActivity(intent);
                                Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                            } else {
                                finishSub();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        } else {
            dialog.dismissLoadingDlg();
            if (!isSub) {
                Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ReportUpdateActivity.class);
                getActivity().startActivity(intent);
            } else {
                finishSub();
            }
        }
    }

    private String[] addFileId(ArrayList<Integer> ids, String result) {
        int id = 0;
        String name = null;
        try {
            JSONObject resultObj = new JSONObject(result);
            JSONObject resultObj2 = resultObj.getJSONObject("result");
            name = resultObj2.getString("file_name");
            id = resultObj2.getInt("id");
            ids.add(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[]{name, id + ""};
    }

    private void initView(View v) {
        ll_content = (LinearLayout) v.findViewById(R.id.ll_content);
        dialog = new LoadingDialog(getActivity(), getResources().getText(R.string.upload).toString(), 180000);
    }

    public static ReportUpdateAttachFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportUpdateAttachFragment();
        }
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: " + allPaths.size());
    }
}
