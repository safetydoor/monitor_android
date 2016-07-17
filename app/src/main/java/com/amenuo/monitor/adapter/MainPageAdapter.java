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
        mLumpModels.add(new MainLumpModel("电视直播", R.drawable.main_live));
        mLumpModels.add(new MainLumpModel("智能摄像头", R.drawable.main_camera));
        mLumpModels.add(new MainLumpModel("实时路况", R.drawable.main_traffic));
        mLumpModels.add(new MainLumpModel("便民服务", R.drawable.main_convenience));
        mLumpModels.add(new MainLumpModel("超市", R.drawable.main_market));
        mLumpModels.add(new MainLumpModel("超市", R.drawable.main_market));
        mLumpModels.add(new MainLumpModel("超市", R.drawable.main_market));
        mLumpModels.add(new MainLumpModel("超市", R.drawable.main_market));
        mLumpModels.add(new MainLumpModel("超市", R.drawable.main_market));
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
        mainLumpView.setImageResource(model.getDrawableId());
        mainLumpView.setText(model.getName());

        return mainLumpView;
    }
}
