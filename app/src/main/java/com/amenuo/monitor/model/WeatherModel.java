package com.amenuo.monitor.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laps on 8/2/16.
 */
public class WeatherModel {

    private String city;
    private String date;
    private String weather;
    private String temp;
    private String l_tmp;
    private String h_tmp;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getL_tmp() {
        return l_tmp;
    }

    public void setL_tmp(String l_tmp) {
        this.l_tmp = l_tmp;
    }

    public String getH_tmp() {
        return h_tmp;
    }

    public void setH_tmp(String h_tmp) {
        this.h_tmp = h_tmp;
    }

    public static WeatherModel jsonToModel(String json){
        try {
            if (TextUtils.isEmpty(json)){
                return null;
            }
            JSONObject object = new JSONObject(json);
            JSONObject retData = object.getJSONObject("retData");
            WeatherModel weather = new WeatherModel();
            weather.setCity(retData.getString("city"));
            weather.setDate(retData.getString("date"));
            weather.setWeather(retData.getString("weather"));
            weather.setTemp(retData.getString("temp"));
            weather.setL_tmp(retData.getString("l_tmp"));
            weather.setH_tmp(retData.getString("h_tmp"));
            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
