package com.jwkj.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.jwkj.activity.MainActivity;
import com.jwkj.adapter.AlarmRecordAdapter;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.yoosee.R;

public class KeyboardFrag extends BaseFragment {
	private boolean isRegFilter = false;
	private Context mContext;
	private ListView list_alarm;
	private List<AlarmRecord> list;
	private AlarmRecordAdapter adapter;
	private ImageView clear;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_keyboard, container,
				false);
		mContext = MainActivity.mContext;
		initComponent(view);
		regFilter();

		return view;
	}

	public void initComponent(View view) {
		clear = (ImageView) view.findViewById(R.id.clear);
		list_alarm = (ListView) view.findViewById(R.id.list_allarm);
		list = DataManager.findAlarmRecordByActiveUser(mContext,
				NpcCommon.mThreeNum);
		adapter = new AlarmRecordAdapter(mContext, list);
		list_alarm.setAdapter(adapter);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NormalDialog dialog = new NormalDialog(mContext, mContext
						.getResources()
						.getString(R.string.delete_alarm_records), mContext
						.getResources().getString(R.string.confirm_clear),
						mContext.getResources().getString(R.string.clear),
						mContext.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						DataManager.clearAlarmRecord(mContext,
								NpcCommon.mThreeNum);
						adapter.updateData();
						adapter.notifyDataSetChanged();
					}
				});
				dialog.showDialog();
			}
		});
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.REFRESH_ALARM_RECORD);
		filter.addAction(Constants.Action.REFRESH_ALARM_MESSAGE);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.REFRESH_ALARM_RECORD)) {
				adapter.updateData();
				adapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constants.Action.REFRESH_ALARM_MESSAGE)){
				adapter.updateData();
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			isRegFilter = false;
			mContext.unregisterReceiver(mReceiver);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// clearEditText();
	}
}
