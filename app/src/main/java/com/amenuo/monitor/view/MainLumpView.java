package com.amenuo.monitor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.squareup.picasso.Picasso;

/**
 * Created by laps on 7/5/16.
 */
public class MainLumpView extends FrameLayout {

    private MainLumpImageView mImageView;
    private TextView mTextView;

    public MainLumpView(Context context) {
        super(context);
    }

    public MainLumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View root = LayoutInflater.from(context).inflate(R.layout.layou_main_lump, this);
        mImageView = (MainLumpImageView) root.findViewById(R.id.main_lump_image);
        mTextView = (TextView) root.findViewById(R.id.main_lump_text);
    }

    public void setImageResource(int resId) {
        Picasso.with(this.getContext()).load(resId).into(this.mImageView);
    }

    public void setText(String text) {
        this.mTextView.setText(text);
    }

}
