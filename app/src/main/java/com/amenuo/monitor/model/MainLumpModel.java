package com.amenuo.monitor.model;

/**
 * Created by laps on 7/14/16.
 */
public class MainLumpModel {

    private String name;
    private int drawableId;

    public MainLumpModel(){

    }

    public MainLumpModel(String name, int drawableId){
        this.name = name;
        this.drawableId = drawableId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
