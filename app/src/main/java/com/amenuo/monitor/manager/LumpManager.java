package com.amenuo.monitor.manager;

import com.amenuo.monitor.R;
import com.amenuo.monitor.application.MonitorApplication;
import com.amenuo.monitor.model.LumpModel;
import com.amenuo.monitor.utils.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 7/19/16.
 */
public class LumpManager {

    private final static String fileName = "lumps.json";

    private static LumpManager instance;

    public static LumpManager getInstance() {
        if (instance == null) {
            instance = new LumpManager();
        }
        return instance;
    }

    private List<LumpModel> lumpList;
    public List<LumpModel> getLumpList(){
        if(lumpList == null){
            lumpList = readLumpList();
        }
        if (lumpList == null){
            lumpList = initLumpList();
            saveLumps();
        }
        return lumpList;
    }
//
//    public void setLumpList(List<LumpModel> lumpList) {
//        if (this.lumpList != lumpList){
//            this.lumpList = lumpList;
//            saveLumps();
//        }
//    }

    public void addLump(LumpModel lumpModel){
        if (this.lumpList != null){
            LumpModel lastLumpModel = this.lumpList.get(this.lumpList.size() - 1);
            this.lumpList.remove(lastLumpModel);
            this.lumpList.add(lumpModel);
            this.lumpList.add(lastLumpModel);
            saveLumps();
        }
    }

    public void removeLump(String name){
        LumpModel removeLump = null;
        for (LumpModel lumpModel: this.lumpList) {
            if (lumpModel.getName().equals(name)){
                removeLump = lumpModel;
                break;
            }
        }
        if (removeLump != null){
            this.lumpList.remove(removeLump);
        }
    }

    public Boolean exists(String name){
        if (this.lumpList == null){
            return false;
        }
        for (LumpModel lumpModel: this.lumpList) {
            if (lumpModel.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public List<LumpModel> jsonToList(String json) {
        List<LumpModel> lumpList = new ArrayList<LumpModel>();
        try {
            JSONArray result = new JSONArray(json);
            int length = result.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                LumpModel lumpModel = new LumpModel();
                lumpModel.setId(jsonObject.getInt("id"));
                lumpModel.setName(jsonObject.getString("name"));
                lumpModel.setDesc(jsonObject.getString("desc"));
                if (jsonObject.has("url")){
                    lumpModel.setUrl(jsonObject.getString("url"));
                }
                if (jsonObject.has("iconUrl")) {
                    lumpModel.setIconUrl(jsonObject.getString("iconUrl"));
                }
                if (jsonObject.has("followed")){
                    lumpModel.setFollowed(jsonObject.getBoolean("followed"));
                }
                if (jsonObject.has("iconResId")) {
                    lumpModel.setIconResId(jsonObject.getInt("iconResId"));
                }
                lumpList.add(lumpModel);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return lumpList;
    }

    private List<LumpModel> initLumpList(){
        List<LumpModel> lumpList = new ArrayList<LumpModel>();
        LumpModel cameraLump = new LumpModel();
        cameraLump.setId(100000);
        cameraLump.setFollowed(true);
        cameraLump.setName("智能摄像头");
        cameraLump.setIconResId(R.drawable.main_lump_camera_icon);
        lumpList.add(cameraLump);

        LumpModel liveLump = new LumpModel();
        liveLump.setId(100001);
        liveLump.setFollowed(true);
        liveLump.setName("电视直播");
        liveLump.setIconResId(R.drawable.main_lump_live_icon);
        lumpList.add(liveLump);

        LumpModel convenienceLump = new LumpModel();
        convenienceLump.setId(100002);
        convenienceLump.setFollowed(true);
        convenienceLump.setName("生活缴费");
        convenienceLump.setIconResId(R.drawable.main_lump_convenience_icon);
        lumpList.add(convenienceLump);

        LumpModel addLump = new LumpModel();
        addLump.setId(0);
        addLump.setFollowed(true);
        addLump.setName("");
        addLump.setIconResId(R.drawable.main_lump_background_add);
        lumpList.add(addLump);

        return lumpList;
    }

    private void saveLumps() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (LumpModel lumpModel: this.lumpList) {
                JSONObject jsonObject = lumpModel.toJson();
                jsonArray.put(jsonObject);
            }
            FileHelper.writeFile(fileName, jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LumpModel> readLumpList() {
        try {
            String json = FileHelper.readFile(fileName);
            if (json != null){
                List<LumpModel> lumpList = jsonToList(json);
                return lumpList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
