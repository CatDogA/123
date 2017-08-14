package com.shanghaigm.dms.model.entity.ck;

import com.shanghaigm.dms.model.entity.mm.OrderDetailInfoAllocation;

import java.util.ArrayList;

/**
 * Created by Tom on 2017/8/10.
 * 用于储存修改了的信息
 */

public class AllocationModifyInfo {
    public String entry_name;
    public ArrayList<OrderDetailInfoAllocation> infos;
    public AllocationModifyInfo(String entry_name, ArrayList<OrderDetailInfoAllocation> infos) {
        this.entry_name = entry_name;
        this.infos = infos;
    }
}
