package com.chumi.widget.dialog;

import android.content.Context;

import com.chumi.widget.R;

/**
 *
 * Created by CHUMI.Jim on 2016/12/8.
 */

public class TimePickerDialog extends DateTimePickerDialog {
    public static TimePickerDialog getInstance(Context mContext) {
        return new TimePickerDialog(mContext, DatePickerDialog.TYPE_TIME);
    }

    private TimePickerDialog(Context mContext, int mLayoutId) {
        super(mContext, mLayoutId);
    }
}
