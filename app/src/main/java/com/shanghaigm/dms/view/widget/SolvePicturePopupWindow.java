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
    private Button btn_camera, btn_album;
    public static final int CAMERA1 = 10001;
    public static final int ALBUM1= 10002;

    public static final int CAMERA2 = 20001;
    public static final int ALBUM2 = 20002;

    public static final int CAMERA3 = 30001;
    public static final int ALBUM3 = 30002;

    public static final int CAMERA4 = 40001;
    public static final int ALBUM4 = 40002;

    private static String TAG = "SolvePicturePopupWindow";
    private Context context;
    public static String mPublicPhotoPath;
    private int flag;

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
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    private void setUpView() {
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
                        Uri photoURI = FileProvider.getUriForFile(context,
                                "com.shanghaigm.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        switch (flag){
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
                }
                galleryAddPic();
                dismiss();
            }
        });
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseIntent = new Intent(Intent.ACTION_PICK);
                chooseIntent.setType("image/*");
                switch (flag){
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
    }

    private void initView(View v) {
        btn_camera = (Button) v.findViewById(R.id.btn_camera);
        btn_album = (Button) v.findViewById(R.id.btn_album);
    }

    private File createPublicImageFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        // Create an image file name
        Log.i(TAG, "path:" + path.getAbsolutePath());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                path      /* 文件夹 */
        );
        mPublicPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPublicPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case CAMERA:
//                if (resultCode != Activity.RESULT_OK) return;
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = true;
//                // Get the dimensions of the View
//                int targetW = 100;
//                int targetH = 100;
//
//                // Get the dimensions of the bitmap
//                bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(mPublicPhotoPath, bmOptions);
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
//
//                Bitmap bitmap = BitmapFactory.decodeFile(mPublicPhotoPath, bmOptions);
////                img1.setImageBitmap(bitmap);
////                list_img.add(bitmap);
////                adapter.notifyDataSetChanged();
//                Intent intent = new Intent(PictureSolveActivity.this, ReportAddActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(GET_BITMAP, bitmap);
////                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case ALBUM:
//                if (resultCode != Activity.RESULT_OK) return;
//
//                Uri uri = data.getData();
//                Bitmap bit = null;
//                try {
//                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Intent intent1 = new Intent(this,ReportAddActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putParcelable(GET_BITMAP, bit);
////                intent1.putExtras(bundle1);
//                startActivity(intent1);
////                img1.setImageBitmap(bit);
////                list_img.add(bit);
////                adapter.notifyDataSetChanged();
//
//                break;
//        }
//    }
    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
