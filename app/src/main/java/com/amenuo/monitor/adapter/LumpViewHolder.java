package com.amenuo.monitor.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by laps on 8/6/16.
 */
public class LumpViewHolder {

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
