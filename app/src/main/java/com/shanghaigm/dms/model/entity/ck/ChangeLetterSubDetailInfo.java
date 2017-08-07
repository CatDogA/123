package com.shanghaigm.dms.model.entity.ck;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/19.
 * 更改函提报明细对象类
 */

public class ChangeLetterSubDetailInfo extends BaseObservable implements Serializable {
    private String contract_id;  //记得改过来。。。
    private String models_name;
    private String company_name;
    private String number;
    private String contract_price;
    private String change_contract_price;
    private String config_change_date;
    private String config_chang_delivery_date;
    private String contract_delivery_date;

    public ChangeLetterSubDetailInfo() {
    }

    public ChangeLetterSubDetailInfo(JSONObject object) {
        try {
            this.contract_id = object.getString("contract_id");  //记得改过来。。。
            this.models_name = object.getString("models_name");
            this.company_name = object.getString("company_name");
            this.number = object.getString("number");
            this.contract_price = object.getString("contract_price");
            this.change_contract_price = object.getString("contract_price");
            this.config_change_date = object.getString("config_change_date").split(" ")[0];
            this.config_chang_delivery_date = object.getString("config_chang_delivery_date").split(" ")[0];
            this.contract_delivery_date = object.getString("contract_delivery_date").split(" ")[0];
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ChangeLetterSubDetailInfo(ChangeLetterSubDetailBean.ResultEntity.OrderInfo info) {
        this.contract_id = info.contract_id;  //记得改过来。。。
        this.models_name = info.models_name;

        this.company_name = info.company_name;
        this.number = info.number;
        this.contract_price = info.contract_price;
        this.change_contract_price = info.contract_price;
        this.config_change_date = info.config_change_date.split(" ")[0];
        this.config_chang_delivery_date = info.config_chang_delivery_date.split(" ")[0];
        this.contract_delivery_date = info.contract_delivery_date.split(" ")[0];
    }

    @Bindable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
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
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Bindable
    public String getContract_price() {
        return contract_price;
    }

    public void setContract_price(String contract_price) {
        this.contract_price = contract_price;
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
    public String getConfig_chang_delivery_date() {
        return config_chang_delivery_date;
    }

    public void setConfig_chang_delivery_date(String config_chang_delivery_date) {
        this.config_chang_delivery_date = config_chang_delivery_date;
    }

    @Bindable
    public String getContract_delivery_date() {
        return contract_delivery_date;
    }

    public void setContract_delivery_date(String contract_delivery_date) {
        this.contract_delivery_date = contract_delivery_date;
    }
}
