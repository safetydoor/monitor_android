package com.jwkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jwkj.entity.Account;
import com.jwkj.entity.LocalDevice;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.TcpClient;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.p2p.core.P2PValue;
import com.yoosee.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LogoActivity extends BaseActivity {
	Handler handler;
    private static boolean isConnectApWifi=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		getIsConnectApWifi();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Intent i = new Intent(LogoActivity.this, MainActivity.class);
				i.putExtra("isConnectApWifi", isConnectApWifi);
				startActivity(i);
				finish();
			}
		};
		Message msg = new Message();
		msg.what = 0x11;
		handler.sendMessageDelayed(msg, 2000);
	}
	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_LOGOACTIVITY;
	}
	public void getIsConnectApWifi(){
		String wifiName="";
		try {
			wifiName=WifiUtils.getInstance().getConnectWifiName();
		}catch (Exception e){
			Log.e("lpp", "wifiName" + e);
		}

		if(wifiName.charAt(0)=='"'){
			wifiName=wifiName.substring(1, wifiName.length()-1);
		}
		int length=AppConfig.Relese.APTAG.length();
		isConnectApWifi=false;
		if(wifiName.startsWith(AppConfig.Relese.APTAG)){
		    String contactId=wifiName.substring(length,wifiName.length());
		 	Account	account =AccountPersist.getInstance().getActiveAccountInfo(MyApp.app);
			if(account==null){
				account=new Account();
			}
			account.three_number ="0517401";
			account.rCode1 = "0";
			account.rCode2 = "0";
			account.sessionId = "0";
			AccountPersist.getInstance().setActiveAccount(MyApp.app, account);
			NpcCommon.mThreeNum=AccountPersist.getInstance()
					.getActiveAccountInfo(MyApp.app).three_number;
		    isConnectApWifi=true;
//			 gainIsApMode(contactId);
		}else{
			isConnectApWifi=false;
		}
	}
	public void gainIsApMode(String contactId){
 		TcpClient tcpClient = new TcpClient(Utils.gainWifiMode());
 		try {
 			tcpClient.setIpdreess(InetAddress.getByName("192.168.1.1"));
 			tcpClient.setCallBack(myHandler);
 		} catch (UnknownHostException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		tcpClient.createClient();
 	}
	private static Handler myHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case TcpClient.SEARCH_AP_DEVICE:
				Bundle bundle = msg.getData();
				String id = bundle.getString("contactId");
				String wifiName=WifiUtils.getInstance().getConnectWifiName();
				if(wifiName.charAt(0)=='"'){
					wifiName=wifiName.substring(1, wifiName.length()-1);
				}
				
				int length=AppConfig.Relese.APTAG.length();
				if(wifiName.startsWith(AppConfig.Relese.APTAG)){
					String contactId=wifiName.substring(length,wifiName.length());
					if(id.equals(contactId)){
						LocalDevice apdevice = new LocalDevice();
						apdevice.contactId = contactId;
						apdevice.name = wifiName;
						apdevice.flag = Constants.DeviceFlag.AP_MODE;
						apdevice.type = P2PValue.DeviceType.IPC;
						apdevice.apModeState=Constants.APmodeState.LINK;
						try {
							apdevice.address = InetAddress.getByName("192.168.1.1");
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.e("LogoActivity","contactId="+contactId);
						Account	account =AccountPersist.getInstance().getActiveAccountInfo(MyApp.app);
						if(account==null){
							account=new Account();
						}
						account.three_number ="0517401";
						account.rCode1 = "0";
						account.rCode2 = "0";
						// account.phone = "0";
						// account.email = "0";
						account.sessionId = "0";
						// account.countryCode = "0";
						AccountPersist.getInstance().setActiveAccount(MyApp.app, account);
						NpcCommon.mThreeNum = AccountPersist.getInstance()
								.getActiveAccountInfo(MyApp.app).three_number;
						isConnectApWifi=true;
					}
				}
				break;
			default:
				break;
			}
			return false;
		}
	});

}
