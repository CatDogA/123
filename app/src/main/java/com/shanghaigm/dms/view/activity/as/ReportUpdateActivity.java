package com.shanghaigm.dms.view.activity.as;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.model.entity.mm.PaperInfo;
import com.shanghaigm.dms.model.util.ContentUriUtil;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailAttachFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailInfoFragment;
import com.shanghaigm.dms.view.fragment.as.ReportUpdateAttachFragment;
import com.shanghaigm.dms.view.fragment.as.ReportUpdateInfoFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReportUpdateActivity extends BaseActivity {
    private ArrayList<BaseFragment> fragments;
    public static String REPORT_DETAIL_INFO = "update_info";
    private TabLayout tabLayout;
    private RelativeLayout rl_back, rl_end;
    private TextView title;
    private FragmentManager fm;
    private DmsApplication app;
    private static String TAG = "ReportUpdateActivity";
    public static ArrayList<ArrayList<PathInfo>> allPaths = null;
    public static int daily_id;
    public static ArrayList<String> paths1 = new ArrayList<>(), paths2 = new ArrayList<>(), paths3 = new ArrayList<>(), paths4 = new ArrayList<>();
    public static ArrayList<String> cpPaths = new ArrayList<>(), cpPaths2 = new ArrayList<>(), cpPaths3 = new ArrayList<>(), cpPaths4 = new ArrayList<>();
    public static ArrayList<String> names1 = new ArrayList<>(), names2 = new ArrayList<>(), names3 = new ArrayList<>(), names4 = new ArrayList<>();
    public String videoPath;
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>(), bitmaps2 = new ArrayList<>(), bitmaps3 = new ArrayList<>(), bitmaps4 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_update);
        initIntent();
        initView();
        initData();
        setUpView();
    }

    private void initIntent() {
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = getIntent().getExtras();
        ReportQueryDetailInfoBean reportDetailInfo = (ReportQueryDetailInfoBean) bundle.getSerializable(PaperInfo.REPORT_DETAI_INFO);
        daily_id = reportDetailInfo.daily_id;
        //进来即清理
        if (allPaths != null) {
            allPaths.clear();
        }
        allPaths = (ArrayList<ArrayList<PathInfo>>) bundle.getSerializable(PaperInfo.REPORT_FILE_INFO);
        Log.i(TAG, "initIntent: allPaths        " + allPaths.size());
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(ReportUpdateActivity.REPORT_DETAIL_INFO, reportDetailInfo);
        ReportUpdateInfoFragment fragment = ReportUpdateInfoFragment.getInstance();
        fragment.setArguments(fragmentBundle);
        ft.add(R.id.fragment_content, fragment).commit();
    }

    private void initData() {
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
                app.endApp();
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

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.report_info)).setTag(0));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.attacn_preview)).setTag(1));

//        fm.beginTransaction().add(R.id.fragment_content, fragments.get(0)).commit();

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
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_report);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SolvePicturePopupWindow.CAMERA1:
            case SolvePicturePopupWindow.CAMERA2:
            case SolvePicturePopupWindow.CAMERA3:
            case SolvePicturePopupWindow.CAMERA4:
                if (resultCode != Activity.RESULT_OK) return;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                // Get the dimensions of the View
                WindowManager wm1 = this.getWindowManager();
                int width1 = wm1.getDefaultDisplay().getWidth();
                int height1 = wm1.getDefaultDisplay().getHeight();
                int targetW = width1;
                int targetH = height1;

                // Get the dimensions of the bitmap
                bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(SolvePicturePopupWindow.mPublicPhotoPath, bmOptions);
                Log.i(TAG, "onActivityResult:   " + SolvePicturePopupWindow.mPublicPhotoPath);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                //获取filename传给压缩图片
                File file = new File(SolvePicturePopupWindow.mPublicPhotoPath);
                String name = file.getName();
                Bitmap bitmap = BitmapFactory.decodeFile(SolvePicturePopupWindow.mPublicPhotoPath, bmOptions);
                //压缩图片
                Bitmap cpBit = CpPic(SolvePicturePopupWindow.mPublicPhotoPath, 4);
                String cpPath = null;
                try {
                    //存入路径
                    cpPath = SaveCpPic(cpBit, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int type = 0;
                //压缩图片到sdcard
                switch (requestCode) {
                    case SolvePicturePopupWindow.CAMERA1:
                        paths1.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        cpPaths.add(cpPath);
                        bitmaps.add(cpBit);
                        names1.add(name);
                        type = 15;
                        break;
                    case SolvePicturePopupWindow.CAMERA2:
                        paths2.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        cpPaths2.add(cpPath);
                        bitmaps2.add(cpBit);
                        names2.add(name);
                        type = 16;
                        break;
                    case SolvePicturePopupWindow.CAMERA3:
                        paths3.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        cpPaths3.add(cpPath);
                        bitmaps3.add(cpBit);
                        names3.add(name);
                        type = 18;
                        break;
                    case SolvePicturePopupWindow.CAMERA4:
                        paths4.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        cpPaths4.add(cpPath);
                        bitmaps4.add(cpBit);
                        names4.add(name);
                        type = 19;
                        break;
                }
                //把图片信息添加
                ArrayList<PathInfo> pathInfos1 = new ArrayList<>();
                for (ArrayList<PathInfo> paths : allPaths) {
                    if (paths.size() > 0) {
                        if (paths.get(0).type == type) {
                            pathInfos1 = paths;
                        }
                    }
                }
                allPaths.remove(pathInfos1);
                pathInfos1.add(new PathInfo(type, SolvePicturePopupWindow.mPublicPhotoPath, cpPath, name, 0));
                allPaths.add(pathInfos1);
                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
                break;
            case SolvePicturePopupWindow.ALBUM1:
            case SolvePicturePopupWindow.ALBUM2:
            case SolvePicturePopupWindow.ALBUM3:
            case SolvePicturePopupWindow.ALBUM4:
                if (resultCode != Activity.RESULT_OK) return;
                Uri uri = data.getData();
                //将Uri转化为路径
                String path = ContentUriUtil.getPath(this, uri);
//                File alFile = new File(path);
                //不压缩
                Bitmap albumPic = CpPic(path, 1);
                String albumPath = null;
                String[] infos = null;
                try {
                    //转化为jpg并存入内存
                    infos = SaveAlbumCpPic(albumPic);
                    albumPath = infos[0];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //压缩
                Bitmap cpAlbumPic = CpPic(path, 4);
                String cpAlbumPath = null;
                try {
                    //压缩后路径,名字与原图相同
                    cpAlbumPath = SaveCpPic(cpAlbumPic, infos[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bit = null;
//                try {
//                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                int type2 = 0;
                switch (requestCode) {
                    case SolvePicturePopupWindow.ALBUM1:
                        paths1.add(albumPath);
                        cpPaths.add(cpAlbumPath);
                        bitmaps.add(cpAlbumPic);
                        names1.add(infos[1]);
                        type2 = 15;
                        break;
                    case SolvePicturePopupWindow.ALBUM2:
                        paths2.add(albumPath);
                        cpPaths2.add(cpAlbumPath);
                        bitmaps2.add(cpAlbumPic);
                        names1.add(infos[1]);
                        type2 = 16;
                        break;
                    case SolvePicturePopupWindow.ALBUM3:
                        paths3.add(albumPath);
                        cpPaths3.add(cpAlbumPath);
                        bitmaps3.add(cpAlbumPic);
                        names1.add(infos[1]);
                        type2 = 18;
                        break;
                    case SolvePicturePopupWindow.ALBUM4:
                        paths4.add(albumPath);
                        cpPaths4.add(cpAlbumPath);
                        bitmaps4.add(cpAlbumPic);
                        names1.add(infos[1]);
                        type2 = 19;
                        break;
                }
                //把图片信息添加
                ArrayList<PathInfo> pathInfos2 = new ArrayList<>();
                for (ArrayList<PathInfo> paths : allPaths) {
                    if (paths.size() > 0) {
                        if (paths.get(0).type == type2) {
                            pathInfos2 = paths;
                        }
                    }
                }
                allPaths.remove(pathInfos2);
                pathInfos2.add(new PathInfo(type2, infos[0], cpAlbumPath, infos[1], 0));
                allPaths.add(pathInfos2);
                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
                break;
            case SolvePicturePopupWindow.VIDEO:
                File video = new File(SolvePicturePopupWindow.mPublicVideoPath);
                String vName = video.getName();
//                ReportAttachSubFragment fragment = ReportAttachSubFragment.getInstance();
//                fragment.ChangeImage(SolvePicturePopupWindow.mPublicVideoPath);
                videoPath = SolvePicturePopupWindow.mPublicVideoPath;
                //通过路径获得图片
                Log.i(TAG, "onActivityResult:   " + videoPath);
                Bitmap video_bit = getVideoThumb(videoPath);
                String video_cp_path = "";
                try {
                    //保存图片到路径
                    video_cp_path = SaveCpPic(video_bit, vName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //把视频信息存入
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
                    if (ShowPictureLayout.pathsDelete == null) {
                        ShowPictureLayout.pathsDelete = new ArrayList<>();
                        ShowPictureLayout.pathsDelete.add(allPaths.get(i).get(0));
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
                break;
        }
    }

    public Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    public Bitmap CpPic(String path, int divide) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        // Get the dimensions of the View
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height1 = wm1.getDefaultDisplay().getHeight();
        int targetW = width1 / divide;
        int targetH = height1 / divide;

        // Get the dimensions of the bitmap
        bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(SolvePicturePopupWindow.mPublicPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return bitmap;
    }

    //存压缩文件
    private String SaveCpPic(Bitmap bit, String name) throws IOException {
        File path = null;
        path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        // Create an image file name
        File image = new File(path.getPath() + "/" + name);
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

    //存相册原图,全转化为jpg
    private String[] SaveAlbumCpPic(Bitmap bit) throws IOException {
        File path = null;
        path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPG" + timeStamp;
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                path      /* 文件夹 */
        );
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
        String[] infos = {image.getPath(), image.getName()};
        return infos;
    }
}
