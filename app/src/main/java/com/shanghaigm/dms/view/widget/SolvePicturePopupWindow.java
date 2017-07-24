package com.shanghaigm.dms.view.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.as.PictureSolveActivity;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tom on 2017/7/14.
 */

public class SolvePicturePopupWindow extends PopupWindow {
    private Button btn_camera, btn_album, btn_video;
    public static final int CAMERA1 = 10001;
    public static final int ALBUM1 = 10002;

    public static final int CAMERA2 = 20001;
    public static final int ALBUM2 = 20002;

    public static final int CAMERA3 = 30001;
    public static final int ALBUM3 = 30002;

    public static final int CAMERA4 = 40001;
    public static final int ALBUM4 = 40002;

    public static final int VIDEO = 50001;
    private static String TAG = "SolvePicturePopupWindow";
    private Context context;
    public static String mPublicPhotoPath;
    public static String mPublicVideoPath;
    private int flag;
    private boolean IsVersion7 = false;
    private RelativeLayout rl_back,rl_out;

    public SolvePicturePopupWindow(Context context, int flag) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_picture_solve, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        this.flag = flag;
        initView(v);
        initData();
        setUpView();
    }

    private void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IsVersion7 = true;
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    private void setUpView() {
        rl_out.setVisibility(View.INVISIBLE);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {//判断是否有相机应用
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createPublicImageFile();//创建临时图片文件
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;
                        if (IsVersion7) {
                            photoURI = FileProvider.getUriForFile(context,
                                    "com.shanghaigm.fileprovider",
                                    photoFile);
                        } else {
                            photoURI = Uri.fromFile(photoFile);
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        switch (flag) {
                            case 1:
                                ((Activity) context).startActivityForResult(takePictureIntent, CAMERA1);
                                break;
                            case 2:
                                ((Activity) context).startActivityForResult(takePictureIntent, CAMERA2);
                                break;
                            case 3:
                                ((Activity) context).startActivityForResult(takePictureIntent, CAMERA3);
                                break;
                            case 4:
                                ((Activity) context).startActivityForResult(takePictureIntent, CAMERA4);
                                break;
                        }
                    }
                    galleryAddPic();
                    dismiss();
                }
            }
        });
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseIntent = new Intent(Intent.ACTION_PICK);
                chooseIntent.setType("image/*");
                switch (flag) {
                    case 1:
                        ((Activity) context).startActivityForResult(chooseIntent, ALBUM1);
                        break;
                    case 2:
                        ((Activity) context).startActivityForResult(chooseIntent, ALBUM2);
                        break;
                    case 3:
                        ((Activity) context).startActivityForResult(chooseIntent, ALBUM3);
                        break;
                    case 4:
                        ((Activity) context).startActivityForResult(chooseIntent, ALBUM4);
                        break;
                }
                dismiss();
            }
        });
        btn_video.setOnClickListener(new View.OnClickListener() {
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
                        if (IsVersion7) {
                            videoUri = FileProvider.getUriForFile(context, "com.shanghaigm.fileprovider", videoFile);
                        } else {
                            videoUri = Uri.fromFile(videoFile);
                        }
                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    }
                }
                videoIntent.addCategory(Intent.CATEGORY_DEFAULT);
                ((Activity) context).startActivityForResult(videoIntent, VIDEO);
                galleryAddVideo();
                dismiss();
            }
        });
    }

    private void initView(View v) {
        btn_camera = (Button) v.findViewById(R.id.btn_camera);
        btn_album = (Button) v.findViewById(R.id.btn_album);
        btn_video = (Button) v.findViewById(R.id.btn_video);
        rl_back = (RelativeLayout) v.findViewById(R.id.rl_back);
        rl_out = (RelativeLayout) v.findViewById(R.id.rl_out);

        if (flag == 5) {
            btn_camera.setVisibility(View.GONE);
            btn_album.setVisibility(View.GONE);
        } else {
            btn_video.setVisibility(View.GONE);
        }
    }

    private File createPublicImageFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPG" + timeStamp;
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                path      /* 文件夹 */
        );
        mPublicPhotoPath = image.getAbsolutePath();
        return image;
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
    //把图片存相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPublicPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    //把视频存到相册
    private void galleryAddVideo() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPublicVideoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
