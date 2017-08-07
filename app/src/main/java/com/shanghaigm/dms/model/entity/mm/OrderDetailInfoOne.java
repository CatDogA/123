package com.shanghaigm.dms.model.entity.mm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/8.
 */

public class OrderDetailInfoOne extends BaseObservable implements Serializable {
    private String customer_name;
    private String sex;
    private String fixed_telephone;
    private String mobile_phone;
    private String province;
    private String city;
    private String county;
    private String detailed_address;
    private String terminal_customer_name;
    private String terminal_customer_tel;
    private String terminal_customer_address;
    private String models_name;
    private String number;
    private String battery_system;
    private String battery_number;
    private String endurance_mileage;
    private String battery_manufacturer;
    private String color_determine;
    private String ekg;
    private String licensing_addeess;

    public OrderDetailInfoOne() {
    }

    public OrderDetailInfoOne(OrderDetailInfoBean bean) {
        OrderDetailInfoBean.ResultEntity resultEntity = bean.resultEntity;
        this.customer_name = resultEntity.customer_name;
        this.sex = resultEntity.sex;
        this.fixed_telephone = resultEntity.fixed_telephone;
        this.mobile_phone = resultEntity.mobile_phone;
        this.province = resultEntity.province;
        this.city = resultEntity.city;
        this.county = resultEntity.county;
        this.detailed_address = resultEntity.detailed_address;
        this.terminal_customer_name = resultEntity.terminal_customer_name;
        this.terminal_customer_tel = resultEntity.terminal_customer_tel;
        this.terminal_customer_address = resultEntity.terminal_customer_address;
        this.models_name = resultEntity.models_name;
        this.number = resultEntity.number;
        this.battery_system = resultEntity.battery_system;
        this.battery_number = resultEntity.battery_number;
        this.endurance_mileage = resultEntity.endurance_mileage;
        this.battery_manufacturer = resultEntity.battery_manufacturer;
        this.ekg = resultEntity.ekg;
        this.licensing_addeess = resultEntity.licensing_addeess;
        if (!resultEntity.color_determine.equals("")) {
            int detemine = Integer.parseInt(resultEntity.color_determine);
            switch (detemine) {
                case 1:
                    this.color_determine = "已确认";
                    break;
                case 2:
                    this.color_determine = "5个工作日内";
                    break;
                case 3:
                    this.color_determine = "7个工作日内";
                    break;
            }
        }
    }

    public OrderDetailInfoOne(String customer_name, String sex, String fixed_telephone, String mobile_phone, String province, String city, String county, String detailed_address, String terminal_customer_name, String terminal_customer_tel, String terminal_customer_address, String models_name, String number, String battery_system, String battery_number, String endurance_mileage, String battery_manufacturer, String color_determine, String ekg, String licensing_addeess) {
        this.customer_name = customer_name;
        this.sex = sex;
        this.fixed_telephone = fixed_telephone;
        this.mobile_phone = mobile_phone;
        this.province = province;
        this.city = city;
        this.county = county;
        this.detailed_address = detailed_address;
        this.terminal_customer_name = terminal_customer_name;
        this.terminal_customer_tel = terminal_customer_tel;
        this.terminal_customer_address = terminal_customer_address;
        this.models_name = models_name;
        this.number = number;
        this.battery_system = battery_system;
        this.battery_number = battery_number;
        this.endurance_mileage = endurance_mileage;
        this.battery_manufacturer = battery_manufacturer;
        this.color_determine = color_determine;
        this.ekg = ekg;
        this.licensing_addeess = licensing_addeess;
    }

    @Bindable
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Bindable
    public String getFixed_telephone() {
        return fixed_telephone;
    }

    public void setFixed_telephone(String fixed_telephone) {
        this.fixed_telephone = fixed_telephone;
    }

    @Bindable
    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    @Bindable
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Bindable
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Bindable
    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    @Bindable
    public String getTerminal_customer_name() {
        return terminal_customer_name;
    }

    public void setTerminal_customer_name(String terminal_customer_name) {
        this.terminal_customer_name = terminal_customer_name;
    }

    @Bindable
    public String getLicensing_addeess() {
        return licensing_addeess;
    }

    public void setLicensing_addeess(String licensing_addeess) {
        this.licensing_addeess = licensing_addeess;
    }

    @Bindable
    public String getTerminal_customer_tel() {
        return terminal_customer_tel;
    }

    public void setTerminal_customer_tel(String terminal_customer_tel) {
        this.terminal_customer_tel = terminal_customer_tel;
    }

    @Bindable
    public String getTerminal_customer_address() {
        return terminal_customer_address;
    }

    public void setTerminal_customer_address(String terminal_customer_address) {
        this.terminal_customer_address = terminal_customer_address;
    }

    @Bindable
    public String getModels_name() {
        return models_name;
    }

    public void setModels_name(String models_name) {
        this.models_name = models_name;
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Bindable
    public String getBattery_system() {
        return battery_system;
    }

    public void setBattery_system(String battery_system) {
        this.battery_system = battery_system;
    }

    @Bindable
    public String getBattery_number() {
        return battery_number;
    }

    public void setBattery_number(String battery_number) {
        this.battery_number = battery_number;
    }

    @Bindable
    public String getEndurance_mileage() {
        return endurance_mileage;
    }

    public void setEndurance_mileage(String endurance_mileage) {
        this.endurance_mileage = endurance_mileage;
    }

    @Bindable
    public String getBattery_manufacturer() {
        return battery_manufacturer;
    }

    public void setBattery_manufacturer(String battery_manufacturer) {
        this.battery_manufacturer = battery_manufacturer;
    }

    @Bindable
    public String getColor_determine() {
        return color_determine;
    }

    public void setColor_determine(String color_determine) {
        this.color_determine = color_determine;
    }

    @Bindable
    public String getEkg() {
        return ekg;
    }

    public void setEkg(String ekg) {
        this.ekg = ekg;
    }
}
