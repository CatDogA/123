package com.shanghaigm.dms.model.entity.as;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/7/21.
 * 存储下载路径等信息
 */

public class PathInfo extends BaseObservable implements Serializable {
    public int type;     //15,16,18,19,20
    public int kind;  //1.压缩，2.原图
    public String path;
    public String name;

    public PathInfo(int type,int kind, String path,String name) {
        this.type = type;
        this.path = path;
        this.name = name;
        this.kind = kind;
    }


}
