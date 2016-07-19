package com.amenuo.monitor.model;

import android.graphics.drawable.Drawable;

import com.amenuo.monitor.R;

/**
 * Created by laps on 7/14/16.
 */
public class MainLumpModel {

    private int id;
    private String name;
    private String iconUrl;

    public MainLumpModel(){

    }

    public MainLumpModel(int id, String name, String iconUrl){
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBgDrawableId(){
        int d = 1 + (getId() % 4);
        if (d ==1){
            return R.drawable.main_lump_background_1;
        }else if (d == 2){
            return R.drawable.main_lump_background_2;
        }else if (d == 3){
            return R.drawable.main_lump_background_3;
        }else if (d == 4){
            return R.drawable.main_lump_background_4;
        }else {
            return R.drawable.main_lump_background_1;
        }
    }

}
