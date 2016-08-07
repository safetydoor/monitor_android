package com.jwkj.activity;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.ModifyLoginPasswordResult;
import com.p2p.core.network.NetManager;

public class ModifyLoginPasswordActivity extends BaseActivity implements
		OnClickListener {

	Context mContext;
	ImageView mBack;
	Button mSave;
	EditText old_pwd, new_pwd, re_new_pwd;
	String password_old, password_new, password_re_new;
	NormalDialog dialog;
	private MyPassLinearLayout llPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_login_password);

		mContext = this;
		initCompent();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (Button) findViewById(R.id.save);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		re_new_pwd = (EditText) findViewById(R.id.re_new_pwd);

		old_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		re_new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(new_pwd);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

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
				T.showShort(mContext, R.string.input_old_pwd);
				return;
			}
			if ("".equals(password_new.trim())) {
				T.showShort(mContext, R.string.input_new_pwd);
				return;
			}
			if (password_new.length() > 27) {
				T.showShort(mContext, R.string.password_length_error);
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
			if (null == dialog) {
				dialog = new NormalDialog(this, this.getResources().getString(
						R.string.verification), "", "", "");
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
			}
			dialog.showDialog();
			dialog.setCancelable(false);
			Account account = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext);
			new ModifyLoginPasswordTask(NpcCommon.mThreeNum, account.sessionId,
					password_old, password_new, password_re_new).execute();
		}
	}

	class ModifyLoginPasswordTask extends AsyncTask {
		String threeNum;
		String sessionId;
		String oldPwd;
		String newPwd;
		String rePwd;

		public ModifyLoginPasswordTask(String threeNum, String sessionId,
				String oldPwd, String newPwd, String rePwd) {
			this.threeNum = threeNum;
			this.sessionId = sessionId;
			this.oldPwd = oldPwd;
			this.newPwd = newPwd;
			this.rePwd = rePwd;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			Utils.sleepThread(1000);
			return NetManager.getInstance(mContext).modifyLoginPassword(
					threeNum, sessionId, oldPwd, newPwd, rePwd);
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub
			ModifyLoginPasswordResult result = NetManager
					.createModifyLoginPasswordResult((JSONObject) object);
			switch (Integer.parseInt(result.error_code)) {
			case NetManager.SESSION_ID_ERROR:
				Intent i = new Intent();
				i.setAction(Constants.Action.SESSION_ID_ERROR);
				MyApp.app.sendBroadcast(i);
				break;
			case NetManager.CONNECT_CHANGE:
				new ModifyLoginPasswordTask(threeNum, sessionId, oldPwd,
						newPwd, rePwd).execute();
				return;
			case NetManager.MODIFY_LOGIN_PWD_SUCCESS:
				if (null != dialog) {
					dialog.dismiss();
					dialog = null;
				}
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext);
				account.sessionId = result.sessionId;
				AccountPersist.getInstance()
						.setActiveAccount(mContext, account);
				T.showShort(mContext, R.string.modify_pwd_success);
				Intent canel = new Intent();
				canel.setAction(Constants.Action.ACTION_SWITCH_USER);
				mContext.sendBroadcast(canel);
				finish();
				break;
			case NetManager.MODIFY_LOGIN_PWD_INCONSISTENCE:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				T.showShort(mContext, R.string.pwd_inconsistence);
				break;
			case NetManager.MODIFY_LOGIN_PWD_OLD_PWD_ERROR:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				T.showShort(mContext, R.string.old_pwd_error);
				break;
			default:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				T.showShort(mContext, R.string.operator_error);
				break;
			}
		}

	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFYLOGINPASSWORDACTIVITY;
	}

}
