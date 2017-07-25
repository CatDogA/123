package com.shanghaigm.dms.model.entity.as;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25.
 */

public class SaveUsedPaths implements Serializable{
    public String name;
    public String savePath;

    public SaveUsedPaths(String name, String savePath) {
        this.name = name;
        this.savePath = savePath;
    }
}
