package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;

/**
 * Created by Tom on 2017/7/5.
 * 每个按钮都定义一个layout方便增减
 */

public class ReportSubButton extends RelativeLayout {
    public ReportSubButton(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.button_report_sub, this, true);
    }
}
