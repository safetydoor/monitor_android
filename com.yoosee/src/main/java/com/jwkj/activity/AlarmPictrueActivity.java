package com.jwkj.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jwkj.global.Constants;
import com.jwkj.utils.ImageUtils;
import com.jwkj.widget.PictrueView;
import com.yoosee.R;

public class AlarmPictrueActivity extends BaseActivity {
	PictrueView iv_alarm_pictrue;
	String alarmPictruePath;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_alarm_pictrue);
		alarmPictruePath=getIntent().getStringExtra("alarmPictruePath");
		iv_alarm_pictrue=(PictrueView)findViewById(R.id.iv_alarm_pictrue);
//		Bitmap bitmap = ImageUtils.getBitmap(alarmPictruePath);
//		iv_alarm_pictrue.setImageBitmap(bitmap);
		iv_alarm_pictrue.setPictrue(alarmPictruePath);
	}
	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ALRAM_PICTRUE;
	}

}
