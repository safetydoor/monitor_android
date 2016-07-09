package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class ModifyAlarmIdActivity extends BaseActivity implements
		OnClickListener {
	Context mContext;
	Contact mContact;
	ImageView mBack;
	Button mSave;
	EditText mAlarmId;
	NormalDialog dialog;
	private boolean isRegFilter = false;
	String[] data;
	int position;
	String alarmId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_npc_alarm_id);

		mContact = (Contact) getIntent().getSerializableExtra("contact");
		data = getIntent().getStringArrayExtra("data");
		mContext = this;
		initCompent();
		regFilter();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (Button) findViewById(R.id.save);
		mAlarmId = (EditText) findViewById(R.id.alarmId);

		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
		filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_SET_BIND_ALARM_ID)) {
				int result = intent.getIntExtra("result", -1);
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
					T.showShort(mContext, R.string.modify_success);
					finish();
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)) {
				int result = intent.getIntExtra("result", -1);
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					finish();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					T.showShort(mContext, R.string.net_error_operator_fault);
				}
			}
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int resId = view.getId();
		if (resId == R.id.back_btn) {
			finish();
		} else if (resId == R.id.save) {
			alarmId = mAlarmId.getText().toString();
			if ("".equals(alarmId.trim())) {
				T.showShort(mContext, R.string.input_alarmId);
				return;
			}

			if (alarmId.charAt(0) != '0') {
				T.showShort(mContext, R.string.alarm_id_must_first_with_zero);
				return;
			}

			if (alarmId.length() > 9) {
				T.showShort(mContext, R.string.alarm_id_too_long);
				return;
			}

			if (null == dialog) {
				dialog = new NormalDialog(this, this.getResources().getString(
						R.string.verification), "", "", "");
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
			}
			dialog.showDialog();

			String[] new_data = new String[data.length + 1];
			for (int i = 0; i < data.length; i++) {
				new_data[i] = data[i];
			}
			new_data[new_data.length - 1] = alarmId;

			P2PHandler.getInstance().setBindAlarmId(mContact.contactId,
					mContact.contactPassword, new_data.length, new_data);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFYALARMIDACTIVITY;
	}
}
