package com.chumi.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.chumi.widget.R;

/**
 *
 * Created by CHUMI.Jim on 2016/12/8.
 */

public class DateTimePickerDialog {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_TIME = 1;

    private Context mContext;
    private Dialog mDialog;
    private int mType;

    private OnDialogButtonClickListener mListener;

    public DateTimePickerDialog(Context mContext, int mType) {
        this.mContext = mContext;
        this.mType = mType;
        initDialog();
        initView();
    }

    private void initView() {
        Button btnSure = (Button) mDialog.findViewById(R.id.btn_sure);
        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateTime = "";
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                switch (mType) {
                    case TYPE_DATE:
                        DatePicker datePicker = (DatePicker) mDialog.findViewById(R.id.date_picker);
                        String monStr;
                        int month = datePicker.getMonth() + 1;
                        if (month < 10) {
                            monStr = "-0" + month;
                        } else {
                            monStr = "-" + month;
                        }
                        String dayStr;
                        int day = datePicker.getDayOfMonth();
                        if (day < 10) {
                            dayStr = "-0" + day;
                        } else {
                            dayStr = "-" + day;
                        }
                        dateTime = "" + datePicker.getYear() + monStr + dayStr;
                        break;

                    case TYPE_TIME:
                        TimePicker timePicker = (TimePicker) mDialog.findViewById(R.id.time_picker);
                        String hourStr;
                        int hour;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                        } else {
                            hour = timePicker.getCurrentHour();
                        }
                        if (hour < 10) {
                            hourStr = "0" + hour;
                        } else {
                            hourStr = "" + hour;
                        }
                        String minStr;
                        int min;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            min = timePicker.getMinute();
                        } else {
                            min = timePicker.getCurrentMinute();
                        }
                        if (min < 10) {
                            minStr = ":0" + min;
                        } else {
                            minStr = ":" + min;
                        }
                        dateTime = hourStr + minStr;
                        break;

                    default:
                        break;
                }
                mDialog.dismiss();
                mListener.onBtnYesClick(dateTime);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mListener.onBtnNoClick();
            }
        });
    }

    private void initDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.dateTimeDialog);
            mDialog.setCancelable(false);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(mType == TYPE_DATE ? R.layout.widget_dlg_date_picker : R.layout.widget_dlg_time_picker);
            Window window = mDialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = dm.widthPixels;
                window.setAttributes(lp);
            }
        }
    }

    public void show() {
        mDialog.show();
    }

    public void setOnClickListener(OnDialogButtonClickListener listener) {
        this.mListener = listener;
    }

    public interface OnDialogButtonClickListener {
        void onBtnYesClick(String dateTime);

        void onBtnNoClick();
    }
}
