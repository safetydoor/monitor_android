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
public class MainPageAdapter extends BaseAdapter implements MainLumpView.OnDeleteListener {

    private List<LumpModel> mLumpModels;
    private Context mContext;
    private boolean edit = false;

    public MainPageAdapter(Context context) {

        mLumpModels = LumpManager.getInstance().getLumpList();
        mContext = context;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Override
    public void notifyDataSetChanged() {
        mLumpModels = LumpManager.getInstance().getLumpList();
        super.notifyDataSetChanged();
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
        final MainLumpView mainLumpView;
        if (convertView == null) {
            mainLumpView = new MainLumpView(mContext);
            mainLumpView.setOnDeleteListener(this);
        } else {
            mainLumpView = (MainLumpView) convertView;
        }
        if (position == mLumpModels.size() - 1) {
            //最后的加号
            mainLumpView.setBgImageResource(R.drawable.main_lump_background_add);
            mainLumpView.setIconImageUrl(null);
            mainLumpView.setText(null);
            mainLumpView.setEdit(false);
        } else {
            mainLumpView.setBgImageResource(model.getBgDrawableId());
            String iconUrl = model.getIconUrl();
            int iconResId = model.getIconResId();
            if (!TextUtils.isEmpty(iconUrl)) {
                mainLumpView.setIconImageUrl(iconUrl);
            } else if (iconResId != 0) {
                mainLumpView.setIconImageResource(iconResId);
            }
            mainLumpView.setText(model.getName());
            if (position > 1 && edit) {
                mainLumpView.setEdit(true);
            } else {
                mainLumpView.setEdit(false);
            }
        }

        mainLumpView.setTag(model);

        return mainLumpView;
    }

    @Override
    public void onDelete(View v) {
        if (v == null){
            return;
        }
        LumpModel model = (LumpModel) (v.getTag());
        if (model != null){
            LumpManager.getInstance().removeLump(model.getName());
            notifyDataSetChanged();
        }
    }
}
