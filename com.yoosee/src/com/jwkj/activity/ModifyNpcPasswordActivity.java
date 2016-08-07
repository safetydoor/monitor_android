package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class ModifyNpcPasswordActivity extends BaseActivity implements
		OnClickListener {
	Context mContext;
	Contact mContact;
	ImageView mBack;
	Button mSave;
	EditText old_pwd, new_pwd, re_new_pwd;
	TextView tv_weak_password;
	TextView tv_title;
	NormalDialog dialog;
	private boolean isRegFilter = false;
	String password_old, password_new, password_re_new;
	private MyPassLinearLayout llPass;
	boolean isWeakPwd=false;
	String userPassword;
	boolean isModifyNvrPwd=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_npc_pwd);

		mContact = (Contact) getIntent().getSerializableExtra("contact");
		isWeakPwd=getIntent().getBooleanExtra("isWeakPwd", false);
		isModifyNvrPwd=getIntent().getBooleanExtra("isModifyNvrPwd", false);
		mContext = this;
		initCompent();
		regFilter();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (Button) findViewById(R.id.save);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		re_new_pwd = (EditText) findViewById(R.id.re_new_pwd);
		tv_title=(TextView)findViewById(R.id.tv_title);

		old_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		re_new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(new_pwd);
		tv_weak_password=(TextView)findViewById(R.id.tv_weak_password);
		if(isWeakPwd){
			tv_weak_password.setText(getResources().getString(R.string.weak_password));
//			tv_weak_password.setVisibility(TextView.VISIBLE);
		}else{
			tv_weak_password.setText(getResources().getString(R.string.new_device_password));
//			tv_weak_password.setVisibility(TextView.GONE);
		}
		if(isModifyNvrPwd){
			tv_title.setText(getResources().getString(R.string.modify_nvr_pwd));
		}else{
			tv_title.setText(getResources().getString(R.string.modify_device_password));
		}
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction()
					.equals(Constants.P2P.RET_SET_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (result == Constants.P2P_SET.DEVICE_PASSWORD_SET.SETTING_SUCCESS) {
					mContact.contactPassword = password_new;
					mContact.userPassword=userPassword;
					FList.getInstance().update(mContact);
					Intent refreshContans = new Intent();
					refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
					refreshContans.putExtra("contact", mContact);
					mContext.sendBroadcast(refreshContans);

					T.showShort(mContext, R.string.modify_success);
					finish();
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
					T.showShort(mContext, R.string.old_pwd_error);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					T.showShort(mContext, R.string.net_error_operator_fault);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
				finish();
			}
		}
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			finish();
		} else if (id == R.id.save) {
			password_old = old_pwd.getText().toString();
			password_new = new_pwd.getText().toString();
			password_re_new = re_new_pwd.getText().toString();
			if ("".equals(password_old.trim())) {
				T.showShort(mContext, R.string.input_old_device_pwd);
				return;
			}
			if ("".equals(password_new.trim())) {
				T.showShort(mContext, R.string.input_new_device_pwd);
				return;
			}
			if (password_new.length()>30|| password_new.charAt(0) == '0') {
				T.showShort(mContext, R.string.device_password_invalid);
				return;
			}
			if ("".equals(password_re_new.trim())) {
				T.showShort(mContext, R.string.input_re_new_device_pwd);
				return;
			}
			if (!password_re_new.equals(password_new)) {
				T.showShort(mContext, R.string.pwd_inconsistence);
				return;
			}
			if (llPass.isWeakpassword()) {
				if (password_new.length() < 6) {
					T.showShort(mContext, R.string.simple_password);
				} else {
					T.showShort(mContext, R.string.simple_password);
				}
				return;
			}
			if (null == dialog) {
				dialog = new NormalDialog(this, this.getResources().getString(
						R.string.verification), "", "", "");
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
			}
			dialog.showDialog();
			userPassword=password_new;
			password_old=P2PHandler.getInstance().EntryPassword(password_old);
			password_new=P2PHandler.getInstance().EntryPassword(password_new);
			P2PHandler.getInstance().setDevicePassword(mContact.contactId,
					password_old, password_new);
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
		return Constants.ActivityInfo.ACTIVITY_MODIFNPCPASSWORDACTIVITY;
	}
}
