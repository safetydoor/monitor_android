package com.amenuo.monitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.activity.WebviewActivity;
import com.amenuo.monitor.model.AdModel;
import com.amenuo.monitor.view.MainLumpImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by laps on 7/19/16.
 */
public class MainAdFragment extends Fragment implements View.OnClickListener {

    private static MainAdFragment instance;

    public static MainAdFragment getInstance() {
        if (instance == null) {
            instance = new MainAdFragment();
        }
        return instance;
    }

    private MainLumpImageView mMainLumpImageView;
    private TextView mTextView;
    private AdModel mAdModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_main_ad, null);

        mMainLumpImageView = (MainLumpImageView) root.findViewById(R.id.main_ad_image);
        mMainLumpImageView.setOnClickListener(this);
        mTextView = (TextView) root.findViewById(R.id.main_ad_text);
        if (mAdModel != null) {
            setImageUrl(mAdModel.getImageUrl());
            if (!TextUtils.isEmpty(mAdModel.getName())) {
                mTextView.setText(mAdModel.getName());
            }
        }
        return root;
    }

    public void setImageUrl(String url) {
        if (TextUtils.isEmpty(url)){
            return;
        }
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(getActivity())
                .load(url)
                .fit()
                .transform(transformation)
                .into(this.mMainLumpImageView);
    }

    public void setAd(AdModel adModel) {
        this.mAdModel = adModel;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        intent.setClass(getActivity(), WebviewActivity.class);
        if (mAdModel != null){
            intent.putExtra("name", mAdModel.getName());
            intent.putExtra("address", mAdModel.getAdUrl());
        }
        getActivity().startActivity(intent);
    }
}