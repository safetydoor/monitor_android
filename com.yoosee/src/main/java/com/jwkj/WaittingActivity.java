package com.jwkj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.jwkj.activity.BaseActivity;
import com.jwkj.activity.MainActivity;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.p2p.core.BaseCallActivity;
import com.p2p.core.BaseCoreActivity;
import com.p2p.core.P2PHandler;
import com.yoosee.R;

public class WaittingActivity extends BaseCallActivity {
	private Context mContext;
	boolean isRegFilter = false;
	String password, callId;
	int type;
	String ipFlag;
	boolean isReject = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_waitting);
		mContext = this;
		password = getIntent().getStringExtra("password");
		callId = getIntent().getStringExtra("callId");
		type = getIntent().getIntExtra("type", -1);
		ipFlag = getIntent().getStringExtra("ipFlag");
		String push_mesg = NpcCommon.mThreeNum
				+ ":"
				+ mContext.getResources()
						.getString(R.string.p2p_call_push_mesg);
		P2PHandler.getInstance().call(NpcCommon.mThreeNum, password, true,
				type, "1", ipFlag, push_mesg,AppConfig.VideoMode,callId);
		Log.e("monitor", "password=" + password + "type=" + type + "callId="
				+ callId + "ipFlag=" + ipFlag + "push_mesg=" + push_mesg);
		regFilter();
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.P2P_ACCEPT);
		filter.addAction(Constants.P2P.P2P_READY);
		filter.addAction(Constants.P2P.P2P_REJECT);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
				// P2PHandler.getInstance().openAudioAndStartPlaying();
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
				Intent monitor = new Intent(mContext,
						APdeviceMonitorActivity.class);
				monitor.putExtra("password", password);
				monitor.putExtra("callId", callId);
				monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
				monitor.putExtra("ipFlag", ipFlag);
				startActivity(monitor);
			} else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				reject();
			}
		}
	};

	public void reject() {
		if (!isReject) {
			isReject = true;
			P2PHandler.getInstance().reject();
			finish();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_WAITTING;
	}

	@Override
	protected void onGoBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onGoFront() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onExit() {
		// TODO Auto-generated method stub

	}

}
