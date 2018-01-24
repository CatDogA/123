package com.shanghaigm.dms.model.entity.common;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/19.
 */

public class CustomManageInfo extends BaseObservable implements Serializable {
    private String name1;
    private String name2;
    private String name3;
    private String name4;
    private String name5;
    private String name6;
    private String name7;
    private String name8;
    private String name9;
    private String name10;
    private String name11;
    private String name12;
    private String name13;
    private String name14;
    private int id;
    private int id2;
    private int id3;
    private int id4;
    private String line;

    public CustomManageInfo(String name1, String name2, String name3, String name4, String name5, String name6, int id) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.name6 = name6;
        this.id = id;
    }

    //for day report
    public CustomManageInfo(String name1, String name2, String name3, String name4, String name5, String name6, String name7,
                            String name8, String name9, String name10, String name11, String name12, String name13, String name14, int id, int id2, int id3, int id4) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.name6 = name6;
        this.name7 = name7;
        this.name8 = name8;
        this.name9 = name9;
        this.name10 = name10;
        this.name11 = name11;
        this.name12 = name12;
        this.name13 = name13;
        this.name14 = name14;
        this.id = id;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
        this.line = "-";
    }
    @BindingAdapter("set_month_contents_color")
    public static void setState(TextView tv, String name4) {
        if(name4.equals("1")){
            tv.setTextColor(0XFFff0000);   //红    未提交
        }else {
            tv.setTextColor(0XFF858585);
        }
    }

    @Bindable
    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    @Bindable
    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Bindable
    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    @Bindable
    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    @Bindable
    public String getName5() {
        return name5;
    }

    public void setName5(String name5) {
        this.name5 = name5;
    }

    @Bindable
    public String getName7() {
        return name7;
    }

    public void setName7(String name7) {
        this.name7 = name7;
    }

    @Bindable
    public String getName8() {
        return name8;
    }

    public void setName8(String name8) {
        this.name8 = name8;
    }

    @Bindable
    public String getName9() {
        return name9;
    }

    public void setName9(String name9) {
        this.name9 = name9;
    }

    @Bindable
    public String getName10() {
        return name10;
    }

    public void setName10(String name10) {
        this.name10 = name10;
    }

    @Bindable
    public String getName11() {
        return name11;
    }

    public void setName11(String name11) {
        this.name11 = name11;
    }

    @Bindable
    public String getName12() {
        return name12;
    }

    public void setName12(String name12) {
        this.name12 = name12;
    }
    public String getName13() {
        return name13;
    }
    public String getName14() {
        return name14;
    }
    @Bindable
    public String getLine() {
        return line;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName6() {
        return name6;
    }

    public void setName6(String name6) {
        this.name6 = name6;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public int getId3() {
        return id3;
    }

    public int getId4() {
        return id4;
    }

    public void setId3(int id3) {
        this.id3 = id3;
    }
}
