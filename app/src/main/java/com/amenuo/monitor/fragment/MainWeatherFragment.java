package com.amenuo.monitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.model.WeatherModel;
import com.amenuo.monitor.utils.FileHelper;
import com.amenuo.monitor.utils.HttpRequest;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by laps on 7/19/16.
 */
public class MainWeatherFragment extends Fragment {


    private final static String fileName = "weather.json";
    private static MainWeatherFragment instance;

    public static MainWeatherFragment getInstance() {
        if (instance == null) {
            instance = new MainWeatherFragment();
        }
        return instance;
    }
    private ImageView mImageView;
    private TextView mDateTextView;
    private TextView mCityTextView;
    private TextView mWeatherTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_main_weather, null);

        mImageView = (ImageView) root.findViewById(R.id.main_weather_bg);
        mDateTextView = (TextView) root.findViewById(R.id.main_weather_date);
        mCityTextView = (TextView) root.findViewById(R.id.main_weather_city);
        mWeatherTextView = (TextView) root.findViewById(R.id.main_weather_weather);

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(4)
                .borderWidthDp(0)
                .oval(false)
                .build();
        Picasso.with(root.getContext())
                .load(R.drawable.main_weather_bg)
                .fit()
                .transform(transformation)
                .into(mImageView);
        String json = FileHelper.readFile(fileName);
        final WeatherModel weatherModel = WeatherModel.jsonToModel(json);
        if (weatherModel != null) {
            setUIText(weatherModel);
        }
        HttpRequest.requestWeather("天津", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final WeatherModel weatherModel = WeatherModel.jsonToModel(json);
                if (weatherModel == null) {
                    return;
                }
                FileHelper.writeFile("weather.json", json);
                setUIText(weatherModel);
            }
        });
        return root;
    }

    private void setUIText(final WeatherModel weatherModel){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDateTextView.setText(weatherModel.getDate());
                mCityTextView.setText(weatherModel.getCity() + " " + weatherModel.getTemp() + "°");
                mWeatherTextView.setText(weatherModel.getWeather() + " " + weatherModel.getL_tmp() + "° - " + weatherModel.getH_tmp() + "°");
            }
        });
    }

}