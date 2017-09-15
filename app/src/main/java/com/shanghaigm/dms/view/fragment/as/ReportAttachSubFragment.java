package com.shanghaigm.dms.view.fragment.as;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathUpLoadInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ShowQueryPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 废弃
 */
public class ReportAttachSubFragment extends BaseFragment {
    private static ReportAttachSubFragment reportAttachSubFragment;
    private ArrayList<Bitmap> bits_car_sign, bits_trouble, bits_repair, bits_other;
    private ArrayList<PathUpLoadInfo> infos_car_sign = new ArrayList<>(), infos_trouble = new ArrayList<>(),
            infos_repair = new ArrayList<>(), infos_other = new ArrayList<>();
    private static String TAG = "ReportAttachSub";
    public static String SHOW_PHOTO = "show_photo";
    private GridView gv_car_sign, gv_trouble, gv_repair, gv_other;
    private GridViewAdapter adapter_car_sign, adapter_trouble, adapter_repair, adapter_other;
    private ArrayList<String> paths1, paths2, paths3, paths4;
    private ArrayList<String> cpPaths1, cpPaths2, cpPaths3, cpPaths4;
    private String videoPath = null, video_pic_path = null;
    private ImageView img_video_add, img_video;
    private LoadingDialog dialog;
    public static String VIDEO_PATH = "video_path";
    private int count = 0;
    private int countFill = 0;
    private Boolean isPicAdd = false;  //如果都可以，才能上传文件
    private ArrayList<Integer> file_ids = new ArrayList<>();
    private Button btn_all_car, btn_trouble, btn_repair, btn_other, btn_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_attach_sub, container, false);
        initView(v);
        setUpView();
        return v;
    }

    public void ChangeImage(String s) {
        //清空
        videoPath = null;
        video_pic_path = null;
        //通过视频路径获取bit
        Bitmap bit = getVideoThumb(s);
        img_video.setImageBitmap(bit);
        img_video.setVisibility(View.VISIBLE);
        videoPath = s;
        try {
            video_pic_path = SaveCpPic(bit);
            Log.i(TAG, "ChangeImage: video_pic_path        " + video_pic_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //把图片存到本地
    private String SaveCpPic(Bitmap bit) throws IOException {
        File path = null;
        path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());  //名字
        // Create an image file name
        File image = new File(path.getPath() + "/" + timeStamp + ".jpg");
        Log.i(TAG, "SaveCpPic: " + image.getPath());
        try {
            FileOutputStream out = new FileOutputStream(image);
            if (bit != null) {
                if (bit.compress(Bitmap.CompressFormat.PNG, 40, out)) {
                    out.flush();
                    out.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image.getPath();
    }

    private void setUpView() {
        adapter_car_sign = new GridViewAdapter(getActivity(), bits_car_sign);
        adapter_trouble = new GridViewAdapter(getActivity(), bits_trouble);
        adapter_repair = new GridViewAdapter(getActivity(), bits_repair);
        adapter_other = new GridViewAdapter(getActivity(), bits_other);

        gv_car_sign.setAdapter(adapter_car_sign);
        gv_trouble.setAdapter(adapter_trouble);
        gv_repair.setAdapter(adapter_repair);
        gv_other.setAdapter(adapter_other);
        btn_all_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths1.size()==8){
                    Toast.makeText(getActivity(),getString(R.string.too_many), Toast.LENGTH_SHORT).show();
                    return;
                }
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 15);
                pop.showPopup(img_video);
            }
        });
        btn_trouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths2.size()==8){
                    Toast.makeText(getActivity(),getString(R.string.too_many), Toast.LENGTH_SHORT).show();
                    return;
                }
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 16);
                pop.showPopup(img_video);
            }
        });
        btn_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths3.size()==8){
                    Toast.makeText(getActivity(),getString(R.string.too_many), Toast.LENGTH_SHORT).show();
                    return;
                }
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 18);
                pop.showPopup(img_video);
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths4.size()==8){
                    Toast.makeText(getActivity(),getString(R.string.too_many), Toast.LENGTH_SHORT).show();
                    return;
                }
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 19);
                pop.showPopup(img_video);
            }
        });
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 20);
                pop.showPopup(img_video);
            }
        });
        gv_car_sign.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bits_car_sign.remove(position);
                paths1.remove(position);
                cpPaths1.remove(position);
                adapter_car_sign.notifyDataSetChanged();
                return true;
            }
        });
        gv_trouble.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bits_trouble.remove(position);
                paths2.remove(position);
                cpPaths2.remove(position);
                adapter_trouble.notifyDataSetChanged();
                return true;
            }
        });
        gv_repair.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bits_repair.remove(position);
                paths3.remove(position);
                cpPaths3.remove(position);
                adapter_repair.notifyDataSetChanged();
                return true;
            }
        });
        gv_other.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bits_other.remove(position);
                paths4.remove(position);
                cpPaths4.remove(position);
                adapter_other.notifyDataSetChanged();
                return true;
            }
        });
        img_video.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                video_pic_path = "";
                videoPath = "";
                img_video.setVisibility(View.GONE);
                return true;
            }
        });
        gv_car_sign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick:     " + position);
                Intent intent = new Intent(getActivity(), ShowQueryPhotoActivity.class);
                Bundle b = new Bundle();
                b.putString(ShowPictureLayout.SHOW_PHOTO, paths1.get(position).toString());
                intent.putExtras(b);
                getActivity().startActivity(intent);
            }
        });

        gv_trouble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "onItemClick:     " + position);
                Intent intent = new Intent(getActivity(), ShowQueryPhotoActivity.class);
                Bundle b = new Bundle();
                b.putString(ShowPictureLayout.SHOW_PHOTO, paths2.get(position).toString());
                intent.putExtras(b);
                getActivity().startActivity(intent);
            }
        });
        gv_repair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick:     " + position);
                Intent intent = new Intent(getActivity(), ShowQueryPhotoActivity.class);
                Bundle b = new Bundle();
                b.putString(ShowPictureLayout.SHOW_PHOTO, paths3.get(position).toString());
                intent.putExtras(b);
                getActivity().startActivity(intent);

            }
        });

        gv_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick:     " + position);
                Intent intent = new Intent(getActivity(), ShowQueryPhotoActivity.class);
                Bundle b = new Bundle();
                b.putString(ShowPictureLayout.SHOW_PHOTO, paths4.get(position).toString());
                intent.putExtras(b);
                getActivity().startActivity(intent);
            }
        });

        img_video_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPath != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ReportAttachSubFragment.VIDEO_PATH, videoPath);
                    goToActivity(ShowVideoActivity.class, bundle);
                }
            }
        });
    }

    public void subAttachInfo() {
        if (((ReportAddActivity) getActivity()).isInfoAdd()) {
            if (isPicAdd) {
                JSONArray idArray = new JSONArray();
                Log.i(TAG, "onClick:file_ids            " + file_ids.size());
                for (int i = 0; i < file_ids.size(); i++) {
                    Log.i(TAG, "subAttachInfo:           " + file_ids.get(i));
                }
                for (int id : file_ids) {
                    JSONObject idObj = new JSONObject();
                    try {
                        idObj.put("id", id);
                        idObj.put("file_id", ((ReportAddActivity) getActivity()).getReport_id());
                        idArray.put(idObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Map<String, Object> params = new HashMap<>();
                params.put("fileList", idArray.toString());
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_SUB_PIC_VIDEO, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess:       " + responseObj.toString());
                        JSONObject obj = (JSONObject) responseObj;
                        try {
                            String s = obj.getString("returnCode");
                            if (s.equals("1")) {
//                                Toast.makeText(getActivity(), getResources().getText(R.string.sub_file_success), Toast.LENGTH_SHORT).show();
                                //清空
                                videoPath = null;
                                video_pic_path = null;
                                ((ReportAddActivity) getActivity()).setButton();
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
                Toast.makeText(getActivity(), getResources().getText(R.string.save_file), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.save_page_one_info), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveAttachInfo() {
        file_ids.clear();
        if (((ReportAddActivity) getActivity()).isInfoAdd()) {
            infos_car_sign.clear();
            infos_trouble.clear();
            infos_repair.clear();
            infos_other.clear();
            Log.i(TAG, "onClick: " + "       " + paths1.size() + "       " + paths2.size() + "       " + paths3.size() + "       " + paths4.size());
            Log.i(TAG, "onClick: " + "       " + cpPaths1.size() + "       " + cpPaths2.size() + "       " + cpPaths3.size() + "       " + cpPaths4.size());
            for (int i = 0; i < paths1.size(); i++) {
                infos_car_sign.add(new PathUpLoadInfo(getName(paths1.get(i)), cpPaths1.get(i), paths1.get(i)));
            }
            for (int i = 0; i < paths2.size(); i++) {
                infos_trouble.add(new PathUpLoadInfo(getName(paths2.get(i)), cpPaths2.get(i), paths2.get(i)));
            }
            for (int i = 0; i < paths3.size(); i++) {
                infos_repair.add(new PathUpLoadInfo(getName(paths3.get(i)), cpPaths3.get(i), paths3.get(i)));
            }
            for (int i = 0; i < paths4.size(); i++) {
                infos_other.add(new PathUpLoadInfo(getName(paths4.get(i)), cpPaths4.get(i), paths4.get(i)));
            }
            count = 0;          //判断是否完成
            countFill = 0;  //有数值的paths个数
            if (paths1.size() > 0) {
                countFill++;
            }
            if (paths2.size() > 0) {
                countFill++;
            }
            if (paths3.size() > 0) {
                countFill++;
            }
            if (paths4.size() > 0) {
                countFill++;
            }
            if (videoPath != null) {
                countFill++;
            }
            if (countFill == 0) {
                Log.i(TAG, "onClick: countFill           " + countFill);
                ((ReportAddActivity) getActivity()).setButton();
                Toast.makeText(getActivity(), getResources().getText(R.string.add_file), Toast.LENGTH_SHORT).show();
            } else {
                dialog.showLoadingDlg();
                Log.i(TAG, "onClick:countFill    " + countFill);
                final ArrayList<String> results1 = new ArrayList<String>();         //存储完成的结果
                final ArrayList<String> results2 = new ArrayList<String>();
                final ArrayList<String> results3 = new ArrayList<String>();
                final ArrayList<String> results4 = new ArrayList<String>();

                for (int i = 0; i < paths1.size(); i++) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //压缩上传
                            Map<String, String> cpParams = new HashMap<String, String>();
                            cpParams.put("type", "15");
                            cpParams.put("is_compress", "1");
                            File cpFile = new File(cpPaths1.get(finalI));
                            String cpReuslt = HttpUpLoad.uploadFile(cpFile, Constant.URL_GET_PICTURE_VIDEO_FILE, cpParams);
                            int file_id = 0;
                            if (cpReuslt != null) {
                                file_id = addFileId(file_ids, cpReuslt);
                            }
                            Log.i(TAG, "run:infos_car_sign, cpReuslt    " + cpReuslt + "    file_id  " + file_id + "    infos_car_sign  " + infos_car_sign.size() + " paths1   " + paths1.size());
                            for (int j = 0; j < infos_car_sign.size(); j++) {
                                if (cpFile.getName().equals(infos_car_sign.get(j).fileName)) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("type", "15");
                                    params.put("is_compress", "2");
                                    params.put("id", file_id + "");
                                    File file = new File(paths1.get(j));
                                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                    Log.i(TAG, "onClick:    " + result);
                                    if (result != null) {
                                        results1.add(result);
                                    }
                                    if (results1.size() == paths1.size()) {
                                        count++;
                                        Log.i(TAG, "run: count  " + count);
                                        if (count == countFill) {
                                            dialog.dismissLoadingDlg();
                                            isPicAdd = true;
                                            subAttachInfo();
//                                            Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }

                for (int i = 0; i < paths2.size(); i++) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> cpParams = new HashMap<String, String>();
                            cpParams.put("type", "16");
                            cpParams.put("is_compress", "1");
                            File cpFile = new File(cpPaths2.get(finalI));
                            String cpReuslt = HttpUpLoad.uploadFile(cpFile, Constant.URL_GET_PICTURE_VIDEO_FILE, cpParams);
                            int file_id = 0;
                            if (cpReuslt != null) {
                                file_id = addFileId(file_ids, cpReuslt);
                            }
                            Log.i(TAG, "run:infos_car_sign, cpReuslt    " + cpReuslt + "    file_id  " + file_id + "    infos_car_sign  " + infos_car_sign.size() + " paths1   " + paths1.size());
                            for (int j = 0; j < infos_trouble.size(); j++) {
                                if (cpFile.getName().equals(infos_trouble.get(j).fileName)) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("type", "16");
                                    params.put("is_compress", "2");
                                    params.put("id", file_id + "");
                                    File file = new File(paths2.get(j));
                                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                    Log.i(TAG, "onClick:    " + result);
                                    if (result != null) {
                                        results2.add(result);
                                    }
                                    if (results2.size() == paths2.size()) {
                                        count++;
                                        Log.i(TAG, "run: count  " + count);
                                        if (count == countFill) {
                                            dialog.dismissLoadingDlg();
                                            isPicAdd = true;
                                            subAttachInfo();
                                            Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }
                for (int i = 0; i < paths3.size(); i++) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> cpParams = new HashMap<String, String>();
                            cpParams.put("type", "18");
                            cpParams.put("is_compress", "1");
                            File cpFile = new File(cpPaths3.get(finalI));
                            String cpReuslt = HttpUpLoad.uploadFile(cpFile, Constant.URL_GET_PICTURE_VIDEO_FILE, cpParams);
                            int file_id = 0;
                            if (cpReuslt != null) {
                                file_id = addFileId(file_ids, cpReuslt);
                            }
                            Log.i(TAG, "run:infos_car_sign, cpReuslt    " + cpReuslt + "    file_id  " + file_id + "    infos_car_sign  " + infos_car_sign.size() + " paths1   " + paths1.size());
                            for (int j = 0; j < infos_repair.size(); j++) {
                                if (cpFile.getName().equals(infos_repair.get(j).fileName)) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("type", "18");
                                    params.put("is_compress", "2");
                                    params.put("id", file_id + "");
                                    File file = new File(paths3.get(j));
                                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                    Log.i(TAG, "onClick:    " + result);
                                    if (result != null) {
                                        results3.add(result);
                                    }
                                    if (results3.size() == paths3.size()) {
                                        count++;
                                        Log.i(TAG, "run: count  " + count);
                                        if (count == countFill) {
                                            dialog.dismissLoadingDlg();
                                            isPicAdd = true;
                                            subAttachInfo();
                                            Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }
                for (int i = 0; i < paths4.size(); i++) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> cpParams = new HashMap<String, String>();
                            cpParams.put("type", "19");
                            cpParams.put("is_compress", "1");
                            File cpFile = new File(cpPaths4.get(finalI));
                            String cpReuslt = HttpUpLoad.uploadFile(cpFile, Constant.URL_GET_PICTURE_VIDEO_FILE, cpParams);
                            int file_id = 0;
                            if (cpReuslt != null) {
                                file_id = addFileId(file_ids, cpReuslt);
                            }
                            Log.i(TAG, "run:infos_car_sign, cpReuslt    " + cpReuslt + "    file_id  " + file_id + "    infos_car_sign  " + infos_car_sign.size() + " paths1   " + paths1.size());
                            for (int j = 0; j < infos_other.size(); j++) {
                                if (cpFile.getName().equals(infos_other.get(j).fileName)) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("type", "19");
                                    params.put("is_compress", "2");
                                    params.put("id", file_id + "");
                                    File file = new File(paths4.get(j));
                                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                    Log.i(TAG, "onClick:    " + result);
                                    if (result != null) {
                                        results4.add(result);
                                    }
                                    if (results4.size() == paths4.size()) {
                                        count++;
                                        Log.i(TAG, "run: count  " + count);
                                        if (count == countFill) {
                                            dialog.dismissLoadingDlg();
                                            isPicAdd = true;
                                            subAttachInfo();
                                            Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> cpParams = new HashMap<String, String>();
                        cpParams.put("type", "20");
                        cpParams.put("is_compress", "1");
                        if (video_pic_path != null) {
                            File cpFile = new File(video_pic_path);
                            Log.i(TAG, "runvideo_pic_path        : " + video_pic_path);
                            String cpReuslt = HttpUpLoad.uploadFile(cpFile, Constant.URL_GET_PICTURE_VIDEO_FILE, cpParams);
                            int file_id = 0;
                            if (cpReuslt != null) {
                                file_id = addFileId(file_ids, cpReuslt);
                                Map<String, String> params = new HashMap<>();
                                params.put("type", "20");
                                params.put("is_compress", "");
                                params.put("id", file_id + "");
                                if (videoPath != null) {
                                    File file = new File(videoPath);
                                    String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                    Log.i(TAG, "run: video_result       " + result);
                                    if (result != null) {
//                                            try {
//                                                JSONObject resultObj = new JSONObject(result);
//                                                JSONObject resultObj2 = resultObj.getJSONObject("result");
//                                                int id = resultObj2.getInt("id");
                                        file_ids.add(file_id);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
                                        count++;
                                        Log.i(TAG, "run: count  " + count);
                                        if (count == countFill) {
                                            dialog.dismissLoadingDlg();
                                            isPicAdd = true;
                                            subAttachInfo();
                                            Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    Log.i(TAG, "onClick:    " + result);
                                }
                            }
                        }
                    }
                }).start();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.save_page_one_info), Toast.LENGTH_SHORT).show();
        }
    }

    public void clear() {
        if (paths1 != null) {
            paths1.clear();
        }
        if (paths2 != null) {
            paths2.clear();
        }
        if (paths3 != null) {
            paths3.clear();
        }
        if (paths4 != null) {
            paths4.clear();
        }
        if (cpPaths1 != null) {
            cpPaths1.clear();
        }
        if (cpPaths2 != null) {
            cpPaths2.clear();
        }
        if (cpPaths3 != null) {
            cpPaths3.clear();
        }
        if (cpPaths4 != null) {
            cpPaths4.clear();
        }
    }

    private void initView(View v) {
        ReportAddActivity.isAtttachShow = true;
        //这样paths中对应的图片比bits中比，少1
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.add_pic);
//        bits_car_sign = ((ReportAddActivity) getActivity()).getBitmaps();
////        bits_car_sign.add(bitmap);
//        bits_trouble = ((ReportAddActivity) getActivity()).getBitmaps2();
////        bits_trouble.add(bitmap);
//        bits_repair = ((ReportAddActivity) getActivity()).getBitmaps3();
////        bits_repair.add(bitmap);
//        bits_other = ((ReportAddActivity) getActivity()).getBitmaps4();
////        bits_other.add(bitmap);
//
//        paths1 = ((ReportAddActivity) getActivity()).getUris();
//        paths2 = ((ReportAddActivity) getActivity()).getUris2();
//        paths3 = ((ReportAddActivity) getActivity()).getUris3();
//        paths4 = ((ReportAddActivity) getActivity()).getUris4();
//
//        cpPaths1 = ((ReportAddActivity) getActivity()).getCpPaths();
//        cpPaths2 = ((ReportAddActivity) getActivity()).getCpPaths2();
//        cpPaths3 = ((ReportAddActivity) getActivity()).getCpPaths3();
//        cpPaths4 = ((ReportAddActivity) getActivity()).getCpPaths4();


        gv_car_sign = (GridView) v.findViewById(R.id.gv_car_sign);
        gv_repair = (GridView) v.findViewById(R.id.gv_repair);
        gv_trouble = (GridView) v.findViewById(R.id.gv_trouble);
        gv_other = (GridView) v.findViewById(R.id.gv_other);
        dialog = new LoadingDialog(getActivity(), getResources().getString(R.string.upload), 180000);
        img_video = (ImageView) v.findViewById(R.id.img_video);
        img_video_add = (ImageView) v.findViewById(R.id.img_video_add);
        img_video_add.setVisibility(View.GONE);
        btn_all_car = (Button) v.findViewById(R.id.btn_all_car);
        btn_trouble = (Button) v.findViewById(R.id.btn_trouble);
        btn_repair = (Button) v.findViewById(R.id.btn_repair);
        btn_other = (Button) v.findViewById(R.id.btn_other);
        btn_video = (Button) v.findViewById(R.id.btn_video);
    }

    public static ReportAttachSubFragment getInstance() {
        if (reportAttachSubFragment == null) {
            reportAttachSubFragment = new ReportAttachSubFragment();
        }
        return reportAttachSubFragment;
    }

    public int addFileId(ArrayList<Integer> ids, String result) {
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

    private String getName(String path) {
        File file = new File(path);
        return file.getName();
    }

    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }
}
