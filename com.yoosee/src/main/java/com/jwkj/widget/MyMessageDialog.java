package com.jwkj.widget;

import java.util.List;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.data.AlarmMask;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.p2p.core.P2PValue;
import com.yoosee.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
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

public class MyMessageDialog extends Dialog implements
		android.view.View.OnClickListener {
	public interface OnCustomDialogListener {
		public void dissmiss();

		public void pingbi();

		public void go();

		public void check();
	}

	private String title;
	private OnCustomDialogListener customDialogListener;
	private Context mContext;

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

	protected MyMessageDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public MyMessageDialog(Context context) {
		super(context);
	}

	public MyMessageDialog(Context context, String title,
			OnCustomDialogListener customDialogListener) {
		super(context);
		this.mContext = context;
		this.title = title;
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mymessage);
		initComponent();
	}

	public void setMessage(int alarm_id, int alarm_type, int group, int item) {
		this.alarm_id = alarm_id;
		this.alarm_type = alarm_type;
		this.group = group;
		this.item = item;
	}

	public void initComponent() {
		monitor_btn = (TextView) findViewById(R.id.monitor_btn);
		ignore_btn = (TextView) findViewById(R.id.ignore_btn);
		shield_btn = (TextView) findViewById(R.id.shield_btn);
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
		alarm_id_text.setText(String.valueOf(alarm_id));

		layout_area_chanel = (LinearLayout) findViewById(R.id.layout_area_chanel);
		area_text = (TextView) findViewById(R.id.area_text);
		chanel_text = (TextView) findViewById(R.id.chanel_text);

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

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		if (resId == R.id.ignore_btn){
			customDialogListener.dissmiss();
		} else if (resId == R.id.monitor_btn){
			customDialogListener.pingbi();
		} else if (resId == R.id.alarm_go){
			customDialogListener.go();
		} else if (resId == R.id.shield_btn){
			customDialogListener.check();
		}
	}

	public void dissmiss() {
		this.dismiss();
	}

}
