package com.shanghaigm.dms.model.entity.mm;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shanghaigm.dms.model.entity.ck.AllocationAddChooseUndefaultInfo;
import com.shanghaigm.dms.view.activity.ck.AllocationAddChooseUndefaultActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tom on 2017/6/9.
 * 配置信息
 */

public class OrderDetailInfoAllocation extends BaseObservable implements Serializable {
    public static String UNDEFALT_ALLOCATION_INFO;
    private String assembly;                //系统
    private String entry_name;              //项目名
    private String standard_information;      //配置
    private String num;                     //数量
    private String remarks;                 //备注
    private int isdefault;
    private int matchLength;                 //选配数量
    private Double price;                    //价格
    private ArrayList<AllocationAddChooseUndefaultInfo> matching;
    private int standard_id;     //判断唯一性
    private String cost_change;
    private String supporting_id;


    public OrderDetailInfoAllocation() {
    }

    public OrderDetailInfoAllocation(String assembly, String entry_name, String config_information, String num, String remarks, int isdefault) {
        this.assembly = assembly;
        this.entry_name = entry_name;
        this.standard_information = config_information;
        this.num = num;
        this.remarks = remarks;
        this.isdefault = isdefault;
    }
    //最正规规的标配
    public OrderDetailInfoAllocation(String assembly, String entry_name, String standard_information,String cost_change,String supporting_id, String num, String remarks, int matchLength, ArrayList<AllocationAddChooseUndefaultInfo> list, int standard_id) {
        this.assembly = assembly;
        this.entry_name = entry_name;
        this.standard_information = standard_information;
        this.num = num;
        this.remarks = remarks;
        this.matchLength = matchLength;
        this.matching = list;
        this.cost_change = cost_change;
        this.supporting_id = supporting_id;
        this.standard_id = standard_id;
    }
    //利用选配伪造的标配
    public OrderDetailInfoAllocation(String assembly, String entry_name, String config_information, Double price, String num, String remarks, int matchLength, ArrayList<AllocationAddChooseUndefaultInfo> list, int standard_id) {
        this.assembly = assembly;
        this.entry_name = entry_name;
        this.standard_information = config_information;
        this.num = num;
        this.remarks = remarks;
        this.matchLength = matchLength;
        this.matching = list;
        this.price = price;
        this.standard_id = standard_id;
    }

    @BindingAdapter("set_text_color")
    public static void setTextColor(TextView tv, int isdefault) {
        if (isdefault == 0) {
            tv.setTextColor(Color.BLACK);
        } else {
            tv.setTextColor(Color.RED);
        }
    }

    @BindingAdapter("set_match_color")
    public static void setMatchTextColor(TextView tv, int matchLength) {
        if (matchLength > 0) {
            tv.setTextColor(Color.RED);
        } else {
            tv.setTextColor(Color.BLACK);
        }
    }

    public void onQueryUndefaultClick(final View v) {
        if (matchLength > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderDetailInfoAllocation.UNDEFALT_ALLOCATION_INFO, matching);
            Intent intent = new Intent(v.getContext(), AllocationAddChooseUndefaultActivity.class);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        }
    }

    @Bindable
    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    @Bindable
    public String getEntry_name() {
        return entry_name;
    }

    public void setEntry_name(String entry_name) {
        this.entry_name = entry_name;
    }

    @Bindable
    public String getConfig_information() {
        return standard_information;
    }

    public void setConfig_information(String config_information) {
        this.standard_information = config_information;
    }

    @Bindable
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Bindable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Bindable
    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    @Bindable
    public int getMatchLength() {
        return matchLength;
    }

    public void setMatchLength(int matchLength) {
        this.matchLength = matchLength;
    }

    public ArrayList<AllocationAddChooseUndefaultInfo> getList() {
        return matching;
    }

    public void setList(ArrayList<AllocationAddChooseUndefaultInfo> list) {
        this.matching = list;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStandard_id() {
        return standard_id;
    }

    public void setStandard_id(int standard_id) {
        this.standard_id = standard_id;
    }


}
