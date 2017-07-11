package com.shanghaigm.dms.model.entity.as;

import com.shanghaigm.dms.model.entity.mm.OrderQueryInfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tom on 2017/7/11.
 * 日报查询
 */

public class ReportQueryInfoBean implements Serializable{
    public ResultEntity resultEntity;
    public class ResultEntity {
        public int total;
        public List<ReportInfo> rows;
        public class ReportInfo {
            public String daily_code;
            public String models_name;
            public String car_sign;
            public int state;
            public int daily_id;
        }
    }
}
