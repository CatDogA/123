package com.shanghaigm.dms.view.activity.as;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

import java.io.File;

public class ShowQueryPhotoActivity extends BaseActivity {
    private ImageView img;
    private RelativeLayout rl_back, rl_out;
    private DmsApplication app;
    private final String TAG = "ShowQueryPhoto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_query_photo);
        initView();
        initBundle();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_out = (RelativeLayout) findViewById(R.id.rl_out);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_out.setVisibility(View.INVISIBLE);
    }

    private void initBundle() {
        Bundle b = getIntent().getExtras();
        String s = b.getString(ShowPictureLayout.SHOW_PHOTO);
        Log.i(TAG, "initBundle: " + s);
        File file = new File(s);
        if (file.exists()) {
//            Glide.with(context).load(s).into(img);
            Glide.with(context)
                    .load(s)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (img == null) {
                                return false;
                            }
                            if (img.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                img.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            ViewGroup.LayoutParams params = img.getLayoutParams();
                            int vw = img.getWidth() - img.getPaddingLeft() - img.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = vh + img.getPaddingTop() + img.getPaddingBottom();
                            img.setLayoutParams(params);
                            return false;
                        }
                    })
                    .into(img);
        }
    }
}
