package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/14.
 * 更改函信息
 */

public class ChangeLetterDetailInfo extends BaseObservable implements Serializable {
    //更改函审核明细
    private String contract_id;
    private String contract_price;
    private String number;
    private String models_name;
    private String company_name;
    private String change_contract_price;
    private String config_change_date;          //配置更改日期
    private String contract_delivery_date;
    private String config_chang_delivery_date;
    private int letter_id;
    public ChangeLetterDetailInfo() {
    }

    public ChangeLetterDetailInfo(String contract_id, String contract_price, String number, String models_name, String company_name, String change_contract_price, String config_change_date, String contract_delivery_date, String config_chang_delivery_date,int letter_id) {
        this.contract_id = contract_id;
        this.contract_price = contract_price;
        this.number = number;
        this.models_name = models_name;
        this.company_name = company_name;
        this.change_contract_price = change_contract_price;
        this.config_change_date = config_change_date.split(" ")[0];
        this.contract_delivery_date = contract_delivery_date.split(" ")[0];
        this.config_chang_delivery_date = config_chang_delivery_date.split(" ")[0];
        this.letter_id = letter_id;
    }

    @Bindable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }
    @Bindable
    public String getContract_price() {
        return contract_price;
    }

    public void setContract_price(String contract_price) {
        this.contract_price = contract_price;
    }
    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @Bindable
    public String getModels_name() {
        return models_name;
    }

    public void setModels_name(String models_name) {
        this.models_name = models_name;
    }
    @Bindable
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    @Bindable
    public String getChange_contract_price() {
        return change_contract_price;
    }

    public void setChange_contract_price(String change_contract_price) {
        this.change_contract_price = change_contract_price;
    }
    @Bindable
    public String getConfig_change_date() {
        return config_change_date;
    }

    public void setConfig_change_date(String config_change_date) {
        this.config_change_date = config_change_date;
    }
    @Bindable
    public String getContract_delivery_date() {
        return contract_delivery_date;
    }

    public void setContract_delivery_date(String contract_delivery_date) {
        this.contract_delivery_date = contract_delivery_date;
    }
    @Bindable
    public String getConfig_chang_delivery_date() {
        return config_chang_delivery_date;
    }

    public void setConfig_chang_delivery_date(String config_chang_delivery_date) {
        this.config_chang_delivery_date = config_chang_delivery_date;
    }
}
