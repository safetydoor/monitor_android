package com.amenuo.monitor.manager;

import com.amenuo.monitor.R;
import com.amenuo.monitor.model.LumpCategoryModel;
import com.amenuo.monitor.model.LumpModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 7/19/16.
 */
public class LumpManager {

    private static LumpManager instance;

    public static LumpManager getInstance() {
        if (instance == null) {
            instance = new LumpManager();
        }
        return instance;
    }

    private List<LumpModel> allModels;
    private List<LumpCategoryModel> allCategoryModels;

    public List<LumpModel> getFollowedLumpModels() {
        if (allModels == null) {
            allModels = new ArrayList<LumpModel>();
            allModels.add(new LumpModel(1, "智能摄像头", R.drawable.main_lump_camera_icon));
            allModels.add(new LumpModel(2, "电视直播", R.drawable.main_lump_live_icon));
            allModels.add(new LumpModel(3, "生活缴费", R.drawable.main_lump_convenience_icon));
            allModels.add(new LumpModel(4, "实时路况", R.drawable.main_lump_traffic_icon));
            allModels.add(new LumpModel(0, "", null));
        }
        return allModels;
    }

    public List<LumpModel> getLumpModels() {
        List<LumpModel> allModels = new ArrayList<LumpModel>();
        allModels.add(new LumpModel(1, "智能摄像头", R.drawable.main_lump_camera_icon));
        allModels.add(new LumpModel(2, "电视直播", R.drawable.main_lump_live_icon));
        allModels.add(new LumpModel(3, "生活缴费", R.drawable.main_lump_convenience_icon));
        allModels.add(new LumpModel(4, "实时路况", R.drawable.main_lump_traffic_icon));
        return allModels;
    }

    public List<LumpCategoryModel> getLumpCategoryModels() {

        if (allCategoryModels == null) {
            allCategoryModels = new ArrayList<LumpCategoryModel>();
            LumpCategoryModel model1 = new LumpCategoryModel("社区政务");
            LumpCategoryModel model2 = new LumpCategoryModel("风尚文化");
            LumpCategoryModel model3 = new LumpCategoryModel("便民生活");
            model1.setLumpModels(getLumpModels());
            model2.setLumpModels(getLumpModels());
            model3.setLumpModels(getLumpModels());
            allCategoryModels.add(model1);
            allCategoryModels.add(model2);
            allCategoryModels.add(model3);
        }

        return allCategoryModels;
    }
}
