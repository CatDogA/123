package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/1.
 * 选车型
 */

public class PopListInfo extends BaseObservable implements Serializable {
    private String str;
    public PopListInfo(String str){
        this.setStr(str);
    }

    @Bindable
    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
