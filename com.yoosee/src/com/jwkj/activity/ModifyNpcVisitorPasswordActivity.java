package com.jwkj.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class ModifyNpcVisitorPasswordActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;
	private Contact mContact;
	EditText et_pwd;
	Button msave;
	ImageView back_bt;
	boolean isRegFilter = false;
	NormalDialog dialog;
	private MyPassLinearLayout llPass;
	private String visitorPwd = "0";
	private Button btnClearPwd;
	private boolean isSeeVisitorPwd = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.modify_npc_visitor_pwd);
		mContext = this;
		mContact = (Contact) getIntent().getSerializableExtra("contact");
		visitorPwd = getIntent().getStringExtra("visitorpwd");
		isSeeVisitorPwd = getIntent().getBooleanExtra("isSeeVisitorPwd", false);
		if (visitorPwd.equals("0")) {
			visitorPwd = "";
		}
		initComponent();
		regFilter();
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.RET_SET_VISITOR_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.ACK_RET_SET_VISITOR_DEVICE_PASSWORD);
		mContext.registerReceiver(br, filter);
		isRegFilter = true;
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(
					Constants.P2P.RET_SET_VISITOR_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (result == Constants.P2P_SET.DEVICE_VISITOR_PASSWORD_SET.SETTING_SUCCESS) {
					T.showShort(mContext, R.string.modify_success);
					Intent intents = new Intent();
					if (isSeeVisitorPwd) {
						intents.putExtra("visitorpwd", visitor_password);
					} else {
						intents.putExtra("visitorpwd", "0");
					}
					((Activity) mContext).setResult(RESULT_OK, intents);
					finish();
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_VISITOR_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("state", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
					T.showShort(mContext, R.string.old_pwd_error);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					T.showShort(mContext, R.string.net_error_operator_fault);
				}
			}

		}
	};

	public void initComponent() {
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd.setText(visitorPwd);
		if (visitorPwd != null) {
			et_pwd.setSelection(visitorPwd.length());
		}
		msave = (Button) findViewById(R.id.save);
		back_bt = (ImageView) findViewById(R.id.back_btn);
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		btnClearPwd = (Button) findViewById(R.id.btn_clear_visitorpwd);
		if (visitorPwd == null || visitorPwd.length() <= 0
				|| visitorPwd.equals("0")) {
			btnClearPwd.setVisibility(View.GONE);
		} else {
			btnClearPwd.setVisibility(View.VISIBLE);
		}
		btnClearPwd.setOnClickListener(this);
//		llPass.setEditextListener(et_pwd);
		msave.setOnClickListener(this);
		back_bt.setOnClickListener(this);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFY_NPC_VISITOR_PASSWORD_ACTIVITY;
	}

	private String visitor_password;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			setResult(RESULT_CANCELED);
			finish();
		} else if (id == R.id.btn_clear_visitorpwd) {
			// 清除访客密码
			showClearEmail();
		} else if (id == R.id.save) {
			visitor_password = et_pwd.getText().toString();
			if ("".equals(visitor_password.trim())) {
				T.showShort(mContext, R.string.input_visitor_pwd);
				return;
			}
			if (!Utils.isNumeric(visitor_password)||visitor_password.length() >9||visitor_password.charAt(0)=='0') {
				T.showShort(mContext, R.string.visitor_pwd_error);
				return;
			}
			//			if (llPass.isWeakpassword()) {
//				T.showShort(mContext, R.string.simple_password);
//				return;
//			}
			ClearEmail(visitor_password);
		} else {
		}

	}

	void showClearEmail() {
		NormalDialog dialog = new NormalDialog(mContext, mContext
				.getResources().getString(R.string.clear_visitorpwd), mContext
				.getResources().getString(R.string.clear_visitorpwd) + "?",
				mContext.getResources().getString(R.string.confirm), mContext
						.getResources().getString(R.string.cancel));
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				ClearEmail("0");
			}
		});
		dialog.showDialog();
	}

	void ClearEmail(String pwd) {
		if (null == dialog) {
			dialog = new NormalDialog(this, this.getResources().getString(
					R.string.verification), "", "", "");
			dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
		}
		dialog.showDialog();
		pwd=P2PHandler.getInstance().EntryPassword(pwd);
		P2PHandler.getInstance().setDeviceVisitorPassword(mContact.contactId,
				mContact.contactPassword, pwd);
		visitor_password = pwd;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(br);
			isRegFilter = false;
		}
	}
}
