package com.shanghaigm.dms.view.fragment.as;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.ArrayMap;
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
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ShowPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowVideoPopupWindow;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportAttachSubFragment extends BaseFragment {
    private static ReportAttachSubFragment reportAttachSubFragment;
    //    private Button btn_car_sign, btn_trouble, btn_repair, btn_other, btn_video;
    private ArrayList<Bitmap> bits_car_sign, bits_trouble, bits_repair, bits_other;
    private static String TAG = "ReportAttachSub";
    public static String SHOW_PHOTO = "show_photo";
    private GridView gv_car_sign, gv_trouble, gv_repair, gv_other;
    private GridViewAdapter adapter_car_sign, adapter_trouble, adapter_repair, adapter_other;
    private ArrayList<String> paths1, paths2, paths3, paths4;
    private String videoPath = null;
    private Button btn_save, btn_sub;
    private ImageView img_video_add, img_video;
    private LoadingDialog dialog;
    public static String VIDEO_PATH = "video_path";
    private int count = 0;
    private int countFill = 0;
    private Boolean isPicAdd = false;  //如果都可以，才能上传文件
    private ArrayList<Integer> file_ids = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_attach_sub, container, false);
        initView(v);
        setUpView();
        return v;
    }

    public void ChangeImage(String s) {
        Bitmap bit = getVideoThumb(s);
        img_video.setImageBitmap(bit);
        videoPath = s;
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

        gv_car_sign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 1);
                    pop.showPopup(img_video);
                } else {
                    Log.i(TAG, "onItemClick:     " + position);
                    Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.SHOW_PHOTO, paths1.get(position - 1).toString());
                    intent.putExtras(b);
                    getActivity().startActivity(intent);
                }
            }
        });

        gv_trouble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 2);
                    pop.showPopup(img_video);
                } else {
                    Log.i(TAG, "onItemClick:     " + position);
                    Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.SHOW_PHOTO, paths2.get(position - 1).toString());
                    intent.putExtras(b);
                    getActivity().startActivity(intent);
                }
            }
        });
        gv_repair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 3);
                    pop.showPopup(img_video);
                } else {
                    Log.i(TAG, "onItemClick:     " + position);
                    Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.SHOW_PHOTO, paths3.get(position - 1).toString());
                    intent.putExtras(b);
                    getActivity().startActivity(intent);
                }
            }
        });

        gv_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 4);
                    pop.showPopup(img_video);
                } else {
                    Log.i(TAG, "onItemClick:     " + position);
                    Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.SHOW_PHOTO, paths4.get(position - 1).toString());
                    intent.putExtras(b);
                    getActivity().startActivity(intent);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(getActivity(), getResources().getText(R.string.add_file), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.showLoadingDlg();
                    Log.i(TAG, "onClick:countFill    " + countFill);
                    final ArrayList<String> results1 = new ArrayList<String>();         //存储完成的结果
                    final ArrayList<String> results2 = new ArrayList<String>();
                    final ArrayList<String> results3 = new ArrayList<String>();
                    final ArrayList<String> results4 = new ArrayList<String>();

                    for (final String path : paths1) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                params.size();
                                params.put("type", "15");
                                params.put("is_compress", "2");
                                File file = new File(path);
                                String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                Log.i(TAG, "onClick:    " + result);
                                if (result != null) {
                                    results1.add(result);
                                    try {
                                        JSONObject resultObj = new JSONObject(result);
                                        JSONObject resultObj2 = resultObj.getJSONObject("result");
                                        int id = resultObj2.getInt("id");
                                        file_ids.add(id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (results1.size() == paths1.size()) {
                                    count++;
                                    Log.i(TAG, "run: count  " + count);
                                    if (count == countFill) {
                                        dialog.dismissLoadingDlg();
                                        isPicAdd = true;
                                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).start();
                    }
                    for (final String path : paths2) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                params.put("type", "15");
                                params.put("is_compress", "2");
                                File file = new File(path);
                                String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                Log.i(TAG, "onClick:    " + result);
                                if (result != null) {
                                    results2.add(result);
                                    try {
                                        JSONObject resultObj = new JSONObject(result);
                                        JSONObject resultObj2 = resultObj.getJSONObject("result");
                                        int id = resultObj2.getInt("id");
                                        file_ids.add(id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (results2.size() == paths2.size()) {
                                    count++;
                                    Log.i(TAG, "run: count  " + count);
                                    if (count == countFill) {
                                        dialog.dismissLoadingDlg();
                                        isPicAdd = true;
                                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).start();

                    }
                    for (final String path : paths3) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                params.put("type", "15");
                                params.put("is_compress", "2");
                                File file = new File(path);
                                String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                Log.i(TAG, "onClick:    " + result);
                                if (result != null) {
                                    results3.add(result);
                                    try {
                                        JSONObject resultObj = new JSONObject(result);
                                        JSONObject resultObj2 = resultObj.getJSONObject("result");
                                        int id = resultObj2.getInt("id");
                                        file_ids.add(id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (results3.size() == paths3.size()) {
                                    count++;
                                    Log.i(TAG, "run: count  " + count);
                                    if (count == countFill) {
                                        dialog.dismissLoadingDlg();
                                        isPicAdd = true;
                                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).start();
                    }
                    for (final String path : paths4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                params.put("type", "15");
                                params.put("is_compress", "2");
                                File file = new File(path);
                                String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                Log.i(TAG, "onClick:    " + result);
                                if (result != null) {
                                    results4.add(result);
                                    try {
                                        JSONObject resultObj = new JSONObject(result);
                                        JSONObject resultObj2 = resultObj.getJSONObject("result");
                                        int id = resultObj2.getInt("id");
                                        file_ids.add(id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (results4.size() == paths4.size()) {
                                    count++;
                                    Log.i(TAG, "run: count  " + count);
                                    if (count == countFill) {
                                        dialog.dismissLoadingDlg();
                                        isPicAdd = true;
                                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).start();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<>();
                            params.put("type", "20");
                            params.put("is_compress", "2");
                            if (videoPath != null) {
                                File file = new File(videoPath);
                                String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                                if (result != null) {
                                    try {
                                        JSONObject resultObj = new JSONObject(result);
                                        JSONObject resultObj2 = resultObj.getJSONObject("result");
                                        int id = resultObj2.getInt("id");
                                        file_ids.add(id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    count++;
                                    Log.i(TAG, "run: count  " + count);
                                    if (count == countFill) {
                                        dialog.dismissLoadingDlg();
                                        isPicAdd = true;
                                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file_success), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Log.i(TAG, "onClick:    " + result);
                            }
                        }
                    }).start();
                }
                if (isPicAdd) {
                    btn_save.setEnabled(false);
                }
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ReportAddActivity) getActivity()).isInfoAdd()) {
                    if (isPicAdd) {
                        JSONArray idArray = new JSONArray();
                        for (int id : file_ids) {
                            JSONObject idObj = new JSONObject();
                            try {
                                idObj.put("id", ((ReportAddActivity) getActivity()).getReport_id());
                                idObj.put("file_id", id);
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
                    } else {
                        Toast.makeText(getActivity(), getResources().getText(R.string.save_file), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.save_page_one_info), Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_video_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 5);
                pop.showPopup(img_video);
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


    private void initView(View v) {
        btn_sub = (Button) v.findViewById(R.id.btn_sub);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        //这样paths中对应的图片比bits中比，少1
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture_add);
        bits_car_sign = ((ReportAddActivity) getActivity()).getBitmaps();
        bits_car_sign.add(bitmap);

        bits_trouble = ((ReportAddActivity) getActivity()).getBitmaps2();
        bits_trouble.add(bitmap);
        bits_repair = ((ReportAddActivity) getActivity()).getBitmaps3();
        bits_repair.add(bitmap);
        bits_other = ((ReportAddActivity) getActivity()).getBitmaps4();
        bits_other.add(bitmap);

        paths1 = ((ReportAddActivity) getActivity()).getUris();
        paths2 = ((ReportAddActivity) getActivity()).getUris2();
        paths3 = ((ReportAddActivity) getActivity()).getUris3();
        paths4 = ((ReportAddActivity) getActivity()).getUris4();
//        videoPaths = ((ReportAddActivity) getActivity()).getVideoPaths();
//        btn_car_sign = (Button) v.findViewById(R.id.btn_car_sign);
//        btn_trouble = (Button) v.findViewById(R.id.btn_trouble);
//        btn_repair = (Button) v.findViewById(R.id.btn_repair);
//        btn_other = (Button) v.findViewById(R.id.btn_other);
//        btn_video = (Button) v.findViewById(R.id.btn_video);

        gv_car_sign = (GridView) v.findViewById(R.id.gv_car_sign);
        gv_repair = (GridView) v.findViewById(R.id.gv_repair);
        gv_trouble = (GridView) v.findViewById(R.id.gv_trouble);
        gv_other = (GridView) v.findViewById(R.id.gv_other);
        dialog = new LoadingDialog(getActivity(), getResources().getString(R.string.upload));

        img_video = (ImageView) v.findViewById(R.id.img_video);
        img_video_add = (ImageView) v.findViewById(R.id.img_video_add);
    }

    public static ReportAttachSubFragment getInstance() {
        if (reportAttachSubFragment == null) {
            reportAttachSubFragment = new ReportAttachSubFragment();
        }
        return reportAttachSubFragment;
    }

    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }
}
