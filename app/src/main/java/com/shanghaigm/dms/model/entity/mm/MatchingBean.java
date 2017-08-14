package com.shanghaigm.dms.model.entity.mm;

import java.io.Serializable;

/**
 * Created by Tom on 2017/6/29.
 */

public class MatchingBean implements Serializable {
    public String assembly;
    public String entry_name;
    public String config_information;
    public int num;
    public String remarks;
    public int isdefault;
    public String cost_change;
    public int isother;

    public MatchingBean(String assembly, String entry_name, String config_information, int num, String remarks, int isdefault, String cost_change, int isother) {
        this.assembly = assembly;
        this.entry_name = entry_name;
        this.config_information = config_information;
        this.num = num;
        this.remarks = remarks;
        this.isdefault = isdefault;
        this.cost_change = cost_change;
        this.isother = isother;
    }

    public MatchingBean(String assembly, String entry_name, String config_information, String remarks, int isdefault, String cost_change, int isother) {
        this.assembly = assembly;
        this.entry_name = entry_name;
        this.config_information = config_information;
        this.remarks = remarks;
        this.isdefault = isdefault;
        this.cost_change = cost_change;
        this.isother = isother;
    }
}
