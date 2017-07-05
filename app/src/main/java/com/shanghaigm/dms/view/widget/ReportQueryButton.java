package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shanghaigm.dms.R;

/**
 * Created by Tom on 2017/7/5.
 */

public class ReportQueryButton extends RelativeLayout{
    public ReportQueryButton(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.button_report_query,this,true);
    }
}
