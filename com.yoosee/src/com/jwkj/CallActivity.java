package com.jwkj;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.MainActivity;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.NearlyTell;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.PhoneWatcher;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;
import com.p2p.core.BaseCallActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

public class CallActivity extends BaseCallActivity implements OnClickListener {
	// test svn
	Context mContext;
	public final static int P2PCONECT = 0;
	public final static int RTSPCONNECT = 1;
	PhoneWatcher mPhoneWatcher;
	TextView top_text, reject_text, title_text;
	RelativeLayout accept, reject, layout_accept;
	boolean isOutCall;
	ImageView call_anim;
	HeaderView header_img;
	String callId;
	String contactName;
	String ipFlag;
	int type;
	int contactType;
	String password;
	boolean isRegFilter = false;

	boolean isAccept = false;
	boolean isReject = false;
	boolean isSurpportOpenDoor = false;

	private int connectType = 0;
	private String ipAddress;
	int callType;
    private Contact contact;
    String push_mesg;
    String idOrIp;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		mContext = this;
		setContentView(R.layout.activity_call);

		isOutCall = this.getIntent().getBooleanExtra("isOutCall", false);
		callId = this.getIntent().getStringExtra("callId");
		contactName = this.getIntent().getStringExtra("contactName");
		ipFlag = this.getIntent().getStringExtra("ipFlag");
		type = this.getIntent().getIntExtra("type", -1);
		password = this.getIntent().getStringExtra("password");
		contactType=this.getIntent().getIntExtra("contactType",-1);
		contact=(Contact) this.getIntent().getSerializableExtra("contact");
		connectType = getIntent().getIntExtra("connectType", 0);
		ipAddress = getIntent().getStringExtra("ipAddress");
		isSurpportOpenDoor = this.getIntent().getBooleanExtra(
				"isSurpportOpenDoor", false);
		idOrIp=callId;
		if(contact!=null){
			if(contact.ipadressAddress!=null){
				String mark=contact.ipadressAddress.getHostAddress();
				Log.e("callId", "mark=" + mark);
				String ip=mark.substring(mark.lastIndexOf(".")+1,mark.length());
				if(!ip.equals("")&&ip!=null){
					idOrIp=ip;
				}	
			}
		}
		Log.e("callId", "idOrIp="+idOrIp);
		if(contactType==P2PValue.DeviceType.NPC){
			connectType=P2PCONECT;
		}
		if (!Utils.hasDigit(callId)) {
			if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
				T.showShort(mContext, R.string.monitor_id_must_include_digit);
			} else {
				T.showShort(mContext, R.string.call_id_must_include_digit);
			}

			finish();
		} else {
			P2PConnect.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
			P2PConnect.setCurrent_call_id(callId);
			initComponent();
			regFilter();
			startWatcher();

			push_mesg = NpcCommon.mThreeNum
					+ ":"
					+ mContext.getResources().getString(
							R.string.p2p_call_push_mesg);
			 connectType= P2PCONECT;
			 if (connectType == P2PCONECT) {
				    callType=1;
		            P2PHandler.getInstance().call(NpcCommon.mThreeNum, password, isOutCall, type,idOrIp, ipFlag, push_mesg,AppConfig.VideoMode,callId);
		     } else if (connectType == RTSPCONNECT) {
		    	    callType=3;
		    	  	P2PHandler.getInstance().checkPassword(idOrIp, password);
		      }
			Log.e("monitor", "password="+password+"isOutCall="+isOutCall+"type="+type
					+"callId="+ callId+"ipFlag="+ipFlag+"push_mesg="+push_mesg+"ipAddress="+ipAddress);
		}
	}

	private Handler rtspHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.e("dxswifi", "rtsp失败");
				T.showShort(mContext, R.string.conn_fail);
				reject();
				break;
			case 1:
				Log.e("dxswifi", "rtsp成功");
				P2PConnect.setCurrent_state(2);
				playReady();
				break;
			}
		}
	};

	private void playReady() {
		P2PHandler.getInstance().openAudioAndStartPlaying(callType);
		Intent ready = new Intent();
		ready.setAction(Constants.P2P.P2P_READY);
		this.sendBroadcast(ready);
	}

	@Override
	public void onHomePressed() {
		// TODO Auto-generated method stub
		super.onHomePressed();
		reject();
	}

	private void startWatcher() {
		mPhoneWatcher = new PhoneWatcher(mContext);
		mPhoneWatcher
				.setOnCommingCallListener(new PhoneWatcher.OnCommingCallListener() {

					@Override
					public void onCommingCall() {
						// TODO Auto-generated method stub
						reject();
					}

				});
		mPhoneWatcher.startWatcher();
	}

	public void initComponent() {
		top_text = (TextView) findViewById(R.id.top_text);
		accept = (RelativeLayout) findViewById(R.id.accept);
		layout_accept = (RelativeLayout) findViewById(R.id.layout_accept);
		reject = (RelativeLayout) findViewById(R.id.reject);
		reject_text = (TextView) findViewById(R.id.reject_text);
		title_text = (TextView) findViewById(R.id.title_text);
		call_anim = (ImageView) findViewById(R.id.call_anim);
		header_img = (HeaderView) findViewById(R.id.header_img);
		header_img.updateImage(callId, false);
		if (isOutCall) {
			reject_text.setText(R.string.hungup);
			layout_accept.setVisibility(RelativeLayout.GONE);
			if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
				top_text.setText(mContext.getResources().getString(
						R.string.connecting_to)
						+ "......");
				if (contactName != null && !contactName.equals("")) {
					title_text.setText(contactName);
				} else {
					title_text.setText(callId);
				}
				call_anim.setImageResource(R.anim.monitor);
			} else {
				if (contactName != null && !contactName.equals("")) {
					title_text.setText(contactName);
				} else {
					title_text.setText(callId);
				}
				call_anim.setImageResource(R.anim.call_out);
				top_text.setText(mContext.getResources().getString(
						R.string.calling_to)
						+ "......");
			}

		} else {
			call_anim.setImageResource(R.anim.call_in);
			reject_text.setText(R.string.reject);
			layout_accept.setVisibility(RelativeLayout.VISIBLE);
			// top_text.setText(mContext.getResources().getString(
			// R.string.calling_from)
			// + " " + callId);

			Contact contact = FList.getInstance().isContact(callId);
			if (contact == null) {
				title_text.setText(callId);
			} else {
				title_text.setText(contact.contactName);
			}

		}

		final AnimationDrawable anim = (AnimationDrawable) call_anim
				.getDrawable();
		// anim.start();
		OnPreDrawListener opdl = new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				anim.start();
				return true;
			}

		};
		call_anim.getViewTreeObserver().addOnPreDrawListener(opdl);
		accept.setOnClickListener(this);
		reject.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.P2P_ACCEPT);
		filter.addAction(Constants.P2P.P2P_READY);
		filter.addAction(Constants.P2P.P2P_REJECT);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
				int[] type = intent.getIntArrayExtra("type");
				P2PView.type = type[0];
				P2PView.scale = type[1];
				P2PHandler.getInstance().openAudioAndStartPlaying(callType);
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
				Intent intentCall = new Intent();
				if (type == Constants.P2P_TYPE.P2P_TYPE_CALL) {
					intentCall.setClass(mContext, VideoActivity.class);
				} else if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
					if (connectType == P2PCONECT) {
						intentCall.setClass(mContext, MonitorActivity.class);
					} else if (connectType == RTSPCONNECT) {
						intentCall.setClass(mContext, MonitorActivity.class);
					}
					intentCall.putExtra("contactType", contactType);
					intentCall.putExtra("callId", callId);
					intentCall.putExtra("connectType", connectType);
					intentCall.putExtra("password", password);
					intentCall.putExtra("isSurpportOpenDoor",
							isSurpportOpenDoor);
					intentCall.putExtra("contact", contact);
				}
				intentCall.putExtra("type", type);
				intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intentCall);
				finish();
			} else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				reject();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				reject();
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
					if(connectType == RTSPCONNECT){
						P2PHandler.getInstance().RTSPConnect(NpcCommon.mThreeNum, password, isOutCall, 3, callId, ipFlag, push_mesg, ipAddress,AppConfig.VideoMode, rtspHandler,callId);	
					}
				}else if(result ==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
			         T.showShort(mContext, R.string.password_error);
                     finish();
				}else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					P2PHandler.getInstance().checkPassword(idOrIp, password);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
					
				}

			}
		}
	};

	@Override
	public void onBackPressed() {
		reject();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.accept) {
			if (!isAccept) {
				isAccept = true;
				P2PHandler.getInstance().accept();
			}
		} else if (id == R.id.reject) {
			reject();
		}
	}

	public void reject() {
		if (!isReject) {
			isReject = true;
			P2PHandler.getInstance().reject();
			if (!activity_stack
					.containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
				Intent i = new Intent(CallActivity.this, MainActivity.class);
				startActivity(i);
			}
			finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}

		if (null != mPhoneWatcher) {
			mPhoneWatcher.stopWatcher();
		}
		insertNearly();
	}

	public void insertNearly() {
		NearlyTell nearlyTell = new NearlyTell();
		nearlyTell.activeUser = NpcCommon.mThreeNum;
		nearlyTell.tellId = callId;
		nearlyTell.tellTime = String.valueOf(System.currentTimeMillis());
		nearlyTell.tellState = type;
		if (isOutCall && isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_OUT_REJECT;
		} else if (isOutCall && !isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_OUT_ACCEPT;
		} else if (!isOutCall && isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_IN_REJECT;
		} else {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_IN_ACCEPT;
		}
		DataManager.insertNearlyTell(mContext, nearlyTell);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_CALLACTIVITY;
	}

	@Override
	protected void onGoBack() {
		// TODO Auto-generated method stub
		MyApp.app.showNotification();
	}

	@Override
	protected void onGoFront() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

	@Override
	protected void onExit() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

}
