package com.shanghaigm.dms.view.activity.as;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

import java.io.File;

public class ShowQueryPhotoActivity extends AppCompatActivity {
    private ImageView img;
    private DmsApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_query_photo);
        initView();
        initBundle();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
    }

    private void initBundle() {

        Bundle b = getIntent().getExtras();
        String s = b.getString(ShowPictureLayout.SHOW_PHOTO);

        File file = new File(s);
        if (file.exists()) {
            img.setImageBitmap(BitmapFactory.decodeFile(s));
        }
    }
}
