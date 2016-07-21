package com.amenuo.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.lib.SectionedBaseAdapter;
import com.amenuo.monitor.manager.LumpManager;
import com.amenuo.monitor.model.LumpCategoryModel;
import com.amenuo.monitor.model.LumpModel;

import java.util.List;

/**
 * Created by laps on 7/19/16.
 */
public class FollowAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<LumpCategoryModel> lumpCategoryModels;

    public FollowAdapter(Context context) {
        this.mContext = context;
        this.lumpCategoryModels = LumpManager.getInstance().getLumpCategoryModels();
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int section, int position) {
        return lumpCategoryModels.get(section).getLumpModels().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return ((LumpModel) getItem(section, position)).getId();
    }

    @Override
    public int getSectionCount() {
        return lumpCategoryModels.size();
    }

    @Override
    public int getCountForSection(int section) {
        return lumpCategoryModels.get(section).getLumpModels().size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        ImageView iconImageView = null;
        TextView nameTextView = null;
        TextView descTextView = null;
        Button followButton = null;
        View dividerView = null;
        View view = null;
        ViewHolder viewHolder = null;
        LumpModel lumpModel = (LumpModel)getItem(section, position);
        if (convertView == null){
            view = mLayoutInflater.inflate(R.layout.layout_follow_list_item, null);
            iconImageView = (ImageView)view.findViewById(R.id.follow_list_item_icon);
            nameTextView = (TextView)view.findViewById(R.id.follow_list_item_name);
            descTextView = (TextView)view.findViewById(R.id.follow_list_item_desc);
            followButton = (Button)view.findViewById(R.id.follow_list_item_follow);
            dividerView = view.findViewById(R.id.follow_list_item_divider);
            viewHolder = new ViewHolder();
            viewHolder.setIconImageView(iconImageView);
            viewHolder.setDescTextView(descTextView);
            viewHolder.setNameTextView(nameTextView);
            viewHolder.setFollowButton(followButton);
            viewHolder.setDividerView(dividerView);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            iconImageView = viewHolder.getIconImageView();
            nameTextView = viewHolder.getNameTextView();
            descTextView = viewHolder.getDescTextView();
            followButton = viewHolder.getFollowButton();
            dividerView = viewHolder.getDividerView();
        }
        iconImageView.setImageResource(lumpModel.getIconResId());
        iconImageView.setBackgroundResource(lumpModel.getBgDrawableId());
        nameTextView.setText(lumpModel.getName());
        descTextView.setText(lumpModel.getDesc());
        followButton.setSelected(lumpModel.isFollowed());
        if(position == getCountForSection(section) - 1){
            dividerView.setVisibility(View.GONE);
        }else{
            dividerView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_follow_list_section, null);
        TextView nameTextView = (TextView)view.findViewById(R.id.follow_list_section_text);
        nameTextView.setText(lumpCategoryModels.get(section).getName());
        return view;
    }

    public class ViewHolder {

        public ImageView getIconImageView() {
            return iconImageView;
        }

        public void setIconImageView(ImageView iconImageView) {
            this.iconImageView = iconImageView;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public void setNameTextView(TextView nameTextView) {
            this.nameTextView = nameTextView;
        }

        public TextView getDescTextView() {
            return descTextView;
        }

        public void setDescTextView(TextView descTextView) {
            this.descTextView = descTextView;
        }

        public Button getFollowButton() {
            return followButton;
        }

        public void setFollowButton(Button followButton) {
            this.followButton = followButton;
        }

        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView descTextView;
        private Button followButton;
        private View dividerView;

        public View getDividerView() {
            return dividerView;
        }

        public void setDividerView(View dividerView) {
            this.dividerView = dividerView;
        }
    }
}
