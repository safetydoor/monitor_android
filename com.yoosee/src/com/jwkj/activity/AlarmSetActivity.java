package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.adapter.DateNumericAdapter;
import com.jwkj.data.AlarmMask;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.wheel.widget.WheelView;
import com.jwkj.widget.NormalDialog;
import com.lib.addBar.AddBar;
import com.lib.addBar.OnItemChangeListener;
import com.lib.addBar.OnLeftIconClickListener;

public class AlarmSetActivity extends BaseActivity implements OnClickListener {
	Context mContext;
	WheelView date_seconds;
	ImageView back_btn;
	RelativeLayout setting_time;
	TextView time_text;
	RelativeLayout add_alarm_item, alarm_record;
	AddBar addBar;
	List<AlarmMask> mList = new ArrayList<AlarmMask>();
	private boolean isRegFilter = false;
	NormalDialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_alarm_set);
		mContext = this;
		Log.e("NpcCommon","NpcCommon---");
		initComponent();
		regFilter();
	}

	public void initComponent() {
		int time_interval = SharedPreferencesManager.getInstance()
				.getAlarmTimeInterval(mContext);
		date_seconds = (WheelView) findViewById(R.id.date_seconds);
		date_seconds.setViewAdapter(new DateNumericAdapter(mContext, 1, 90));
		date_seconds.setCurrentItem(time_interval - 1);
		// date_seconds.addScrollingListener(scrolledListener);
		date_seconds.setCyclic(true);
		back_btn = (ImageView) findViewById(R.id.back_btn);
		setting_time = (RelativeLayout) findViewById(R.id.setting_time);
		time_text = (TextView) findViewById(R.id.time_text);
		time_text.setText(String.valueOf((time_interval)));
		add_alarm_item = (RelativeLayout) findViewById(R.id.add_alarm_item);
		alarm_record = (RelativeLayout) findViewById(R.id.alarm_record);
		addBar = (AddBar) findViewById(R.id.add_bar);
		addBar.setMax_count(999);
		addBar.setArrowVisiable(false);
		addBar.setOnItemChangeListener(new OnItemChangeListener() {

			@Override
			public void onChange(int item) {
				// TODO Auto-generated method stub
				if (item > 0) {
					add_alarm_item.setBackgroundResource(R.drawable.tiao_bg_up);
				} else {
					add_alarm_item
							.setBackgroundResource(R.drawable.tiao_bg_single);
				}
			}

		});

		addBar.setOnLeftIconClickListener(new OnLeftIconClickListener() {

			@Override
			public void onClick(View icon, final int position) {
				// TODO Auto-generated method stub
				final AlarmMask alarmMask = mList.get(position);
				dialog = new NormalDialog(mContext, mContext.getResources()
						.getString(R.string.cancel_shield), mContext
						.getResources()
						.getString(R.string.ensure_cancel_shield)
						+ " " + alarmMask.deviceId + "?", mContext
						.getResources().getString(R.string.ensure), mContext
						.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub

						DataManager.deleteAlarmMask(mContext,
								NpcCommon.mThreeNum, alarmMask.deviceId);
						mList.remove(position);
						addBar.removeItem(position);
					}
				});
				dialog.showDialog();
			}
		});
		Log.e("NpcCommon","NpcCommon="+NpcCommon.mThreeNum);
		mList = DataManager.findAlarmMaskByActiveUser(mContext,
				NpcCommon.mThreeNum);
		for (AlarmMask alarmMask : mList) {
			addBar.addItem(alarmMask.deviceId);
		}

		alarm_record.setOnClickListener(this);
		add_alarm_item.setOnClickListener(this);
		setting_time.setOnClickListener(this);
		back_btn.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.ADD_ALARM_MASK_ID_SUCCESS);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(
					Constants.Action.ADD_ALARM_MASK_ID_SUCCESS)) {
				AlarmMask alarmMask = (AlarmMask) intent
						.getSerializableExtra("alarmMask");
				addBar.addItem(alarmMask.deviceId);
				mList.add(alarmMask);
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			finish();
		} else if (id == R.id.setting_time) {
			SharedPreferencesManager.getInstance().putAlarmTimeInterval(
					mContext, date_seconds.getCurrentItem() + 1);
			Log.e("my", date_seconds.getCurrentItem() + "");
			time_text
					.setText(String.valueOf((date_seconds.getCurrentItem() + 1)));
			T.showShort(mContext, R.string.modify_success);
		} else if (id == R.id.add_alarm_item) {
			Intent modify_alarmMaskId = new Intent(mContext,
					AddAlarmMaskIdActivity.class);
			mContext.startActivity(modify_alarmMaskId);
		} else if (id == R.id.alarm_record) {
			Intent go_alarm_record = new Intent(mContext,
					AlarmRecordActivity.class);
			mContext.startActivity(go_alarm_record);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ALARMSETACTIVITY;
	}
}
