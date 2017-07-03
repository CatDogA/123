package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/9.
 */

public class OrderDetailInfoTwo extends BaseObservable implements Serializable{
    private String payment_method;
    private String payment_method_remarks;
    private String delivery_time;
    private String freight;//合同价
    private String service_fee;
    private String contract_price;
    private String carriage;
    private String invoice_amount;
    private String billing_requirements;

    public OrderDetailInfoTwo() {
    }
    public OrderDetailInfoTwo(OrderDetailInfoBean bean) {
        OrderDetailInfoBean.ResultEntity resultEntity = bean.resultEntity;
        this.payment_method_remarks = resultEntity.payment_method_remarks;
        this.delivery_time = resultEntity.delivery_time;
        this.freight = resultEntity.freight;
        this.service_fee = resultEntity.service_fee;
        this.contract_price = resultEntity.contract_price;
        this.carriage = resultEntity.carriage;
        this.invoice_amount = resultEntity.invoice_amount;
        this.billing_requirements = resultEntity.billing_requirements;
        int method = Integer.parseInt(resultEntity.payment_method);
        switch (method){
            case 1:
                this.payment_method = "全款";
                break;
            case 2:
                this.payment_method = "分期";
                break;
            case 3:
                this.payment_method = "按揭";
                break;
        }
    }
    @Bindable
    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
    @Bindable
    public String getPayment_method_remarks() {
        return payment_method_remarks;
    }

    public void setPayment_method_remarks(String payment_method_remarks) {
        this.payment_method_remarks = payment_method_remarks;
    }
    @Bindable
    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
    @Bindable
    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }
    @Bindable
    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }
    @Bindable
    public String getContract_price() {
        return contract_price;
    }

    public void setContract_price(String contract_price) {
        this.contract_price = contract_price;
    }
    @Bindable
    public String getCarriage() {
        return carriage;
    }

    public void setCarriage(String carriage) {
        this.carriage = carriage;
    }
    @Bindable
    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }
    @Bindable
    public String getBilling_requirements() {
        return billing_requirements;
    }

    public void setBilling_requirements(String billing_requirements) {
        this.billing_requirements = billing_requirements;
    }
}
