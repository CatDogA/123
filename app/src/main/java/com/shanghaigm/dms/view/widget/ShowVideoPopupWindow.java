package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.VideoView;

import com.shanghaigm.dms.R;

/**
 * Created by Administrator on 2017/7/18.
 */
//TODO-不能用，得删
public class ShowVideoPopupWindow extends PopupWindow{
    private Context context;
    private String videoPath;
    private Button btn;
    private VideoView vv;
    public ShowVideoPopupWindow(Context context,String path){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_show_video, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(h);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        this.videoPath = path;
        initView(v);
        setUpView();
    }

    private void setUpView() {
        vv.setVideoPath(videoPath);
        vv.setMediaController(new MediaController(context));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.start();
            }
        });
    }

    private void initView(View v) {
        vv = (VideoView) v.findViewById(R.id.video);
        btn = (Button) v.findViewById(R.id.btn);
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
