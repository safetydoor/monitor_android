package com.jwkj.fragment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.jwkj.activity.AddApDeviceActivity;
import com.jwkj.activity.ApMonitorActivity;
import com.jwkj.activity.LoginActivity;
import com.jwkj.activity.LogoActivity;
import com.jwkj.activity.MainActivity;
import com.jwkj.adapter.APContactAdapter;
import com.jwkj.adapter.APContactAdapter.onConnectListner;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.LocalDevice;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnNormalDialogTimeOutListner;
import com.lib.pullToRefresh.PullToRefreshBase;
import com.lib.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.lib.pullToRefresh.PullToRefreshListView;
import com.p2p.core.P2PValue;
import com.yoosee.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class APContactFrag extends BaseFragment {
	private Context mContext;
	ListView ap_list;
	APContactAdapter adapter;
	boolean isRegFilter = false;
	PullToRefreshListView pull_refresh_list;
	private Contact apContact;
	NormalDialog dialog_connect;
	String wifiName;
	private ImageView ivChangeMode;
	NormalDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_apcontact, container,
				false);
		mContext = MainActivity.mContext;
		initUI(view);
		regFilter();
	    FList.getInstance().searchLocalDevice();
		return view;
	}

	public void initUI(View view) {
		pull_refresh_list = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		pull_refresh_list
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(mContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						new GetDataTask().execute();
					}

				});
		adapter = new APContactAdapter(mContext);
		ap_list = pull_refresh_list.getRefreshableView();
		ap_list.setAdapter(adapter);
		adapter.setOnSrttingListner(new onConnectListner() {
			
			@Override
			public void onConnectClick(APContact contact) {
				// TODO Auto-generated method stub
				apContact=new Contact();
				apContact.contactId=contact.contactId;
				apContact.contactName=contact.nickName;
				apContact.wifiPassword=contact.Pwd;
				apContact.activeUser=NpcCommon.mThreeNum;
				apContact.contactType=P2PValue.DeviceType.IPC;
				apContact.messageCount=0;
				apContact.isConnectApWifi=false;
				try {
					apContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connectWifi(apContact.contactName, apContact.wifiPassword);
				wifiName=apContact.contactName;
			}
		});
		
		ivChangeMode=(ImageView) view.findViewById(R.id.iv_changemode);
		ivChangeMode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					NormalDialog dialog = new NormalDialog(mContext, mContext
							.getResources().getString(R.string.exit), mContext
							.getResources().getString(R.string.exit_ap_m_confirm),
							mContext.getResources().getString(R.string.exit),
							mContext.getResources().getString(R.string.cancel));
					dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

						@Override
						public void onClick() {
							if(Change!=null){
							   Change.OnChangeMode();
							}
						}
					});
					dialog.showNormalDialog();
			}
		});

	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			Log.e("my", "doInBackground");
			FList flist = FList.getInstance();
			flist.searchLocalDevice();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mListItems.addFirst("Added after refresh...");
			// Call onRefreshComplete when the list has been refreshed.
			pull_refresh_list.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private void connectWifi(String wifiName,String wifiPwd) {
		if (wifiPwd.length() < 8) {
			// 密码必须8位
			T.showShort(mContext, R.string.wifi_pwd_error);
			return;
		}
		WifiUtils.getInstance().connectWifi(wifiName,wifiPwd,
				1);
		if (dialog_connect == null) {
			dialog_connect = new NormalDialog(mContext);
		}
		dialog_connect.setTitle(R.string.wait_connect);
		dialog_connect.showLoadingDialog();
		dialog_connect.setTimeOut(30 * 1000);
		dialog_connect.setOnNormalDialogTimeOutListner(new OnNormalDialogTimeOutListner() {

			@Override
			public void onTimeOut() {
				T.showShort(mContext, R.string.connect_wifi_timeout);
				reConnectApModeWifi();
			}
		});

	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.SEARCH_AP);
		filter.addAction(Constants.Action.REFRESH_CONTANTS);
		filter.addAction(Constants.Action.NET_WORK_TYPE_CHANGE);
		filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
		filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
		mContext.registerReceiver(br, filter);
		isRegFilter = true;
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constants.Action.SEARCH_AP)) {
				adapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constants.Action.REFRESH_CONTANTS)){
				adapter.notifyDataSetChanged();
			}else if(intent.getAction().endsWith(Constants.Action.NET_WORK_TYPE_CHANGE)){
//				String connect_name=WifiUtils.getInstance().getConnectWifiName();
//				boolean isApDevice=WifiUtils.getInstance().isApDevice(connect_name);
//				if(isApDevice){
//					String deviceId=connect_name.substring(AppConfig.Relese.APTAG.length());
//					LocalDevice device=APList.getInstance().isLocal(deviceId);
//					if(device!=null){
//						device.apModeState=Constants.APmodeState.LINK;
//						Intent it=new Intent();
//						it.setAction(Constants.Action.REFRESH_CONTANTS);
//						mContext.sendBroadcast(it);
//					}
//				}
				if(dialog_connect!=null&&dialog_connect.isShowing()){
					dialog_connect.dismiss();
				}else{
					return;
				}
				if(WifiUtils.getInstance().isConnectWifi(
						wifiName)){
					Intent apMonitor=new Intent(mContext, ApMonitorActivity.class);
					 try {
						 apContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
					 } catch (UnknownHostException e) {
							// TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					apMonitor.putExtra("contact", apContact);
					apMonitor.putExtra("connectType", Constants.ConnectType.RTSPCONNECT);
					startActivity(apMonitor);
				}else{
					reConnectApModeWifi();
				}
				adapter.notifyDataSetChanged();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
//				int state = intent.getIntExtra("state", -1);
//				String contactId = intent.getStringExtra("contactId");
//				LocalDevice device = APList.getInstance().isLocal(contactId);
//
//				if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
//					if (null != device && device.isClickGetDefenceState) {
//						T.showShort(mContext, R.string.net_error);
//					}
//				} else if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
//					if (null != device && device.isClickGetDefenceState) {
//						T.showShort(mContext, R.string.password_error);
//					}
//				}
//
//				if (null != device && device.isClickGetDefenceState) {
//					FList.getInstance().setIsClickGetDefenceState(contactId,
//							false);
//				}
//				pull_refresh_list.onRefreshComplete();
//				adapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constants.P2P.RET_SET_REMOTE_DEFENCE)){
//				APList.getInstance().getDefenceState();
			}
		}
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(br);
		}
	}
	public void reConnectApModeWifi(){
		Intent modify=new Intent();
		modify.setClass(mContext, AddApDeviceActivity.class);
		modify.putExtra("isAPModeConnect", 0);
	    modify.putExtra("contact", apContact);
	    modify.putExtra("ipFlag","1");
	    modify.putExtra("isCreatePassword", false);
	    startActivity(modify);
		T.showShort(mContext, R.string.conn_fail);
	}
	
	public interface ChangeMode{
		void OnChangeMode();
	}
	private ChangeMode Change;
	public void setOnChangeMode(ChangeMode Change){
		this.Change=Change;
	}

}
