package com.amenuo.monitor.manager;

import com.amenuo.monitor.application.MonitorApplication;
import com.amenuo.monitor.model.AdModel;
import com.amenuo.monitor.utils.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 8/2/16.
 */
public class AdManager {

    private final static String fileName = "ads.json";

    private static AdManager instance;

    public static AdManager getInstance() {
        if (instance == null) {
            instance = new AdManager();
        }
        return instance;
    }

    public List<AdModel> adList;

    public List<AdModel> getAdList() {
        if (adList == null) {
            adList = readAdList();
        }
        return adList;
    }

    public void saveJson(String json) {
        try {
            adList = jsonToList(json);
            FileHelper.writeFile(fileName, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AdModel> readAdList() {
        try {
            String json = FileHelper.readFile(fileName);
            if (json!=null){
                List<AdModel> adList = jsonToList(json);
                return adList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<AdModel>();
    }

    private List<AdModel> jsonToList(String json) {
        List<AdModel> adList = new ArrayList<AdModel>();
        try {
            JSONArray result = new JSONArray(json);
            int length = result.length();
            for (int i = 0; i < length; i++) {
                JSONObject ad = result.getJSONObject(i);
                AdModel adModel = new AdModel();
                adModel.setName(ad.getString("name"));
                adModel.setImageUrl(ad.getString("imageUrl"));
                adModel.setAdUrl(ad.getString("adUrl"));
                adList.add(adModel);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return adList;
    }
}
