package com.shanghaigm.dms.view.activity.as;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.FileUtils;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportUpdateAttachFragment;
import com.shanghaigm.dms.view.fragment.as.ReportUpdateInfoFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelector;

public class ReportUpdateActivity extends BaseActivity {
    private ArrayList<BaseFragment> fragments;
    public static String REPORT_DETAIL_INFO = "update_info";
    private TabLayout tabLayout;
    private RelativeLayout rl_back, rl_end;
    private TextView title;
    private FragmentManager fm;
    private DmsApplication app;
    private static String TAG = "ReportUpdateActivity";
    public static ArrayList<ArrayList<PathInfo>> allPaths;
    public static int daily_id;
    private int type;
    public String videoPath;
    public static Boolean isAttachShow;
    private Button btn_save, btn_sub;
    public static int RUNTIME_PERMISSION_REQUEST_CODE1 = 10004;
    public static int flag;     //判断新增:1和修改:2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_update);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("videoPath", ShowPictureLayout2.mPublicVideoPath);
        Log.i(TAG, "onSaveInstanceState:videoPath            " + ShowPictureLayout2.mPublicVideoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            videoPath = savedInstanceState.getString("videoPath");
            Log.i(TAG, "onRestoreInstanceState:videoPath             " + videoPath);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setButton();
    }

    private void initIntent() {
        //修改和查询
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = 2;
            ReportQueryDetailInfoBean reportDetailInfo = (ReportQueryDetailInfoBean) bundle.getSerializable(PaperInfo.REPORT_DETAI_INFO);
            if (reportDetailInfo != null) {
                daily_id = reportDetailInfo.daily_id;
            }
            //进来即清理
            if (allPaths != null) {
                allPaths.clear();
            }
            allPaths = (ArrayList<ArrayList<PathInfo>>) bundle.getSerializable(PaperInfo.REPORT_FILE_INFO);
            for (ArrayList<PathInfo> pathInfos : allPaths) {
                for (PathInfo pathInfo : pathInfos) {
                    Log.i(TAG, "initIntent:    path      " + pathInfo.path + "   cp    " + pathInfo.cp_path);
                }
            }
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putSerializable(ReportUpdateActivity.REPORT_DETAIL_INFO, reportDetailInfo);
            ReportUpdateInfoFragment fragment = ReportUpdateInfoFragment.getInstance();
            fragment.setArguments(fragmentBundle);
            ft.add(R.id.fragment_content, fragment).commit();
        } else {
            daily_id = -1;
            flag = 1;
            allPaths = new ArrayList<>();
            ReportUpdateInfoFragment fragment = ReportUpdateInfoFragment.getInstance();
            ft.add(R.id.fragment_content, fragment).commit();
        }

    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    private void saveInfo() {
        ((ReportUpdateInfoFragment) fragments.get(0)).saveInfo(1);
    }

    private void subInfo() {
        ((ReportUpdateInfoFragment) fragments.get(0)).subInfo();
    }

    private void initData() {
        isAttachShow = false;
        type = 0;
        fragments = new ArrayList<>();
        fragments.add(ReportUpdateInfoFragment.getInstance());
        fragments.add(ReportUpdateAttachFragment.getInstance());
        app = DmsApplication.getInstance();
    }

    private void setUpView() {
        title.setText(getResources().getText(R.string.report_detail));
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.endApp(ReportUpdateActivity.this);
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        if (flag == 1) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.report_fill)).setTag(0));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.attach_sub)).setTag(1));
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.report_info)).setTag(0));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.attach_preview)).setTag(1));
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                int tag = (int) tab.getTag();
                if (!fragments.get(tag).isAdded()) {
                    ft.add(R.id.fragment_content, fragments.get(tag));
                } else {
                    ft.show(fragments.get(tag));
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tag = (int) tab.getTag();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(fragments.get(tag)).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subInfo();
            }
        });
    }

    public void setButton() {
        btn_save.setEnabled(false);
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_report);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_sub = (Button) findViewById(R.id.btn_sub);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ArrayList<String> newPaths = new ArrayList<>();
        if (data != null) {
            if (data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT) != null) {
                newPaths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == ShowPictureLayout2.REQUEST_IMAGE) {
                Log.i(TAG, "onActivityResult:requestCode         " + requestCode);
                switch (requestCode) {
                    case 10015:
                        type = 15;
                        break;
                    case 10016:
                        type = 16;
                        break;
                    case 10018:
                        type = 18;
                        break;
                    case 10019:
                        type = 19;
                        break;
                }
                ArrayList<PathInfo> pathInfos = new ArrayList<>();
                for (ArrayList<PathInfo> paths : allPaths) {
                    if (paths.size() > 0) {
                        if (paths.get(0).type == type) {
                            Log.i(TAG, "onActivityResult:        " + type);
                            pathInfos = paths;
                        }
                    }
                }
                //删除原先的
                allPaths.remove(pathInfos);
                Log.i(TAG, "onActivityResult: " + type + "     " + pathInfos.size());
                int c = -1;
                if (newPaths.size() > 0) {
                    for (final String path : newPaths) {
                        c++;
                        Bitmap bm = BitmapFactory.decodeFile(path);
                        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
                        final String name = time + c + ".jpg";           //获取名字并压缩

                        //异步处理压缩，每次都是新的，所以地址不同，对象也就不同，所以不用担心会走同一个方法！
                        FileUtils fileUtils = new FileUtils();
                        //接口回调处理异步压缩，每次传入参数，等压缩完成后存入数据？
                        final ArrayList<PathInfo> finalPathInfos = pathInfos;
                        Log.i(TAG, "onActivityResult: " + name + "          " + path);
                        fileUtils.saveCpBitmap(bm, name, new FileUtils.SavePic() {
                            @Override
                            public void onExchangeFinish(String cpPath, String name, String path) {
                                Log.i(TAG, "onActivityResult: " + name + "          " + path);
                                finalPathInfos.add(new PathInfo(type, path, cpPath, name, 0));
                            }
                        }, path);
                    }
                }
                Log.i(TAG, "onActivityResult: pathInfos     " + pathInfos.size());
                if (pathInfos.size() <= 8) {
                    //添加之后的
                    allPaths.add(pathInfos);
                    ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
                } else {
                    for (int i = 0; i <= 2; i++) {
                        pathInfos.remove(pathInfos.size() - 1);
                        if (pathInfos.size() <= 8) {
                            //添加之后的
                            allPaths.add(pathInfos);
                            ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
                            Toast.makeText(this, getText(R.string.too_many), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            } else {
                String vName = null;
                Boolean isUse = false;
                //拍摄
                if (requestCode == ShowPictureLayout2.REQUEST_VIDEO) {
                    isUse = true;
                    File video = new File(ShowPictureLayout2.mPublicVideoPath);
                    vName = video.getName();
                    videoPath = ShowPictureLayout2.mPublicVideoPath;
                    solveVideo(isUse,vName);
                }
                //选取
                if (requestCode == ShowPictureLayout2.REQUEST_VIDEO2 && data != null) {
                    isUse = true;
                    Uri selectedVideo = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedVideo,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    videoPath = cursor.getString(columnIndex);
                    File video = new File(videoPath);
                    vName = video.getName();
                    cursor.close();
                    solveVideo(isUse,vName);
                }
                //通过路径获得图片
//                Log.i(TAG, "onActivityResult:   " + videoPath);
//                File file = new File(videoPath);
//                if (file.exists()) {
//                    Bitmap video_bit = null;
//                    try {
//                        video_bit = getVideoThumb(videoPath);
//                    } catch (RuntimeException e) {
//                        Log.i(TAG, "onActivityResult: " + "直接返回");
//                        isUse = false;
//                    }
//
//                    String video_cp_path = "";
//                    try {
//                        //保存图片到路径
//                        video_cp_path = SaveCpPic(video_bit, vName);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //把视频信息存入
//                    if (isUse) {
//                        int i = -1;
//                        for (int j = 0; j < allPaths.size(); j++) {
//                            if (allPaths.get(j).size() > 0) {
//                                if (allPaths.get(j).get(0).type == 20) {
//                                    i = j;
//                                }
//                            }
//                        }
//                        //已经有
//                        if (i != -1) {
//                            if (ShowPictureLayout2.pathsDelete == null) {
//                                ShowPictureLayout2.pathsDelete = new ArrayList<>();
//                                ShowPictureLayout2.pathsDelete.add(allPaths.get(i).get(0));
//                            }
//                            allPaths.get(i).clear();
//                            ArrayList<PathInfo> videoPaths = new ArrayList<>();
//                            videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
//                            allPaths.add(videoPaths);
//                        }
//                        //还没有
//                        if (i == -1) {
//                            ArrayList<PathInfo> videoPaths = new ArrayList<>();
//                            videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
//                            allPaths.add(videoPaths);
//                        }
//                        ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
//                    }
//                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void solveVideo(Boolean isUse,String vName){
        //通过路径获得图片
        Log.i(TAG, "onActivityResult:   " + videoPath);
        File file = new File(videoPath);
        if (file.exists()) {
            Bitmap video_bit = null;
            try {
                video_bit = getVideoThumb(videoPath);
            } catch (RuntimeException e) {
                Log.i(TAG, "onActivityResult: " + "直接返回");
                isUse = false;
            }

            String video_cp_path = "";
            try {
                //保存图片到路径
                video_cp_path = SaveCpPic(video_bit, vName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把视频信息存入
            if (isUse) {
                int i = -1;
                for (int j = 0; j < allPaths.size(); j++) {
                    if (allPaths.get(j).size() > 0) {
                        if (allPaths.get(j).get(0).type == 20) {
                            i = j;
                        }
                    }
                }
                //已经有
                if (i != -1) {
                    if (ShowPictureLayout2.pathsDelete == null) {
                        ShowPictureLayout2.pathsDelete = new ArrayList<>();
                        ShowPictureLayout2.pathsDelete.add(allPaths.get(i).get(0));
                    }
                    allPaths.get(i).clear();
                    ArrayList<PathInfo> videoPaths = new ArrayList<>();
                    videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
                    allPaths.add(videoPaths);
                }
                //还没有
                if (i == -1) {
                    ArrayList<PathInfo> videoPaths = new ArrayList<>();
                    videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
                    allPaths.add(videoPaths);
                }
                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
            }
        }
    }
    public Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    //存压缩文件
    private String SaveCpPic(Bitmap bit, String name) throws IOException {
        File path = null;
        path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File path2 = new File(path.getPath() + "/report_cp");
        if (!path2.exists()) {
            path2.mkdir();
        }
        // Create an image file name
        File image = new File(path2.getPath() + "/" + name);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RUNTIME_PERMISSION_REQUEST_CODE1) {
            Log.i(TAG, "onRequestPermissionsResult: " + grantResults[0]);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MultiImageSelector.create()
                        .showCamera(true) // show camera or not. true by default
                        .count(3) // max select image size, 9 by default. used width #.multi()
                        .multi() // multi mode, default mode;
                        .start(ReportUpdateActivity.this, ShowPictureLayout2.REQUEST_IMAGE);
            } else {
                // Permission Denied
                Toast.makeText(ReportUpdateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String cpPicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/report_cp";
        String preVideoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/report_video_cp";
        FileUtils.deleteCpDir(cpPicDir);
        FileUtils.deleteCpDir(preVideoDir);
    }
}
