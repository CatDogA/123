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
import com.shanghaigm.dms.model.entity.ck.ChangeLetterAllocationInfo;
import com.shanghaigm.dms.model.entity.ck.ChangeLetterSubDetailInfo;
import com.shanghaigm.dms.model.util.GsonUtil;
import com.shanghaigm.dms.model.util.HttpUpLoad;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.HomeActivity;
import com.shanghaigm.dms.view.activity.as.ReportDetailActivity;
import com.shanghaigm.dms.view.activity.as.ReportUpdateActivity;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterAddActivity;
import com.shanghaigm.dms.view.activity.ck.ChangeLetterQueryDetailActivity;
import com.shanghaigm.dms.view.activity.ck.OrderAddActivity;
import com.shanghaigm.dms.view.activity.mm.ChangeBillDetailActivity;
import com.shanghaigm.dms.view.activity.mm.ChangeLetterDetailActivity;
import com.shanghaigm.dms.view.activity.mm.ContractReviewDetailActivity;
import com.shanghaigm.dms.view.activity.mm.OrderDetailActivity;
import com.shanghaigm.dms.view.fragment.ck.OrderSubFragment;
import com.shanghaigm.dms.view.widget.ShowPictureLayout;

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
    public static String CONTRACT_MATCHING_INFOS = "contract_matching_infos";
    public static String CHANGE_LETTER_INFO = "change_letter_info";
    public static String REPORT_DETAI_INFO = "report_detail_info";
    public static String REPORT_FILE_INFO = "report_file_info";
    public static String REPORT_DELETE = "report_delete";
    public static String ORDER_MODIFY = "oder_modify";
    private int flag;//判断进入的详情页面  1.订单审核，2.合同审核，3.更改函审核，4.更改单审核，5.订单提交，6.更改函提交
    private LoadingDialog dialog;
    private static String TAG = "PaperInfo";
    private static DmsApplication app = DmsApplication.getInstance();
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
    private int letter_id;

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
    public PaperInfo(String name, String number, String model, Boolean isModify, int orderId, Context context, int flag, ChangeLetterDetailInfo changeLetterDetailInfo, String examination_result, int flow_details_id, int letter_id) {
        super(name);
        this.number = number;
        this.model = model;
        this.orderId = orderId;
        this.context = context;
        this.flag = flag;
        this.flow_details_id = flow_details_id;
        this.examination_result = examination_result;
        this.changeLetterDetailInfo = changeLetterDetailInfo;
        this.letter_id = letter_id;
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
    public PaperInfo(String name, String state, String contract_id, String change_letter_number, Context context, int flag, ChangeLetterSubDetailInfo changeLetterSubDetailInfo, int letter_id,String model) {
        super(name);
        this.state = state;
        this.contract_id = contract_id;
        this.change_letter_number = change_letter_number;
        this.context = context;
        this.flag = flag;
        this.letter_id = letter_id;
        this.model = model;
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
                    CommonOkHttpClient.get(new CommonRequest().createGetRequestInt(Constant.URL_GET_ORDER_DETAIL_INFO, params), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            Log.i(TAG, "onSuccess:          " + responseObj.toString());
                            JSONObject object = (JSONObject) responseObj;
                            ArrayList<MatchingBean> matchings = new ArrayList<MatchingBean>();
                            try {
                                JSONObject resultEntity = object.getJSONObject("resultEntity");
                                Object matching = resultEntity.get("matching");
                                if (matching.toString().equals("")) {
                                    Log.i(TAG, "onSuccess: " + "没有配置数据");
                                } else {
                                    JSONArray matches = new JSONArray(matching.toString());
                                    Log.i(TAG, "onSuccess:matching       " + matching.toString());
                                    for (int i = 0; i < matches.length(); i++) {
                                        JSONObject match = matches.getJSONObject(i);
                                        int num = 0;
                                        if(!match.get("num").equals("")){
                                            num = Integer.parseInt(match.get("num").toString());
                                            String cost_change = "";
                                            if(match.getString("cost_change")!=null){
                                                cost_change = match.getString("cost_change");
                                            }
                                            matchings.add(new MatchingBean(match.getString("assembly"),match.getString("entry_name"),match.getString("config_information"),num,match.getString("remarks"),match.getInt("isdefault"),cost_change,match.getInt("isother")));
                                        }else {
                                            matchings.add(new MatchingBean(match.getString("assembly"),match.getString("entry_name"),match.getString("config_information"),match.getString("remarks"),match.getInt("isdefault"),match.getString("cost_change"),match.getInt("isother")));
                                        }
//                                        matchings.add(GsonUtil.GsonToBean(match.toString(), MatchingBean.class));
                                    }
                                    app.setMatchingBeanArrayList(matchings);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OrderDetailInfoBean orderDetailInfoBean = GsonUtil.GsonToBean(responseObj.toString(), OrderDetailInfoBean.class);
                            app.setOrderDetailInfoBean(orderDetailInfoBean);
                            Intent intent1 = new Intent(view.getContext(), OrderAddActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(ORDER_MODIFY, 1);
                            intent1.putExtras(b);
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
            Map<String, Object> params = new HashMap<>();
            params.put("letterId", letter_id);
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_CHANGE_LETTER_INFO, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.i(TAG, "onSuccess: responseObj      " + responseObj.toString());
                    JSONObject obj = (JSONObject) responseObj;
                    try {
                        JSONObject result = obj.getJSONObject("resultEntity");
                        JSONArray items = result.getJSONArray("items");
                        ArrayList<ChangeLetterAllocationInfo> infos = new ArrayList<ChangeLetterAllocationInfo>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            infos.add(new ChangeLetterAllocationInfo(item.getString("config_item"), item.getString("change_content"), item.getString("price_change"), item.getString("change_content")));
                        }
                        JSONObject infoObj = result.getJSONObject("fromData");
                        ChangeLetterSubDetailInfo info = new ChangeLetterSubDetailInfo(infoObj.getString("contract_id"), infoObj.getString("models_name"), infoObj.getString("company_name"), infoObj.getString("number"), infoObj.getString("contract_price"), infoObj.getString("change_contract_price"), infoObj.getString("config_change_date"), infoObj.getString("config_chang_delivery_date"), infoObj.getString("contract_delivery_date"), infoObj.getInt("letter_id"), infoObj.getInt("order_id"), infoObj.getString("change_letter_number"));
                        app.setChangeLetterSubDetailInfo(info);
                        Intent intent = new Intent(view.getContext(), ChangeLetterAddActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(CHANGE_LETTER_INFO, infos);
                        intent.putExtras(b);
                        view.getContext().startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
//            Map<String, Object> params = new HashMap<>();
//            app.setChangeLetterSubDetailInfo(changeLetterSubDetailInfo);
//            Intent intent = new Intent(view.getContext(), ChangeLetterModifyActivity.class);
//            view.getContext().startActivity(intent);
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

    //订单/更改函提交查询详情
    public void onCkQueryOrderDetailClick(final View view) {
        if (flag == 5) {
            dialog.showLoadingDlg();
            Map<String, Object> params = new HashMap<>();
            params.put("loginName", app.getAccount());
            params.put("jobCode", app.getJobCode());
            params.put("order_id", orderId);
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
                                int num = 0;
                                if(!match.get("num").equals("")){
                                    num = Integer.parseInt(match.get("num").toString());
                                    matchings.add(new MatchingBean(match.getString("assembly"),match.getString("entry_name"),match.getString("config_information"),num,match.getString("remarks"),match.getInt("isdefault"),match.getString("cost_change"),match.getInt("isother")));
                                }else {
                                    matchings.add(new MatchingBean(match.getString("assembly"),match.getString("entry_name"),match.getString("config_information"),match.getString("remarks"),match.getInt("isdefault"),match.getString("cost_change"),match.getInt("isother")));
                                }
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
            Map<String, Object> params = new HashMap<>();
            params.put("letterId", letter_id);
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_CHANGE_LETTER_INFO, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.i(TAG, "onSuccess: responseObj      " + responseObj.toString());
                    JSONObject obj = (JSONObject) responseObj;
                    try {
                        JSONObject result = obj.getJSONObject("resultEntity");
                        JSONArray items = result.getJSONArray("items");
                        ArrayList<ChangeLetterAllocationInfo> infos = new ArrayList<ChangeLetterAllocationInfo>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            infos.add(new ChangeLetterAllocationInfo(item.getString("config_item"), item.getString("change_content"), item.getString("price_change"), item.getString("change_content")));
                        }
                        JSONObject infoObj = result.getJSONObject("fromData");
                        ChangeLetterSubDetailInfo info = new ChangeLetterSubDetailInfo(infoObj.getString("contract_id"), infoObj.getString("models_name"), infoObj.getString("company_name"), infoObj.getString("number"), infoObj.getString("contract_price"), infoObj.getString("change_contract_price"), infoObj.getString("config_change_date"), infoObj.getString("config_chang_delivery_date"), infoObj.getString("contract_delivery_date"), infoObj.getInt("letter_id"), infoObj.getInt("order_id"), infoObj.getString("change_letter_number"));
                        app.setChangeLetterSubDetailInfo(info);
                        Intent intent = new Intent(view.getContext(), ChangeLetterQueryDetailActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(CHANGE_LETTER_INFO, infos);
                        intent.putExtras(b);
                        view.getContext().startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        }
        //日报修改详情
        if (flag == 8) {
            if (report_state != 2) {
                allPaths.clear();
                //先清空
                if (ShowPictureLayout.pathsDelete != null) {
                    ShowPictureLayout.pathsDelete.clear();
                }
                //把要删除的文件清空
                reportCount = 0;
                final Map<String, Object> params = new HashMap<>();
                params.put("daily_id", daily_id);
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_REPORT_DETAIL, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            reportCount++;
                            JSONObject resultEntity = object.getJSONObject("resultEntity");
                            reportQueryDetailInfoBean = GsonUtil.GsonToBean(resultEntity.toString(), ReportQueryDetailInfoBean.class);
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
            } else {
                Toast.makeText(context, context.getResources().getText(R.string.no_click), Toast.LENGTH_SHORT).show();
            }
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
                    dialog.showLoadingDlg();
                    //获取选配信息
                    Map<String, Object> params1 = new HashMap<>();
                    params1.put("loginName", app.getAccount());
                    params1.put("jobCode", app.getJobCode());
                    params1.put("order_id", orderId);
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_ORDER_DETAIL_INFO, params1, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            dialog.dismissLoadingDlg();
                            JSONObject object = (JSONObject) responseObj;
                            ArrayList<MatchingBean> matchings = new ArrayList<MatchingBean>();
                            try {
                                JSONObject resultEntity = object.getJSONObject("resultEntity");
                                Object matching = resultEntity.get("matching");
                                if (matching.toString().equals("")) {
                                } else {
                                    JSONArray matches = new JSONArray(matching.toString());
                                    for (int i = 0; i < matches.length(); i++) {
                                        JSONObject match = matches.getJSONObject(i);
                                        matchings.add(GsonUtil.GsonToBean(match.toString(), MatchingBean.class));
                                    }
                                }
                                app.setContractDetailInfo(contractDetailInfo);
                                app.setFlow_detail_id(flow_details_id);
                                app.setContract_id(contractDetailInfo.getContract_id());
                                Intent intent2 = new Intent(view.getContext(), ContractReviewDetailActivity.class);
                                Bundle b = new Bundle();
                                b.putSerializable(CONTRACT_MATCHING_INFOS, matchings);
                                intent2.putExtras(b);
                                view.getContext().startActivity(intent2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
                break;
            case 3:
                if (examination_result.equals("-1")) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("letterId", letter_id);
                    Log.i(TAG, "onImageClick:letter_id               " + letter_id);
                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_CHANGE_LETTER_INFO, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: responseObj      " + responseObj.toString());
                            JSONObject obj = (JSONObject) responseObj;
                            ArrayList<ChangeLetterAllocationInfo> infos = new ArrayList<ChangeLetterAllocationInfo>();
                            try {
                                JSONObject result = obj.getJSONObject("resultEntity");
                                if (!result.toString().equals("{}")) {
                                    JSONArray items = result.getJSONArray("items");
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject item = items.getJSONObject(i);
                                        infos.add(new ChangeLetterAllocationInfo(item.getString("config_item"), item.getString("change_content"), item.getString("price_change"), item.getString("change_content")));
                                    }
                                    JSONObject infoObj = result.getJSONObject("fromData");
                                    ChangeLetterDetailInfo info = new ChangeLetterDetailInfo(infoObj.getString("contract_id"), infoObj.getString("contract_price"), infoObj.getString("number"), infoObj.getString("models_name"), infoObj.getString("company_name"), infoObj.getString("change_contract_price"), infoObj.getString("config_change_date"), infoObj.getString("config_chang_delivery_date"), infoObj.getString("contract_delivery_date"), infoObj.getInt("letter_id"));
                                    app.setChangeLetterDetailInfo(info);
                                    app.setFlow_detail_id(flow_details_id);
                                    Intent intent3 = new Intent(view.getContext(), ChangeLetterDetailActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable(CHANGE_LETTER_INFO, infos);
                                    intent3.putExtras(b);
                                    view.getContext().startActivity(intent3);
                                }else {
                                    app.setChangeLetterDetailInfo(changeLetterDetailInfo);
                                    app.setFlow_detail_id(flow_details_id);
                                    Intent intent3 = new Intent(view.getContext(), ChangeLetterDetailActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable(CHANGE_LETTER_INFO, infos);
                                    intent3.putExtras(b);
                                    view.getContext().startActivity(intent3);
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
                //已提交
                if ((report_state == 1 || report_state == 3) && app.getRoleCode().equals("fwjl")) {
                    Toast.makeText(context, context.getResources().getText(R.string.no_click), Toast.LENGTH_SHORT).show();
                } else {
                    allPaths.clear();      //把要展示的文件清空
                    if (ShowPictureLayout.pathsDelete != null) {
                        ShowPictureLayout.pathsDelete.clear();   //把要删除的文件清空
                    }
                    reportCount = 0;
                    final Map<String, Object> params = new HashMap<>();
                    params.put("daily_id", daily_id);
                    dialog.showLoadingDlg();

                    OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_REPORT_DETAIL, params, new DisposeDataListener() {
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
                }
                break;
        }
    }

    public void OnDeleteOrderClick(final View v) {
        if (state.equals("1") || state.equals("4")) {
            Map<String, Object> params = new HashMap<>();
            params.put("order_id", orderId + "");
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_ORDER_DELETE, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    Log.i(TAG, "onSuccess: " + responseObj.toString());
                    OrderSubFragment fragment = OrderSubFragment.getInstance();
                    fragment.refresh();
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        }
    }

    /**
     * @param v
     */
    //删除更改函
    public void OnDeleteChangeLetterClick(final View v) {
        if (state.equals("1") || state.equals("4")) {
            Map<String, Object> params = new HashMap<>();
            Log.i(TAG, "OnDeleteChangeLetterClick: letter_id            " + letter_id);
            params.put("letterIds", letter_id + "");
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonReportRequest(Constant.URL_DELETE_CHANGE_LETTER, params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    dialog.dismissLoadingDlg();
                    Log.i(TAG, "onSuccess:       " + responseObj.toString());
//                    ChangeLetterSubFragment fragment = ChangeLetterSubFragment.getInstance();
//                    fragment.refresh();
                    Toast.makeText(context, context.getResources().getText(R.string.delete_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Object reasonObj) {
                    dialog.dismissLoadingDlg();
                }
            });
        }
    }

    //report删除
    public void OnDeleteClick(final View view) {
        if (report_state != 2) {
            Map<String, Object> params = new HashMap<>();
            params.put("daily_id", daily_id);
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        } else {
            Toast.makeText(context, context.getResources().getText(R.string.no_click), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ChangeLetterAllocationInfo> getChangeLetterAllocationInfo() {
        final ArrayList<ChangeLetterAllocationInfo>[] infos = new ArrayList[]{null};
        Map<String, Object> params = new HashMap<>();
        params.put("letterId", letter_id);
        dialog.showLoadingDlg();
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_CHANGE_LETTER_INFO, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dialog.dismissLoadingDlg();
                Log.i(TAG, "onSuccess: responseObj      " + responseObj.toString());
                JSONObject obj = (JSONObject) responseObj;
                infos[0] = new ArrayList<ChangeLetterAllocationInfo>();
                try {
                    JSONObject result = obj.getJSONObject("resultEntity");
                    if (!result.toString().equals("{}")) {
                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            infos[0].add(new ChangeLetterAllocationInfo(item.getString("config_item"), item.getString("change_content"), item.getString("price_change"), item.getString("change_content")));
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
        return infos[0];
    }

    private void goToReportDetail(View view, ArrayList<ArrayList<PathInfo>> allPaths) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PaperInfo.REPORT_DETAI_INFO, reportQueryDetailInfoBean);
        bundle.putSerializable(PaperInfo.REPORT_FILE_INFO, allPaths);
        if (flag == 7) {
            Intent intent7 = new Intent(view.getContext(), ReportDetailActivity.class);
            intent7.putExtras(bundle);
            view.getContext().startActivity(intent7);
        }
        if (flag == 8) {
            Intent intent8 = new Intent(view.getContext(), ReportUpdateActivity.class);
            intent8.putExtras(bundle);
            view.getContext().startActivity(intent8);
        }
    }

    private void getPaths(final int type, final View view) {
        Map<String, Object> params2 = new HashMap<>();
        final ArrayList<PathInfo> pathInfos = new ArrayList<>();
        params2.put("id", daily_id);
        params2.put("type", type + "");
//                params2.put("isById",daily_id);
        params2.put("loginRole", app.getJobCode());
        params2.put("loginName", app.getAccount());
        OkhttpRequestCenter.getCommonReportRequest(Constant.URL_GET_FILE_INFO, params2, new DisposeDataListener() {
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
                            final int id = fileObj.getInt("id");
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
                                        pathInfos.add(new PathInfo(type, file_path, s, file_name, id));
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

    //审核
    @BindingAdapter("set_report_img")
    public static void setReportImage(ImageView iv, int report_state) {
        if (app.getRoleCode().equals("out_service")) {
            iv.setImageResource(R.mipmap.order_review_pre);
        } else {
            switch (report_state) {
                case 2:
                    iv.setImageResource(R.mipmap.order_sub_pre);
                    break;
                case 1:
                case 3:
                    iv.setImageResource(R.mipmap.order_sub);
                    break;
            }
        }
    }

    //提交
    @BindingAdapter("set_report_sub_img")
    public static void setReportSubImage(ImageView iv, int report_state) {
        switch (report_state) {
            case 1:
            case 3:
                iv.setImageResource(R.mipmap.modify_pre);
                break;
            case 2:
                iv.setImageResource(R.mipmap.modify_grey);
                break;
        }
    }

    @BindingAdapter("set_report_sub_img_delete")
    public static void setReportSubDeleteImage(ImageView iv, int report_state) {
        switch (report_state) {
            case 1:
            case 3:
                iv.setImageResource(R.mipmap.delete_pre);
                break;
            case 2:
                iv.setImageResource(R.mipmap.delete);
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

    //未提交为红1,提交蓝，驳回黄
    @BindingAdapter("set_report_text_color")
    public static void setReportTableColor(TextView tv, int report_state) {
        switch (report_state) {
            case 1:
                tv.setTextColor(Color.RED);
                break;
            case 2:
                tv.setTextColor(Color.BLUE);
                break;
            case 3:
                tv.setTextColor(0XFFFF6500);
                break;
        }
    }

    @BindingAdapter("set_report_text")
    public static void setReportTableText(TextView tv, int report_state) {
        switch (report_state) {
            case 1:
                tv.setText("未提交");
                break;
            case 2:
                tv.setText("已提交");
                break;
            case 3:
                tv.setText("已驳回");
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
