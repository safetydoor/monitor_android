package com.amenuo.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.amenuo.monitor.action.TwiceBack;
import com.amenuo.monitor.view.MainLumpView;
import com.amenuo.monitor.R;
import com.jwkj.activity.MainActivity;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private MainLumpView mWeatherLumpView;
    private MainLumpView mLiveLumpView;
    private MainLumpView mCameraLumpView;
    private MainLumpView mTrafficLumpView;
    private MainLumpView mConvenienceLumpView;
    private MainLumpView mMarketLumpView;
    private TwiceBack mTwiceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mWeatherLumpView = (MainLumpView) this.findViewById(R.id.main_page_weather_lumpView);
        mWeatherLumpView.setImageResource(R.drawable.main_weather);
        mWeatherLumpView.setText("天气预报");

        mLiveLumpView = (MainLumpView) this.findViewById(R.id.main_page_live_lumpView);
        mLiveLumpView.setImageResource(R.drawable.main_live);
        mLiveLumpView.setText("电视直播");
        mLiveLumpView.setOnClickListener(this);

        mCameraLumpView = (MainLumpView) this.findViewById(R.id.main_page_camera_lumpView);
        mCameraLumpView.setImageResource(R.drawable.main_camera);
        mCameraLumpView.setText("智能摄像头");
        mCameraLumpView.setOnClickListener(this);

        mTrafficLumpView = (MainLumpView) this.findViewById(R.id.main_page_traffic_lumpView);
        mTrafficLumpView.setImageResource(R.drawable.main_traffic);
        mTrafficLumpView.setText("实时路况");

        mConvenienceLumpView = (MainLumpView) this.findViewById(R.id.main_page_convenience_lumpView);
        mConvenienceLumpView.setImageResource(R.drawable.main_convenience);
        mConvenienceLumpView.setText("便民服务");

//        mMarketLumpView = (MainLumpView) this.findViewById(R.id.main_page_market_lumpView);
//        mMarketLumpView.setImageResource(R.drawable.main_market);
//        mMarketLumpView.setText("商场信息");

        mTwiceBack = new TwiceBack();
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        Intent intent = new Intent();
        if (resId == R.id.main_page_camera_lumpView) {
            intent.setClass(this, MainActivity.class);

        } else if (resId == R.id.main_page_live_lumpView) {
            intent.setClass(this, LiveListActivity.class);
        }

        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mTwiceBack.backPress();
            if (!mTwiceBack.canBack()) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
