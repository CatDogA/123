package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/15.
 * 更改单数据
 */

public class ChangeBillDetailInfo extends BaseObservable implements Serializable {
    private String customer_name;//客户
    private String org_name;
    private String user_name;  //业务员
    private String sex;
    private String address;    //"320000,320300,320324"  地址？
    private String vehicle_state;   //车辆状态
    private String vehicle_number;
    private String number;
    private String contract_id;
    private String config_change_date;
    private String config_chang_delivery_date;
    private String deliver_place;
    private String delivery_date;
    private String change_content;     //更改如下
    private String models_name;
    public ChangeBillDetailInfo(ChangeBillInfoBean.ResultEntity.OrderInfo info) {

        this.customer_name = info.customer_name;//客户
        this.org_name = info.org_name;
        this.user_name = info.user_name;  //业务员
        this.sex = info.sex;
        this.address = info.address;    //"320000,320300,320324"  地址？
        this.vehicle_state = info.vehicle_state;   //车辆状态
        this.vehicle_number = info.vehicle_number;
        this.number = info.number;
        this.contract_id = info.contract_id;
        this.config_change_date = info.config_change_date.split(" ")[0];
        this.config_chang_delivery_date = info.config_chang_delivery_date;
        this.deliver_place = info.deliver_place;
        this.delivery_date = info.delivery_date.split(" ")[0];
        this.change_content = info.change_content;     //更改如下
        this.models_name = info.models_name;
    }

    @Bindable
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    @Bindable
    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    @Bindable
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Bindable
    public String getVehicle_state() {
        return vehicle_state;
    }

    public void setVehicle_state(String vehicle_state) {
        this.vehicle_state = vehicle_state;
    }

    @Bindable
    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Bindable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
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
    public String getDeliver_place() {
        return deliver_place;
    }

    public void setDeliver_place(String deliver_place) {
        this.deliver_place = deliver_place;
    }

    @Bindable
    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    @Bindable
    public String getChange_content() {
        return change_content;
    }

    public void setChange_content(String change_content) {
        this.change_content = change_content;
    }

    @Bindable
    public String getModels_name() {
        return models_name;
    }

    public void setModels_name(String models_name) {
        this.models_name = models_name;
    }
}
