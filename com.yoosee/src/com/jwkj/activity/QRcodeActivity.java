package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;

public class QRcodeActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	Button bt_next;
	String ssid;
	int type;
	TextView tv_ssid;
	EditText edit_pwd;
	ImageView img_back;
	boolean isRegFilter = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		setContentView(R.layout.activity_qr_code);
		initComponent();
		currentWifi();
		regFilter();
	}

	public void initComponent() {
		tv_ssid = (TextView) findViewById(R.id.tv_ssid);
		edit_pwd = (EditText) findViewById(R.id.edit_pwd);
		img_back = (ImageView) findViewById(R.id.img_back);
		bt_next = (Button) findViewById(R.id.next);
		bt_next.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}

	public void regFilter() {
		isRegFilter = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.CURRENT_WIFI_NAME);
		filter.addAction(Constants.Action.SETTING_WIFI_SUCCESS);
		filter.addAction(Constants.Action.ACTIVITY_FINISH);
		mContext.registerReceiver(br, filter);
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.Action.CURRENT_WIFI_NAME)) {
				ssid = intent.getStringExtra("ssid");
				type = intent.getIntExtra("type", 0);
				tv_ssid.setText(ssid);
				Log.e("ssid", ssid);
			} else if (intent.getAction().equals(
					Constants.Action.SETTING_WIFI_SUCCESS)) {
				finish();
			} else if (intent.getAction().equals(
					Constants.Action.ACTIVITY_FINISH)) {
				finish();
			}

		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.next) {
			InputMethodManager manager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
			if (manager != null) {
				manager.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
			}
			String wifiPwd = edit_pwd.getText().toString();
			if (ssid == null) {
				T.showShort(mContext, R.string.please_choose_wireless);
				return;
			}
			if (ssid.equals("<unknown ssid>")) {
				T.showShort(mContext, R.string.please_choose_wireless);
				return;
			}
			if (null == wifiPwd || wifiPwd.length() <= 0
					&& (type == 1 || type == 2)) {
				T.showShort(mContext, R.string.please_input_wifi_password);
				return;
			}
			Intent qr_code = new Intent(mContext, CreateQRcodeActivity.class);
			qr_code.putExtra("ssidname", ssid);
			qr_code.putExtra("wifiPwd", wifiPwd);
			startActivity(qr_code);
		} else if (id == R.id.img_back) {
			finish();
		} else {
		}

	}

	public void currentWifi() {
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!manager.isWifiEnabled())
			return;
		WifiInfo info = manager.getConnectionInfo();
		ssid = info.getSSID();
		Log.e("ssid", ssid);
		List<ScanResult> datas = new ArrayList<ScanResult>();
		manager.startScan();
		datas = manager.getScanResults();
		if (ssid == null)
			return;
		if (ssid.equals("")) {
			return;
		}
		int a = ssid.charAt(0);
		if (a == 34) {
			ssid = ssid.substring(1, ssid.length() - 1);
		}
		if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
			tv_ssid.setText(ssid);
			Log.e("ssid", ssid);
		}
		for (int i = 0; i < datas.size(); i++) {
			ScanResult result = datas.get(i);
			if (datas.get(i).SSID.equals(ssid)) {
				if (result.capabilities.indexOf("WPA") > 0) {
					type = 2;
				} else if (result.capabilities.indexOf("WEP") > 0) {
					type = 1;
				} else {
					type = 0;
				}
				return;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isRegFilter = true) {
			isRegFilter = false;
			mContext.unregisterReceiver(br);
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_QRCODEACTIVITY;
	}
}
