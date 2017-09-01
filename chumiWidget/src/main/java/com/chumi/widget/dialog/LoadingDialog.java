package com.chumi.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.R;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * cmat项目内使用的MyDialog修改后的版本
 * Created by CHUMI.Jim on 2016/11/23.
 */

public class LoadingDialog {
    private static final String TAG = "LoadingDialog";
    private Context context;
    private static int dialogCount = 0; // 对话框数量
    private Dialog loadingDlg;
    private Boolean flag = true;
    private String loadingStr;
    private int time;

    public LoadingDialog(Context context, String loadingStr) {
        this.context = context;
        this.loadingStr = loadingStr;
        this.flag = true;
        time = 20000;
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_loading, null);
        loadingDlg = new Dialog(context, R.style.FullHeightDialog);
        loadingDlg.setContentView(view);
        initView(view);
    }

    public LoadingDialog(Context context, String loadingStr, int time) {
        this.context = context;
        this.loadingStr = loadingStr;
        this.flag = true;
        this.time = time;
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_loading, null);
        loadingDlg = new Dialog(context, R.style.FullHeightDialog);
        loadingDlg.setContentView(view);
        initView(view);
    }

    private void initView(View view) {
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
        flag = true;
        Log.i(TAG, "showLoadingDlg = " + dialogCount);
        if (!((Activity) context).isFinishing()) {
            if (dialogCount == 1 && loadingDlg != null) {
                loadingDlg.setCancelable(false);
                loadingDlg.setCanceledOnTouchOutside(false);
                loadingDlg.show();
                mThread.start();
            } else {
                dialogCount--;
            }
        }
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
                    flag = false;
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

    private Thread mThread = new Thread() {
        @Override
        public void run() {
            super.run();
            Lock lock = new ReentrantLock();
            Log.i(TAG, "run:flag线程前：     " + flag);
            if (flag) {
                try {
                    lock.lock();
                    Log.i(TAG, "run: " + "睡觉");
                    for (int i = 0; i < time; i++) {
                        if (flag) {
                            Thread.sleep(1);
                        } else {
                            return;
                        }
                    }
                    Log.i(TAG, "run: " + "睡醒");
                    Log.i(TAG, "run:flag线程后：     " + flag);
                    if (flag) {    //如果sleep期间,dialog已经消失则不再继续执行
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && loadingDlg.isShowing()) {
                if (dialogCount == 1) {
                    loadingDlg.dismiss();
                    Toast.makeText(context, "网络故障", Toast.LENGTH_SHORT).show();
                    dialogCount--;
                }
            }
        }
    };
}
