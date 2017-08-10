package com.shanghaigm.dms.model.entity.ck;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ChangeLetterAllocationInfo extends BaseObservable implements Serializable {
    private String config_item;      //配置项
    private String change_content;   //更改内容
    private String price;        //价格
    private String man_hour;        //工时费

    public ChangeLetterAllocationInfo() {
    }

    public ChangeLetterAllocationInfo(String config_item, String change_content, String price, String man_hour) {
        this.config_item = config_item;
        this.change_content = change_content;
        this.price = price;
        this.man_hour = man_hour;
    }
    @Bindable
    public String getConfig_item() {
        return config_item;
    }

    public void setConfig_item(String config_item) {
        this.config_item = config_item;
    }
    @Bindable
    public String getChange_content() {
        return change_content;
    }

    public void setChange_content(String change_content) {
        this.change_content = change_content;
    }
    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    @Bindable
    public String getMan_hour() {
        return man_hour;
    }

    public void setMan_hour(String man_hour) {
        this.man_hour = man_hour;
    }
}
