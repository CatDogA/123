package com.shanghaigm.dms.model.entity.as;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/7/11.
 */

public class ReportQueryDetailInfoBean extends BaseObservable implements Serializable {
    public String feedback_date;
    public String models_name;
    public int vehicle_series;
    public String car_no;
    public String car_sign;
    public String chassis_num;
    public int daily_id;
    public String factory_date;
    public String license_date;
    public Double mileage_num;
    public String fault_describe;
    public String treatment_process;
    public String treatment_result;
    public int responsibility_classification;
    public String rc_value;
    public String first_fault_name;
    public String fault_makers;
}
