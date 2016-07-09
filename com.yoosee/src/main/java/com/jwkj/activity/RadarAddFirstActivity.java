package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.widget.NormalDialog;

public class RadarAddFirstActivity extends BaseActivity {
	private Context mContext;
	Button next;
	ImageView back;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_radar_add_first);
		mContext = this;
		next = (Button) findViewById(R.id.next);
		back = (ImageView) findViewById(R.id.back_btn);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(mContext, RadarAddActivity.class);
				startActivity(it);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_RADARADDFIRSTACTIVITY;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
