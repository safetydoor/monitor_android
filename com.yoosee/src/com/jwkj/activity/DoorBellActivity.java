package com.jwkj.activity;

import java.util.List;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.SettingListener;
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
import com.yoosee.R;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoorBellActivity extends Activity implements OnClickListener {
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
	String contactId;
	boolean isOpendoor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// if(P2PConnect.isPlaying()){
		// if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }
		// }
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		contactId = getIntent().getStringExtra("contactId");
		setContentView(R.layout.activity_alarm);
		initComponent();
		regFilter();
		loadMusicAndVibrate();
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
		alarm_id_text = (TextView) findViewById(R.id.alarm_id_text);
		alarm_type_text = (TextView) findViewById(R.id.alarm_type_text);
		alarm_go = (TextView) findViewById(R.id.alarm_go);
		tv_info = (TextView) findViewById(R.id.tv_info);
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
		mPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
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
		alarm_id_text.setText(String.valueOf(contactId));

		alarm_go.setOnClickListener(this);
		monitor_btn.setOnClickListener(this);
		ignore_btn.setOnClickListener(this);
		shield_btn.setOnClickListener(this);
		tv_info.setText(R.string.visitor_messge);
		alarm_type_text.setText(R.string.door_bell);
		shield_btn.setText(R.string.open_door);

	}

	public void regFilter() {
		isRegFilter = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.RET_CUSTOM_CMD_DISCONNECT);
		registerReceiver(br, filter);
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(
					Constants.P2P.RET_CUSTOM_CMD_DISCONNECT)) {
				finish();
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
			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
			monitor.putExtra("isSurpportOpenDoor", true);
			startActivity(monitor);
			return false;
		}
	});

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ignore_btn) {
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
			alarm_go.setText("GO");
			mPassword.setHint(R.string.input_monitor_pwd);
			isOpendoor = false;
			final Contact contact = FList.getInstance().isContact(
					String.valueOf(contactId));
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
			final String password = mPassword.getText().toString();
			if (password.trim().equals("")) {
				T.showShort(mContext, R.string.input_monitor_pwd);
				return;
			}
			if (password.length() > 9) {
				T.showShort(mContext, R.string.password_length_error);
				return;
			}
			if (!isOpendoor) {
				P2PConnect.vReject("");

				new Thread() {
					public void run() {
						while (true) {
							if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
								Message msg = new Message();
								String[] data = new String[] {
										String.valueOf(contactId), password };
								msg.obj = data;
								handler.sendMessage(msg);
								break;
							}
							Utils.sleepThread(500);
						}
					}
				}.start();
			} else {
				String open_door_order = "IPC1anerfa:unlock";
				P2PHandler.getInstance().sendCustomCmd(contactId, password,
						open_door_order);
				P2PHandler.getInstance().setGPIO1_0(contactId, password);
				finish();
			}
		} else if (id == R.id.shield_btn) {
			Contact mcontact = FList.getInstance().isContact(
					String.valueOf(contactId));
			String open_door_order = "IPC1anerfa:unlock";
			if (mcontact != null) {
				P2PHandler.getInstance().sendCustomCmd(mcontact.contactId,
						mcontact.contactPassword, open_door_order);
				P2PHandler.getInstance().setGPIO1_0(mcontact.contactId,
						mcontact.contactPassword);
				finish();
			} else {
				isOpendoor = true;
				alarm_go.setText(R.string.unlock);
				mPassword.setHint(R.string.input_lock_password);
				alarm_input.setVisibility(LinearLayout.VISIBLE);
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
				System.currentTimeMillis());
		MusicManger.getInstance().stop();
		isAlarm = false;
		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		SettingListener.setAlarm(true);
		P2PConnect.setDoorbell(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isRegFilter = true) {
			mContext.unregisterReceiver(br);
		}
		SettingListener.setAlarm(false);
		P2PConnect.setDoorbell(false);
	}
}
