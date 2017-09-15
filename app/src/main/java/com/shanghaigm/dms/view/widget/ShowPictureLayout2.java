package com.shanghaigm.dms.view.widget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.activity.as.ShowQueryPhotoActivity;
import com.shanghaigm.dms.view.activity.as.ShowVideoActivity;
import com.shanghaigm.dms.view.adapter.GridPathAdapter;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

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
    private ArrayList<ArrayList<PathInfo>> allPaths;
    private int type;
    public static ArrayList<PathInfo> pathsDelete;
    private ImageView img;
    private int img_path;
    private ArrayList<String> cpPaths;        //压缩图片路径集合
    public static int REQUEST_IMAGE;
    public static int REQUEST_VIDEO, REQUEST_VIDEO2;
    public static String mPublicVideoPath;
    private PopupWindow popupWindow;
    private View popupView;
    private TranslateAnimation animation;
    private Boolean isQuery;
    private int position1;
    private Boolean isSave;

    /**
     * @param context  activity
     * @param paths    某功能路径
     * @param title    标题
     * @param img_path 功能图标
     * @param isPic    判断图片和视频
     * @param allPaths 全部路径
     * @param type     功能标识
     */
    public ShowPictureLayout2(Context context, ArrayList<PathInfo> paths, String title, int img_path, Boolean isPic, ArrayList<ArrayList<PathInfo>> allPaths, int type, Boolean isQuery) {
        super(context);
        this.context = context;
        this.title = title;
        this.paths = paths;
        this.isPic = isPic;
        this.allPaths = allPaths;
        this.type = type;
        this.img_path = img_path;
        this.isQuery = isQuery;
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
        if (ReportUpdateActivity.flag == 1 || ReportUpdateActivity.flag == 2) {
            btn.setCompoundDrawables(homepressed, null, homepressed2, null);
        } else {
            btn.setCompoundDrawables(homepressed, null, null, null);
        }
        if (isQuery) {
            btn.setEnabled(false);       //查询不可使用
        }
        gv.setAdapter(adapter);
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        //1.新增传入本机路径并处理；2.修改和查询传入下载路径
        if (isPic) {
            //如果有文件，就不要再下载
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    position1 = position;
                    choose(false);
                }
            });
        }

        if (!isPic) {
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position1 = position;
                    choose(false);
                }
            });
        }
        if (ReportUpdateActivity.flag == 2 || ReportUpdateActivity.flag == 1 && !isQuery) {
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paths.size() < 8) {
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
                                REQUEST_VIDEO2 = 10021;
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
                            choose(true);
                        }
                    } else {
                        Toast.makeText(context, getResources().getText(R.string.too_many), Toast.LENGTH_SHORT).show();
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

    private String savePic() {
        dialog.showLoadingDlg();
        final String[] s = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(position1).name;
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(position1).path);
                if (isSave) {
                    s[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_pic");
                } else {
                    s[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_cp");
                    Toast.makeText(context, getResources().getText(R.string.cached), Toast.LENGTH_SHORT).show();
                }
                if (!s[0].equals("")) {
                    if (!isSave) {
                        dialog.dismissLoadingDlg();
                        Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ShowPictureLayout.SHOW_PHOTO, s[0]);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        dialog.dismissLoadingDlg();
                        Toast.makeText(context, getResources().getString(R.string.loaded), Toast.LENGTH_SHORT).show();
                    }
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
        dialog = new LoadingDialog(context, "正在加载", 120000);
    }

    /**
     * 弹出popupWindow
     */
    private void choose(Boolean isVideotape) {
        if (popupWindow == null) {
            popupView = View.inflate(context, R.layout.pop_big_or_delete, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);

            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        if (isVideotape) {
            popupView.findViewById(R.id.btn_video_tape).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.btn_choose).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.btn_preview).setVisibility(View.GONE);
            popupView.findViewById(R.id.btn_load).setVisibility(View.GONE);
            popupView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
            popupView.findViewById(R.id.btn_video_tape).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    popupWindow.dismiss();
                }
            });
            popupView.findViewById(R.id.btn_choose).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) context).startActivityForResult(i, REQUEST_VIDEO2);
                    popupWindow.dismiss();
                }
            });
        } else {
            if (isPic) {
                popupView.findViewById(R.id.btn_preview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ReportUpdateActivity.flag == 2) {
                            if (paths.get(position1).file_id == 0) {          //新添加的
                                Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                Bundle b = new Bundle();
                                b.putString(ShowPictureLayout.SHOW_PHOTO, paths.get(position1).path);
                                intent.putExtras(b);
                                context.startActivity(intent);
                            } else {
                                //有则直接取，无则下载
                                Log.i(TAG, "onClick:         " + position1);
                                File file = new File(root.getPath() + "/report_cp");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File file2 = new File(file, paths.get(position1).name);
                                if (file2.exists()) {
                                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString(ShowPictureLayout.SHOW_PHOTO, file2.getPath());
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                } else {
                                    isSave = false;
                                    savePic();
                                }
                            }
                        } else {
                            //查询
                            if (paths.size() > 0) {
                                File file = new File(root.getPath() + "/report_cp");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File file2 = new File(file, paths.get(position1).name);
                                if (file2.exists()) {
                                    Intent intent = new Intent(context, ShowQueryPhotoActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString(ShowPictureLayout.SHOW_PHOTO, file2.getPath());
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                } else {
                                    isSave = false;
                                    savePic();
                                }
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ReportUpdateActivity.flag == 2) {
                            if (paths.get(position1).file_id == 0) {          //新添加的
                                Toast.makeText(context, "图片已存在", Toast.LENGTH_SHORT).show();
                            } else {
                                //有则直接取，无则下载
                                File file = new File(root.getPath() + "/report_pic");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File file2 = new File(file, paths.get(position1).name);
                                if (file2.exists()) {
                                    Toast.makeText(context, "图片已存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    isSave = true;
                                    savePic();
                                }
                            }
                        } else {
                            //查询
                            if (paths.size() > 0) {
                                File file = new File(root.getPath() + "/report_pic");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File file2 = new File(file, paths.get(position1).name);
                                if (file2.exists()) {
                                    Toast.makeText(context, "图片已存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    isSave = true;
                                    savePic();
                                }
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
                if (!isQuery) {
                    popupView.findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete();
                            popupWindow.dismiss();
                        }
                    });
                } else {
                    popupView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
                }

                popupView.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                //视频
            } else {
                popupView.findViewById(R.id.btn_preview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //修改
                        if (!isQuery) {
                            if (paths.size() > 0) {
                                if (position1 == 0) {
                                    //0说明是修改后的,直接进入
                                    if (paths.get(0).file_id == 0) {
                                        Log.i(TAG, "onItemClick: " + "修改");
                                        Intent intent = new Intent(context, ShowVideoActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString(ReportAttachSubFragment.VIDEO_PATH, paths.get(0).path);
                                        intent.putExtras(b);
                                        context.startActivity(intent);
                                    } else {
                                        //加载视频
                                        File file = new File(root.getPath() + "/report_video_cp");
                                        if (!file.exists()) {
                                            file.mkdir();
                                        }
                                        String name = paths.get(0).name;
                                        if (name.endsWith("jpg")) {
                                            name = name.replace("jpg", "3gp");
                                        }
                                        File file2 = new File(file, name);
                                        if (file2.exists()) {
                                            Log.i(TAG, "onItemClick: " + "已有，直接查看" + file2.getPath());
                                            Intent intent = new Intent(context, ShowVideoActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                                            intent.putExtras(b);
                                            context.startActivity(intent);
                                        } else {
                                            Log.i(TAG, "onItemClick: " + "保存");
                                            saveVideo(false);
                                        }
                                    }
                                }
                            }
                            //查询
                        } else {
                            //加载视频
                            File file = new File(root.getPath() + "/report_video_cp");
                            if (!file.exists()) {
                                file.mkdir();
                            }
                            String name = paths.get(0).name;
                            if (name.endsWith("jpg")) {
                                name = name.replace("jpg", "3gp");
                            }
                            File file2 = new File(file, name);
                            if (file2.exists()) {
                                Log.i(TAG, "onItemClick: " + "已经有了");
                                Intent intent = new Intent(context, ShowVideoActivity.class);
                                Bundle b = new Bundle();
                                b.putString(ReportAttachSubFragment.VIDEO_PATH, file2.getPath());
                                intent.putExtras(b);
                                context.startActivity(intent);
                            } else {
                                Log.i(TAG, "onItemClick: " + "还没有，要加载");
                                saveVideo(false);
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isQuery) {
                            if (paths.size() > 0) {
                                //加载视频
                                File file = new File(root.getPath() + "/report_video");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                String name = paths.get(0).name;
                                if (name.endsWith("jpg")) {
                                    name = name.replace("jpg", "3gp");
                                }
                                File file2 = new File(file, name);
                                if (file2.exists()) {
                                    Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "onItemClick: " + "保存");
                                    saveVideo(true);
                                }
                            }
                            //查询
                        } else {
                            //加载视频
                            File file = new File(root.getPath() + "/report_video");
                            if (!file.exists()) {
                                file.mkdir();
                            }
                            String name = paths.get(0).name;
                            if (name.endsWith("jpg")) {
                                name = name.replace("jpg", "3gp");
                            }
                            File file2 = new File(file, name);
                            if (file2.exists()) {
                                Log.i(TAG, "onItemClick: " + "已经有了");
                                Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "onItemClick: " + "还没有，要加载");
                                saveVideo(true);
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
                if (!isQuery) {
                    popupView.findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete();
                            popupWindow.dismiss();
                        }
                    });
                } else {
                    popupView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
                }
            }
        }
        popupView.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (ReportUpdateActivity.flag == 1) {
            popupView.findViewById(R.id.btn_load).setVisibility(View.GONE);
        }
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(btn, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }

    private void delete() {
        if (ReportUpdateActivity.flag == 2 || ReportUpdateActivity.flag == 1) {
            for (ArrayList<PathInfo> pathInfos : allPaths) {
                if (paths.size() > 0) {
                    if (pathsDelete == null) {
                        pathsDelete = new ArrayList<PathInfo>();
                    }
                    Log.i(TAG, "onItemLongClick:paths.get(0).type        " + paths.get(0).type);
                    switch (paths.get(0).type) {
                        case 15:
                            if (pathInfos.size() > 0) {
                                if (pathInfos.get(0).type == 15) {
                                    pathInfos.remove(position1);        //得到剩余的路径
                                }
                            }
                            break;
                        case 16:
                            if (pathInfos.size() > 0) {
                                if (pathInfos.get(0).type == 16) {
                                    pathInfos.remove(position1);        //得到剩余的路径
                                }
                            }
                            break;
                        case 18:
                            if (pathInfos.size() > 0) {
                                if (pathInfos.get(0).type == 18) {
                                    pathInfos.remove(position1);        //得到剩余的路径
                                }
                            }
                            break;
                        case 19:
                            if (pathInfos.size() > 0) {
                                if (pathInfos.get(0).type == 19) {
                                    pathInfos.remove(position1);        //得到剩余的路径
                                }
                            }
                            break;
                        case 20:
                            if (pathInfos.size() > 0) {
                                if (pathInfos.get(0).type == 20) {
                                    pathInfos.remove(position1);        //得到剩余的路径
                                }
                            }
                            break;
                    }
                }
            }
            pathsDelete.add(paths.get(position1));
            Log.i(TAG, "delete:         " + pathsDelete.size() + "          " + pathsDelete.get(0).path);
            paths.remove(position1);
            cpPaths.remove(position1);
            adapter.notifyDataSetChanged();
        }
    }

    private String saveVideo(final Boolean isLoad) {
        dialog.showLoadingDlg();
        final String[] vPath = {""};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = paths.get(0).name;
                if (name.endsWith("jpg")) {
                    name = name.replace("jpg", "3gp");
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileNames", name);
                params.put("fileId", paths.get(0).path);
                if (isLoad) {
                    vPath[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_video");
                } else {
                    vPath[0] = HttpUpLoad.downloadFile(name, params, Constant.URL_DOWNLOAD_FILE, "/report_video_cp");
                }
                Log.i(TAG, "run:从后台加载视频path     " + paths.get(0).path + "   加载之后的路径    " + vPath[0]);
                if (!vPath[0].equals("")) {
                    dialog.dismissLoadingDlg();
                    if (isLoad) {
                        //strDir视频路径
                        Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, ShowVideoActivity.class);
                        Bundle b = new Bundle();
                        b.putString(ReportAttachSubFragment.VIDEO_PATH, vPath[0]);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    }
                }
            }
        }).start();
        return vPath[0];
    }
}
