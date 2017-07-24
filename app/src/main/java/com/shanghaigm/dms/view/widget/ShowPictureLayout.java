package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.view.activity.as.ShowPhotoActivity;
import com.shanghaigm.dms.view.adapter.GridViewAdapter;
import com.shanghaigm.dms.view.fragment.as.ReportAttachSubFragment;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/7/21.
 */

public class ShowPictureLayout extends RelativeLayout {
    private Context context;
    private ArrayList<PathInfo> paths;
    private String title;
    private TextView text_title;
    private MyGridView gv;
    private GridViewAdapter adapter;
    private static String TAG = "ShowPicture";
    private Button btn;
    private String path;

    public ShowPictureLayout(Context context, ArrayList<PathInfo> paths, String title) {
        super(context);
        this.context = context;
        this.title = title;
        this.paths = paths;
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.layout_show_picture, this, true);
        initView(v);
        initData();
        setUpView();
    }

    private void setUpView() {
        text_title.setText(title);
        gv.setAdapter(adapter);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowPhotoActivity.class);
                Bundle b = new Bundle();
                b.putString(ReportAttachSubFragment.SHOW_PHOTO, path);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    private void initData() {
//        paths
        ArrayList<Bitmap> bits = new ArrayList<>();
        for (PathInfo path : paths) {
            if (path.kind == 1) {
                Bitmap bit = BitmapFactory.decodeFile(path.path);
                Log.i(TAG, "initData:       " + path.path);
                this.path = path.path;
                bits.add(bit);
            }
        }
        Log.i(TAG, "initData: bits" + bits.size());
        adapter = new GridViewAdapter(context, bits);
    }

    private void initView(View v) {
        text_title = (TextView) v.findViewById(R.id.text_title);
        gv = (MyGridView) v.findViewById(R.id.gv_show_pic);
        btn = (Button) v.findViewById(R.id.btn);
    }
}
