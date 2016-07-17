package com.amenuo.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


import com.amenuo.monitor.action.TwiceBack;
import com.amenuo.monitor.adapter.MainPageAdapter;
import com.amenuo.monitor.utils.PLog;
import com.amenuo.monitor.view.HeaderGridView;
import com.amenuo.monitor.view.MainHeaderView;
import com.amenuo.monitor.view.MainLumpView;
import com.amenuo.monitor.R;
//import com.jwkj.activity.MainActivity;

public class MainPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private MainLumpView mWeatherLumpView;
    private HeaderGridView mGridView;
    private TwiceBack mTwiceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
//        mWeatherLumpView = new MainLumpView(this);
//        mWeatherLumpView.setImageResource(R.drawable.main_weather);
//        mWeatherLumpView.setText("天气预报");

        MainHeaderView mainHeaderView = new MainHeaderView(this);
        mGridView = (HeaderGridView)this.findViewById(R.id.main_gridView);
        mGridView.addHeaderView(mainHeaderView);
        mGridView.setAdapter(new MainPageAdapter(this));
        mGridView.setOnItemClickListener(this);

        mTwiceBack = new TwiceBack();
    }

//    @Override
//    public void onClick(View v) {
//        int resId = v.getId();
//        Intent intent = new Intent();
//        if (resId == R.id.main_page_camera_lumpView) {
//            intent.setClass(this, MainActivity.class);
//
//        } else if (resId == R.id.main_page_live_lumpView) {
//            intent.setClass(this, LiveListActivity.class);
//        }else{
//            intent.setClass(this, WebviewActivity.class);
//        }
//
//        startActivity(intent);
//    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PLog.e("posotion:"+position);
        position = position - 2; // headerview占用了2个position
        Intent intent = new Intent();
        if (position == 0){
//            intent.setClass(this, MainActivity.class);
        }else if(position == 1){
            intent.setClass(this, LiveListActivity.class);
        }else{
//            intent.setClass(this, MainActivity.class);
        }
//        startActivity(intent);
    }
}
