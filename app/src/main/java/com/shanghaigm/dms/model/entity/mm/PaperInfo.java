package com.shanghaigm.dms.model.entity.mm;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataHandle;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.chumi.widget.http.okhttp.CommonOkHttpClient;
import com.chumi.widget.http.okhttp.CommonRequest;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.BasePaperInfo;
import com.shanghaigm.dms.model.entity.as.PathInfo;
import com.shanghaigm.dms.model.entity.as.ReportQueryDetailInfoBean;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportDetailActivity;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterModifyActivity;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterQueryDetailActivity;
import com.shanghaigm.dms.view.activity.ck.OrderModifyActivity;
import com.shanghaigm.dms.view.activity.mm.ChangeBillDetailActivity;
import com.shanghaigm.dms.view.activity.mm.ChangeLetterDetailActivity;
import com.shanghaigm.dms.view.activity.mm.ContractReviewDetailActivity;
import com.shanghaigm.dms.view.activity.mm.OrderDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2017/5/15.
 */

public class PaperInfo extends BasePaperInfo {
    public static String ORDER_TYPE = "order_type";
    public static String ORDER_SUB_DETAIL_INFO = "query_order_sub_detail";      //判断查询还是修改
    public static String ORDER_REVIEW_DETIAL_INFO = "query_order_review_detail";
    public static String REPORT_DETAI_INFO = "report_detail_info";
    public static String REPORT_FILE_INFO = "report_file_info";
    private int flag;//判断进入的详情页面  1.订单审核，2.合同审核，3.更改函审核，4.更改单审核，5.订单提交，6.更改函提交
    private LoadingDialog dialog;
    private static String TAG = "PaperInfo";
    private DmsApplication app = DmsApplication.getInstance();
    private String number;
    private String model;
    private String state;
    private int orderId;
    private String change_letter_number;
    private String contract_id;
    private String examination_result;
    private int flow_details_id;

    private int report_state;
    private String car_sign;
    private String daily_code;
    private int daily_id;

    private Context context;
    private ChangeLetterDetailInfo changeLetterDetailInfo;
    private ContractDetailInfo contractDetailInfo;
    private ChangeBillDetailInfo changeBillDetailInfo;
    private ChangeLetterSubDetailInfo changeLetterSubDetailInfo;

    private ReportQueryDetailInfoBean reportQueryDetailInfoBean;
    private int reportCount = 0;
    private ArrayList<ArrayList<PathInfo>> allPaths = new ArrayList<>();

    public PaperInfo() {
    }

    //合同审核
    public PaperInfo(String name, String number, String model, Boolean isModify, int orderId, Context context, int flag, ContractDetailInfo contractDetailInfo, String examination_result, int flow_details_id) {
        super(name);
        this.number = number;
        this.model = model;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.contractDetailInfo = contractDetailInfo;
        this.examination_result = examination_result;
        this.flow_details_id = flow_details_id;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //更改函审核
    public PaperInfo(String name, String number, String model, Boolean isModify, int orderId, Context context, int flag, ChangeLetterDetailInfo changeLetterDetailInfo, String examination_result, int flow_details_id) {
        super(name);
        this.number = number;
        this.model = model;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.flow_details_id = flow_details_id;
        this.examination_result = examination_result;
        this.changeLetterDetailInfo = changeLetterDetailInfo;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //更改单审核
    public PaperInfo(String name, String number, String model, Boolean isModify, int orderId, Context context, int flag, ChangeBillDetailInfo changeBillDetailInfo, String examination_result, int flow_details_id) {
        super(name);
        this.number = number;
        this.model = model;
        this.examination_result = examination_result;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.flow_details_id = flow_details_id;
        this.changeBillDetailInfo = changeBillDetailInfo;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //更改函提交
    public PaperInfo(String name, String state, String contract_id, String change_letter_number, Context context, int flag, ChangeLetterSubDetailInfo changeLetterSubDetailInfo) {
        super(name);
        this.state = state;
        this.contract_id = contract_id;
        this.change_letter_number = change_letter_number;
        this.context = context;
        this.flag = flag;
        this.changeLetterSubDetailInfo = changeLetterSubDetailInfo;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //订单提交
    public PaperInfo(String name, String number, String model, String state, Boolean isModify, int orderId, Context context, int flag) {
        super(name);
        this.number = number;
        this.model = model;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.state = state;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //订单审核
    public PaperInfo(String name, String number, String model, Boolean isModify, int orderId, Context context, int flag, String examination_result) {
        super(name);
        this.number = number;
        this.model = model;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.examination_result = examination_result;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //日报审核
    public PaperInfo(Context context, String daily_code, String model, String car_sign, int state, int daily_id, int flag) {
        this.daily_code = daily_code;
        this.report_state = state;
        this.model = model;
        this.car_sign = car_sign;
        this.daily_id = daily_id;
        this.flag = flag;
        this.context = context;
        dialog = new LoadingDialog(this.context, "正在加载");
    }

    //提交进入修改界面
    public void onImageViewClick(final View view) {

        if (flag == 5) {
            switch (state) {
                case "1":
                case "4":
                    dialog.showLoadingDlg();
                    Map<String, Object> params = new HashMap<>();
                    params.put("loginName", app.getAccount());
                    params.put("jobCode", app.getJobCode());
                    params.put("order_id", orderId);
                    Log.i(TAG, "onImgClick: " + app.getAccount() + "  " + app.getJobCode() + "  " + orderId);
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_DETAIL_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            JSONObject object = (JSONObject) responseObj;
                            ArrayList<MatchingBean> matchings = new ArrayList<MatchingBean>();
                            try {
                                JSONObject resultEntity = object.getJSONObject("resultEntity");
                                Object matching = resultEntity.get("matching");
                                if (matching.toString().equals("")) {
                                    Log.i(TAG, "onSuccess: " + "没有配置数据");
                                } else {
                                    JSONArray matches = new JSONArray(matching.toString());
                                    for (int i = 0; i < matches.length(); i++) {
                                        JSONObject match = matches.getJSONObject(i);
                                        matchings.add(GsonUtil.GsonToBean(match.toString(), MatchingBean.class));
                                    }
                                    app.setMatchingBeanArrayList(matchings);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OrderDetailInfoBean orderDetailInfoBean = GsonUtil.GsonToBean(responseObj.toString(), OrderDetailInfoBean.class);
                            app.setOrderDetailInfoBean(orderDetailInfoBean);
                            Intent intent1 = new Intent(view.getContext(), OrderModifyActivity.class);
                            view.getContext().startActivity(intent1);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            Log.i(TAG, "onFailure:paperInfo " + reasonObj);
                        }
                    }));

                    break;
            }
        }
        if (flag == 6) {
            app.setChangeLetterSubDetailInfo(changeLetterSubDetailInfo);
            Intent intent = new Intent(view.getContext(), ChangeLetterModifyActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    public void onNoAccessClick(final View view) {
        if (flag == 5) {
            switch (state) {
                case "2":
                case "3":
                    Toast.makeText(view.getContext(), "不可修改", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //订单提交查询详情
    public void onCkQueryOrderDetailClick(final View view) {
        if (flag == 5) {
            dialog.showLoadingDlg();
            Map<String, Object> params = new HashMap<>();
            params.put("loginName", app.getAccount());
            params.put("jobCode", app.getJobCode());
            params.put("order_id", orderId);
            Log.i(TAG, "onImgClick: " + app.getAccount() + "  " + app.getJobCode() + "  " + orderId);
            CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_DETAIL_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
                /**
                 * @param responseObj
                 */
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    JSONObject object = (JSONObject) responseObj;
                    ArrayList<MatchingBean> matchings = new ArrayList<MatchingBean>();
                    try {
                        JSONObject resultEntity = object.getJSONObject("resultEntity");
                        Object matching = resultEntity.get("matching");
                        if (matching.toString().equals("")) {
                            Log.i(TAG, "onSuccess: " + "没有配置数据");
                        } else {
                            JSONArray matches = new JSONArray(matching.toString());
                            for (int i = 0; i < matches.length(); i++) {
                                JSONObject match = matches.getJSONObject(i);
                                matchings.add(GsonUtil.GsonToBean(match.toString(), MatchingBean.class));
                            }
                            app.setMatchingBeanArrayList(matchings);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OrderDetailInfoBean orderDetailInfoBean = GsonUtil.GsonToBean(responseObj.toString(), OrderDetailInfoBean.class);
                    app.setOrderDetailInfoBean(orderDetailInfoBean);
                    Bundle bundle = new Bundle();
                    bundle.putString(PaperInfo.ORDER_TYPE, PaperInfo.ORDER_SUB_DETAIL_INFO);
                    Intent intent1 = new Intent(view.getContext(), OrderDetailActivity.class);
                    intent1.putExtras(bundle);
                    view.getContext().startActivity(intent1);
                }

                @Override
                public void onFailure(Object reasonObj) {
                    Log.i(TAG, "onFailure:paperInfo " + reasonObj);
                }
            }));
        }

        if (flag == 6) {
            app.setChangeLetterSubDetailInfo(changeLetterSubDetailInfo);
            Intent intent = new Intent(view.getContext(), ChangeLetterQueryDetailActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    //审核进入详情
    public void onImageClick(final View view) {
        switch (flag) {
            case 1:
                if (examination_result.equals("-1")) {
                    dialog.showLoadingDlg();
                    Map<String, Object> params = new HashMap<>();
                    params.put("loginName", app.getAccount());
                    params.put("jobCode", app.getJobCode());
                    params.put("order_id", orderId);
                    Log.i(TAG, "onImgClick: " + app.getAccount() + "  " + app.getJobCode() + "  " + orderId);
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_DETAIL_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            JSONObject object = (JSONObject) responseObj;
                            ArrayList<MatchingBean> matchings = new ArrayList<MatchingBean>();
                            try {
                                JSONObject resultEntity = object.getJSONObject("resultEntity");
                                Object matching = resultEntity.get("matching");
                                if (matching.toString().equals("")) {
                                    Log.i(TAG, "onSuccess: " + "没有match");
                                } else {
                                    JSONArray matches = new JSONArray(matching.toString());
                                    for (int i = 0; i < matches.length(); i++) {
                                        JSONObject match = matches.getJSONObject(i);
                                        matchings.add(GsonUtil.GsonToBean(match.toString(), MatchingBean.class));
                                    }
                                    Log.i(TAG, "onSuccess: " + matchings.size());
                                    app.setMatchingBeanArrayList(matchings);
                                }
                                Object flow_details_ids = resultEntity.get("flowdetails");
                                int detail_id = 0;
                                if (!flow_details_ids.toString().equals("")) {
                                    JSONArray details = new JSONArray(flow_details_ids.toString());
                                    detail_id = details.getJSONObject(details.length() - 1).getInt("flow_details_id");
                                } else {
                                }
                                app.setFlow_detail_id(detail_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OrderDetailInfoBean orderDetailInfoBean = GsonUtil.GsonToBean(responseObj.toString(), OrderDetailInfoBean.class);
                            app.setOrderDetailInfoBean(orderDetailInfoBean);
                            Bundle bundle = new Bundle();
                            bundle.putString(PaperInfo.ORDER_TYPE, PaperInfo.ORDER_REVIEW_DETIAL_INFO);
                            Intent intent1 = new Intent(view.getContext(), OrderDetailActivity.class);
                            intent1.putExtras(bundle);
                            view.getContext().startActivity(intent1);
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            Log.i(TAG, "onFailure:paperInfo " + reasonObj);
                        }
                    }));
                }
                break;
            case 2:
                if (examination_result.equals("-1")) {
                    app.setContractDetailInfo(contractDetailInfo);
                    app.setFlow_detail_id(flow_details_id);
                    app.setContract_id(contractDetailInfo.getContract_id());
                    Intent intent2 = new Intent(view.getContext(), ContractReviewDetailActivity.class);
                    view.getContext().startActivity(intent2);
                }
                break;
            case 3:
                if (examination_result.equals("-1")) {
                    app.setChangeLetterDetailInfo(changeLetterDetailInfo);
                    app.setFlow_detail_id(flow_details_id);
                    Intent intent3 = new Intent(view.getContext(), ChangeLetterDetailActivity.class);
                    view.getContext().startActivity(intent3);
                }
                break;
            case 4:
                if (examination_result.equals("-1")) {
                    app.setChangeBillDetailInfo(changeBillDetailInfo);
                    app.setFlow_detail_id(flow_details_id);
                    Intent intent4 = new Intent(view.getContext(), ChangeBillDetailActivity.class);
                    view.getContext().startActivity(intent4);
                }
                break;
            case 7:
                reportCount = 0;
                final ArrayList<PathInfo> pathInfos = new ArrayList<>();
                final ArrayList<String> cpPaths = new ArrayList<>();
                final Map<String, Object> params = new HashMap<>();
                params.put("daily_id", daily_id);
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_REPORT_DETAIL, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            reportCount++;
                            JSONObject resultEntity = object.getJSONObject("resultEntity");
                            reportQueryDetailInfoBean = GsonUtil.GsonToBean(resultEntity.toString(), ReportQueryDetailInfoBean.class);
                            Log.i(TAG, "onSuccess:reportCount    " + reportCount);
                            if (reportCount == 6) {
                                dialog.dismissLoadingDlg();
                                goToReportDetail(view, allPaths);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
                getPaths(15, view);
                getPaths(16, view);
                getPaths(18, view);
                getPaths(19, view);
                getPaths(20, view);
//                Map<String, Object> params2 = new HashMap<>();
//                params2.put("id", daily_id);
//                params2.put("type", "15");
////                params2.put("isById",daily_id);
//                params2.put("loginRole", app.getJobCode());
//                params2.put("loginName", app.getAccount());
//                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_FILE_INFO, params2, new DisposeDataListener() {
//                    /**
//                     * @param responseObj
//                     */
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        Log.i(TAG, "onSuccess:file_info " + responseObj.toString());
//                        JSONObject obj = (JSONObject) responseObj;
//                        try {
//                            final JSONArray resultArray = obj.getJSONArray("result");
//                            if (resultArray.length() > 0) {
//                                for (int i = 0; i < resultArray.length(); i++) {
//                                    final JSONObject fileObj = resultArray.getJSONObject(i);
//                                    final String file_name = fileObj.getString("file_name");
//                                    final String cp_file_path = fileObj.getString("compress_path");
//                                    final String file_path = fileObj.getString("upload_path");
//                                    //整体信息添加
//                                    Log.i(TAG, "onSuccess: path " + file_path.toString() + " pathsize " + pathInfos.size());
//                                    //作用：拼接Url
//                                    final Map<String, String> params3 = new HashMap<String, String>();
//                                    params3.put("fileNames", file_name);
//                                    params3.put("fileId", cp_file_path);
//                                    Log.i(TAG, "onSuccess: cp_file_path     " + cp_file_path);
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //把压缩图片下载到本地并传回地址
//                                            String s = HttpUpLoad.downloadFile(file_name, params3, Constant.URL_DOWNLOAD_FILE, "/report_cp");
//                                            if (!s.equals("") && s != null) {
//                                                dialog.dismissLoadingDlg();
//                                                Log.i(TAG, "run:    " + s.toString());
//                                                cpPaths.add(s);
//                                                //s是处理后压缩文件在内存中的路径
//                                                //file_path是原文件下载路径
//                                                pathInfos.add(new PathInfo(15, file_path, s, file_name));
//                                                //路径是内存路径
//                                                if (pathInfos.size() == resultArray.length()) {   //下载完毕
//                                                    reportCount++;
//                                                    allPaths.add(pathInfos);
//                                                    if (reportCount == 2) {
//                                                        dialog.dismissLoadingDlg();
//                                                        goToReportDetail(view, allPaths);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }).start();
//                                }
//                            } else {
//                                reportCount++;
//                                if (reportCount == 2) {
//                                    dialog.dismissLoadingDlg();
//                                    goToReportDetail(view, allPaths);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Object reasonObj) {
//
//                    }
//                });
                break;
        }
    }

    private void goToReportDetail(View view, ArrayList<ArrayList<PathInfo>> allPaths) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PaperInfo.REPORT_DETAI_INFO, reportQueryDetailInfoBean);
        bundle.putSerializable(PaperInfo.REPORT_FILE_INFO, allPaths);
        Intent intent7 = new Intent(view.getContext(), ReportDetailActivity.class);
        intent7.putExtras(bundle);
        view.getContext().startActivity(intent7);
    }

    private void getPaths(final int type, final View view) {
        Map<String, Object> params2 = new HashMap<>();
        final ArrayList<PathInfo> pathInfos = new ArrayList<>();
        params2.put("id", daily_id);
        params2.put("type", type + "");
//                params2.put("isById",daily_id);
        params2.put("loginRole", app.getJobCode());
        params2.put("loginName", app.getAccount());
        OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_FILE_INFO, params2, new DisposeDataListener() {
            /**
             * @param responseObj
             */
            @Override
            public void onSuccess(Object responseObj) {
                Log.i(TAG, "onSuccess:file_info " + responseObj.toString());
                JSONObject obj = (JSONObject) responseObj;
                try {
                    final JSONArray resultArray = obj.getJSONArray("result");
                    if (resultArray.length() > 0) {
                        for (int i = 0; i < resultArray.length(); i++) {
                            final JSONObject fileObj = resultArray.getJSONObject(i);
                            final String file_name = fileObj.getString("file_name");
                            final String cp_file_path = fileObj.getString("compress_path");
                            final String file_path = fileObj.getString("upload_path");
                            //整体信息添加
                            Log.i(TAG, "onSuccess: path " + file_path.toString() + " pathsize " + pathInfos.size() + "    " + cp_file_path + "   " + file_name);
                            //作用：拼接Url
                            final Map<String, String> params3 = new HashMap<String, String>();
                            params3.put("fileNames", file_name);
                            params3.put("fileId", cp_file_path);
                            Log.i(TAG, "onSuccess: cp_file_path     " + cp_file_path);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //把压缩图片下载到本地并传回地址
                                    String s = HttpUpLoad.downloadFile(file_name, params3, Constant.URL_DOWNLOAD_FILE, "/report_cp");
                                    if (!s.equals("") && s != null) {
//                                        dialog.dismissLoadingDlg();
                                        Log.i(TAG, "run:    " + s.toString());
                                        //s是处理后压缩文件在内存中的路径
                                        //file_path是原文件下载路径
                                        pathInfos.add(new PathInfo(type, file_path, s, file_name));
                                        //路径是内存路径
                                        if (pathInfos.size() == resultArray.length()) {   //下载完毕
                                            reportCount++;
                                            Log.i(TAG, "onSuccess:reportCount    " + reportCount);
                                            allPaths.add(pathInfos);
                                            if (reportCount == 6) {
                                                dialog.dismissLoadingDlg();
                                                goToReportDetail(view, allPaths);
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                    } else {
                        Log.i(TAG, "onSuccess:reportCount    " + reportCount);
                        reportCount++;
                        if (reportCount == 6) {
                            dialog.dismissLoadingDlg();
                            goToReportDetail(view, allPaths);
                        }
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

    //     1 未提交   默认    2 审核中 3 已通过  4 驳回  //针对业务员
    @BindingAdapter("set_img")
    public static void getImage(ImageView iv, String state) {
        switch (state) {
            case "1":
            case "2":
                iv.setImageResource(R.mipmap.order_sub_pre);
                break;
            case "3":
            case "4":
                iv.setImageResource(R.mipmap.order_sub);
                break;
        }
    }


    @BindingAdapter("set_review_img")
    public static void setReviewImage(ImageView iv, String examination_result) {
        switch (examination_result) {
            case "-1":
                iv.setImageResource(R.mipmap.order_sub_pre);
                break;
            case "1":
            case "2":
                iv.setImageResource(R.mipmap.order_sub);
                break;
        }
    }

    @BindingAdapter("set_img_modify")
    public static void getImageModify(ImageView iv, String state) {
        switch (state) {
            case "1":
            case "4":
                iv.setImageResource(R.mipmap.modify_pre);
                break;
            case "2":
            case "3":
                iv.setImageResource(R.mipmap.modify_grey);
                break;
        }
    }

    @BindingAdapter("set_img_delete")
    public static void getImageDelete(ImageView iv, String state) {
        switch (state) {
            case "1":
            case "4":
                iv.setImageResource(R.mipmap.delete_pre);
                break;
            case "2":
            case "3":
                iv.setImageResource(R.mipmap.delete);
                break;
        }
    }

    //1 未提交   默认    2 审核中 3 已通过（绿）  4 驳回（黄）  //针对业务员
    @BindingAdapter("set_text_color")
    public static void setColor(TextView tv, String state) {
        switch (state) {
            case "1":
                tv.setTextColor(Color.RED);
                break;
            case "2":
                tv.setTextColor(Color.BLUE);
                break;
            case "3":
                tv.setTextColor(0XFF018000);
                break;
            case "4":
                tv.setTextColor(0XFFFF6500);
                break;
        }
    }

    //-1：待审核    1：通过    2：驳回

    @BindingAdapter("set_review_text_color")
    public static void setReviewTableColor(TextView tv, String examination_result) {
        switch (examination_result) {
            case "-1":
                tv.setTextColor(Color.BLUE);
                break;
            case "1":
                tv.setTextColor(0XFF018000);            //通过
                break;
            case "2":
                tv.setTextColor(0XFFFF6500);            //驳回
                break;
        }
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Bindable
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Bindable
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Bindable
    public String getChange_letter_number() {
        return change_letter_number;
    }

    public void setChange_letter_number(String change_letter_number) {
        this.change_letter_number = change_letter_number;
    }

    @Bindable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @Bindable
    public String getExamination_result() {
        return examination_result;
    }

    public void setExamination_result(String examination_result) {
        this.examination_result = examination_result;
    }

    public int getFlow_details_id() {
        return flow_details_id;
    }

    public void setFlow_details_id(int flow_details_id) {
        this.flow_details_id = flow_details_id;
    }

    @Bindable
    public int getReport_state() {
        return report_state;
    }

    public void setReport_state(int report_state) {
        this.report_state = report_state;
    }

    @Bindable
    public String getCar_sign() {
        return car_sign;
    }

    public void setCar_sign(String car_sign) {
        this.car_sign = car_sign;
    }

    @Bindable
    public String getDaily_code() {
        return daily_code;
    }

    public void setDaily_code(String daily_code) {
        this.daily_code = daily_code;
    }
}
