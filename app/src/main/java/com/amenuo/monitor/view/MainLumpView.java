package com.amenuo.monitor.view;

import android.content.Context;

import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by laps on 7/5/16.
 */
public class MainLumpView extends FrameLayout {

    private MainLumpImageView mLumpBgView;
    private ImageView mLumpIconView;
    private TextView mTextView;
    private ImageView mDeleteImageView;

    public MainLumpView(Context context) {
        this(context, null);
    }

    public MainLumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_main_lump, this);
        mLumpBgView = (MainLumpImageView) root.findViewById(R.id.main_lump_image);
        mLumpIconView = (ImageView) root.findViewById(R.id.main_lump_icon);
        mTextView = (TextView) root.findViewById(R.id.main_lump_text);
        mTextView.setVisibility(View.GONE);
        mDeleteImageView = (ImageView) root.findViewById(R.id.main_lump_delete);
    }

    public void setBgImageResource(int resId) {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(this.getContext())
                .load(resId)
                .fit()
                .transform(transformation)
                .into(this.mLumpBgView);
    }

    public void setIconImageUrl(String url){
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(this.getContext())
                .load(url)
                .fit()
                .transform(transformation)
                .into(this.mLumpIconView);
    }

    public void setIconImageResource(int resId){
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(this.getContext())
                .load(resId)
                .fit()
                .transform(transformation)
                .into(this.mLumpIconView);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)){
            this.mTextView.setVisibility(View.GONE);
        }else{
            this.mTextView.setVisibility(View.VISIBLE);
            this.mTextView.setText(text);
        }
    }

    public void setEdit(boolean edit){
        if (edit){
            mDeleteImageView.setVisibility(View.VISIBLE);
        }else{
            mDeleteImageView.setVisibility(View.GONE);
        }
    }

    public void setOnDeleteListener(OnClickListener listener){
        mDeleteImageView.setOnClickListener(listener);
    }

}
