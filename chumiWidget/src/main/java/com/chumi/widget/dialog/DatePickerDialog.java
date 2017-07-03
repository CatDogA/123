package com.chumi.widget.dialog;

import android.content.Context;

import com.chumi.widget.R;

/**
 *
 * Created by CHUMI.Jim on 2016/12/8.
 */

public class DatePickerDialog extends DateTimePickerDialog {

    public static DatePickerDialog getInstance(Context mContext) {
        return new DatePickerDialog(mContext, DatePickerDialog.TYPE_DATE);
    }

    private DatePickerDialog(Context mContext, int mType) {
        super(mContext, mType);
    }
}
