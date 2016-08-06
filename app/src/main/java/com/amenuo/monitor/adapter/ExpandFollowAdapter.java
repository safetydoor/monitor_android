package com.amenuo.monitor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.manager.LumpCategoryManager;
import com.amenuo.monitor.manager.LumpManager;
import com.amenuo.monitor.model.LumpCategoryModel;
import com.amenuo.monitor.model.LumpModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by laps on 8/5/16.
 */
public class ExpandFollowAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<LumpCategoryModel> lumpCategoryModels;

    public ExpandFollowAdapter(Context context) {
        this.mContext = context;
        this.lumpCategoryModels = LumpCategoryManager.getInstance().getLumpCategoryList();
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void notifyDataSetChanged() {
        lumpCategoryModels = LumpCategoryManager.getInstance().getLumpCategoryList();
        super.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return lumpCategoryModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lumpCategoryModels.get(groupPosition).getLumpModels().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lumpCategoryModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lumpCategoryModels.get(groupPosition).getLumpModels().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_follow_list_section, null);
        TextView nameTextView = (TextView) view.findViewById(R.id.follow_list_section_text);
        nameTextView.setText(lumpCategoryModels.get(groupPosition).getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ImageView iconImageView = null;
        TextView nameTextView = null;
        TextView descTextView = null;
        Button followButton = null;
        View dividerView = null;
        View view = null;
        LumpViewHolder viewHolder = null;
        LumpModel lumpModel = (LumpModel) getChild(groupPosition, childPosition);
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.layout_follow_list_item, null);
            iconImageView = (ImageView) view.findViewById(R.id.follow_list_item_icon);
            nameTextView = (TextView) view.findViewById(R.id.follow_list_item_name);
            descTextView = (TextView) view.findViewById(R.id.follow_list_item_desc);
            followButton = (Button) view.findViewById(R.id.follow_list_item_follow);
            followButton.setOnClickListener(this);
            dividerView = view.findViewById(R.id.follow_list_item_divider);
            viewHolder = new LumpViewHolder();
            viewHolder.setIconImageView(iconImageView);
            viewHolder.setDescTextView(descTextView);
            viewHolder.setNameTextView(nameTextView);
            viewHolder.setFollowButton(followButton);
            viewHolder.setDividerView(dividerView);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (LumpViewHolder) view.getTag();
            iconImageView = viewHolder.getIconImageView();
            nameTextView = viewHolder.getNameTextView();
            descTextView = viewHolder.getDescTextView();
            followButton = viewHolder.getFollowButton();
            dividerView = viewHolder.getDividerView();
        }
        Picasso.with(this.mContext)
                .load(lumpModel.getIconUrl())
                .fit()
                .into(iconImageView);
        iconImageView.setBackgroundResource(lumpModel.getBgDrawableId());
        nameTextView.setText(lumpModel.getName());
        descTextView.setText(lumpModel.getDesc());
        Boolean isFollowed = lumpModel.isFollowed();
        followButton.setSelected(isFollowed);
        if (isFollowed){
            followButton.setText("取消");
        }else{
            followButton.setText("关注");
        }
        followButton.setTag(lumpModel);
        if (childPosition == getChildrenCount(groupPosition) - 1) {
            dividerView.setVisibility(View.GONE);
        } else {
            dividerView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onClick(View v) {
        LumpModel lumpModel = (LumpModel) v.getTag();
        if (lumpModel != null) {
            Boolean isFollowed = lumpModel.isFollowed();
            lumpModel.setFollowed(!isFollowed);
            isFollowed = !isFollowed;
            if (isFollowed) {
                LumpManager.getInstance().addLump(lumpModel);
            } else {
                LumpManager.getInstance().removeLump(lumpModel.getName());
            }
            this.notifyDataSetChanged();
        }
    }
}
