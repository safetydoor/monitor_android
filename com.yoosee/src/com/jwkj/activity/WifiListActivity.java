package com.jwkj.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.adapter.InfraredWifiAdapter;
import com.jwkj.global.Constants;

public class WifiListActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	ImageView img_back;
	InfraredWifiAdapter adapter;
	TextView tv_no_wifi;
	ListView lv_wifi_list;
	String ssid;
	int type;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mContext = this;
		setContentView(R.layout.activity_wifi_list);
		initComponent();
	}

	public void initComponent() {
		tv_no_wifi = (TextView) findViewById(R.id.tv_no_wifi);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		lv_wifi_list = (ListView) findViewById(R.id.lv_wifi_list);
		adapter = new InfraredWifiAdapter(this, tv_no_wifi);
		lv_wifi_list.setAdapter(adapter);
		lv_wifi_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ScanResult result = (ScanResult) adapter.getItem(arg2);
				ssid = result.SSID;
				if (result.capabilities.indexOf("WPA") > 0) {
					type = 2;
				} else if (result.capabilities.indexOf("WEP") > 0) {
					type = 1;
				} else {
					type = 0;
				}
				Intent it = new Intent();
				it.setAction(Constants.Action.CURRENT_WIFI_NAME);
				it.putExtra("ssid", ssid);
				it.putExtra("type", type);
				mContext.sendBroadcast(it);
				finish();
			}
		});
	}

	@Override
	public int getActivityInfo() {
		return Constants.ActivityInfo.WIFILISTACTIVITY;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.img_back) {
			finish();
		}

	}

}
