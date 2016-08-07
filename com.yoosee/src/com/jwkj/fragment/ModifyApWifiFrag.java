package com.jwkj.fragment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.jwkj.activity.MainActivity;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.SearchWather;
import com.jwkj.utils.T;
import com.jwkj.utils.TcpClient;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.NormalDialog;
import com.yoosee.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyApWifiFrag extends BaseFragment {
	private Context mContext;
	Button bt_ensure;
	EditText et_pwd;
	NormalDialog dialog;
	boolean isRegFilter = false;
	String pwd;
	String wifiName;
	private Contact mContact;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext=getActivity();
		mContact=(Contact) getArguments().getSerializable("contact");
		View view=inflater.inflate(R.layout.activity_modify_apwifi_pwd, container, false);
		initComponent(view);
		
		return view;
		
	}
	
	@Override
	public void onResume() {
		regFilter();
		super.onResume();
	}
	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.SET_AP_DEVICE_WIFI_PWD);
		mContext.registerReceiver(br, filter);
		isRegFilter = true;

	}
	public void initComponent(View view){
		bt_ensure = (Button) view.findViewById(R.id.next);
		et_pwd = (EditText)view. findViewById(R.id.modify_pwd);
		et_pwd.addTextChangedListener(new SearchWather(et_pwd));
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
					String apContactId=mContact.contactName.substring(AppConfig.Relese.APTAG.length());
					APContact contact = DataManager
							.findAPContactByActiveUserAndContactId(mContext,
									NpcCommon.mThreeNum,apContactId);
					Log.e("apwifimodify", "NpcCommon.mThreeNum="+NpcCommon.mThreeNum+"--"+"mContact.contactId="+mContact.contactId+"pwd="+pwd);
					if (contact != null) {
						contact.Pwd = pwd;
						Log.e("pwd", "pwd=" + pwd);
						DataManager.updateAPContact(mContext, contact);
					}
					Contact savecontact=DataManager.
							findContactByActiveUserAndContactId(mContext, NpcCommon.mThreeNum, mContact.contactId);
					if(savecontact!=null){
						savecontact.wifiPassword=pwd;
						DataManager.updateContact(mContext, savecontact);
					}
					WifiUtils.getInstance().connectWifi(wifiName, pwd, 1);
					Intent toMain=new Intent();
					toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					toMain.setClass(mContext, MainActivity.class);
					mContext.startActivity(toMain);
				} else {
					T.showShort(mContext,
							getResources()
									.getString(R.string.set_wifi_pwd_fail));
				}
				
			}
		}
	};
	
	
	public void onPause() {
		Utils.hindKeyBoard(et_pwd);
		if (isRegFilter) {
			mContext.unregisterReceiver(br);
			isRegFilter = false;
		}
		super.onPause();
	};

}
