package com.shanghaigm.dms.view.fragment.as;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
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
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.activity.as.ShowPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.ShowVideoPopupWindow;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

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
    private Button btn_sub;
    private ImageView img_video_add, img_video;
    private LoadingDialog dialog;
    public static String VIDEO_PATH = "video_path";
    private int count = 0;

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
//        btn_car_sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 1);
//                pop.showPopup(btn_car_sign);
//            }
//        });
//        btn_trouble.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 2);
//                pop.showPopup(btn_car_sign);
//            }
//        });
//        btn_repair.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 3);
//                pop.showPopup(btn_car_sign);
//            }
//        });
//        btn_other.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 4);
//                pop.showPopup(btn_car_sign);
//            }
//        });

//        btn_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SolvePicturePopupWindow pop = new SolvePicturePopupWindow(getActivity(), 5);
//                pop.showPopup(btn_car_sign);
//            }
//        });
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

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                count = 0;
                final ArrayList<String> results = new ArrayList<String>();
                for (final String path : paths1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<>();
                            params.put("type", "15");
                            params.put("is_compress", "2");
                            File file = new File(path);
                            String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                            Log.i(TAG, "onClick:    " + result);
                            if(result!=null){
                                results.add(result);
                            }
                            if(results.size()==paths1.size()){
                                count++;
                                if(count==5){
                                    dialog.dismissLoadingDlg();
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
                            if(result!=null){
                                results.add(result);
                            }
                            if(results.size()==paths2.size()){
                                count++;
                                if(count==5){
                                    dialog.dismissLoadingDlg();
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
                            if(result!=null){
                                results.add(result);
                            }
                            if(results.size()==paths3.size()){
                                count++;
                                if(count==5){
                                    dialog.dismissLoadingDlg();
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
                            if(result!=null){
                                results.add(result);
                            }
                            if(results.size()==paths4.size()){
                                count++;
                                if(count==5){
                                    dialog.dismissLoadingDlg();
                                }
                            }
                        }
                    }).start();
                }
                dialog.showLoadingDlg();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> params = new HashMap<>();
                        params.put("type", "20");
                        params.put("is_compress", "2");
                        File file = new File(videoPath);
                        String result = HttpUpLoad.uploadFile(file, Constant.URL_GET_PICTURE_VIDEO_FILE, params);
                        if (result != null) {
                            count++;
                            if(count==5){
                                dialog.dismissLoadingDlg();
                            }
                        }
                        Log.i(TAG, "onClick:    " + result);
                    }
                }).start();
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
