package com.shanghaigm.dms.model.entity.ck;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ChangeLetterAllocationInfo implements Serializable {
    public String config_item;      //配置项
    public String change_content;   //更改内容
    public Double price;        //价格
    public int man_hour;        //工时费
}
