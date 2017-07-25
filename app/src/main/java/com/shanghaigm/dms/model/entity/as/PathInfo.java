package com.shanghaigm.dms.model.entity.as;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/7/21.
 * 存储下载路径等信息
 */

public class PathInfo extends BaseObservable implements Serializable {
    public int type;     //15,16,18,19,20
    public String path;
    public String name;
    public String cp_path;

    public PathInfo(int type, String path, String cp_path, String name) {
        this.type = type;
        this.path = path;
        this.name = name;
        this.cp_path = cp_path;
    }


}
