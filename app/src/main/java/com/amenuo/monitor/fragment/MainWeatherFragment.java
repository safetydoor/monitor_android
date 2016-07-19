package com.amenuo.monitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amenuo.monitor.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by laps on 7/19/16.
 */
public class MainWeatherFragment extends Fragment {

    private static MainWeatherFragment instance;

    public static  MainWeatherFragment getInstance(){
        if(instance==null){
            instance = new MainWeatherFragment();
        }
        return instance;
    }

    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_main_weather, null);

        mImageView = (ImageView)root.findViewById(R.id.main_weather_bg);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(root.getContext())
                .load(R.drawable.weather_sunny_bg)
                .fit()
                .transform(transformation)
                .into(mImageView);
        return root;
    }
}