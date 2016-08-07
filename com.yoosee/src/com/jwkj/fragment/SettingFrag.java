package com.jwkj.fragment;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.AccountInfoActivity;
import com.jwkj.activity.AlarmSetActivity;
import com.jwkj.activity.ImageBrowser;
import com.jwkj.activity.MainActivity;
import com.jwkj.activity.QRcodeActivity;
import com.jwkj.activity.SettingSystemActivity;
import com.jwkj.activity.SysMsgActivity;
import com.jwkj.data.DataManager;
import com.jwkj.data.SysMessage;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.thread.MainThread;
import com.jwkj.thread.UpdateCheckVersionThread;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.utils.MyUtils;

public class SettingFrag extends BaseFragment implements OnClickListener {
	private Context mContext;
	private RelativeLayout mCheckUpdateTextView = null;
	private RelativeLayout mLogOut, mExit, center_about, account_set, sys_set,
			sys_msg, qr_code_set_wifi, alarm_set;
	private TextView mName;
	boolean isRegFilter = false;
	// update add
	private Handler handler;
	private boolean isCancelCheck = false;
	private NormalDialog dialog;
	ImageView sysMsg_notify_img, network_type;
	boolean isLogin=true;

	// end
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		Log.e("my", "createSettingFrag");
		mContext = MainActivity.mContext;
		initComponent(view);
		regFilter();
		updateSysMsg();
		return view;
	}

	public void initComponent(View view) {
		mCheckUpdateTextView = (RelativeLayout) view
				.findViewById(R.id.center_t);
		mName = (TextView) view.findViewById(R.id.mailAdr);
		mLogOut = (RelativeLayout) view.findViewById(R.id.logout_layout);
		account_set = (RelativeLayout) view.findViewById(R.id.account_set);
		sys_set = (RelativeLayout) view.findViewById(R.id.system_set);
		mExit = (RelativeLayout) view.findViewById(R.id.exit_layout);
		center_about = (RelativeLayout) view.findViewById(R.id.center_about);
		sys_msg = (RelativeLayout) view.findViewById(R.id.system_message);
		qr_code_set_wifi = (RelativeLayout) view
				.findViewById(R.id.qr_code_layout);
		alarm_set = (RelativeLayout) view.findViewById(R.id.alarm_set_btn);

		sysMsg_notify_img = (ImageView) view
				.findViewById(R.id.sysMsg_notify_img);

		network_type = (ImageView) view.findViewById(R.id.network_type);
		if (NpcCommon.mThreeNum.equals("0517401")) {
			mName.setText(R.string.no_login);
			isLogin=false;
		} else {
			mName.setText(String.valueOf(NpcCommon.mThreeNum));
		}
		if (NpcCommon.mNetWorkType == NpcCommon.NETWORK_TYPE.NETWORK_WIFI) {
			network_type.setImageResource(R.drawable.wifi);
		} else {
			network_type.setImageResource(R.drawable.net_3g);
		}

		mLogOut.setOnClickListener(this);
		account_set.setOnClickListener(this);
		sys_msg.setOnClickListener(this);
		mExit.setOnClickListener(this);
		sys_set.setOnClickListener(this);
		center_about.setOnClickListener(this);
		mCheckUpdateTextView.setOnClickListener(this);
		qr_code_set_wifi.setOnClickListener(this);
		alarm_set.setOnClickListener(this);
		mName.setOnClickListener(this);
		handler = new MyHandler();
		if (NpcCommon.mThreeNum.equals("0517401")) {
			account_set.setVisibility(RelativeLayout.GONE);
			sys_set.setBackgroundResource(R.drawable.tiao_bg_up);
		}
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.RECEIVE_SYS_MSG);
		filter.addAction(Constants.Action.NET_WORK_TYPE_CHANGE);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.Action.RECEIVE_SYS_MSG)) {
				updateSysMsg();
			} else if (intent.getAction().equals(
					Constants.Action.NET_WORK_TYPE_CHANGE)) {
				if (NpcCommon.mNetWorkType == NpcCommon.NETWORK_TYPE.NETWORK_WIFI) {
					network_type.setImageResource(R.drawable.wifi);
				} else {
					network_type.setImageResource(R.drawable.net_3g);
				}
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		String wifiName=WifiUtils.getInstance().getConnectWifiName();
		if(wifiName.charAt(0)=='"'){
			wifiName=wifiName.substring(1, wifiName.length()-1);
		}
		if(wifiName.startsWith(AppConfig.Relese.APTAG)){
			mLogOut.setVisibility(View.GONE);
			mExit.setVisibility(View.GONE);
		}else{
			mLogOut.setVisibility(View.VISIBLE);
			mExit.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.logout_layout) {
			Intent canel = new Intent();
			canel.setAction(Constants.Action.ACTION_SWITCH_USER);
			mContext.sendBroadcast(canel);
		} else if (id == R.id.exit_layout) {
			Intent exit = new Intent();
			exit.setAction(Constants.Action.ACTION_EXIT);
			exit.putExtra("isLogin", isLogin);
			mContext.sendBroadcast(exit);
		} else if (id == R.id.center_t) {
			if (null != dialog && dialog.isShowing()) {
				Log.e("my", "isShowing");
				return;
			}
			dialog = new NormalDialog(mContext);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					isCancelCheck = true;
				}

			});
			dialog.setTitle(mContext.getResources().getString(
					R.string.check_update));
			dialog.showLoadingDialog();
			dialog.setCanceledOnTouchOutside(false);
			isCancelCheck = false;
			new UpdateCheckVersionThread(handler).start();
		} else if (id == R.id.center_about) {
			NormalDialog about = new NormalDialog(mContext);
			about.showAboutDialog();
		} else if (id == R.id.account_set) {
			Intent goAccount_set = new Intent();
			goAccount_set.setClass(mContext, AccountInfoActivity.class);
			startActivity(goAccount_set);
		} else if (id == R.id.system_set) {
			Intent goSys_set = new Intent();
			goSys_set.setClass(mContext, SettingSystemActivity.class);
			startActivity(goSys_set);
		} else if (id == R.id.system_message) {
			Intent goSysMsg = new Intent(mContext, SysMsgActivity.class);
			startActivity(goSysMsg);
		} else if (id == R.id.qr_code_layout) {
			Intent go_qr = new Intent(mContext, QRcodeActivity.class);
			mContext.startActivity(go_qr);
		} else if (id == R.id.alarm_set_btn) {
			Intent go_alarm_set = new Intent(mContext, AlarmSetActivity.class);
			startActivity(go_alarm_set);
		} else if (id == R.id.mailAdr) {
		} else {
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.Update.CHECK_UPDATE_HANDLE_FALSE:
				Log.e("my", "diss");
				if (null != dialog) {
					Log.e("my", "diss ok");
					dialog.dismiss();
					dialog = null;
				}
				if (isCancelCheck) {
					return;
				}

				dialog = new NormalDialog(mContext, mContext.getResources()
						.getString(R.string.update_prompt_title),
						Utils.getFormatString(R.string.update_check_false,
								MyUtils.getVersion()), "", "");
				dialog.setStyle(NormalDialog.DIALOG_STYLE_PROMPT);
				dialog.showDialog();
				break;
			case Constants.Update.CHECK_UPDATE_HANDLE_TRUE:
				if (null != dialog) {
					dialog.dismiss();
					dialog = null;
				}
				if (isCancelCheck) {
					return;
				}
				Intent i = new Intent(Constants.Action.ACTION_UPDATE);
				i.putExtra("updateDescription", (String) msg.obj);
				mContext.sendBroadcast(i);
				break;
			}
		}
	}

	public void updateSysMsg() {
		List<SysMessage> sysmessages = DataManager.findSysMessageByActiveUser(
				mContext, NpcCommon.mThreeNum);
		boolean isNewSysMsg = false;
		for (SysMessage msg : sysmessages) {
			if (msg.msgState == SysMessage.MESSAGE_STATE_NO_READ) {
				isNewSysMsg = true;
			}
		}

		if (isNewSysMsg) {
			sysMsg_notify_img.setVisibility(RelativeLayout.VISIBLE);
		} else {
			sysMsg_notify_img.setVisibility(RelativeLayout.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			isRegFilter = false;
			mContext.unregisterReceiver(mReceiver);
		}
	}
}
