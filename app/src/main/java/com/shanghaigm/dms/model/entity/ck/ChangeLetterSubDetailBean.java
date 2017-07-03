package com.shanghaigm.dms.model.entity.ck;

import com.shanghaigm.dms.model.entity.mm.ChangeBillInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ChangeLetterSubDetailBean {
    public ResultEntity resultEntity;

    public class ResultEntity {
        public int total;
        public List<OrderInfo> rows;

        public class OrderInfo {
            public int order_id;
            public String change_letter_number;
            public String customer_name;
            public String order_number;
            public String state;
            public String contract_id;  //记得改过来。。。
            public String company_name;
            public String models_name;
            public String number;
            public String contract_price;
            public String change_contract_price;
            public String config_change_date;
            public String config_chang_delivery_date;
            public String contract_delivery_date;
        }
    }
}