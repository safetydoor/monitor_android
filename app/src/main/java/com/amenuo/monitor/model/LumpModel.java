package com.amenuo.monitor.model;

import android.graphics.drawable.Drawable;

import com.amenuo.monitor.R;
import com.amenuo.monitor.manager.LumpManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laps on 7/14/16.
 */
public class LumpModel {

    private int id;
    private String name;
    private String desc;
    private String iconUrl;
    private int iconResId = 0;
    private String url;
    private boolean followed = false;

    public LumpModel() {

    }

    public LumpModel(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public LumpModel(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
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

    public boolean isFollowed() {
        return LumpManager.getInstance().exists(this.name);
//        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBgDrawableId() {
        int d = 1 + (getId() % 4);
        if (d == 1) {
            return R.drawable.main_lump_background_1;
        } else if (d == 2) {
            return R.drawable.main_lump_background_2;
        } else if (d == 3) {
            return R.drawable.main_lump_background_3;
        } else if (d == 4) {
            return R.drawable.main_lump_background_4;
        } else {
            return R.drawable.main_lump_background_1;
        }
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("name", this.name);
            if (this.desc == null) {
                jsonObject.put("desc", "");
            } else {
                jsonObject.put("desc", this.desc);
            }
            if (this.iconUrl == null) {
                jsonObject.put("iconUrl", "");
            } else {
                jsonObject.put("iconUrl", this.iconUrl);
            }
            if (this.url == null) {
                jsonObject.put("url", "");
            } else {
                jsonObject.put("url", this.url);
            }
            jsonObject.put("followed", this.followed);
            jsonObject.put("iconResId", this.iconResId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
