package com.amenuo.monitor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amenuo.monitor.R;
import com.amenuo.monitor.manager.LumpManager;
import com.amenuo.monitor.model.LumpModel;
import com.amenuo.monitor.utils.PLog;
import com.amenuo.monitor.view.MainLumpView;

import java.util.List;

/**
 * Created by laps on 7/14/16.
 */
public class MainPageAdapter extends BaseAdapter {

    private List<LumpModel> mLumpModels;
    private Context mContext;
    private boolean edit = false;

    public MainPageAdapter(Context context){

        mLumpModels = LumpManager.getInstance().getFollowedLumpModels();
        mContext = context;
    }

    public void setEdit(boolean edit){
        this.edit = edit;
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
        LumpModel model = mLumpModels.get(position);
        MainLumpView mainLumpView;
        if (convertView == null){
            mainLumpView = new MainLumpView(mContext);
            mainLumpView.setOnDeleteListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PLog.e("delete");
                }
            });
        }else{
            mainLumpView = (MainLumpView) convertView;
        }
        if (position == mLumpModels.size() - 1){
            //最后的加号
            mainLumpView.setBgImageResource(R.drawable.main_lump_background_add);
            mainLumpView.setText(null);
        }else{
            mainLumpView.setBgImageResource(model.getBgDrawableId());
            String iconUrl = model.getIconUrl();
            int iconResId = model.getIconResId();
            if (!TextUtils.isEmpty(iconUrl)){
                mainLumpView.setIconImageUrl(iconUrl);
            }else if(iconResId != 0){
                mainLumpView.setIconImageResource(iconResId);
            }
            mainLumpView.setText(model.getName());
            if (position > 1 && edit){
                mainLumpView.setEdit(true);
            }else{
                mainLumpView.setEdit(false);
            }
        }

        mainLumpView.setTag(model);

        return mainLumpView;
    }
}
