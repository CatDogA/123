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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.model.util.ScreenUtil;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.activity.mm.CenterActivity;
import com.shanghaigm.dms.view.fragment.ck.OrderSubFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/9/6.
 */

public class EnsureDeletePopUpWindow extends PopupWindow {
    private Context context;
    private Button btn_yes, btn_no;
    private LoadingDialog dialog;
    private TextView txtType;
    private static String TAG = "EnsureDeletePopUpWindow";
    private DmsApplication app;
    private EditText edtRemark;
    private LinearLayout llRemark;

    public EnsureDeletePopUpWindow(final Context context, final int flag, final int id, final int id2, final int id3) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_ensure_delete, null, false);
        this.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        if (flag == 9 || flag == 13 || flag == 17 || flag == 10 || flag == 14 || flag == 18) {
            this.setHeight(h / 3);
        }else {
            this.setHeight(h / 5);
        }
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.update();
        this.context = context;
        this.app = DmsApplication.getInstance();
        dialog = new LoadingDialog(context, "正在处理");
        btn_yes = (Button) v.findViewById(R.id.btn_yes);
        btn_no = (Button) v.findViewById(R.id.btn_no);
        txtType = (TextView) v.findViewById(R.id.txt_type);
        edtRemark = (EditText) v.findViewById(R.id.edt_remark);
        llRemark = (LinearLayout) v.findViewById(R.id.ll_remark);
        switch (flag) {
            case 4:
            case 7:
            case 12:
            case 15:
                txtType.setText("是否确定提交?");
                break;
            case 6:
                txtType.setText("是否确定作废?");
                break;
            case 8:
            case 11:
            case 16:
                txtType.setText("是否确定删除?");
                break;
            case 9:
            case 13:
            case 17:
                llRemark.setVisibility(View.VISIBLE);
                txtType.setText("是否确定通过?");
                break;
            case 10:
            case 14:
            case 18:
                llRemark.setVisibility(View.VISIBLE);
                txtType.setText("是否确定驳回?");
                break;
        }

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
                if (flag == 2) {
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
                if (flag == 3) {
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
                //区域竞品/月报、周、日提交/删除/通过/驳回
                String url = "";
                if (flag == 10 || flag == 14 || flag == 18) {
                    if (edtRemark.getText().toString().equals("")) {
                        Toast.makeText(context, "驳回须填写备注", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (flag != 1 && flag != 2 && flag != 3) {
                    Map<String, Object> params = new HashMap<>();
                    switch (flag) {
                        case 4:
                            url = Constant.URL_AREA_COM_SUB2;
                            params.put("competitor_id", id);
                            break;
                        case 5:
                            url = Constant.URL_AREA_COM_DELETE;
                            params.put("competitor_id", id);
                            break;
                        case 6:
                            url = Constant.URL_AREA_COM_CANCEL;
                            params.put("competitor_id", id);
                            break;
                        case 7:
                            url = Constant.URL_MONTH_SUB;
                            params.put("id", id);
                            break;
                        case 8:
                            url = Constant.URL_MONTH_DELETE;
                            params.put("ids", id);
                            break;
                        case 9:
                            params.put("file_id", id);
                            params.put("remarks", edtRemark.getText().toString());
                            params.put("flow_details_id", id2);
                            params.put("flow_type", id3);
                            url = Constant.URL_MONTH_REPORT_REVIEW;
                            params.put("examination_result", 1);
                            break;
                        case 10:  //驳回
                            url = Constant.URL_MONTH_REPORT_REVIEW;
                            params.put("remarks", edtRemark.getText().toString());
                            params.put("file_id", id);
                            params.put("flow_details_id", id2);
                            params.put("flow_type", id3);
                            params.put("examination_result", 2);
                            break;
                        case 11:
                            url = Constant.URL_WEEK_REPORT_DELETE;
                            params.put("week_id", id);
                            break;
                        case 12:
                            url = Constant.URL_WEEK_REPORT_SUB;
                            params.put("week_id", id);
                            break;
                        case 13:
                            url = Constant.URL_WEEK_REPORT_REVIEW;
                            params.put("remarks", edtRemark.getText().toString());
                            params.put("file_id", id);
                            params.put("flow_details_id", id2);
                            params.put("flow_type", id3);
                            params.put("examination_result", 1);
                            break;
                        case 14:   //驳回
                            url = Constant.URL_WEEK_REPORT_REVIEW;
                            params.put("remarks", edtRemark.getText().toString());
                            params.put("file_id", id);
                            params.put("flow_details_id", id2);
                            params.put("flow_type", id3);
                            params.put("examination_result", 2);
                            break;
                        case 15:  //日提
                            url = Constant.URL_DAY_REPORT_SUB;
                            params.put("file_id", id);
                            break;
                        case 16:  //日删
                            url = Constant.URL_DAY_REPOET_DELETE;
                            params.put("day_report_id", id);
                            break;
                        case 17:  //日通
                            url = Constant.URL_DAY_REPORT_REVIEW;
                            params.put("remarks", edtRemark.getText().toString());
                            params.put("day_report_id", id);
                            params.put("flow_Type", id2);
                            params.put("flow_details_id", id3);
                            params.put("examination_result", 1);
                            break;
                        case 18:  //日驳回
                            params.put("remarks", edtRemark.getText().toString());
                            url = Constant.URL_DAY_REPORT_REVIEW;
                            params.put("day_report_id", id);
                            params.put("flow_Type", id2);
                            params.put("flow_details_id", id3);
                            params.put("examination_result", 2);
                            break;
                    }
                    params.put("loginName", app.getAccount());
                    params.put("roleCode", app.getRoleCode());
                    params.put("jobCode", app.getJobCode());
                    params.put("login_name", app.getAccount());
                    params.put("job_code", app.getJobCode());
                    params.put("role_code", app.getRoleCode());
                    dialog.showLoadingDlg();
                    Log.i(TAG, "onClick: " + params.toString());
                    OkhttpRequestCenter.getCommonReportRequest(url, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            try {
                                if (((JSONObject) responseObj).getString("returnCode").equals("1")) {
                                    if (flag == 4 || flag == 7 || flag == 12 || flag == 15) {
                                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                                    }
                                    if (flag == 5 || flag == 8 || flag == 11 || flag == 16) {
                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                    if (flag == 6) {
                                        Toast.makeText(context, "作废完成", Toast.LENGTH_SHORT).show();
                                    }
                                    if (flag == 9 || flag == 13 || flag == 17) {
                                        Toast.makeText(context, "通过完成", Toast.LENGTH_SHORT).show();
                                    }
                                    if (flag == 10 || flag == 14 || flag == 18) {
                                        Toast.makeText(context, "驳回成功", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(context, CenterActivity.class);
                                    context.startActivity(intent);
                                    dismiss();
                                }else if(((JSONObject) responseObj).getString("returnCode").equals("-1")){
                                    Toast.makeText(context, ((JSONObject) responseObj).getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            dialog.dismissLoadingDlg();
                            dismiss();
                            Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
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
