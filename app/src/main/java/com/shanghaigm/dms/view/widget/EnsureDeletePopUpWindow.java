package com.shanghaigm.dms.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.model.util.ScreenUtil;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.fragment.ck.OrderSubFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/9/6.
 */

public class EnsureDeletePopUpWindow extends PopupWindow {
    private Context context;
    private Button btn_yes, btn_no;
    private LoadingDialog dialog;
    private static String TAG = "EnsureDeletePopUpWindow";

    public EnsureDeletePopUpWindow(final Context context, final int flag, final int id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_ensure_delete, null, false);
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
        dialog = new LoadingDialog(context, "正在处理");
        btn_yes = (Button) v.findViewById(R.id.btn_yes);
        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("daily_id", id);
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonRequest(Constant.URL_REPORT_DELETE, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess:OnDeleteClick      " + responseObj.toString());
                            dialog.dismissLoadingDlg();
                            JSONObject obj = (JSONObject) responseObj;
                            try {
                                JSONObject result = obj.getJSONObject("resultEntity");
                                String code = result.getString("returnCode");
                                if (code.equals("1")) {
                                    Toast.makeText(context, context.getResources().getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                    HomeActivity.refresh();
                                    dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
                if(flag==2){
                    Map<String, Object> params = new HashMap<>();
                    params.put("letterIds", id + "");
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_CHANGE_LETTER, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess:       " + responseObj.toString());
                            Bundle b = new Bundle();
                            b.putInt(com.shanghaigm.dms.view.activity.ck.HomeActivity.ORDER_LETTER_SUB, 2);
                            Intent intent = new Intent(context, com.shanghaigm.dms.view.activity.ck.HomeActivity.class);
                            intent.putExtras(b);
                            context.startActivity(intent);
                            dismiss();
                            Toast.makeText(context, context.getResources().getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            dialog.dismissLoadingDlg();
                        }
                    });
                }
                if(flag==3){
                    Map<String, Object> params = new HashMap<>();
                    params.put("order_id", id + "");
                    dialog.showLoadingDlg();
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ORDER_DELETE, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            OrderSubFragment fragment = OrderSubFragment.getInstance();
                            fragment.refresh();
                            dismiss();
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 居中显示
     *
     * @param parent 控件所在layout
     * @param w      pop宽
     */
    public void showPopup(View parent, int w) {
        if (!this.isShowing()) {
            int H = ScreenUtil.getScreenHeight(context);
            int W = ScreenUtil.getScreenWidth(context);
            int h = this.getHeight();
            Log.i("location", "showPopup: " + H + "         " + h + "         " + W + "         " + w);
            this.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, (W - w) / 2, (H - h) / 2);
        } else {
            this.dismiss();
        }
    }
}
