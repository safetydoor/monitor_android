package com.amenuo.monitor.model;

import java.util.List;

/**
 * Created by laps on 7/20/16.
 */
public class LumpCategoryModel {

    private String name;
    private List<LumpModel> lumpModels;

    public LumpCategoryModel(){}

    public LumpCategoryModel(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LumpModel> getLumpModels() {
        return lumpModels;
    }

    public void setLumpModels(List<LumpModel> lumpModels) {
        this.lumpModels = lumpModels;
    }
}
