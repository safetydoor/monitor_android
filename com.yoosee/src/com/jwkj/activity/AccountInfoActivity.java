package com.jwkj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;

public class AccountInfoActivity extends BaseActivity implements
		OnClickListener {
	Context mContext;
	ImageView back_btn;
	RelativeLayout change_3c, change_email, change_phone;
	RelativeLayout modify_login_pwd;
	TextView three_number_text, email_text, phone_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		mContext = this;
		initCompent();
		loadAccountInfo();
	}

	public void initCompent() {
		back_btn = (ImageView) findViewById(R.id.back_btn);

		change_3c = (RelativeLayout) findViewById(R.id.change_3c);
		change_email = (RelativeLayout) findViewById(R.id.change_email);
		change_phone = (RelativeLayout) findViewById(R.id.change_phone);

		three_number_text = (TextView) findViewById(R.id.three_number_text);
		email_text = (TextView) findViewById(R.id.email_text);
		phone_text = (TextView) findViewById(R.id.phone_text);

		three_number_text.setText(String.valueOf(NpcCommon.mThreeNum));

		modify_login_pwd = (RelativeLayout) findViewById(R.id.modify_login_pwd);

		modify_login_pwd.setOnClickListener(this);
		back_btn.setOnClickListener(this);
		change_3c.setOnClickListener(this);
		change_email.setOnClickListener(this);
		change_phone.setOnClickListener(this);
	}

	void loadAccountInfo() {
		Account account = AccountPersist.getInstance().getActiveAccountInfo(
				mContext);
		String email = "";
		String phone = "";
		String countryCode = "86";
		if (null != account) {
			email = account.email;
			phone = account.phone;
			countryCode = account.countryCode;
		}

		if (!email.equals("")) {
			email_text.setText(email);
		} else {
			email_text.setText(R.string.unbound);
		}

		if (!(phone.equals("0") || phone.equals(""))) {
			phone_text.setText("+" + countryCode + "-" + phone);
		} else {
			phone_text.setText(R.string.unbound);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			finish();
		} else if (id == R.id.change_3c) {
		} else if (id == R.id.change_email) {
			Intent modify_email = new Intent(mContext,
					ModifyAccountEmailActivity.class);
			startActivity(modify_email);
		} else if (id == R.id.change_phone) {
			Account account = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext);
			if (!(account.phone.equals("0") || account.phone.equals(""))) {
				Intent unbind_phone = new Intent(mContext,
						UnbindPhoneActivity.class);
				startActivity(unbind_phone);
			} else {
				Intent modify_phone = new Intent(mContext,
						ModifyAccountPhoneActivity.class);
				startActivity(modify_phone);
			}
		} else if (id == R.id.modify_login_pwd) {
			Intent modify_password = new Intent(mContext,
					ModifyLoginPasswordActivity.class);
			startActivity(modify_password);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadAccountInfo();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ACCOUNTINFOACTIVITY;
	}

}
