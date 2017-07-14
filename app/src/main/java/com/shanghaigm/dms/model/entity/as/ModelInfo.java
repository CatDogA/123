package com.shanghaigm.dms.model.entity.as;

import java.io.Serializable;

/**
 * Created by Tom on 2017/7/12.
 */

public class ModelInfo implements Serializable{
    public String model_Id;
    public String model_name;

    public ModelInfo(String model_Id, String model_name) {
        this.model_Id = model_Id;
        this.model_name = model_name;
    }
}
