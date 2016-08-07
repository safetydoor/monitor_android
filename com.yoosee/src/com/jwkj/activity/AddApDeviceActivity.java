package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.fragment.AddAPDeviceFrag;
import com.jwkj.fragment.APModeFrag;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.WifiUtils;
import com.jwkj.data.APContact;
import com.p2p.core.P2PValue;
import com.yoosee.R;

public class AddApDeviceActivity extends BaseActivity implements
		OnClickListener {
	private TextView txTitle;
	private ImageView ivBack;
	private AddAPDeviceFrag Addfragment;
	private APModeFrag APModeFrag;
	private Contact APContact;
	private boolean isRegFilter = false;
	private Context mContext;
	private APContact DBContact;
	private int isAPModeConnect = 0;// 是否已确定为AP设备，默认否
	boolean islogin=true;

	// public static AddApDeviceActivity instance = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_add_ap_contact);
		// instance=this;
		// if(arg0==null){
		APContact = (Contact) getIntent().getSerializableExtra("contact");
		isAPModeConnect = getIntent().getIntExtra("isAPModeConnect", 0);
		// }else{
		// APContact=(Contact) arg0.getSerializable("contact");
		// isAPModeConnect=arg0.getInt("isAPModeConnect", 0);
		// }
		islogin=getIntent().getBooleanExtra("islogin", true);
		mContext = this;
		initUI();
		regFilter();
		initFrag(isAPModeConnect);
	}

	private void initFrag(int isAPModeConnect) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("contact", APContact);
		bundle.putBoolean("islogin", islogin);
		if (isAPModeConnect == 0) {
			replaceAddFrag(bundle);
		} else {
			replaceAPModeFrag(bundle);
		}
	}

	void replaceAddFrag(Bundle bundle) {
		if (Addfragment == null) {
			Addfragment = new AddAPDeviceFrag();
			Addfragment.setArguments(bundle);
		}
		if(APContact!=null){
			txTitle.setText(APContact.contactName);
		}
		replaceFragment(R.id.fl_addapdevice, Addfragment, "Addfragment");
	}

	void replaceAPModeFrag(Bundle bundle) {
		if (APModeFrag == null) {
			APModeFrag = new APModeFrag();
			APModeFrag.setArguments(bundle);
		}
		txTitle.setText(R.string.ap_mode);
		replaceFragment(R.id.fl_addapdevice, APModeFrag, "APModeFrag");
	}

	private void initUI() {
		txTitle = (TextView) findViewById(R.id.tx_addtitle);
		ivBack = (ImageView) findViewById(R.id.back_btn);
		ivBack.setOnClickListener(this);
		DBContact = DataManager.findAPContactByActiveUserAndContactId(mContext,
				NpcCommon.mThreeNum, APContact.contactId);
		if (DBContact != null) {
			APContact.contactPassword = DBContact.Pwd;
		} else {
			Log.e("dxsDatamaneger", "DBContact==NULL");
		}
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.SEARCH_AP_APMODE);
		filter.addAction(Constants.Action.SEARCH_AP_ADDAPDEVICE);
		filter.addAction(Constants.Action.SEARCH_AP_QUITEAPDEVICE);
		this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.Action.SEARCH_AP_APMODE)) {
				String pwd = intent.getStringExtra("pwd");
				APContact.contactPassword = pwd;
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("contact", APContact);
//				replaceAPModeFrag(bundle);
//				Log.e("dxspppwwwddd", "bundle-------------" + pwd);
//				Contact contact=new Contact();
//				contact.contactId=APContact.contactId;
//				contact.contactName=APContact.contactName;
//				contact.contactType=P2PValue.DeviceType.IPC;
//				contact.wifiPassword=pwd;
//				contact.mode=P2PValue.DeviceMode.AP_MODE;
//				contact.activeUser=NpcCommon.mThreeNum;
//				contact.messageCount=0;
//				contact.contactPassword="0";
//				contact.ipadressAddress=APContact.ipadressAddress;
//				contact.apModeState=Constants.APmodeState.LINK;
//				Contact saveContact=FList.getInstance().isContact(APContact.contactId);
//			    if(saveContact==null&&islogin==true){
//			    	FList.getInstance().insert(contact);		
//			    }else if(saveContact!=null&&islogin==true){
//			    	saveContact.wifiPassword=pwd;
//			    	FList.getInstance().update(saveContact);
//			    }
//				FList.getInstance().updateLocalDeviceWithLocalFriends();
//				FList.getInstance().updateApModelist();
//				Intent refreshContans = new Intent();
//				refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
//				refreshContans.putExtra("contact", contact);
//				mContext.sendBroadcast(refreshContans);
				Intent it=new Intent(mContext,ApMonitorActivity.class);
				it.putExtra("contact", APContact);
				it.putExtra("connectType", Constants.ConnectType.RTSPCONNECT);
				startActivity(it);
				finish();
			} else if (intent.getAction().equals(
					Constants.Action.SEARCH_AP_ADDAPDEVICE)) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("contact", APContact);
				bundle.putBoolean("islogin", islogin);
				replaceAddFrag(bundle);
			} else if (intent.getAction().equals(
					Constants.Action.SEARCH_AP_QUITEAPDEVICE)) {
				abortAPMode();
			}
		}
	};

	public void setTitle(String title) {
		txTitle.setText(title);
	}

	public void replaceFragment(int container, Fragment fragment, String tag) {
		try {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			// transaction.setCustomAnimations(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			transaction.replace(container, fragment, tag);
			// transaction.add(container, fragment, tag);
			// transaction.addToBackStack(tag);
			transaction.commit();
			manager.executePendingTransactions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ADDAPDEVICEACTIVITY;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			// 销毁页面同时退出AP模式
			abortAPMode();
		} else {
		}

	}

	private void abortAPMode() {
		// 忘记当前连接的网络
//		WifiUtils.getInstance().disConnectWifi(APContact.contactName);
		finish();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("contact", APContact);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			isRegFilter = false;
			this.unregisterReceiver(mReceiver);
		}
	}

}
