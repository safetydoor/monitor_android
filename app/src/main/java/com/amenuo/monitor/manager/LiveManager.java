package com.amenuo.monitor.manager;

import com.amenuo.monitor.model.LiveModel;
import com.amenuo.monitor.utils.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 8/2/16.
 */
public class LiveManager {

    private final static String fileName = "lives.json";

    private static LiveManager instance;

    public static LiveManager getInstance() {
        if (instance == null) {
            instance = new LiveManager();
        }
        return instance;
    }

    private List<LiveModel> liveList;

    public List<LiveModel> getLiveList() {
        if (liveList == null) {
            liveList = readLiveList();
        }
        return liveList;
    }

    public void setLiveList(List<LiveModel> liveList) {
        this.liveList = liveList;
    }

    public void saveJson(String json) {
        try {
            liveList = jsonToList(json);
            FileHelper.writeFile(fileName, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LiveModel> readLiveList() {
        try {
            String json = FileHelper.readFile(fileName);
            if (json != null) {
                List<LiveModel> liveList = jsonToList(json);
                return liveList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<LiveModel>();
    }

    private List<LiveModel> jsonToList(String json) {
        List<LiveModel> liveList = new ArrayList<LiveModel>();
        try {
            JSONArray result = new JSONArray(json);
            int length = result.length();
            for (int i = 0; i < length; i++) {
                JSONObject live = result.getJSONObject(i);
                LiveModel liveModel = new LiveModel();
                liveModel.setName(live.getString("name"));
                liveModel.setAddress(live.getString("address"));
                liveList.add(liveModel);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return liveList;
    }
}
