package com.amenuo.monitor.model;

/**
 * Created by laps on 7/19/16.
 */
public class AdModel {

    public AdModel(String title, String adUrl, String imageUrl){
        this.title = title;
        this.adUrl = adUrl;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    private String title;
    private String imageUrl;
    private String adUrl;
}
