package com.amenuo.monitor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amenuo.monitor.R;
import com.amenuo.monitor.model.MainLumpModel;
import com.amenuo.monitor.view.MainLumpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 7/14/16.
 */
public class MainPageAdapter extends BaseAdapter {

    private List<MainLumpModel> mLumpModels;
    private Context mContext;

    public MainPageAdapter(Context context){

        mLumpModels = new ArrayList<MainLumpModel>();
        mLumpModels.add(new MainLumpModel(1, "智能摄像头", null));
        mLumpModels.add(new MainLumpModel(2, "电视直播", null));
        mLumpModels.add(new MainLumpModel(3, "生活缴费", null));
        mLumpModels.add(new MainLumpModel(4, "实时路况", null));
        mContext = context;
    }

    @Override
    public int getCount() {
        return mLumpModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mLumpModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainLumpModel model = mLumpModels.get(position);
        MainLumpView mainLumpView;
        if (convertView == null){
            mainLumpView = new MainLumpView(mContext);
        }else{
            mainLumpView = (MainLumpView) convertView;
        }
        mainLumpView.setImageResource(model.getBgDrawableId());
        mainLumpView.setText(model.getName());

        return mainLumpView;
    }
}
