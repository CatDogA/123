package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.shanghaigm.dms.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/7/31.
 */

public class PopDatePicker extends PopupWindow {
    private DatePicker dp;
    private int year;
    private int month;
    private int day;
    private EditText edt;

    public PopDatePicker(Context context, int divideNum, EditText edt) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View popupView = layoutInflater.inflate(R.layout.pop_date_picker, null, false);
        this.setContentView(popupView);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(h / divideNum);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.edt = edt;
        initView(popupView);
        initData();
        setUpView();
    }

    private void initData() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void setUpView() {
        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                PopDatePicker.this.year = year;
                PopDatePicker.this.day = dayOfMonth;
                PopDatePicker.this.month = monthOfYear;
                edt.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                hidePopup();
            }
        });
    }

    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void hidePopup() {
        this.dismiss();
    }

    private void initView(View v) {
        dp = (DatePicker) v.findViewById(R.id.date_picker);
    }
}
