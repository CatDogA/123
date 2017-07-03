package com.shanghaigm.dms.model.entity.mm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProvincialCitysInfo implements Serializable {
    public List<ProvinceInfo> resultEntity;
    public class ProvinceInfo{
        public int id;
        public String name;
        public int pid;
    }

}
