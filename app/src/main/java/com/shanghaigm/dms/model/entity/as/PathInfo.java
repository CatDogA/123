package com.shanghaigm.dms.model.entity.as;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tom on 2017/7/21.
 * 存储下载路径等信息
 */

public class PathInfo extends BaseObservable implements Serializable {
    public int type;     //15,16,18,19,20
    public String path;     //原文件下载地址
    public String name;
    public String cp_path;   //压缩后本地地址
    public int file_id;
    public PathInfo(int type, String path, String cp_path, String name,int file_id) {
        this.type = type;
        this.path = path;
        this.name = name;
        this.cp_path = cp_path;
        this.file_id = file_id;
    }
}
