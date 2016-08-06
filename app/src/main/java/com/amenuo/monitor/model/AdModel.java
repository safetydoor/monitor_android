package com.amenuo.monitor.model;

/**
 * Created by laps on 7/19/16.
 */
public class AdModel {

    public AdModel(){}
    public AdModel(String name, String adUrl, String imageUrl){
        this.name = name;
        this.adUrl = adUrl;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    private String name;
    private String imageUrl;
    private String adUrl;
}
