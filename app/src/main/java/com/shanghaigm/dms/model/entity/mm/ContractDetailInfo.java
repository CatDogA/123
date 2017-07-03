package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/14.
 */

public class ContractDetailInfo extends BaseObservable implements Serializable {
    private String contract_id;
    private String company_name;
    private String creator_by;
    private String delivery_time;
    private String deposit;
    private String models_name;
    private String battery_manufacturer;
    private String battery_system;
    private String vehicle_number;
    private String number;
    private String delivery_mode;
    private String purpose;
    private String seat_number;

    public ContractDetailInfo() {
    }

    public ContractDetailInfo(String contract_id, String company_name, String creator_by, String delivery_time, String deposit, String models_name, String battery_manufacturer, String battery_system, String vehicle_number, String number, String delivery_mode, String purpose, String seat_number) {
        this.contract_id = contract_id;
        this.company_name = company_name;
        this.creator_by = creator_by;
        this.delivery_time = delivery_time.split(" ")[0];
        this.deposit = deposit;
        this.models_name = models_name;
        this.battery_manufacturer = battery_manufacturer;
        this.battery_system = battery_system;
        this.vehicle_number = vehicle_number;
        this.number = number;
        this.delivery_mode = delivery_mode;
        this.purpose = purpose;
        this.seat_number = seat_number;
    }

    @Bindable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }
    @Bindable
    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    @Bindable
    public String getCreator_by() {
        return creator_by;
    }

    public void setCreator_by(String creator_by) {
        this.creator_by = creator_by;
    }
    @Bindable
    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
    @Bindable
    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
    @Bindable
    public String getModels_name() {
        return models_name;
    }

    public void setModels_name(String models_name) {
        this.models_name = models_name;
    }
    @Bindable
    public String getBattery_manufacturer() {
        return battery_manufacturer;
    }

    public void setBattery_manufacturer(String battery_manufacturer) {
        this.battery_manufacturer = battery_manufacturer;
    }
    @Bindable
    public String getBattery_system() {
        return battery_system;
    }

    public void setBattery_system(String battery_system) {
        this.battery_system = battery_system;
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
    public String getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(String delivery_mode) {
        this.delivery_mode = delivery_mode;
    }
    @Bindable
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Bindable
    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

}
