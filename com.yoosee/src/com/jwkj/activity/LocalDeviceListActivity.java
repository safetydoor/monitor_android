package com.jwkj.activity;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.adapter.LocalDeviceListAdapter;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;

public class LocalDeviceListActivity extends BaseActivity implements
		OnClickListener {
	private ImageView mBack;
	private ListView mList;
	private LocalDeviceListAdapter mAdapter;
	private Context mContext;
	boolean isRegFilter;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_local_device_list);
		initCompent();
		regFilter();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mList = (ListView) findViewById(R.id.list_local_device);
		mAdapter = new LocalDeviceListAdapter(mContext);
		mList.setAdapter(mAdapter);

		mBack.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.ADD_CONTACT_SUCCESS);
		filter.addAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.Action.ADD_CONTACT_SUCCESS)) {
				mAdapter.updateData();
				finish();
			} else if (intent.getAction().equals(
					Constants.Action.LOCAL_DEVICE_SEARCH_END)) {
				mAdapter.updateData();
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			this.finish();
		} else {
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_LOCAL_DEVICE_LIST;
	}

}
