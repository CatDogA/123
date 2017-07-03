package com.shanghaigm.dms.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/5/15.
 */

public class BasePaperInfo extends BaseObservable implements Serializable{
    private String name;
    public BasePaperInfo(){}
    public BasePaperInfo(String name){
        this.name = name;
    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
