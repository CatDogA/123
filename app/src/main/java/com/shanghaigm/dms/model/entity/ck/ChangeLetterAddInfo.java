package com.shanghaigm.dms.model.entity.ck;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/8/3.
 */

public class ChangeLetterAddInfo extends BaseObservable implements Serializable {
    private String contract_id;
    private String company_name;
    private String models_name;
    private int models_Id;
    private String number;
    private String contract_price;
    private String delivery_time;
    private int order_id;
    private String order_number;
    private String customer_name;
    private String sNum;
    private String creator_by;
    private String creator_date;

    public ChangeLetterAddInfo(String contract_id, String company_name, String models_name, int models_Id, int number, String contract_price, String delivery_time, int order_id, String order_number, String customer_name, String creator_by,String creator_date) {
        this.contract_id = contract_id;
        this.company_name = company_name;
        this.models_name = models_name;
        this.models_Id = models_Id;
        this.number = number + "";
        this.contract_price = contract_price;
        this.delivery_time = delivery_time;
        this.order_id = order_id;
        this.order_number = order_number;
        this.customer_name = customer_name;
        this.sNum = number + "";
        this.creator_by = creator_by;
        this.creator_date = creator_date;
    }

    @Bindable
    public String getsNum() {
        return sNum;
    }

    public void setsNum(String sNum) {
        this.sNum = sNum;
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
    public String getModels_name() {
        return models_name;
    }

    public void setModels_name(String models_code) {
        this.models_name = models_code;
    }

    public int getModels_Id() {
        return models_Id;
    }

    public void setModels_Id(int models_Id) {
        this.models_Id = models_Id;
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

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public String getCreator_by() {
        return creator_by;
    }

    public void setCreator_by(String creator_by) {
        this.creator_by = creator_by;
    }

    public String getCreator_date() {
        return creator_date;
    }

    public void setCreator_date(String creator_date) {
        this.creator_date = creator_date;
    }
}
