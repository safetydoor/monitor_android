package com.jwkj.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.Constants.P2P;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.MusicManger;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyInputPassDialog;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.MyInputPassDialog.OnCustomDialogListener;
import com.lib.imagesee.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.yoosee.R;


public class AlarmWithPictrueActivity extends BaseActivity implements OnClickListener {
	TextView tv_deviceid,tv_alarm_type;
	ImageView iv_alarm_pictrue,iv_alarm_unbund,iv_alarm_check,alarming;
	RelativeLayout iv_close;
	LinearLayout l_area_chanel;
	TextView tv_area,tv_chanel;
	RelativeLayout r_alarm_pictrue;
	TextView tv_load_progress;
	int alarm_id, alarm_type, group, item;
	boolean isSupport;
	private boolean isSupportDelete = false;
	private boolean hasPictrue=false;
	private int imageCounts;
	private String ImagePath="";
	private String time="";
	int contactType;
	private String Image = "";
	private int currentImage = 0;
	String callId,Password;
	private String[] paths = null;
	private String[] LocalPaths = null;
	private boolean isRegFilter = false;
	private ImageLoader imageLoader;
	private Context mContext;
	NormalDialog dialog;
	private boolean isGetProgress = false;
	boolean isAlarm;
	String alarmTime="";
	String alarmPictruePath="";
	private int TIME_OUT = 20 * 1000;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_alarm_with_pictrue);
		mContext=this;
		creatFile();
		initAlarmData(getIntent());
		regFilter();
		excuteTimeOutTimer();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		initAlarmData(intent);
	}
	public void initComponent(){
		getImagePath();
	    tv_deviceid=(TextView)findViewById(R.id.tv_deviceid);
	    iv_close=(RelativeLayout)findViewById(R.id.iv_close);
	    iv_alarm_pictrue=(ImageView)findViewById(R.id.iv_alarm_pictrue);
	    tv_alarm_type=(TextView)findViewById(R.id.tv_alarm_type);
	    iv_alarm_unbund=(ImageView)findViewById(R.id.iv_alarm_unbund);
	    iv_alarm_check=(ImageView)findViewById(R.id.iv_alarm_check);
	    l_area_chanel=(LinearLayout)findViewById(R.id.l_area_chanel);
	    tv_area=(TextView)findViewById(R.id.tv_area);
	    tv_chanel=(TextView)findViewById(R.id.tv_chanel);
	    r_alarm_pictrue=(RelativeLayout)findViewById(R.id.r_alarm_pictrue);
	    tv_load_progress=(TextView)findViewById(R.id.tv_load_progress);
	    alarming=(ImageView)findViewById(R.id.alarming);
	    iv_close.setOnClickListener(this);
	    iv_alarm_check.setOnClickListener(this);
	    iv_alarm_unbund.setOnClickListener(this);
	    imageLoader = ImageTools.getImageLoaderInstance(this);
	    tv_deviceid.setText(String.valueOf(alarm_id));
	    AnimationDrawable animationDrawable=(AnimationDrawable) alarming.getBackground();
	    animationDrawable.start(); 		
	    		
		if(isSupportDelete){
			iv_alarm_unbund.setVisibility(View.VISIBLE);
		}else{
			iv_alarm_unbund.setVisibility(View.GONE);
		}
//		if(hasPictrue==true&&imageCounts>0){
//            r_alarm_pictrue.setVisibility(View.VISIBLE);
//		}else{
//			r_alarm_pictrue.setVisibility(View.GONE);
//		}
		switch (alarm_type) {
		case P2PValue.AlarmType.EXTERNAL_ALARM:
			tv_alarm_type.setText(R.string.allarm_type1);
			if (isSupport) {
				l_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				tv_area.setText(mContext.getResources().getString(
						R.string.area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				tv_chanel.setText(mContext.getResources().getString(
						R.string.channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.MOTION_DECT_ALARM:
			tv_alarm_type.setText(R.string.allarm_type2);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.EMERGENCY_ALARM:
			tv_alarm_type.setText(R.string.allarm_type3);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;

		case P2PValue.AlarmType.LOW_VOL_ALARM:
			tv_alarm_type.setText(R.string.low_voltage_alarm);
			if (isSupport) {
				l_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				tv_area.setText(mContext.getResources().getString(
						R.string.area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				tv_area.setText(mContext.getResources().getString(
						R.string.channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.PIR_ALARM:
			tv_alarm_type.setText(R.string.allarm_type4);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.EXT_LINE_ALARM:
			tv_alarm_type.setText(R.string.allarm_type5);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.DEFENCE:
			tv_alarm_type.setText(R.string.defence);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.NO_DEFENCE:
			tv_alarm_type.setText(R.string.no_defence);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.BATTERY_LOW_ALARM:
			tv_alarm_type.setText(R.string.battery_low_alarm);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH:
			tv_alarm_type.setText(R.string.door_bell);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.RECORD_FAILED_ALARM:
			tv_alarm_type.setText(R.string.record_failed);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		case P2PValue.AlarmType.ALARM_TYPE_DOOR_MAGNET:
			tv_alarm_type.setText(R.string.door_alarm);
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		default:
			tv_alarm_type.setText(String.valueOf(alarm_type));
			l_area_chanel.setVisibility(RelativeLayout.GONE);
			break;
		}
//		if(hasPictrue==true&&imageCounts>0){
//			startdown();
//		}
	    
	
	}
	private void initAlarmData(Intent intent) {
			alarm_id = intent.getIntExtra("alarm_id", 0);
			alarm_type = intent.getIntExtra("alarm_type", 0);
			isSupport = intent.getBooleanExtra("isSupport", false);
			group = intent.getIntExtra("group", 0);
			item = intent.getIntExtra("item", 0);
			isSupportDelete = intent.getBooleanExtra("isSupportDelete", false);
			imageCounts=intent.getIntExtra("imageCounts", 0);
			ImagePath=intent.getStringExtra("picture");
			time=intent.getStringExtra("time");
			hasPictrue=intent.getBooleanExtra("hasPictrue",false);
			alarmTime=intent.getStringExtra("alarmTime");
			paths = new String[imageCounts];
			LocalPaths = new String[imageCounts];
			Log.e("imagepath", "alarm_id="+alarm_id+"--"+"alarm_type="+alarm_type+"--"+"imageCounts="+imageCounts+"--"+"ImagePath="+ImagePath);
			Contact c=FList.getInstance().isContact(String.valueOf(alarm_id));
			if(c!=null){
				contactType=c.contactType;
				Password=c.contactPassword;
				callId=c.contactId;
			}else{
				P2PHandler.getInstance().getFriendStatus(new String[]{String.valueOf(alarm_id)});
			}
			initComponent();
	}
	private void creatFile() {
		Image = Environment.getExternalStorageDirectory().getPath()
				+ "/allarmimage/" + String.valueOf(alarm_id) + "/";
		File destDir = new File(Image);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}

	public void startdown() {
		currentImage = 0;
		getImage();
		isGetProgress=true;
		tv_load_progress.setVisibility(View.VISIBLE);
		new MyThread().start();
	}

	private void getImage() {
		if (currentImage < imageCounts) {
			Log.e("imagepath", String.valueOf(alarm_id)+"--"+"Password="+Password+"--------------"+paths[currentImage]+"---"+LocalPaths[currentImage]);
			alarmPictruePath=LocalPaths[currentImage];
			P2PHandler.getInstance().GetAllarmImage(String.valueOf(alarm_id), Password,
					paths[currentImage], LocalPaths[currentImage]);
		}
	}
	private void getImagePath() {
		for (int i = 0; i <imageCounts ; i++) {
			paths[i] = ImagePath + "0" + (i + 1) + ".jpg";
			LocalPaths[i] = Image + time + (i + 1) + ".jpg";
			Log.e("dxsTest", "path-->" + i + "--" + paths[i]);
			Log.e("dxsTest", "LocalPaths-->" + i + "--" + LocalPaths[i]);
		}
	}
	public void regFilter() {
		isRegFilter = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.RET_GET_ALLARMIMAGE);
		filter.addAction(Constants.P2P.RET_GET_ALLARMIMAGE_PROGRESS);
		filter.addAction(Constants.P2P.DELETE_BINDALARM_ID);
		registerReceiver(br, filter);
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_GET_ALLARMIMAGE)) {
				int errorcode = intent.getIntExtra("errorCode", 8);
				String filename = intent.getStringExtra("filename");
				Log.e("dxsTest", "Uri.parse(filename).toString()------>" + Uri.parse(filename).toString());
				tv_load_progress.setVisibility(View.GONE);
				if (errorcode == 0) {
					isGetProgress=false;
					imageLoader.displayImage("file://"+filename, iv_alarm_pictrue);
//					currentImage++;
					AlarmRecord alarmRecord=DataManager.findAlarmRecordByDeviceIdAndTime(mContext, NpcCommon.mThreeNum, String.valueOf(alarm_id), alarmTime);
				    if(alarmRecord!=null){
				    	alarmRecord.alarmPictruePath=alarmPictruePath;
				    	DataManager.updateAlarmRecord(mContext, alarmRecord);
				    	Intent refresh_alarm_record=new Intent();
				    	refresh_alarm_record.setAction(Constants.Action.REFRESH_ALARM_MESSAGE);
				    	sendBroadcast(refresh_alarm_record);
				    }
				} else {
					isGetProgress=false;
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_ALLARMIMAGE_PROGRESS)) {

			} else if (intent.getAction().equals(
					Constants.P2P.DELETE_BINDALARM_ID)) {
				int result = intent.getIntExtra("deleteResult", 1);
				if (DeleteDialog != null && DeleteDialog.isShowing()) {
					DeleteDialog.dismiss();
				}
				if (result == 0) {
					// 删除成功
					T.showShort(mContext, R.string.modify_success);
				} else if (result == -1) {
					// 不支持
					T.showShort(mContext, R.string.device_not_support);
				} else {
					// 失败
				}
				finish();
			}
		}
	};
	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ALRAM_WITH_PICTRUE;
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.iv_close) {
			viewed = true;
			SharedPreferencesManager.getInstance().putAlarmTimeInterval(mContext, 10);
			int timeInterval = SharedPreferencesManager.getInstance()
					.getAlarmTimeInterval(mContext);
			T.showShort(
					mContext,
					mContext.getResources().getString(
							R.string.ignore_alarm_prompt_start)
							+ " "
							+ timeInterval
							+ " "
							+ mContext.getResources().getString(
									R.string.ignore_alarm_prompt_end));
			finish();
		} else if (id == R.id.iv_alarm_check) {
			viewed = true;
			Contact contact=FList.getInstance().isContact(callId);
			if(contact!=null){
//				Intent monitor = new Intent();
//				monitor.setClass(mContext, CallActivity.class);
//				monitor.putExtra("callId", callId);
//				monitor.putExtra("password", Password);
//				monitor.putExtra("isOutCall", true);
//				monitor.putExtra("contactType",contactType);
//				monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
//				startActivity(monitor);
				Intent monitor=new Intent(mContext,ApMonitorActivity.class);
				monitor.putExtra("contact", contact);
				monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
				startActivity(monitor);
				finish();
			}else{
				createPassDialog(String.valueOf(alarm_id));
			}
		} else if (id == R.id.iv_alarm_unbund) {
			viewed = true;
			DeleteDevice();
		} else {
		}
	}
	private Dialog passworddialog;
	void createPassDialog(String id) {
		passworddialog = new MyInputPassDialog(mContext,
				Utils.getStringByResouceID(R.string.check), id, listener);
		passworddialog.show();
	}
	private OnCustomDialogListener listener = new OnCustomDialogListener() {

		@Override
		public void check(final String password, final String id) {
			if (password.trim().equals("")) {
				T.showShort(mContext, R.string.input_monitor_pwd);
				return;
			}
			if (password.length() > 30||password.charAt(0)=='0') {
				T.showShort(mContext,R.string.device_password_invalid);
				return;
			}

			P2PConnect.vReject("");
			new Thread() {
				@Override
				public void run() {
					while (true) {
						if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
							Message msg = new Message();
							String pwd=P2PHandler.getInstance().EntryPassword(password);
							String[] data = new String[] {id, pwd,
									String.valueOf(P2PValue.DeviceType.IPC) };
							msg.obj = data;
							handler.sendMessage(msg);
							break;
						}
						Utils.sleepThread(500);
					}
				}
			}.start();

		}
	};
	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			String[] data = (String[]) msg.obj;
//			Intent monitor = new Intent();
//			monitor.setClass(mContext, CallActivity.class);
//			monitor.putExtra("callId", data[0]);
//			monitor.putExtra("password", data[1]);
//			monitor.putExtra("isOutCall", true);
//			monitor.putExtra("contactType", Integer.parseInt(data[2]));
//			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
//			if (Integer.parseInt(data[2]) == P2PValue.DeviceType.DOORBELL) {
//				monitor.putExtra("isSurpportOpenDoor", true);
//			}
			Contact contact=new Contact();
			contact.contactId=data[0];
			contact.contactName=data[0];
			contact.contactPassword=data[1];
			Intent monitor=new Intent(mContext,ApMonitorActivity.class);
			monitor.putExtra("contact", contact);
			monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
			startActivity(monitor);
			finish();
			return false;
		}
	});
	private void DeleteDevice() {
		dialog = new NormalDialog(mContext, mContext.getResources().getString(
				R.string.clear_bundealarmid), mContext.getResources()
				.getString(R.string.clear_bundealarmid_tips), mContext
				.getResources().getString(R.string.ensure), mContext
				.getResources().getString(R.string.cancel));
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				P2PHandler.getInstance().DeleteDeviceAlarmId(
						String.valueOf(alarm_id));
				dialog.dismiss();
				ShowLoading();
			}
		});
		dialog.showDialog();
	}
	private NormalDialog DeleteDialog;
	private void ShowLoading() {
		DeleteDialog = new NormalDialog(mContext);
		DeleteDialog.showLoadingDialog();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadMusicAndVibrate();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MusicManger.getInstance().stop();
	}
	public void loadMusicAndVibrate() {
		isAlarm = true;
		int a_muteState = SharedPreferencesManager.getInstance().getAMuteState(
				MyApp.app);
		if (a_muteState == 1) {
			MusicManger.getInstance().playAlarmMusic();
		}

		int a_vibrateState = SharedPreferencesManager.getInstance()
				.getAVibrateState(MyApp.app);
		if (a_vibrateState == 1) {
			new Thread() {
				public void run() {
					while (isAlarm) {
						MusicManger.getInstance().Vibrate();
						Utils.sleepThread(100);
					}
					MusicManger.getInstance().stopVibrate();

				}
			}.start();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(isRegFilter==true){
			unregisterReceiver(br);
			isRegFilter=false;
		}
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
				System.currentTimeMillis());
		isAlarm = false;
		P2PConnect.vEndAllarm();
	}
	class MyThread extends Thread {
		int progress = 0;
		public void run() {
			isGetProgress = true;
			while (isGetProgress) {
				progress = P2PHandler.getInstance().GetAllarmImageProgress();
				myHandler.sendEmptyMessage(progress);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private Handler myHandler=new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int  progress=msg.what;
			tv_load_progress.setText(String.valueOf(progress)+"%");
			return false;
		}
	});
	// 超时计时器
	boolean viewed = false;
	Timer timeOutTimer;
	public void excuteTimeOutTimer() {

			timeOutTimer = new Timer();
			TimerTask mTask = new TimerTask() {
				@Override
				public void run() {
					// 弹出一个对话框
					if (!viewed) {
						Message message = new Message();
						message.what = USER_HASNOTVIEWED;
						mHandler.sendMessage(message);
					}
				}
			};
			timeOutTimer.schedule(mTask, TIME_OUT);

		}
	public static final int USER_HASNOTVIEWED = 3;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case USER_HASNOTVIEWED:
				SharedPreferencesManager.getInstance().putAlarmTimeInterval(
						mContext, 1);
				finish();
				break;
			default:
				break;
			}
			return false;

		}
	});

}
