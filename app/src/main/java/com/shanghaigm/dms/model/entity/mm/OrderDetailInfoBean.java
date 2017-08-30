package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tom on 2017/6/8.
 */

public class OrderDetailInfoBean implements Serializable {
    public ResultEntity resultEntity;
    public class ResultEntity extends BaseObservable implements Serializable{
        //订单审核明细
        public String customer_name;
        public int customer_code;
        public String sex;
        public String fixed_telephone;
        public String province;
        public String city;
        public String county;
        public String detailed_address;
        public String terminal_customer_name;
        public String terminal_customer_tel;
        public String terminal_customer_address;
        public String models_name;
        public int models_Id;
        public String number;
        public String battery_system;
        public String battery_number;
        public String endurance_mileage;
        public String battery_manufacturer;
        public String color_determine;
        public String ekg;
        public String licensing_addeess;
        public String payment_method;
        public String payment_method_remarks;
        public String delivery_time;
        public String freight;
        public String service_fee;
        public String contract_price;
        public String carriage;
        public String invoice_amount;
        public String billing_requirements;
        public int order_id;
        public String order_number;
        public String address;
        public String mobile_phone;
        public String addressName;
//        public List<MatchingBean> matching;
//        public class MatchingBean{
//            public String assembly;
//            public String entry_name;
//            public String config_information;
//            public int num;
//            public String remarks;
//            public int isdefault;
//        }
//        public List<FlowBean> flowdetails;
//        public class FlowBean{
//
//            public int flow_details_id;
//        }

        //更改函审核明细
        public String contract_id;
        //车型,数量,原合同价已有
        public String company_name;
        public String change_contract_price;
        public String config_change_date;
        public String contract_delivery_date;
        public String config_chang_delivery_date;
    }
}
