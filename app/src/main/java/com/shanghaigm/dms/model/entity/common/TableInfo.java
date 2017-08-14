package com.shanghaigm.dms.model.entity.common;

import android.widget.LinearLayout;

/**
 * Created by Tom on 2017/8/11.
 * 如果已有，则直接显示，如果没有，则加载，添加，再显示
 */

public class TableInfo {
    public int page;  //对应页数
    public LinearLayout table;    //对应的table
    public Boolean isAdded;     //判断是否已经添加

    public TableInfo(int page, LinearLayout table, Boolean isAdded) {
        this.page = page;
        this.table = table;
        this.isAdded = isAdded;
    }
}
