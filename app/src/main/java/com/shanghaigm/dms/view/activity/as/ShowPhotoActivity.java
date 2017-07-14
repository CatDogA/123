package com.shanghaigm.dms.view.activity.as;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

public class ShowPhotoActivity extends AppCompatActivity {
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        initIntent();
    }
    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Bitmap bit = bundle.getParcelable(ReportAttachSubFragment.SHOW_PHOTO);
            img = (ImageView) findViewById(R.id.image);
            img.setImageBitmap(bit);
        }
    }
}
