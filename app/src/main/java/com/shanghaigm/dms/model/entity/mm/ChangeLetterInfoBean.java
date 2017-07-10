package com.shanghaigm.dms.model.entity.mm;

import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;

import java.util.List;

/**
 * Created by Tom on 2017/6/14.
 * 变更函数据
 */

public class ChangeLetterInfoBean {
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

            public String change_letter_number;
            public String contract_id;
            public String contract_price;
            public String number;
            public String company_name;
            public String change_contract_price;
            public String config_change_date;
            public String contract_delivery_date;
            public String config_chang_delivery_date;

            public String examination_result;
            public String examination_resul;
        }
    }
}
