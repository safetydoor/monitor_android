package com.jwkj.fragment;

import java.io.Serializable;

import com.jwkj.APdeviceMonitorActivity;
import com.jwkj.CallActivity;
import com.jwkj.WaittingActivity;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnNormalDialogTimeOutListner;
import com.p2p.core.P2PHandler;
import com.yoosee.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddAPDeviceFrag extends BaseFragment {

	private EditText contactName, contactPwd;
	private Contact APContacts;
	private Button btnConnect;
	private Context mContext;
	private boolean isRegFilter = false;
	private boolean isDealeWithrecive = false;
	private boolean isConnect = false;
	private static int WIFIState = 0;
	private NormalDialog dialog;
	private APContact apContact;
	private TextView txAPContactID;
	boolean islogin=true;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_addapdevice, container,
				false);
		mContext = getActivity();
		if (savedInstanceState == null) {
			APContacts = (Contact) getArguments().getSerializable("contact");
		} else {
			APContacts = (Contact) savedInstanceState
					.getSerializable("contact");
		}
		// 类型转换
		apContact = changeContactToAPContact(APContacts);
		islogin=getArguments().getBoolean("islogin", true);
		if(NpcCommon.mThreeNum.equals("0517401")){
			islogin=false;
		}else{
			islogin=true;
		}
		Log.e("dxswifi", apContact.toString());
		initUI(view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		regFilter();
	}

	@Override
	public void onPause() {
		super.onPause();
		isDealeWithrecive = false;
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
		}
	}

	private void initUI(View view) {
		contactName = (EditText) view.findViewById(R.id.contactName);
		contactPwd = (EditText) view.findViewById(R.id.et_apcontactPwd);
		btnConnect = (Button) view.findViewById(R.id.bt_ensure);
		txAPContactID = (TextView) view.findViewById(R.id.contactId);
		contactName.setFocusable(false);

		contactName.setText(apContact.nickName);
		contactPwd.setText(apContact.Pwd);
		txAPContactID.setText(apContact.contactId);
		btnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hindKeyBoard(v);
				connectWifi();
			}
		});
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.NET_WORK_TYPE_CHANGE);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		// 接收报警ID---------------
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.NET_WORK_TYPE_CHANGE)) {
				if (NpcCommon.mNetWorkType == NpcCommon.NETWORK_TYPE.NETWORK_WIFI) {
					if (WifiUtils.getInstance().isConnectWifi(
							apContact.nickName)) {
						if (WIFIState == WifiManager.WIFI_STATE_ENABLED
								&& isDealeWithrecive) {
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}
							// 连接成功页面需要跳转并且保存数据库
							if (!DataManager.isAPContactExist(mContext,
									apContact.activeUser, apContact.contactId)) {
								// 保存
								Log.e("dxsDatamaneger", "保存apContact.APname"
										+ apContact.contactId);
								DataManager
										.insertAPContact(mContext, apContact);
							} else {
								// 更新
								DataManager
										.updateAPContact(mContext, apContact);
								Log.e("dxsDatamaneger", "更新apContact.APname"
										+ apContact.APname);
							}
							if (!islogin) {
//								if (APList.getInstance().isApMode(
//										APContacts.contactId)) {
									// 广播换界面
									Intent ConnectOk = new Intent();
									ConnectOk
											.setAction(Constants.Action.SEARCH_AP_APMODE);

									ConnectOk.putExtra("pwd", apContact.Pwd);
									mContext.sendBroadcast(ConnectOk);
//								} else {
//									dialog.dismiss();
//									T.showShort(mContext, "设备不处于AP模式");
//								}
								Log.e("leleapwifi", "connect no login");
							} else {
//								if (FList.getInstance().isApMode(
//										APContacts.contactId)) {
//									// 广播换界面
									Intent ConnectOk = new Intent();
									ConnectOk
											.setAction(Constants.Action.SEARCH_AP_APMODE);

									ConnectOk.putExtra("pwd", apContact.Pwd);
									mContext.sendBroadcast(ConnectOk);
//								} else {
//									dialog.dismiss();
//									T.showShort(mContext, "设备不处于AP模式");
//								}
							    Log.e("leleapwifi", "connect login");
							}
						}
					} else {
						Log.e("dxswifi", "设置失败");
						apContact = changeContactToAPContact(APContacts);
					}

				}
			} else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				int message = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
						-1);
				WIFIState = message;
				if (message == WifiManager.WIFI_STATE_DISABLED) {
					Log.e("dxswifi", "WifiManager.WIFI_STATE_DISABLED");
				} else if (message == WifiManager.WIFI_STATE_DISABLING) {
					Log.e("dxswifi", "WifiManager.WIFI_STATE_DISABLING");
				} else if (message == WifiManager.WIFI_STATE_ENABLED) {
					Log.e("dxswifi", "WifiManager.WIFI_STATE_ENABLED");
				} else if (message == WifiManager.WIFI_STATE_ENABLING) {
					Log.e("dxswifi", "WifiManager.WIFI_STATE_ENABLING");
				} else if (message == WifiManager.WIFI_STATE_UNKNOWN) {
					Log.e("dxswifi", "WifiManager.WIFI_STATE_UNKNOWN");
				}
			}

		}

	};

	private void connectWifi() {
		isDealeWithrecive = true;
		String nikname = contactName.getText().toString().trim();
		String pwd = contactPwd.getText().toString().trim();
		if (nikname.length() < 0) {
			Log.e("dxswifi", "别名不正确");
			return;
		}
		if (pwd.length() < 8) {
			// 密码必须8位
			T.showShort(mContext, R.string.wifi_pwd_error);
			return;
		}
		apContact.nickName = APContacts.contactName;
		apContact.Pwd = pwd;
		WifiUtils.getInstance().connectWifi(apContact.getApName(), apContact.Pwd,
				1);
		if (dialog == null) {
			dialog = new NormalDialog(mContext);
		}
		dialog.setTitle(R.string.wait_connect);
		dialog.showLoadingDialog();
		dialog.setTimeOut(30 * 1000);
		dialog.setOnNormalDialogTimeOutListner(new OnNormalDialogTimeOutListner() {

			@Override
			public void onTimeOut() {
				T.showShort(mContext, R.string.time_out);
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("contact", APContacts);
		super.onSaveInstanceState(outState);
	}

	private APContact changeContactToAPContact(Contact contact) {
		Log.e("findapcontact", NpcCommon.mThreeNum + "--" + contact.contactId);
		if (NpcCommon.mThreeNum == null) {
			NpcCommon.mThreeNum = "0517401";
		}
		APContact ap = DataManager.findAPContactByActiveUserAndContactId(
				mContext, NpcCommon.mThreeNum, contact.contactId);
		if (ap != null) {
			return ap;
		} else {
			return new APContact(contact.contactId, contact.contactName,
					contact.contactName, contact.wifiPassword, NpcCommon.mThreeNum);
		}
	}

}
