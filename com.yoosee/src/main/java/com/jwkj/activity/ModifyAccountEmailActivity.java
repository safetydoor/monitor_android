package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyInputDialog;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.NetManager;

public class ModifyAccountEmailActivity extends BaseActivity implements
		OnClickListener {
	Context mContext;
	ImageView mBack;
	EditText mEmail;
	Button mNext;
	NormalDialog dialog;
	RelativeLayout dialog_input_mask;
	MyInputDialog dialog_input;
	boolean isRegFilter = false;
	String old_email;
	private Button btnClearBundEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_account_email);

		mContext = this;
		initCompent();
		regFilter();
	}

	private boolean isNext = false;

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mNext = (Button) findViewById(R.id.next);
		mEmail = (EditText) findViewById(R.id.email);
		btnClearBundEmail = (Button) findViewById(R.id.btn_clear_bundemail);
		btnClearBundEmail.setOnClickListener(this);
		Account account = AccountPersist.getInstance().getActiveAccountInfo(
				mContext);
		old_email = account.email;
		mEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String email = mEmail.getText().toString();
				if (email.equals(old_email)) {
					mNext.setBackgroundResource(R.drawable.tab_button_disabled);
					// mEmail.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
					isNext = false;
					mNext.setOnClickListener(null);
				} else {
					mNext.setBackgroundResource(R.drawable.tab_button);
					isNext = true;
					// mEmail.setTextColor(mContext.getResources().getColor(R.color.text_color_white));
					mNext.setOnClickListener(ModifyAccountEmailActivity.this);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

		});
		mEmail.setText(account.email);
		mEmail.setSelection(account.email.length());
		if (account.email != null && account.email.length() <= 0) {
			btnClearBundEmail.setVisibility(View.GONE);
		} else {
			btnClearBundEmail.setVisibility(View.VISIBLE);
		}
		dialog_input_mask = (RelativeLayout) findViewById(R.id.dialog_input_mask);
		mBack.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int resId = v.getId();
		if (resId == R.id.back_btn) {
			finish();
		} else if (resId == R.id.btn_clear_bundemail) {
			// 解除绑定邮箱
			showInputPwd("");
		} else if (resId == R.id.next) {
			// if(!isNext){
			// return;
			// }
			String email = mEmail.getText().toString();
			if ("".equals(email.trim())) {
				T.showShort(this, R.string.input_email);
				// showInputPwd(email);
				return;
			}
			if (!Utils.isEmial(email)) {
				T.showShort(this, R.string.email_format_error);
				return;
			}
			if (email.length() > 32 || email.length() < 3) {
				T.showShort(this, R.string.email_too_long);
				return;
			}
			showInputPwd(email);
		}
	}

	public void showInputPwd(final String email) {
		dialog_input = new MyInputDialog(mContext);
		if (email != null && email.length() <= 0) {
			dialog_input.setTitle(mContext.getResources().getString(
					R.string.clear_bundemail));
		} else {
			dialog_input.setTitle(mContext.getResources().getString(
					R.string.change_email));
		}
		dialog_input.setBtn1_str(mContext.getResources().getString(
				R.string.ensure));
		dialog_input.setBtn2_str(mContext.getResources().getString(
				R.string.cancel));
		dialog_input
				.setOnButtonOkListener(new MyInputDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						String password = dialog_input.getInput1Text();
						if ("".equals(password.trim())) {
							T.showShort(mContext, R.string.input_login_pwd);
							return;
						}
						dialog_input.hide(dialog_input_mask);
						if (null == dialog) {
							dialog = new NormalDialog(mContext, mContext
									.getResources().getString(
											R.string.verification), "", "", "");
							dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
						}
						dialog.showDialog();
						new SetAccountInfoTask(password, email).execute();
					}
				});
		dialog_input.show(dialog_input_mask);
		dialog_input.setInput1HintText(R.string.input_login_pwd);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (null != dialog_input && dialog_input.isShowing()) {
			dialog_input.hide(dialog_input_mask);
		} else {
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
	}

	class SetAccountInfoTask extends AsyncTask {
		private String password;
		private String email;

		public SetAccountInfoTask(String password, String email) {
			this.password = password;
			this.email = email;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Account account = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext);
			return NetManager.getInstance(mContext).setAccountInfo(
					NpcCommon.mThreeNum, account.phone, email,
					account.countryCode, account.sessionId, password, "2", "");
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub

			int result = (Integer) object;
			switch (result) {
			case NetManager.SESSION_ID_ERROR:
				Intent i = new Intent();
				i.setAction(Constants.Action.SESSION_ID_ERROR);
				MyApp.app.sendBroadcast(i);
				break;
			case NetManager.CONNECT_CHANGE:
				new SetAccountInfoTask(password, email).execute();
				return;
			case NetManager.SET_ACCOUNT_SUCCESS:
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext);
				account.email = email;
				AccountPersist.getInstance()
						.setActiveAccount(mContext, account);

				T.showShort(mContext, R.string.modify_success);
				finish();
				break;
			case NetManager.SET_ACCOUNT_PWD_ERROR:
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				T.showShort(mContext, R.string.password_error);
				break;
			case NetManager.SET_ACCOUNT_EMAIL_USED:
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				T.showShort(mContext, R.string.email_used);
				break;
			case NetManager.SET_ACCOUNT_EMAIL_FORMAT_ERROR:
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				T.showShort(mContext, R.string.email_format_error);
				break;
			default:
				if (null != dialog && dialog.isShowing()) {
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
		return Constants.ActivityInfo.ACTIVITY_MODIFYACCOUNTEMAILACTIVITY;
	}
}
