package com.jwkj.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.yoosee.R;
import com.jwkj.data.AlarmMask;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.MusicManger;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class AlarmActivity extends Activity implements OnClickListener {
	private static final String TAG = "dxsAlarmActivity";
	Context mContext;
	TextView monitor_btn, ignore_btn, shield_btn;
	TextView alarm_id_text, alarm_type_text;
	ImageView alarm_img;
	int alarm_id, alarm_type, group, item;
	boolean isSupport;
	LinearLayout layout_area_chanel;
	TextView area_text, chanel_text;
	LinearLayout alarm_input, alarm_dialog;
	TextView alarm_go;
	EditText mPassword;
	boolean isAlarm;
	boolean hasContact = false;
	NormalDialog dialog;
	boolean isRegFilter = false;
	TextView tv_info, tv_type;
	private boolean isSupportDelete = false;

	private int TIME_OUT = 20 * 1000;
	int contactType;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (P2PConnect.isPlaying()) {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}

		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		setContentView(R.layout.activity_alarm);
		initAlarmData(getIntent());
		excuteTimeOutTimer();
		regFilter();
		// insertAlarmRecord();
		Log.e(TAG, "onNewIntent"); 
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		initAlarmData(intent);
		Log.e(TAG, "onNewIntent");
	}

	private void initAlarmData(Intent intent) {
		alarm_id = intent.getIntExtra("alarm_id", 0);
		alarm_type = intent.getIntExtra("alarm_type", 0);
		isSupport = intent.getBooleanExtra("isSupport", false);
		group = intent.getIntExtra("group", 0);
		item = intent.getIntExtra("item", 0);
		isSupportDelete = intent.getBooleanExtra("isSupportDelete", false);
		Contact c=FList.getInstance().isContact(String.valueOf(alarm_id));
		if(c!=null){
			contactType=c.contactType;
		}else{
			P2PHandler.getInstance().getFriendStatus(new String[]{String.valueOf(alarm_id)});
		}
		initComponent();
	}

	public void insertAlarmRecord() {
		AlarmRecord alarmRecord = new AlarmRecord();
		alarmRecord.alarmTime = String.valueOf(System.currentTimeMillis());
		alarmRecord.deviceId = String.valueOf(alarm_id);
		alarmRecord.alarmType = alarm_type;
		alarmRecord.activeUser = NpcCommon.mThreeNum;
		if ((alarm_type == P2PValue.AlarmType.EXTERNAL_ALARM || alarm_type == P2PValue.AlarmType.LOW_VOL_ALARM)
				&& isSupport) {
			alarmRecord.group = group;
			alarmRecord.item = item;
		} else {
			alarmRecord.group = -1;
			alarmRecord.item = -1;
		}
		DataManager.insertAlarmRecord(mContext, alarmRecord);
		Intent i = new Intent();
		i.setAction(Constants.Action.REFRESH_ALARM_RECORD);
		mContext.sendBroadcast(i);
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

	public void initComponent() {
		monitor_btn = (TextView) findViewById(R.id.monitor_btn);
		ignore_btn = (TextView) findViewById(R.id.ignore_btn);
		shield_btn = (TextView) findViewById(R.id.shield_btn);
		if (isSupportDelete) {
			shield_btn.setText(R.string.clear_bundealarmid);
			shield_btn.setVisibility(View.VISIBLE);
		} else {
			shield_btn.setText(R.string.shielded);
			shield_btn.setVisibility(View.GONE);
		}
		alarm_id_text = (TextView) findViewById(R.id.alarm_id_text);
		alarm_type_text = (TextView) findViewById(R.id.alarm_type_text);
		alarm_go = (TextView) findViewById(R.id.alarm_go);
		tv_info = (TextView) findViewById(R.id.tv_info);
		tv_type = (TextView) findViewById(R.id.tv_type);
		alarm_go.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					alarm_go.setTextColor(mContext.getResources().getColor(
							R.color.text_color_white));
					break;
				case MotionEvent.ACTION_UP:
					alarm_go.setTextColor(mContext.getResources().getColor(
							R.color.text_color_gray));
					break;
				}
				return false;
			}

		});
		alarm_input = (LinearLayout) findViewById(R.id.alarm_input);
		alarm_img = (ImageView) findViewById(R.id.alarm_img);
		mPassword = (EditText) findViewById(R.id.password);
		mPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		final AnimationDrawable anim = (AnimationDrawable) alarm_img
				.getDrawable();
		OnPreDrawListener opdl = new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				anim.start();
				return true;
			}

		};
		alarm_img.getViewTreeObserver().addOnPreDrawListener(opdl);
		alarm_dialog = (LinearLayout) findViewById(R.id.alarm_dialog);
		alarm_dialog.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_right));
		alarm_id_text.setText(String.valueOf(alarm_id));

		layout_area_chanel = (LinearLayout) findViewById(R.id.layout_area_chanel);
		area_text = (TextView) findViewById(R.id.area_text);
		chanel_text = (TextView) findViewById(R.id.chanel_text);
		Log.e("isSupport", "isSupport=" + isSupport);
		if (isSupport) {
			layout_area_chanel.setVisibility(RelativeLayout.VISIBLE);
		} else {
			layout_area_chanel.setVisibility(RelativeLayout.GONE);
		}

		switch (alarm_type) {
		case P2PValue.AlarmType.EXTERNAL_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.allarm_type1);
			tv_type.setText(R.string.allarm_type);
			if (isSupport) {
				layout_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				area_text.setText(mContext.getResources().getString(
						R.string.area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				chanel_text.setText(mContext.getResources().getString(
						R.string.channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.MOTION_DECT_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.allarm_type2);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.EMERGENCY_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.allarm_type3);
			tv_type.setText(R.string.allarm_type);
			break;

		case P2PValue.AlarmType.LOW_VOL_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.low_voltage_alarm);
			tv_type.setText(R.string.allarm_type);
			if (isSupport) {
				layout_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				area_text.setText(mContext.getResources().getString(
						R.string.area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				chanel_text.setText(mContext.getResources().getString(
						R.string.channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.PIR_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.allarm_type4);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.EXT_LINE_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.allarm_type5);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.DEFENCE:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.defence);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.NO_DEFENCE:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.no_defence);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.BATTERY_LOW_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.battery_low_alarm);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH:
			alarm_type_text.setText(R.string.door_bell);
			tv_info.setText(R.string.visitor_messge);
			tv_type.setText(R.string.allarm_type);
			break;
		case P2PValue.AlarmType.RECORD_FAILED_ALARM:
			tv_info.setText(R.string.alarm_info);
			alarm_type_text.setText(R.string.record_failed);
			tv_type.setText(R.string.allarm_type);
			break;
			
		default:
			tv_info.setText(R.string.alarm_info);
			tv_type.setText(R.string.not_know);
			alarm_type_text.setText(String.valueOf(alarm_type));
			break;
		}

		alarm_go.setOnClickListener(this);
		monitor_btn.setOnClickListener(this);
		ignore_btn.setOnClickListener(this);
		shield_btn.setOnClickListener(this);
	}

	public void regFilter() {
		isRegFilter = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.CHANGE_ALARM_MESSAGE);
		filter.addAction(Constants.P2P.DELETE_BINDALARM_ID);
		filter.addAction(Constants.Action.GET_DEVICE_TYPE);
		registerReceiver(br, filter);
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.CHANGE_ALARM_MESSAGE)) {
				int alarm_id1 = intent.getIntExtra("alarm_id", 0);
				int alarm_type1 = intent.getIntExtra("alarm_type", 0);
				boolean isSupport1 = intent.getBooleanExtra("isSupport", false);
				int group1 = intent.getIntExtra("group", 0);
				int item1 = intent.getIntExtra("item", 0);
				isSupportDelete = intent.getBooleanExtra("isSupportDelete",
						false);
				alarm_id_text.setText(String.valueOf(alarm_id1));
				if (isSupportDelete) {
					shield_btn.setText(R.string.clear_bundealarmid);
				} else {
					shield_btn.setText(R.string.shielded);
				}
				switch (alarm_type1) {
				case P2PValue.AlarmType.EXTERNAL_ALARM:
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.allarm_type1);
					tv_type.setText(R.string.allarm_type);
					if (isSupport1) {
						layout_area_chanel
								.setVisibility(RelativeLayout.VISIBLE);
						area_text
								.setText(mContext.getResources().getString(
										R.string.area)
										+ ":"
										+ Utils.getDefenceAreaByGroup(mContext,
												group1));
						chanel_text.setText(mContext.getResources().getString(
								R.string.channel)
								+ ":" + (item1 + 1));
					}
					break;
				case P2PValue.AlarmType.MOTION_DECT_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.allarm_type2);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.EMERGENCY_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.allarm_type3);
					tv_type.setText(R.string.allarm_type);
					break;

				case P2PValue.AlarmType.LOW_VOL_ALARM:
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.low_voltage_alarm);
					tv_type.setText(R.string.allarm_type);
					if (isSupport1) {
						layout_area_chanel
								.setVisibility(RelativeLayout.VISIBLE);
						area_text
								.setText(mContext.getResources().getString(
										R.string.area)
										+ ":"
										+ Utils.getDefenceAreaByGroup(mContext,
												group1));
						chanel_text.setText(mContext.getResources().getString(
								R.string.channel)
								+ ":" + (item1 + 1));
					}
					break;
				case P2PValue.AlarmType.PIR_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.allarm_type4);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.EXT_LINE_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.allarm_type5);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.DEFENCE:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.defence);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.NO_DEFENCE:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.no_defence);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.BATTERY_LOW_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.battery_low_alarm);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					alarm_type_text.setText(R.string.door_bell);
					tv_info.setText(R.string.visitor_messge);
					tv_type.setText(R.string.allarm_type);
					break;
				case P2PValue.AlarmType.RECORD_FAILED_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					alarm_type_text.setText(R.string.record_failed);
					tv_type.setText(R.string.allarm_type);
					break;
				default:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.alarm_info);
					tv_type.setText(R.string.not_know);
					alarm_type_text.setText(String.valueOf(alarm_type));
					break;
				}

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
			}else if(intent.getAction().equals(Constants.Action.GET_DEVICE_TYPE)){
				String[] contactIds=intent.getStringArrayExtra("contactIDs");
				int[] types=intent.getIntArrayExtra("types");
				for(int i=0;i<contactIds.length;i++){
					if(contactIds[i].equals(String.valueOf(alarm_id))){
						contactType=types[i];
					}
				}
			}
		}
	};
	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			finish();
			String[] data = (String[]) msg.obj;
			Intent monitor = new Intent();
			monitor.setClass(mContext, CallActivity.class);
			monitor.putExtra("callId", data[0]);
			monitor.putExtra("password", data[1]);
			monitor.putExtra("isOutCall", true);
			monitor.putExtra("contactType",contactType);
			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
			startActivity(monitor);
			return false;
		}
	});
	boolean viewed = false;
	Timer timeOutTimer;
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

	// 超时计时器
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ignore_btn) {
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
		} else if (id == R.id.monitor_btn) {
			viewed = true;
			final Contact contact = FList.getInstance().isContact(
					String.valueOf(alarm_id));
			if (null != contact) {
				hasContact = true;
				P2PConnect.vReject("");
				new Thread() {
					public void run() {
						while (true) {
							if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
								Message msg = new Message();
								String[] data = new String[] {
										contact.contactId,
										contact.contactPassword };
								msg.obj = data;
								handler.sendMessage(msg);
								break;
							}
							Utils.sleepThread(500);
						}
					}
				}.start();
			}
			if (!hasContact) {
				if (alarm_input.getVisibility() == RelativeLayout.VISIBLE) {
					return;
				}

				alarm_input.setVisibility(RelativeLayout.VISIBLE);
				alarm_input.requestFocus();
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.slide_in_right);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						InputMethodManager m = (InputMethodManager) alarm_input
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

				});
				alarm_input.startAnimation(anim);
			}
		} else if (id == R.id.alarm_go) {
			viewed = true;
			final String password = mPassword.getText().toString();
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
				public void run() {
					while (true) {
						if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
							Message msg = new Message();
							String pwd=P2PHandler.getInstance().EntryPassword(password);
							String[] data = new String[] {
									String.valueOf(alarm_id),pwd};
							msg.obj = data;
							handler.sendMessage(msg);
							break;
						}
						Utils.sleepThread(500);
					}
				}
			}.start();
		} else if (id == R.id.shield_btn) {
			viewed = true;
			if (!isSupportDelete) {
				ShieldDevice();
			} else {
				DeleteDevice();
			}
		}
	}

	private void ShieldDevice() {
		dialog = new NormalDialog(mContext, mContext.getResources().getString(
				R.string.shielded), mContext.getResources().getString(
				R.string.shielded_alarm_promp), mContext.getResources()
				.getString(R.string.ensure), mContext.getResources().getString(
				R.string.cancel));
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub

				List<AlarmMask> alarmMasks = DataManager
						.findAlarmMaskByActiveUser(mContext,
								NpcCommon.mThreeNum);

				boolean isExist = false;
				for (AlarmMask alarmMask : alarmMasks) {
					if (String.valueOf(alarm_id).equals(alarmMask.deviceId)) {
						isExist = true;
						break;
					}
				}

				if (!isExist) {
					Contact saveContact = new Contact();
					saveContact.contactId = String.valueOf(alarm_id);
					saveContact.activeUser = NpcCommon.mThreeNum;

					AlarmMask alarmMask = new AlarmMask();
					alarmMask.deviceId = String.valueOf(alarm_id);
					alarmMask.activeUser = NpcCommon.mThreeNum;
					DataManager.insertAlarmMask(mContext, alarmMask);

					Intent add_success = new Intent();
					add_success
							.setAction(Constants.Action.ADD_ALARM_MASK_ID_SUCCESS);
					add_success.putExtra("alarmMask", alarmMask);
					mContext.sendBroadcast(add_success);
				}
				finish();
			}
		});
		dialog.showDialog();
	}

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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
				System.currentTimeMillis());
		isAlarm = false;
		P2PConnect.vEndAllarm();
		Log.e("alarmActivity", "---onStop");
	}

	@Override
	protected void onResume() {
		super.onResume();
		P2PConnect.setAlarm(true);
		loadMusicAndVibrate();
		Log.e("alarmActivity", "+++onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManger.getInstance().stop();
		Log.e("alarmActivity", "---onPause");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("alarmActivity", "+++onStart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		P2PConnect.setAlarm(false);
		if (isRegFilter = true) {
			mContext.unregisterReceiver(br);
		}
		if (timeOutTimer != null) {
			timeOutTimer.cancel();
		}
		Log.e("alarmActivity", "---onDestroy");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("alarmActivity", "+++onRestart");
	}

}
