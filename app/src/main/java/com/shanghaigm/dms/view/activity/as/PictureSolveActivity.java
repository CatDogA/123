package com.shanghaigm.dms.view.activity.as;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureSolveActivity extends BaseActivity {
    private Button btn_camera, btn_album;
    private RelativeLayout rl_end, rl_back;
    public static String GET_BITMAP = "get_bitmap";
    private static final int CAMERA = 1;
    private static final int ALBUM = 2;
    private static String TAG = "PictureSolveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_solve);
        initView();
        initData();
        setUpView();
    }

    private void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    private void setUpView() {
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//判断是否有相机应用
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createPublicImageFile();//创建临时图片文件
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(PictureSolveActivity.this,
                                "com.shanghaigm.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA);
                    }
                }
                galleryAddPic();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DmsApplication app = DmsApplication.getInstance();
                app.endApp();
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseIntent = new Intent(Intent.ACTION_PICK);
                chooseIntent.setType("image/*");
                startActivityForResult(chooseIntent, ALBUM);
            }
        });
    }

    private void initView() {
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_album = (Button) findViewById(R.id.btn_album);
        rl_end = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
    }

    String mPublicPhotoPath;

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
        sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA:
                if (resultCode != Activity.RESULT_OK) return;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                // Get the dimensions of the View
                int targetW = 100;
                int targetH = 100;

                // Get the dimensions of the bitmap
                bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mPublicPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mPublicPhotoPath, bmOptions);
//                img1.setImageBitmap(bitmap);
//                list_img.add(bitmap);
//                adapter.notifyDataSetChanged();
                Intent intent = new Intent(PictureSolveActivity.this, ReportAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(GET_BITMAP, bitmap);
//                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case ALBUM:
                if (resultCode != Activity.RESULT_OK) return;

                Uri uri = data.getData();
                Bitmap bit = null;
                try {
                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Intent intent1 = new Intent(this, ReportAddActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(GET_BITMAP, bit);
//                intent1.putExtras(bundle1);
                startActivity(intent1);
//                img1.setImageBitmap(bit);
//                list_img.add(bit);
//                adapter.notifyDataSetChanged();

                break;
        }
    }
}
