package com.amenuo.monitor.manager;

import com.amenuo.monitor.model.LumpCategoryModel;
import com.amenuo.monitor.model.LumpModel;
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
public class LumpCategoryManager {

    private final static String fileName = "categorys.json";

    private static LumpCategoryManager instance;

    public static LumpCategoryManager getInstance() {
        if (instance == null) {
            instance = new LumpCategoryManager();
        }
        return instance;
    }

    private List<LumpCategoryModel> categoryList;
    public List<LumpCategoryModel> getLumpCategoryList(){
        if (categoryList == null){
            categoryList = readLumpCategoryList();
        }
        return categoryList;
    }

    public void setLumpCategoryList(List<LumpCategoryModel> categoryList){
        this.categoryList = categoryList;
    }

    public void saveJson(String json) {
        try {
            categoryList = jsonToList(json);
            FileHelper.writeFile(fileName, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LumpCategoryModel> readLumpCategoryList() {
        try {
            String json = FileHelper.readFile(fileName);
            if (json != null){
                List<LumpCategoryModel> categoryList = jsonToList(json);
                return categoryList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<LumpCategoryModel>();
    }

    private List<LumpCategoryModel> jsonToList(String json) {
        List<LumpCategoryModel> categoryList = new ArrayList<LumpCategoryModel>();
        try {
            JSONArray result = new JSONArray(json);
            int length = result.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                LumpCategoryModel categoryModel = new LumpCategoryModel();
                categoryModel.setName(jsonObject.getString("name"));
                List<LumpModel> lumpModels = LumpManager.getInstance().jsonToList(jsonObject.getString("lumps"));
                categoryModel.setLumpModels(lumpModels);
                categoryList.add(categoryModel);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return categoryList;
    }
}
