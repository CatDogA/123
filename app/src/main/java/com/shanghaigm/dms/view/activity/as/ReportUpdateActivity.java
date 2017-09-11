package com.shanghaigm.dms.view.activity.as;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
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
import com.shanghaigm.dms.view.widget.ShowPictureLayout;
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
    public static ArrayList<ArrayList<PathInfo>> allPaths = null;
    public static int daily_id;
    //    private ArrayList<PathInfo> pathInfos;     //每次从相册选择回来的集合
    private int type;
    //    public static ArrayList<String> paths1 = new ArrayList<>(), paths2 = new ArrayList<>(), paths3 = new ArrayList<>(), paths4 = new ArrayList<>();
//    public static ArrayList<String> cpPaths = new ArrayList<>(), cpPaths2 = new ArrayList<>(), cpPaths3 = new ArrayList<>(), cpPaths4 = new ArrayList<>();
//    public static ArrayList<String> names1 = new ArrayList<>(), names2 = new ArrayList<>(), names3 = new ArrayList<>(), names4 = new ArrayList<>();
    public String videoPath;
    //    public static ArrayList<Bitmap> bitmaps = new ArrayList<>(), bitmaps2 = new ArrayList<>(), bitmaps3 = new ArrayList<>(), bitmaps4 = new ArrayList<>();
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

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    //    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SolvePicturePopupWindow.CAMERA1:
//            case SolvePicturePopupWindow.CAMERA2:
//            case SolvePicturePopupWindow.CAMERA3:
//            case SolvePicturePopupWindow.CAMERA4:
//                if (resultCode != Activity.RESULT_OK) return;
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = true;
//                // Get the dimensions of the View
//                WindowManager wm1 = this.getWindowManager();
//                int width1 = wm1.getDefaultDisplay().getWidth();
//                int height1 = wm1.getDefaultDisplay().getHeight();
//                int targetW = width1;
//                int targetH = height1;
//
//                // Get the dimensions of the bitmap
//                bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(app.picPath, bmOptions);
//                Log.i(TAG, "onActivityResult:app.picPath   " + app.picPath);
//                int photoW = bmOptions.outWidth;
//                int photoH = bmOptions.outHeight;
//
//                // Determine how much to scale down the image
//                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//                // Decode the image file into a Bitmap sized to fill the View
//                bmOptions.inJustDecodeBounds = false;
//                bmOptions.inSampleSize = scaleFactor;
//                bmOptions.inPurgeable = true;
//                //获取filename传给压缩图片
//                File file = new File(app.picPath);
//                String name = file.getName();
//                Bitmap bitmap = BitmapFactory.decodeFile(app.picPath, bmOptions);
//                //压缩图片
//                Bitmap cpBit = CpPic(app.picPath, 4);
//                String cpPath = null;
//                try {
//                    //存入路径
//                    cpPath = SaveCpPic(cpBit, name);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                int type = 0;
//                //压缩图片到sdcard
//                switch (requestCode) {
//                    case SolvePicturePopupWindow.CAMERA1:
//                        paths1.add(app.picPath);
//                        cpPaths.add(cpPath);
//                        bitmaps.add(cpBit);
//                        names1.add(name);
//                        type = 15;
//                        break;
//                    case SolvePicturePopupWindow.CAMERA2:
//                        paths2.add(app.picPath);
//                        cpPaths2.add(cpPath);
//                        bitmaps2.add(cpBit);
//                        names2.add(name);
//                        type = 16;
//                        break;
//                    case SolvePicturePopupWindow.CAMERA3:
//                        paths3.add(app.picPath);
//                        cpPaths3.add(cpPath);
//                        bitmaps3.add(cpBit);
//                        names3.add(name);
//                        type = 18;
//                        break;
//                    case SolvePicturePopupWindow.CAMERA4:
//                        paths4.add(app.picPath);
//                        cpPaths4.add(cpPath);
//                        bitmaps4.add(cpBit);
//                        names4.add(name);
//                        type = 19;
//                        break;
//                }
//                //把图片信息添加
//                ArrayList<PathInfo> pathInfos1 = new ArrayList<>();
//                for (ArrayList<PathInfo> paths : allPaths) {
//                    if (paths.size() > 0) {
//                        if (paths.get(0).type == type) {
//                            pathInfos1 = paths;
//                        }
//                    }
//                }
//                allPaths.remove(pathInfos1);
//                pathInfos1.add(new PathInfo(type, app.picPath, cpPath, name, 0));
//                allPaths.add(pathInfos1);
//                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
//                break;
//            case SolvePicturePopupWindow.ALBUM1:
//            case SolvePicturePopupWindow.ALBUM2:
//            case SolvePicturePopupWindow.ALBUM3:
//            case SolvePicturePopupWindow.ALBUM4:
//                if (resultCode != Activity.RESULT_OK) return;
//                Uri uri = data.getData();
//                //将Uri转化为路径
//                String path = ContentUriUtil.getPath(this, uri);
////                File alFile = new File(path);
//                //不压缩
//                Bitmap albumPic = CpPic(path, 1);
//                String albumPath = null;
//                String[] infos = null;
//                try {
//                    //转化为jpg并存入内存
//                    infos = SaveAlbumCpPic(albumPic);
//                    albumPath = infos[0];
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //压缩
//                Bitmap cpAlbumPic = CpPic(path, 4);
//                String cpAlbumPath = null;
//                try {
//                    //压缩后路径,名字与原图相同
//                    cpAlbumPath = SaveCpPic(cpAlbumPic, infos[1]);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bit = null;
////                try {
////                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                }
//                int type2 = 0;
//                switch (requestCode) {
//                    case SolvePicturePopupWindow.ALBUM1:
//                        paths1.add(albumPath);
//                        cpPaths.add(cpAlbumPath);
//                        bitmaps.add(cpAlbumPic);
//                        names1.add(infos[1]);
//                        type2 = 15;
//                        break;
//                    case SolvePicturePopupWindow.ALBUM2:
//                        paths2.add(albumPath);
//                        cpPaths2.add(cpAlbumPath);
//                        bitmaps2.add(cpAlbumPic);
//                        names1.add(infos[1]);
//                        type2 = 16;
//                        break;
//                    case SolvePicturePopupWindow.ALBUM3:
//                        paths3.add(albumPath);
//                        cpPaths3.add(cpAlbumPath);
//                        bitmaps3.add(cpAlbumPic);
//                        names1.add(infos[1]);
//                        type2 = 18;
//                        break;
//                    case SolvePicturePopupWindow.ALBUM4:
//                        paths4.add(albumPath);
//                        cpPaths4.add(cpAlbumPath);
//                        bitmaps4.add(cpAlbumPic);
//                        names1.add(infos[1]);
//                        type2 = 19;
//                        break;
//                }
//                //把图片信息添加
//                ArrayList<PathInfo> pathInfos2 = new ArrayList<>();
//                for (ArrayList<PathInfo> paths : allPaths) {
//                    if (paths.size() > 0) {
//                        if (paths.get(0).type == type2) {
//                            pathInfos2 = paths;
//                        }
//                    }
//                }
//                allPaths.remove(pathInfos2);
//                pathInfos2.add(new PathInfo(type2, infos[0], cpAlbumPath, infos[1], 0));
//                allPaths.add(pathInfos2);
//                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
//                break;
//            case SolvePicturePopupWindow.VIDEO:
//                File video = new File(SolvePicturePopupWindow.mPublicVideoPath);
//                String vName = video.getName();
////                ReportAttachSubFragment fragment = ReportAttachSubFragment.getInstance();
////                fragment.ChangeImage(SolvePicturePopupWindow.mPublicVideoPath);
//                videoPath = SolvePicturePopupWindow.mPublicVideoPath;
//                //通过路径获得图片
//                Log.i(TAG, "onActivityResult:   " + videoPath);
//                Bitmap video_bit = getVideoThumb(videoPath);
//                String video_cp_path = "";
//                try {
//                    //保存图片到路径
//                    video_cp_path = SaveCpPic(video_bit, vName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //把视频信息存入
//                int i = -1;
//                for (int j = 0; j < allPaths.size(); j++) {
//                    if (allPaths.get(j).size() > 0) {
//                        if (allPaths.get(j).get(0).type == 20) {
//                            i = j;
//                        }
//                    }
//                }
//                //已经有
//                if (i != -1) {
//                    if (ShowPictureLayout.pathsDelete == null) {
//                        ShowPictureLayout.pathsDelete = new ArrayList<>();
//                        ShowPictureLayout.pathsDelete.add(allPaths.get(i).get(0));
//                    }
//                    allPaths.get(i).clear();
//                    ArrayList<PathInfo> videoPaths = new ArrayList<>();
//                    videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
//                    allPaths.add(videoPaths);
//                }
//                //还没有
//                if (i == -1) {
//                    ArrayList<PathInfo> videoPaths = new ArrayList<>();
//                    videoPaths.add(new PathInfo(20, videoPath, video_cp_path, vName, 0));
//                    allPaths.add(videoPaths);
//                }
//                ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
//                break;
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (pathInfos == null) {
//            pathInfos = new ArrayList<>();
//        } else {
//            pathInfos.clear();
//        }

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
            }
            ArrayList<PathInfo> pathInfos = new ArrayList<>();
            for (ArrayList<PathInfo> paths : allPaths) {
                if (paths.size() > 0) {
                    Log.i(TAG, "onActivityResult:        " + "??????????????????");
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
//                    fileUtils.saveCpBitmap(bm, name, new FileUtils.SavePic() {
//                        @Override
//                        public void onExchangeFinish(String cpPath) {
//                            pathInfos.add(new PathInfo(type, path, cpPath, name, 0));
//                        }
//                    });
                }
            }
            //添加之后的
            allPaths.add(pathInfos);
            ((ReportUpdateAttachFragment) fragments.get(1)).setPaths(allPaths);
        }
        if (requestCode == ShowPictureLayout2.REQUEST_VIDEO) {
            File video = new File(ShowPictureLayout2.mPublicVideoPath);
            String vName = video.getName();
//                ReportAttachSubFragment fragment = ReportAttachSubFragment.getInstance();
//                fragment.ChangeImage(SolvePicturePopupWindow.mPublicVideoPath);
            videoPath = ShowPictureLayout2.mPublicVideoPath;
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

//    public Bitmap CpPic(String path, int divide) {
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        // Get the dimensions of the View
//        WindowManager wm1 = this.getWindowManager();
//        int width1 = wm1.getDefaultDisplay().getWidth();
//        int height1 = wm1.getDefaultDisplay().getHeight();
//        int targetW = width1 / divide;
//        int targetH = height1 / divide;
//
//        // Get the dimensions of the bitmap
//        bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(app.picPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
//        return bitmap;
//    }

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

//    //存相册原图,全转化为jpg
//    private String[] SaveAlbumCpPic(Bitmap bit) throws IOException {
//        File path = null;
//        path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM);
//        File path2 = new File(path.getPath() + "/report_cp");
//        if (!path2.exists()) {
//            path2.mkdir();
//        }
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
//        String imageFileName = "JPG" + timeStamp;
//        File image = File.createTempFile(
//                imageFileName,  /* 前缀 */
//                ".jpg",         /* 后缀 */
//                path2      /* 文件夹 */
//        );
//        Log.i(TAG, "SaveCpPic: " + image.getPath());
//        try {
//            FileOutputStream out = new FileOutputStream(image);
//            if (bit != null) {
//                if (bit.compress(Bitmap.CompressFormat.PNG, 40, out)) {
//                    out.flush();
//                    out.close();
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String[] infos = {image.getPath(), image.getName()};
//        return infos;
//    }

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
        FileUtils.deleteCpDir();
    }
}
