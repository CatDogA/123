package com.shanghaigm.dms.model.entity.as;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ReportQueryDetailInfo extends BaseObservable implements Serializable {
    public String feedback_date;
    public String models_name;
    public String vehicle_series;
    public String car_no;
    public String car_sign;
    public String chassis_num;

    public String factory_date;
    public String license_date;
    public String mileage_num;
    public String fault_describe;
    public String treatment_process;
    public String treatment_result;
    public String responsibility_classification;
    public String rc_value;
    public String first_fault_name;
    public String fault_makers;

    public ReportQueryDetailInfo() {
    }
    public ReportQueryDetailInfo(ReportQueryDetailInfoBean bean) {
        this.feedback_date = bean.feedback_date.split(" ")[0];
        this.models_name = bean.models_name;
        this.vehicle_series = bean.vehicle_series + "";
        this.car_no = bean.car_no;
        this.car_sign = bean.car_sign;
        this.chassis_num = bean.chassis_num;

        this.factory_date = bean.factory_date.split(" ")[0];
        this.license_date = bean.license_date.split(" ")[0];
        this.mileage_num = bean.mileage_num + "";
        this.fault_describe = bean.fault_describe;
        this.treatment_process = bean.treatment_process;
        this.treatment_result = bean.treatment_result;
        this.responsibility_classification = bean.responsibility_classification + "";
        this.rc_value = bean.rc_value;
        this.first_fault_name = bean.first_fault_name;
        this.fault_makers = bean.fault_makers;
    }
}
