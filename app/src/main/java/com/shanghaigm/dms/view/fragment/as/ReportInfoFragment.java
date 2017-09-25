package com.shanghaigm.dms.view.fragment.as;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chumi.widget.dialog.LoadingDialog;
import com.chumi.widget.http.listener.DisposeDataListener;
import com.shanghaigm.dms.DmsApplication;
import com.shanghaigm.dms.R;
import com.shanghaigm.dms.model.Constant;
import com.shanghaigm.dms.model.entity.as.ModelInfo;
import com.shanghaigm.dms.model.entity.mm.PopListInfo;
import com.shanghaigm.dms.model.util.OkhttpRequestCenter;
import com.shanghaigm.dms.view.activity.as.ReportAddActivity;
import com.shanghaigm.dms.view.fragment.BaseFragment;
import com.shanghaigm.dms.view.widget.MmPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 废弃
 */
public class ReportInfoFragment extends BaseFragment {
    private static ReportInfoFragment reportInfoFragment;
    private static String TAG = "ReportInfoFragment";
    //    private Button btn_save, btn_sub;
    private EditText edt_feed_back, edt_model, edt_vehicle, edt_car_no, edt_car_sign, edt_chassis_num,
            edt_factory_date, edt_license_date, edt_mile, edt_fault_describe, edt_treatment_process,
            edt_treatment_result, edt_rc, edt_first_fault_name, edt_fault_makers;
    private DmsApplication app;
    private ImageView img_model, img_factory_id, img_model_series, img_duty_type;
    private LoadingDialog dialog;
    private ArrayList<ModelInfo> models;
    private Boolean isInfoAdd = false;
    private String[] infos;
    private int file_id;
    private JSONObject subObj;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_info, container, false);
        initView(v);
        setUpView();
        return v;
    }

    private void setUpView() {
        edt_feed_back.setText(getDate());
        edt_factory_date.setText(getDate());
        edt_license_date.setText(getDate());
        pickDate(edt_factory_date);
        pickDate(edt_license_date);
        img_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_MODELS, null, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        dialog.dismissLoadingDlg();
                        JSONObject object = (JSONObject) responseObj;
                        try {
                            JSONArray array_model = object.getJSONArray("resultEntity");
                            ArrayList<PopListInfo> list_model_pop = new ArrayList<PopListInfo>();
                            models = new ArrayList<ModelInfo>();
                            for (int i = 0; i < array_model.length(); i++) {
                                String id = array_model.getJSONObject(i).getString("models_Id");
                                String model_name = array_model.getJSONObject(i).getString("models_name");
                                list_model_pop.add(new PopListInfo(model_name));
                                models.add(new ModelInfo(id, model_name));
                            }
                            MmPopupWindow pop_model = new MmPopupWindow(getActivity(), edt_model, list_model_pop, 4);
                            pop_model.showPopup(edt_model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            }
        });
        img_factory_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String model = edt_model.getText().toString();
                String model_id = "";
                if (model.equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.choose_model), Toast.LENGTH_SHORT).show();
                } else {
                    for (ModelInfo info : models) {
                        if (info.model_name.equals(model)) {
                            model_id = info.model_Id;
                        }
                    }
                    Map<String, Object> params = new HashMap<>();
                    params.put("models_Id", model_id);
                    Log.i(TAG, "onClick: model_id   " + model_id);
                    OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_CAR_NO, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            JSONObject object = (JSONObject) responseObj;
                            ArrayList<PopListInfo> list_car_no = new ArrayList<PopListInfo>();
                            try {
                                JSONArray array_car_no = object.getJSONArray("resultEntity");
                                for (int i = 0; i < array_car_no.length(); i++) {
                                    list_car_no.add(new PopListInfo(array_car_no.get(i).toString()));
                                }
                                MmPopupWindow pop_car_no = new MmPopupWindow(getActivity(), edt_car_no, list_car_no, 4);
                                pop_car_no.showPopup(edt_car_no);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
            }
        });
        edt_car_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.choose_car_no), Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> params = new HashMap<>();
                    params.put("car_no", s);
                    OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_FOUR_CAR_INFO, params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.i(TAG, "onSuccess: " + responseObj.toString());
                            JSONObject object = (JSONObject) responseObj;
                            try {
                                JSONObject result = object.getJSONObject("resultEntity");
                                edt_car_sign.setText(result.getString("car_sign"));
                                edt_chassis_num.setText(result.getString("chassis_num"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_model_series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> list_series = new ArrayList<PopListInfo>();
                list_series.add(new PopListInfo(getResources().getText(R.string.city_car).toString()));
                list_series.add(new PopListInfo(getResources().getText(R.string.between_city_car).toString()));
                MmPopupWindow pop_series = new MmPopupWindow(getActivity(), edt_vehicle, list_series, 5);
                pop_series.showPopup(edt_vehicle);
            }
        });

        img_duty_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PopListInfo> list_duty = new ArrayList<PopListInfo>();
                String[] dutys = {"生产", "配套", "设计", "商务", "其他"};
                for (int i = 0; i < dutys.length; i++) {
                    list_duty.add(new PopListInfo(dutys[i]));
                }
                MmPopupWindow pop_dutys = new MmPopupWindow(getActivity(), edt_rc, list_duty, 4);
                pop_dutys.showPopup(edt_rc);
            }
        });
    }

    //提交完后即跳转刷新
    public void subInfo() {
        if (isInfoAdd) {
            try {
                Map<String, Object> params = new HashMap<>();
                JSONArray array = new JSONArray();
                subObj.put("daily_id", file_id);
                subObj.put("state", 2);
                array.put(subObj);
                params.put("dailyStr", array.toString());
                params.put("loginName", app.getAccount());
                dialog.showLoadingDlg();
                OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_SUB_REPORT, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.i(TAG, "onSuccess: " + responseObj.toString());
                        dialog.dismissLoadingDlg();
                        JSONObject resultObj = (JSONObject) responseObj;
                        try {
                            JSONObject resultEntity = resultObj.getJSONObject("resultEntity");
                            String s = resultEntity.getString("returnCode");
                            if (s.equals("1")) {
                                Toast.makeText(getActivity(), getResources().getText(R.string.sub_info_success), Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                ReportSubFragment fragment = ReportSubFragment.getInstance();
                                fragment.refreshTable();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.save_info), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveBaseInfo() {
        String feed_back = edt_feed_back.getText().toString();
        String model = edt_model.getText().toString();
        String vehicle = edt_vehicle.getText().toString();
        String car_no = edt_car_no.getText().toString();
        String car_sign = edt_car_sign.getText().toString();
        String chassis_num = edt_chassis_num.getText().toString();
        String factory_date = edt_factory_date.getText().toString();
        String license_date = edt_license_date.getText().toString();
        String mile = edt_mile.getText().toString();
        String fault_describe = edt_fault_describe.getText().toString();
        String treatment_process = edt_treatment_process.getText().toString();
        String treatment_result = edt_treatment_result.getText().toString();
        String rc = edt_rc.getText().toString();
        String first_fault_name = edt_first_fault_name.getText().toString();
        String fault_makers = edt_fault_makers.getText().toString();
        infos = new String[]{feed_back, model, vehicle, car_no, chassis_num, factory_date, mile, fault_describe, treatment_process, treatment_result, rc, first_fault_name};
        Boolean compareDate = compareDate(edt_license_date, edt_factory_date);
        Boolean compareDate2 = compareDate(edt_feed_back, edt_license_date);
        Boolean flag = true;
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].equals("")) {
                flag = false;
            }
        }
        if (!compareDate2) {
            Toast.makeText(getActivity(), "反馈日期应在上牌日期之后", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!compareDate) {
            Toast.makeText(getActivity(), "生产日期应在上牌日期之前", Toast.LENGTH_SHORT).show();
            return;
        }

        if (flag) {
            int model_id = 0;
            if (models != null) {
                for (ModelInfo modelInfo : models) {
                    if (modelInfo.model_name.equals(model)) {
                        model_id = Integer.parseInt(modelInfo.model_Id);
                    }
                }
            }
            int vehicle_id = 0;
            if (!vehicle.equals("")) {
                if (vehicle.equals(getResources().getString(R.string.city_car))) {
                    vehicle_id = 1;
                } else {
                    vehicle_id = 2;
                }
            }
            Double miles = Double.parseDouble(mile);

            //                1生产、2配套、3设计、4商务、5其他
            int rc_type = 0;
            switch (rc) {
                case "生产":
                    rc_type = 1;
                    break;
                case "配套":
                    rc_type = 2;
                    break;
                case "设计":
                    rc_type = 3;
                    break;
                case "商务":
                    rc_type = 4;
                    break;
                case "其他":
                    rc_type = 5;
                    break;
            }
            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray();
            Map<String, Object> map = new HashMap<>();
            try {
                params.put("feedback_date", feed_back);
                params.put("models_Id", model_id);
                params.put("vehicle_series", vehicle_id);
                params.put("car_no", car_no);
                params.put("car_sign", car_sign);
                params.put("chassis_num", chassis_num);
                params.put("factory_date", factory_date);
                params.put("license_date", license_date);
                params.put("mileage_num", miles);
                params.put("fault_describe", fault_describe);
                params.put("treatment_process", treatment_process);
                params.put("treatment_result", treatment_result);
                params.put("responsibility_classification", rc_type);
                params.put("rc_value", rc);           //?从何处获取
                params.put("first_fault_name", first_fault_name);
                params.put("fault_makers", fault_makers);
                subObj = params;
                array.put(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("dailyStr", array.toString());
            map.put("jobCode", app.getJobCode());
            map.put("loginName", app.getAccount());
            dialog.showLoadingDlg();
            OkhttpRequestCenter.getCommonRequest(Constant.URL_GET_ADD_REPORT, map, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.i(TAG, "onSuccess: " + responseObj.toString());
                    dialog.dismissLoadingDlg();
                    JSONObject resultObj = (JSONObject) responseObj;
                    try {
                        JSONObject resultEntity = resultObj.getJSONObject("resultEntity");
                        String code = resultEntity.getString("returnCode");
                        file_id = resultEntity.getInt("result");
                        if (code.equals("1")) {
                            isInfoAdd = true;
                            ((ReportAddActivity) getActivity()).setInfoAdd(true);
                            ((ReportAddActivity) getActivity()).setReport_id(file_id);
                            //判断第二页是否出现
                            if (ReportAddActivity.isAtttachShow) {
                                ReportAttachSubFragment.getInstance().saveAttachInfo();
                            } else {
                                Toast.makeText(getActivity(), getResources().getText(R.string.save_info_success), Toast.LENGTH_SHORT).show();
                                ((ReportAddActivity) getActivity()).setButton();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), getResources().getText(R.string.sub_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Object reasonObj) {
                    dialog.dismissLoadingDlg();
                    Toast.makeText(getActivity(), getResources().getText(R.string.check_info), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.full_filled), Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean compareDate(EditText edt1, EditText edt2) {
        if (!edt1.getText().toString().equals("") && !edt2.getText().toString().equals("")) {
            int y1 = Integer.parseInt(edt1.getText().toString().split("-")[0]);
            int m1 = Integer.parseInt(edt1.getText().toString().split("-")[1]);
            int d1 = Integer.parseInt(edt1.getText().toString().split("-")[2]);

            int y2 = Integer.parseInt(edt2.getText().toString().split("-")[0]);
            int m2 = Integer.parseInt(edt2.getText().toString().split("-")[1]);
            int d2 = Integer.parseInt(edt2.getText().toString().split("-")[2]);
            if (y1 < y2) {
                return false;
            } else if (m1 < m2) {
                return false;
            } else if (d1 <= d2) {
                return false;
            }
        }
        return true;
    }

    private void initView(View v) {
        edt_feed_back = (EditText) v.findViewById(R.id.edt_feed_back);
        edt_model = (EditText) v.findViewById(R.id.edt_model);
        edt_vehicle = (EditText) v.findViewById(R.id.edt_model_series);
        edt_car_no = (EditText) v.findViewById(R.id.edt_factory_id);
        edt_car_sign = (EditText) v.findViewById(R.id.edt_car_sign);
        edt_chassis_num = (EditText) v.findViewById(R.id.edt_vin);
        edt_factory_date = (EditText) v.findViewById(R.id.edt_factory_date);
        edt_license_date = (EditText) v.findViewById(R.id.edt_license_date);
        edt_mile = (EditText) v.findViewById(R.id.edt_mile);
        edt_fault_describe = (EditText) v.findViewById(R.id.edt_fault_describe);
        edt_treatment_process = (EditText) v.findViewById(R.id.edt_treatment_process);
        edt_treatment_result = (EditText) v.findViewById(R.id.edt_treatment_result);
        edt_rc = (EditText) v.findViewById(R.id.edt_duty_type);
        edt_first_fault_name = (EditText) v.findViewById(R.id.edt_first_trouble_thing_name);
        edt_fault_makers = (EditText) v.findViewById(R.id.edt_trouble_thing_factory);

//        btn_save = (Button) v.findViewById(R.id.btn_save);
//        btn_sub = (Button) v.findViewById(R.id.btn_sub);

        app = DmsApplication.getInstance();
        img_model = (ImageView) v.findViewById(R.id.img_model);
        img_factory_id = (ImageView) v.findViewById(R.id.img_factory_id);
        img_model_series = (ImageView) v.findViewById(R.id.img_model_series);
        img_duty_type = (ImageView) v.findViewById(R.id.img_duty_type);
        dialog = new LoadingDialog(getActivity(), "正在加载");
    }

    public static ReportInfoFragment getInstance() {
        if (reportInfoFragment == null) {
            reportInfoFragment = new ReportInfoFragment();
        }
        return reportInfoFragment;
    }

    //点击"日期"按钮布局 设置日期
    private void pickDate(final EditText edt) {
        final int[] mYear = {0};
        final int[] mMonth = {0};
        final int[] mDay = {0};
        final Calendar calendar = Calendar.getInstance();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mYear[0] = year;
                        mMonth[0] = month;
                        mDay[0] = day;
                        edt.setText(new StringBuilder().append(mYear[0]).append("-")
                                .append((mMonth[0] + 1) < 10 ? "0" + (mMonth[0] + 1) : (mMonth[0] + 1))
                                .append("-")
                                .append((mDay[0] < 10) ? "0" + mDay[0] : mDay[0]));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        return date;
    }
}
