package com.jwkj;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;
import com.yoosee.R;

public class APdeviceMonitorActivity extends BaseMonitorActivity {
	private Context mContext;
	boolean isRegFilter = false;
	String password, callId;
	int type;
	String ipFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor_apdevice);
		mContext = this;
		WifiUtils.getInstance().wifiLock();
		String push_mesg = NpcCommon.mThreeNum
				+ ":"
				+ mContext.getResources()
						.getString(R.string.p2p_call_push_mesg);
		password = getIntent().getStringExtra("password");
		callId = getIntent().getStringExtra("callId");
		type = getIntent().getIntExtra("type", -1);
		ipFlag = getIntent().getStringExtra("ipFlag");
		// P2PHandler.getInstance().call(NpcCommon.mThreeNum, password,
		// true, type, callId, ipFlag, push_mesg);
		// Log.e("monitor", "password="+password+"type="+type
		// +"callId="+ callId+"ipFlag="+ipFlag+"push_mesg="+push_mesg);
		initUI();
		// regFilter();
	}

	public void initUI() {
		pView = (P2PView) findViewById(R.id.pView);
		this.initP2PView(P2PValue.DeviceType.IPC);
		setMute(true);
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
				P2PHandler.getInstance().openAudioAndStartPlaying(3);
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {

			} else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				P2PHandler.getInstance().reject();
			}
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		WifiUtils.getInstance().wifiUnlock();
		if (isRegFilter) {
			unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	protected void onP2PViewSingleTap() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			// Capture success
			T.showShort(mContext, R.string.capture_success);
			List<String> pictrues=Utils.getScreenShotImagePath(callId, 1);
			if(pictrues.size()<=0){
				return;
			}
            Utils.saveImgToGallery(pictrues.get(0));
		} else {
			T.showShort(mContext, R.string.capture_failed);
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_APDEVICEMONITORACTIVITY;
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
