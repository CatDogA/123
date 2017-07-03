package com.shanghaigm.dms.model.entity.ck;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/22.
 */

public class AllocationAddChooseUndefaultInfo extends BaseObservable implements Serializable {
    private String assembly;
    private String config_information;
    private Double price;
    private int num;
    private String remarks;
    private int standard_id;  //唯一性
    public AllocationAddChooseUndefaultInfo() {
    }

    public AllocationAddChooseUndefaultInfo(String config_information) {
        this.config_information = config_information;
    }

    public AllocationAddChooseUndefaultInfo(String assembly, String config_information, Double price, int num, String remarks,int standard_id) {
        this.config_information = config_information;
        this.price = price;
        this.num = num;
        this.remarks = remarks;
        this.assembly = assembly;
        this.standard_id = standard_id;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Bindable
    public String getConfig_information() {
        return config_information;
    }

    public void setConfig_information(String config_information) {
        this.config_information = config_information;
    }

    public int getStandard_id() {
        return standard_id;
    }

    public void setStandard_id(int standard_id) {
        this.standard_id = standard_id;
    }
}
