package com.shanghaigm.dms.model.entity.mm;

import com.shanghaigm.dms.model.entity.as.ReportQueryInfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tom on 2017/6/1.
 */

public class OrderQueryInfoBean implements Serializable {
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
            public String examination_result;
        }
    }
}
