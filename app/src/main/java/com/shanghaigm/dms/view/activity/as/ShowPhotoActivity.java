package com.shanghaigm.dms.view.activity.as;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;
public class ShowPhotoActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        initView();
        initIntent();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.image);
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String path = bundle.getString(ReportAttachSubFragment.SHOW_PHOTO);
            Log.i("hahaha", "initIntent: "+path);
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
            BitmapFactory.decodeFile(path, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
            img.setImageBitmap(bitmap);
        }
    }
}
