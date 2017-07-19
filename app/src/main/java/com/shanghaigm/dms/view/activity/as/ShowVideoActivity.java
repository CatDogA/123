package com.shanghaigm.dms.view.activity.as;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

public class ShowVideoActivity extends AppCompatActivity {
    private VideoView vv;
    private static String TAG = "ShowVideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        vv = (VideoView) findViewById(R.id.video);
        Bundle bundle = getIntent().getExtras();
        bundle.getString(ReportAttachSubFragment.VIDEO_PATH);
        vv.setMediaController(new MediaController(this));
        vv.setVideoPath(bundle.getString(ReportAttachSubFragment.VIDEO_PATH));
        vv.start();
    }
}
