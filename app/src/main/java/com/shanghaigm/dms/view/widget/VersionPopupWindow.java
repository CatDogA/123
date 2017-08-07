package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.util.ScreenUtil;

/**
 * Created by Administrator on 2017/8/7.
 */

public class VersionPopupWindow extends PopupWindow {
    private Context context;
    private Button btn_update, btn_cancel;
    private View v;

    public VersionPopupWindow(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.layout_choose_version, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(h / 5);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        btn_update = (Button) v.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //浏览器打开
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.pgyer.com/7CGw");
                intent.setData(content_url);
                v.getContext().startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showPopup(View parent,int w) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent, 0, 0);
            int H = ScreenUtil.getScreenHeight(context);
            int W = ScreenUtil.getScreenWidth(context);
            int h = this.getHeight();
            Log.i("location", "showPopup: "+H+"         "+h+"         "+W+"         "+w);
            this.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,(W-w)/2,(H-h)/2);
        } else {
            this.dismiss();
        }
    }
}
