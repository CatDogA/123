package com.shanghaigm.dms.model.entity.mm;

import java.util.List;

/**
 * Created by Tom on 2017/6/15.
 */

public class ChangeBillInfoBean {
    public String message;
    public String resultCode;
    public ResultEntity resultEntity;

    public class ResultEntity {
        public int total;
        public List<OrderInfo> rows;

        public class OrderInfo {
            public String models_name;
            public String order_number;
            public String state;
            public int order_id;
            public String examination_result;
            public String examination_resul;

            public String customer_name;//客户
            public String org_name;
            public String user_name;  //业务员
            public String sex;
            public String address;    //"320000,320300,320324"  地址？
            public String vehicle_state;   //车辆状态
            public String vehicle_number;
            public String number;
            public String contract_id;
            public String config_change_date;
            public String config_chang_delivery_date;
            public String deliver_place;
            public String delivery_date;
            public String change_content;     //更改如下

        }
    }
}
