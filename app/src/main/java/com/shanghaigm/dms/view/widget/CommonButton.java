package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanghaigm.dms.R;

/**
 * Created by Administrator on 2017/12/18.
 */

public class CommonButton extends RelativeLayout{
    public CommonButton(Context context,String name,int imgPath) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_common_btn, this, true);
        ImageView img = (ImageView) v.findViewById(R.id.img_common_btn);
        TextView txt = (TextView) v.findViewById(R.id.txt_common_btn);
        img.setImageResource(imgPath);
        txt.setText(name);
    }
}
