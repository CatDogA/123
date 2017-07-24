package com.shanghaigm.dms.model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

/**
 * Created by Tom on 2017/7/20.
 * 压缩图片
 */

public class CompressPicture {
    public static Bitmap CpPic(String path, int divide, int wid, int hgt) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        // Get the dimensions of the View
//        WindowManager wm1 = this.getWindowManager();
//        int width1 = wm1.getDefaultDisplay().getWidth();
//        int height1 = wm1.getDefaultDisplay().getHeight();
        int targetW = wid / divide;
        int targetH = hgt / divide;

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
}
