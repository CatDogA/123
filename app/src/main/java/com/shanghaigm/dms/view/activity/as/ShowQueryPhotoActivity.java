package com.shanghaigm.dms.view.activity.as;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

import java.io.File;

public class ShowQueryPhotoActivity extends AppCompatActivity {
    private ImageView img;
    private RelativeLayout rl_back,rl_out;
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
        Log.i(TAG, "initBundle: "+s);

        File file = new File(s);
        if (file.exists()) {
            img.setImageBitmap(BitmapFactory.decodeFile(s));
        }
    }
}
