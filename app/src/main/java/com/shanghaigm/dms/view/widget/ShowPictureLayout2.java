package com.shanghaigm.dms.view.widget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.activity.as.ShowQueryPhotoActivity;
import com.shanghaigm.dms.view.adapter.GridPathAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 利用三方拍照、相册选取以及本机摄像
 * Created by Tom on 2017/9/8.
 */

public class ShowPictureLayout2 extends RelativeLayout {
    public static String SHOW_QUERY_PHOTO = "show_query_photo";
    public static String SHOW_PHOTO = "show_photo";
    private Context context;
    private ArrayList<PathInfo> paths;
    private String title;
    private MyGridView gv;
    private GridPathAdapter adapter;
    private Boolean isPic;
    private static String TAG = "ShowPicture";
    private Button btn;
    private LoadingDialog dialog;
    private DmsApplication app;
    private File root;
    private ArrayList<Bitmap> bits;
    private ArrayList<ArrayList<PathInfo>> allPaths;
    private int type;
    public static ArrayList<PathInfo> pathsDelete;
    private ImageView img;
    private int img_path;
    private int flag;
    private ArrayList<String> cpPaths;        //压缩图片路径集合
    public static int REQUEST_IMAGE;
    public static int REQUEST_VIDEO;
    public static String mPublicVideoPath;

    /**
     * @param context  activity
     * @param paths    某功能路径
     * @param title    标题
     * @param img_path 功能图标
     * @param isPic    判断图片和视频
     * @param allPaths 全部路径
     * @param type     功能标识
     * @param flag     判断新增、修改、查询
     */
    public ShowPictureLayout2(Context context, ArrayList<PathInfo> paths, String title, int img_path, Boolean isPic, ArrayList<ArrayList<PathInfo>> allPaths, int type, int flag) {
        super(context);
        this.context = context;
        this.title = title;
        this.paths = paths;
        this.isPic = isPic;
        this.allPaths = allPaths;
        this.flag = flag;
        this.type = type;
        this.img_path = img_path;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.layout_show_picture, this, true);
        initView(v);
        initData();
        setUpView();
    }

    private void setUpView() {
        //设置图标
        btn.setText(title);
        Drawable homepressed = getResources().getDrawable(img_path);
        Drawable homepressed2 = getResources().getDrawable(R.mipmap.add1);
        homepressed.setBounds(0, 0, homepressed.getMinimumWidth(), homepressed.getMinimumHeight());
        homepressed2.setBounds(0, 0, homepressed2.getMinimumWidth(), homepressed2.getMinimumHeight());
        if (flag == 1 || flag == 2) {
            btn.setCompoundDrawables(homepressed, null, homepressed2, null);
        } else {
            btn.setCompoundDrawables(homepressed, null, null, null);
        }

        gv.setAdapter(adapter);
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

//        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (flag == 2) {
//                    bits.remove(position);
//                    adapter.notifyDataSetChanged();
//                    for (ArrayList<PathInfo> pathInfos : allPaths) {
//                        if (paths.size() > 0) {
//                            if (pathsDelete == null) {
//                                pathsDelete = new ArrayList<PathInfo>();
//                            }
//                            Log.i(TAG, "onItemLongClick:paths.get(0).type        " + paths.get(0).type);
//                            switch (paths.get(0).type) {
//                                case 15:
//                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
//                                    if (pathInfos.size() > 0) {
//                                        if (pathInfos.get(0).type == 15) {
//                                            pathInfos.remove(position);        //得到剩余的路径
//                                        }
//                                    }
//                                    break;
//                                case 16:
//                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
//                                    if (pathInfos.size() > 0) {
//                                        if (pathInfos.get(0).type == 16) {
//                                            pathInfos.remove(position);        //得到剩余的路径
//                                        }
//                                    }
//                                    break;
//                                case 18:
//                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
//                                    if (pathInfos.size() > 0) {
//                                        if (pathInfos.get(0).type == 18) {
//                                            pathInfos.remove(position);        //得到剩余的路径
//                                        }
//                                    }
//                                    break;
//                                case 19:
//                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
//                                    if (pathInfos.size() > 0) {
//                                        if (pathInfos.get(0).type == 19) {
//                                            pathInfos.remove(position);        //得到剩余的路径
//                                        }
//                                    }
//                                    break;
//                                case 20:
//                                    pathsDelete.add(paths.get(position));  //保存被删除的路径
//                                    if (pathInfos.size() > 0) {
//                                        if (pathInfos.get(0).type == 20) {
//                                            pathInfos.remove(position);        //得到剩余的路径
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                    }
//                }
//                return true;
//            }
//        });
        //1.新增传入本机路径并处理；2.修改和查询传入下载路径
        if (isPic) {
            //如果有文件，就不要再下载
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (flag == 2) {
                        if (paths.get(position).file_id == 0) {          //新添加的
                            Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                            Bundle b = new Bundle();
                            b.putString(ShowPictureLayout.SHOW_PHOTO, paths.get(position).path);
                            intent.putExtras(b);
                            context.startActivity(intent);
                        } else {
                            //有则直接取，无则下载
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
                        }
                    } else {
                        //查询
                        if (paths.size() > 0) {
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
                        }
                    }
                }
            });
        }

        if (!isPic) {
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LoadOrScanPopupWindow pop = new LoadOrScanPopupWindow(context, paths, flag == 2, 0);
                    pop.showPopup(gv);
                }
            });
        }
        if (flag == 2 || flag == 1) {
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type) {
                        case 15:
                            REQUEST_IMAGE = 10015;
                            break;
                        case 16:
                            REQUEST_IMAGE = 10016;
                            break;
                        case 18:
                            REQUEST_IMAGE = 10018;
                            break;
                        case 19:
                            REQUEST_IMAGE = 10019;
                            break;
                        case 20:
                            REQUEST_VIDEO = 10020;
                            break;
                    }
                    if (type != 20) {       //拍照和相册选取
                        if (ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED                    //未允许
                                ||
                                ContextCompat.checkSelfPermission(context,
                                        android.Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((ReportUpdateActivity) context,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                                    ReportUpdateActivity.RUNTIME_PERMISSION_REQUEST_CODE1);
                        } else {
                            MultiImageSelector.create()
                                    .showCamera(true) // show camera or not. true by default
                                    .count(3) // max select image size, 9 by default. used width #.multi()
                                    .multi() // multi mode, default mode;
                                    .start((ReportUpdateActivity) context, ShowPictureLayout2.REQUEST_IMAGE);
                        }
                    } else {    //摄像
                        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (videoIntent.resolveActivity(context.getPackageManager()) != null) {
                            File videoFile = null;
                            try {
                                videoFile = createPublicVideoFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (videoFile != null) {
                                Uri videoUri = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    videoUri = FileProvider.getUriForFile(context, "com.shanghaigm.fileprovider", videoFile);
                                } else {
                                    videoUri = Uri.fromFile(videoFile);
                                }
                                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                            }
                        }
                        videoIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        ((Activity) context).startActivityForResult(videoIntent, REQUEST_VIDEO);
                        galleryAddVideo();
                    }
                }
            });
        }
    }
    private File createPublicVideoFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        // Create an video file name
        Log.i(TAG, "path:" + path.getAbsolutePath());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String videoFileName = "VID" + timeStamp;
        File video = File.createTempFile(
                videoFileName,  /* 前缀 */
                ".3gp",         /* 后缀 */
                path      /* 文件夹 */
        );
        mPublicVideoPath = video.getAbsolutePath();
        return video;
    }
    //把视频存到相册
    private void galleryAddVideo() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPublicVideoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
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

        app = DmsApplication.getInstance();
        if (cpPaths == null) {
            cpPaths = new ArrayList<>();
        }
        for (PathInfo info : paths) {
            cpPaths.add(info.cp_path);
        }
        adapter = new GridPathAdapter(context, cpPaths);
    }

    private void initView(View v) {
        gv = (MyGridView) v.findViewById(R.id.gv_show_pic);
        btn = (Button) v.findViewById(R.id.text_title);
        dialog = new LoadingDialog(context, "正在加载");
    }
}
