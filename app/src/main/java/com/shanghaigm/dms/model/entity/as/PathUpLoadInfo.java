package com.shanghaigm.dms.model.entity.as;

/**
 * Created by Tom on 2017/7/24.
 * 文件上传路径等信息
 */

public class PathUpLoadInfo {
    public String fileName;
    public String cp_path;
    public String path;

    public PathUpLoadInfo(String fileName, String cp_path, String path) {
        this.fileName = fileName;
        this.cp_path = cp_path;
        this.path = path;
    }
}
