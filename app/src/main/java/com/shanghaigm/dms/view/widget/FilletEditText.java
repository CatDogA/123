package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.shanghaigm.dms.R;

/**
 * Created by Tom on 2017/8/18.
 */

public class FilletEditText extends android.support.v7.widget.AppCompatEditText{
    public FilletEditText(Context context) {
        super(context);
        setBack();
    }

    private void setBack() {
        this.setBackgroundResource(R.drawable.cic_edt_back);
    }
}
