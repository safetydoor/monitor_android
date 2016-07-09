package com.jwkj.activity;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.jwkj.data.APContact;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.TcpClient;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.NormalDialog;
import com.yoosee.R;

import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyApWifiPwd extends BaseActivity {
	private Context mContext;
	Button bt_ensure;
	EditText et_pwd;
	ImageView back_btn;
	NormalDialog dialog;
	boolean isRegFilter = false;
	String pwd;
	String wifiName;
	String contactId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_modify_apwifi_pwd);
		mContext = this;
		contactId = getIntent().getStringExtra("contactId");
		Log.e("pwd", "contactId=" + contactId);
		bt_ensure = (Button) findViewById(R.id.next);
		et_pwd = (EditText) findViewById(R.id.modify_pwd);
		bt_ensure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WifiManager wifimanager = (WifiManager) mContext
						.getSystemService(mContext.WIFI_SERVICE);
				WifiInfo wifiinfo = wifimanager.getConnectionInfo();
				wifiName = wifiinfo.getSSID().substring(1,
						wifiinfo.getSSID().length() - 1);
				pwd = et_pwd.getText().toString().trim();
				if (pwd.length() < 8 || pwd.length() > 16) {
					T.showShort(mContext, R.string.wifi_password_limit);
					return;
				}
				dialog = new NormalDialog(mContext);
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
				dialog.setTitle(R.string.verification);
				dialog.showDialog();
				TcpClient tcpclient = new TcpClient(Utils
						.setDeviceApWifiPwd(pwd));
				try {
					tcpclient.setIpdreess(InetAddress.getByName("192.168.1.1"));
					tcpclient.setCallBack(FList.myHandler);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tcpclient.createClient();
			}
		});
		back_btn = (ImageView) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		regFilter();
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.SET_AP_DEVICE_WIFI_PWD);
		registerReceiver(br, filter);
		isRegFilter = true;

	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					Constants.Action.SET_AP_DEVICE_WIFI_PWD)) {
				int result = intent.getIntExtra("result", -1);
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result == 0) {
					T.showShort(
							mContext,
							getResources().getString(
									R.string.set_wifi_pwd_success));
					APContact contact = DataManager
							.findAPContactByActiveUserAndContactId(mContext,
									NpcCommon.mThreeNum, contactId);
					if (contact != null) {
						contact.Pwd = pwd;
						Log.e("pwd", "pwd=" + pwd);
						DataManager.updateAPContact(mContext, contact);
					}
					goToMainActivity();
				} else {
					T.showShort(mContext,
							getResources()
									.getString(R.string.set_wifi_pwd_fail));
				}
				WifiUtils.getInstance().connectWifi(wifiName, pwd, 1);
			}
		}
	};

	private void goToMainActivity() {
		Intent main = new Intent();
		main.setClass(mContext, MainActivity.class);
		main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(main);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFYAPWIFIPWD;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			unregisterReceiver(br);
			isRegFilter = false;
		}
	}

}
