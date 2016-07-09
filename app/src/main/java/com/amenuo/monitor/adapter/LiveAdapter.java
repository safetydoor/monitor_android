package com.amenuo.monitor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.model.LiveModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laps on 7/9/16.
 */
public class LiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<LiveModel> mLives;
    private LayoutInflater mInflater;

    public LiveAdapter(Context context) {
        this.mContext = context;
        this.mLives = getLivesFromAssets();
        this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<LiveModel> getLivesFromAssets() {
        List<LiveModel> liveModels = new ArrayList<LiveModel>();
        try {
            InputStream inputStream = this.mContext.getResources().openRawResource(R.raw.live);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";

            while ((line = bufReader.readLine()) != null) {
                if (!line.trim().equals("")) {
                    String[] arr = line.split(",");
                    if (arr.length == 2) {
                        LiveModel live = new LiveModel();
                        live.setName(arr[0]);
                        live.setAddress(arr[1]);
                        liveModels.add(live);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liveModels;
    }

    @Override
    public int getCount() {
        return this.mLives.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mLives.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View liveItemView = null;
        LiveModel liveModel = this.mLives.get(i);
        LiveViewHolder holder = null;
        if (view == null) {
            liveItemView = this.mInflater.inflate(R.layout.layout_live_list_item, null);
            TextView textView = (TextView) liveItemView.findViewById(R.id.live_list_item_textView);
            holder = new LiveViewHolder(textView);
        } else {
            holder = (LiveViewHolder) view.getTag();
            liveItemView = view;
        }
        holder.getTextView().setText(liveModel.getName());
        liveItemView.setTag(holder);
        return liveItemView;
    }

    private class LiveViewHolder {
        private TextView textView;

        public LiveViewHolder(TextView textView) {

            this.textView = textView;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
