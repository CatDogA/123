package com.shanghaigm.dms.view.activity.as;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import com.shanghaigm.dms.model.util.ContentUriUtil;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;
import com.shanghaigm.dms.view.fragment.as.ReportDetailInfoFragment;
import com.shanghaigm.dms.view.fragment.as.ReportInfoFragment;
import com.shanghaigm.dms.view.widget.SolvePicturePopupWindow;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ReportAddActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private FragmentManager fm;
    private ReportInfoFragment reportInfoFragment;
    private ArrayList<BaseFragment> fragments;
    private RelativeLayout rl_back, rl_end;
    private DmsApplication app;
    private TextView title;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps2 = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps3 = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps4 = new ArrayList<>();
    private ArrayList<String> uris = new ArrayList<>();
    private ArrayList<String> uris2 = new ArrayList<>();
    private ArrayList<String> uris3 = new ArrayList<>();
    private ArrayList<String> uris4 = new ArrayList<>();

    private static String TAG = "ReportAddActivity";

    public ReportAddActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);
        initView();
        initData();
        setUpView();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(ReportInfoFragment.getInstance());
        fragments.add(ReportAttachSubFragment.getInstance());
    }

    private void setUpView() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.OutOfApp();
            }
        });
        title.setText(getResources().getText(R.string.report_fill));

        tablayout.setSelectedTabIndicatorColor(Color.GRAY);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tablayout.addTab(tablayout.newTab().setTag(0).setText(getResources().getText(R.string.report_info)));
        tablayout.addTab(tablayout.newTab().setTag(1).setText(getResources().getText(R.string.attach_sub)));

        //先显示基本信息页
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        reportInfoFragment = ReportInfoFragment.getInstance();
        ft.add(R.id.report_add_content, reportInfoFragment).commit();

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                int tag = (int) tab.getTag();
//                isAdded判断fragment是否已被添加到activity
                if (fragments.get(tag).isAdded()) {
                    ft.show(fragments.get(tag)).commit();
                } else {
                    ft.add(R.id.report_add_content, fragments.get(tag)).commit();
                }
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
        tablayout = (TabLayout) findViewById(R.id.tab_layout_report);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_end = (RelativeLayout) findViewById(R.id.rl_out);
        title = (TextView) findViewById(R.id.title_text);
        app = DmsApplication.getInstance();
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

                Bitmap bitmap = BitmapFactory.decodeFile(SolvePicturePopupWindow.mPublicPhotoPath, bmOptions);
                switch (requestCode) {
                    case SolvePicturePopupWindow.CAMERA1:
                        uris.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        bitmaps.add(bitmap);
                        break;
                    case SolvePicturePopupWindow.CAMERA2:
                        uris2.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        bitmaps2.add(bitmap);
                        break;
                    case SolvePicturePopupWindow.CAMERA3:
                        uris3.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        bitmaps3.add(bitmap);
                        break;
                    case SolvePicturePopupWindow.CAMERA4:
                        uris4.add(SolvePicturePopupWindow.mPublicPhotoPath);
                        bitmaps4.add(bitmap);
                        break;
                }
                break;
            case SolvePicturePopupWindow.ALBUM1:
            case SolvePicturePopupWindow.ALBUM2:
            case SolvePicturePopupWindow.ALBUM3:
            case SolvePicturePopupWindow.ALBUM4:
                if (resultCode != Activity.RESULT_OK) return;
                Uri uri = data.getData();
                //将Uri转化为路径
                String path = ContentUriUtil.getPath(this, uri);
                Log.i(TAG, "onActivityResult: " + path);
                Bitmap bit = null;
                try {
                    bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                switch (requestCode) {
                    case SolvePicturePopupWindow.ALBUM1:
                        uris.add(path);
                        bitmaps.add(bit);
                        break;
                    case SolvePicturePopupWindow.ALBUM2:
                        uris2.add(path);
                        bitmaps2.add(bit);
                        break;
                    case SolvePicturePopupWindow.ALBUM3:
                        uris3.add(path);
                        bitmaps3.add(bit);
                        break;
                    case SolvePicturePopupWindow.ALBUM4:
                        uris4.add(path);
                        bitmaps4.add(bit);
                        break;
                }
                break;
        }
    }

    public ArrayList<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public ArrayList<Bitmap> getBitmaps2() {
        return bitmaps2;
    }

    public void setBitmaps2(ArrayList<Bitmap> bitmaps2) {
        this.bitmaps2 = bitmaps2;
    }

    public ArrayList<Bitmap> getBitmaps3() {
        return bitmaps3;
    }

    public void setBitmaps3(ArrayList<Bitmap> bitmaps3) {
        this.bitmaps3 = bitmaps3;
    }

    public ArrayList<Bitmap> getBitmaps4() {
        return bitmaps4;
    }

    public void setBitmaps4(ArrayList<Bitmap> bitmaps4) {
        this.bitmaps4 = bitmaps4;
    }

    public ArrayList<String> getUris() {
        return uris;
    }

    public void setUris(ArrayList<String> uris) {
        this.uris = uris;
    }

    public ArrayList<String> getUris2() {
        return uris2;
    }

    public void setUris2(ArrayList<String> uris2) {
        this.uris2 = uris2;
    }

    public ArrayList<String> getUris3() {
        return uris3;
    }

    public void setUris3(ArrayList<String> uris3) {
        this.uris3 = uris3;
    }

    public ArrayList<String> getUris4() {
        return uris4;
    }

    public void setUris4(ArrayList<String> uris4) {
        this.uris4 = uris4;
    }

    public ReportAddActivity(ArrayList<String> uris, ArrayList<String> uris2, ArrayList<String> uris3, ArrayList<String> uris4) {
        this.uris = uris;
        this.uris2 = uris2;
        this.uris3 = uris3;
        this.uris4 = uris4;
    }
}
