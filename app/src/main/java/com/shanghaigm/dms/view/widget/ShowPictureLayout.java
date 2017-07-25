package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.SaveUsedPaths;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ShowQueryPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/7/21.
 */

public class ShowPictureLayout extends RelativeLayout {
    public static String SHOW_QUERY_PHOTO = "show_query_photo";
    public static String SHOW_PHOTO = "show_photo";
    private Context context;
    private ArrayList<PathInfo> paths;
    private String title;
    private TextView text_title;
    private MyGridView gv;
    private GridViewAdapter adapter;
    private Boolean isPic;
    private static String TAG = "ShowPicture";
    private Button btn;
    private LoadingDialog dialog;
    private DmsApplication app;
    private File root;

    public ShowPictureLayout(Context context, ArrayList<PathInfo> paths, String title, Boolean isPic) {
        super(context);
        this.context = context;
        this.title = title;
        this.paths = paths;
        this.isPic = isPic;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.layout_show_picture, this, true);
        initView(v);
        initData();
        setUpView();
    }

    private void setUpView() {
        text_title.setText(title);
        gv.setAdapter(adapter);

        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File file = new File(path.getPath() + file_dir);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        picFile = new File(file, fileName);

        if (paths.size() > 0 && isPic) {
            //如果有文件，就不要再下载
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    File file = new File(root.getPath() + "/report_pic");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File file2 = new File(file, paths.get(position).name);
                    if (file2.exists()) {
                        Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ShowPictureLayout.SHOW_PHOTO, file2.getPath());
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        savePic(position);
                    }
//                    if(app.usedPaths.size()>0){
//                        int count = 0;
//                        for(SaveUsedPaths path:app.usedPaths){
//                            if(path.name.equals(paths.get(position).name)){
//                                File file = new File(path.savePath);
//                                if(file.exists()){
//                                    count++;
//                                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
//                                    Bundle b = new Bundle();
//                                    b.putString(ShowPictureLayout.SHOW_PHOTO, path.savePath);
//                                    intent.putExtras(b);
//                                    context.startActivity(intent);
//                                }
//                            }
//                        }
//                        if(count==0){
//                            savePic(position);
//                        }
//                    }else {
//                        savePic(position);
//                    }
                }
            });
        }

        if (paths.size() > 0 && !isPic) {
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //加载视频
                    File file = new File(root.getPath() + "/report_video");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File file2 = new File(file, paths.get(0).name);
                    if (file2.exists()) {
                        Intent intent = new Intent(context, ShowVideoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        saveVideo();
                    }
//                    int count = 0;
//                    if(app.usedPaths.size()>0){
//                        for(SaveUsedPaths info:app.usedPaths){
//                            if(paths.get(0).name.equals(info.name)){
//                                File file = new File(info.savePath);
//                                if(file.exists()){
//                                    count++;
//                                    Intent intent = new Intent(context, ShowVideoActivity.class);
//                                    Bundle b = new Bundle();
//                                    b.putString(ReportAttachSubFragment.VIDEO_PATH,info.savePath);
//                                    intent.putExtras(b);
//                                    context.startActivity(intent);
//                                }
//                            }
//                        }
//                        if(count==0){
//                            saveVideo();
//                        }
//                    }else {
//                        saveVideo();
//                    }
                }
            });
        }

    }

    private String saveVideo() {
        dialog.showLoadingDlg();
        final String[] vPath = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(0).name;
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(0).path);
                vPath[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_video");
                if (!vPath[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    Intent intent = new Intent(context, ShowVideoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ReportAttachSubFragment.VIDEO_PATH, vPath[0]);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            }
        }).start();
        return vPath[0];
    }

    private String savePic(final int position) {
        dialog.showLoadingDlg();
        final String[] s = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(position).name;
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(position).path);
                s[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_pic");
                if (!s[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                    Bundle b = new Bundle();
                    b.putString(ShowPictureLayout.SHOW_PHOTO, s[0]);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            }
        }).start();
        return s[0];
    }

    private void initData() {
//        paths
        app = DmsApplication.getInstance();
        ArrayList<Bitmap> bits = new ArrayList<>();
        for (PathInfo path : paths) {
            //从压缩图片路径中获取bitmap
            Bitmap bit = BitmapFactory.decodeFile(path.cp_path);
            Log.i(TAG, "initData:cp_path     " + path.cp_path);
            bits.add(bit);
        }
        Log.i(TAG, "initData: bits" + bits.size());
        adapter = new GridViewAdapter(context, bits);
    }

    private void initView(View v) {
        text_title = (TextView) v.findViewById(R.id.text_title);
        gv = (MyGridView) v.findViewById(R.id.gv_show_pic);
        btn = (Button) v.findViewById(R.id.btn);
        dialog = new LoadingDialog(context, "正在加载");
    }
}
