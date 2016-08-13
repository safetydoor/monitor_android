package com.amenuo.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.manager.LiveManager;
import com.amenuo.monitor.model.LiveModel;

import java.util.List;

/**
 * Created by laps on 7/9/16.
 */
public class LiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<LiveModel> mLives;
    private LayoutInflater mInflater;
    private LiveAdapterListener mLiveAdapterListener;

    public LiveAdapter(Context context) {
        this.mContext = context;
        this.mLives = LiveManager.getInstance().getLiveList();
        this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmLiveAdapterListener(LiveAdapterListener liveAdapterListener) {
        this.mLiveAdapterListener = liveAdapterListener;
    }

    @Override
    public void notifyDataSetChanged() {
        this.mLives = LiveManager.getInstance().getLiveList();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mLives.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i >= this.mLives.size()) {
            return null;
        }
        return this.mLives.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i >= this.mLives.size()) {

            View loadmoreView = this.mInflater.inflate(R.layout.layout_loadmore, null);
            final Button loadmoreButton = (Button)loadmoreView.findViewById(R.id.loadmore_btn);
            loadmoreButton.setText(R.string.text_loadmore);
            loadmoreButton.setEnabled(true);
            loadmoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLiveAdapterListener != null) {
                        mLiveAdapterListener.onLoadmore();
                        loadmoreButton.setText(R.string.text_loading);
                        loadmoreButton.setEnabled(false);
                    }
                }
            });

            return loadmoreView;
        } else {
            View liveItemView = null;
            LiveModel liveModel = this.mLives.get(i);
            LiveViewHolder holder = null;
            if (view == null || view.getTag() == null) {
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
    }

    public interface LiveAdapterListener {
        public void onLoadmore();
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
