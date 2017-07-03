package com.shanghaigm.dms.model.entity.mm;

import java.util.List;

/**
 * Created by Tom on 2017/6/14.
 */

public class ContractInfoBean {
    public String message;
    public String resultCode;
    public ResultEntity resultEntity;

    public class ResultEntity {
        public int total;
        public List<OrderInfo> rows;

        public class OrderInfo {
            public String models_name;
            public String order_number;
            public String customer_name;
            public String state;
            public int order_id;

            //业务员？
            public String contract_id;
            public String company_name;
            public String creator_by;
            public String delivery_time;
            public String deposit;
            public String battery_manufacturer;
            public String battery_system;
            public String vehicle_number;
            public String number;
            public String delivery_mode;
            public String purpose;
            public String seat_number;

            public String examination_result;
            public String examination_resul;
        }
    }
}
