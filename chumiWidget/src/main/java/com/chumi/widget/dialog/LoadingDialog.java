package com.chumi.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chumi.widget.R;

/**
 * cmat项目内使用的MyDialog修改后的版本
 * Created by CHUMI.Jim on 2016/11/23.
 */

public class LoadingDialog {
    private static final String TAG = "LoadingDialog";
    private Context context;
    private static int dialogCount = 0; // 对话框数量
    private Dialog loadingDlg;

    public LoadingDialog(Context context, String loadingStr) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_loading, null);
        loadingDlg = new Dialog(context, R.style.FullHeightDialog);
        //loadingDlg = new Dialog(context);
        loadingDlg.setContentView(view);
        /** 设置选择框居中显示 */
        Window window = loadingDlg.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }

        TextView tv_dlg_loading_content = (TextView) view.findViewById(R.id.tv_dlg_loading_content);
        tv_dlg_loading_content.setText(loadingStr);
    }

    public static LoadingDialog getInstance(Context context, String loadingStr) {
        return new LoadingDialog(context, loadingStr);
    }

    /**
     * 等待对话框
     */
    public void showLoadingDlg() {
        dialogCount++;
        Log.d(TAG, "showLoadingDlg = " + dialogCount);
        if (!((Activity) context).isFinishing()) {
            if (dialogCount == 1 && loadingDlg != null) {
                loadingDlg.setCancelable(false);
                loadingDlg.setCanceledOnTouchOutside(false);
                loadingDlg.show();
            } else {
                dialogCount--;
            }
        }
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                dismissLoadingDlg();
//            }
//        };
//        Handler handler = new Handler();
//        handler.postDelayed(runnable,10);
    }

    /**
     * 等待对话框消失(关闭对管框)
     */
    public void dismissLoadingDlg() {
        dialogCount--;
        Log.d(TAG, "dismissLoadingDlg = " + dialogCount);
        if (!((Activity) context).isFinishing()) {
            if (dialogCount > 0) {
                return;
            }
            if (loadingDlg != null && loadingDlg.isShowing()) {
                try {
                    loadingDlg.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else {
                dialogCount++;
            }
        }
    }

    /**
     * 重置统计数，以防某个界面的对话框有误导致所有界面的提示框都有问题
     */
    public static void resetCount() {
        dialogCount = 0;
    }
}
