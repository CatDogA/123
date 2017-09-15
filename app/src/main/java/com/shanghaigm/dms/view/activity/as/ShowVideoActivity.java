package com.shanghaigm.dms.view.activity.as;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.view.activity.BaseActivity;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

public class ShowVideoActivity extends BaseActivity {
    private VideoView vv;
    private static String TAG = "ShowVideoActivity";
    private RelativeLayout btn_back,btn_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        btn_back = (RelativeLayout) findViewById(R.id.rl_back);
        btn_out = (RelativeLayout) findViewById(R.id.rl_out);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_out.setVisibility(View.GONE);
        vv = (VideoView) findViewById(R.id.video);
        Bundle bundle = getIntent().getExtras();
        bundle.getString(ReportAttachSubFragment.VIDEO_PATH);

        vv.setMediaController(new MediaController(this));
        vv.setVideoPath(bundle.getString(ReportAttachSubFragment.VIDEO_PATH));
        vv.start();
    }
}
