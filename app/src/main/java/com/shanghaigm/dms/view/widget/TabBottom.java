package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;

/**
 * Created by Administrator on 2018/1/21.
 */

public class TabBottom extends LinearLayout {
    private ImageView img;
    private TextView txt;

    public TabBottom(Context context, String name, int imgPath) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_tab_bottom, this, true);
        img = (ImageView) v.findViewById(R.id.img_tab);
        txt = (TextView) v.findViewById(R.id.txt_tab);
        img.setImageResource(imgPath);
        txt.setText(name);
    }

    public void setImg(int imgPath) {
        img.setImageResource(imgPath);
    }

    public void setTxt(String name) {
        txt.setText(name);
    }
}
