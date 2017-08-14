package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/9.
 */

public class OrderDetailInfoTwo extends BaseObservable implements Serializable {
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

    public OrderDetailInfoTwo(String payment_method, String payment_method_remarks, String delivery_time, String freight, String service_fee, String contract_price, String carriage, String invoice_amount, String billing_requirements) {
        this.payment_method = payment_method;
        this.payment_method_remarks = payment_method_remarks;
        this.delivery_time = delivery_time;
        this.freight = freight;
        this.service_fee = service_fee;
        this.contract_price = contract_price;
        this.carriage = carriage;
        this.invoice_amount = invoice_amount;
        this.billing_requirements = billing_requirements;
    }

    public OrderDetailInfoTwo(OrderDetailInfoBean bean) {
        OrderDetailInfoBean.ResultEntity resultEntity = bean.resultEntity;
        if (resultEntity.payment_method_remarks != null) {
            this.payment_method_remarks = resultEntity.payment_method_remarks;
        } else {
            this.payment_method_remarks = "";
        }
        if (resultEntity.delivery_time != null) {
            this.delivery_time = resultEntity.delivery_time;
        } else {
            this.delivery_time = "";
        }
        if (resultEntity.freight != null) {
            this.freight = resultEntity.freight;
        } else {
            this.freight = "";
        }
        if (resultEntity.service_fee != null) {
            this.service_fee = resultEntity.service_fee;
        } else {
            this.service_fee = "";
        }
        if (resultEntity.contract_price != null) {
            this.contract_price = resultEntity.contract_price;
        } else {
            this.contract_price = "";
        }
        if (resultEntity.carriage != null) {
            this.carriage = resultEntity.carriage;
        } else {
            this.carriage = "";
        }
        if (resultEntity.invoice_amount != null) {
            this.invoice_amount = resultEntity.invoice_amount;
        } else {
            this.invoice_amount = "";
        }
        if (resultEntity.billing_requirements != null) {
            this.billing_requirements = resultEntity.billing_requirements;
        } else {
            this.billing_requirements = "";
        }
        this.delivery_time = resultEntity.delivery_time;
        this.freight = resultEntity.freight;
        this.service_fee = resultEntity.service_fee;
        this.contract_price = resultEntity.contract_price;
        this.carriage = resultEntity.carriage;
        this.invoice_amount = resultEntity.invoice_amount;
        this.billing_requirements = resultEntity.billing_requirements;
        if (!resultEntity.payment_method.equals("")) {
            int method = Integer.parseInt(resultEntity.payment_method);
            switch (method) {
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
        } else {
            this.payment_method = "";
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
