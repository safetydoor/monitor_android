package com.jwkj.activity;

import com.yoosee.R;
import com.jwkj.global.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class AltogetherRegisterActivity extends BaseActivity implements
		OnClickListener {
	Button mregister;
	RadioButton phone_register, email_register;
	int current_type;
	Context mcontext;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		mcontext = this;
		initComponent();
	}

	public void initComponent() {
		mregister = (Button) findViewById(R.id.register);
		phone_register = (RadioButton) findViewById(R.id.register_type_phone);
		email_register = (RadioButton) findViewById(R.id.register_type_email);
		mregister.setOnClickListener(this);
		phone_register.setOnClickListener(this);
		email_register.setOnClickListener(this);
		phone_register.setChecked(true);
		email_register.setChecked(false);
		current_type = Constants.RegisterType.PHONE;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.register_type_phone) {
			phone_register.setChecked(true);
			email_register.setChecked(false);
			current_type = Constants.RegisterType.PHONE;
		} else if (id == R.id.register_type_email) {
			email_register.setChecked(true);
			phone_register.setChecked(false);
			current_type = Constants.RegisterType.EMALL;
		} else if (id == R.id.register) {
			if (current_type == Constants.RegisterType.PHONE) {
				Intent register_phone = new Intent(mcontext,
						RegisterActivity.class);
				startActivity(register_phone);
			} else {
				Intent register_email = new Intent(mcontext,
						RegisterActivity2.class);
				register_email.putExtra("isEmailRegister", true);
				startActivity(register_email);
			}
			finish();
		} else {
		}

	}

	@Override
	public int getActivityInfo() {
		return Constants.ActivityInfo.ACTIVITY_ALTOGETHERREGISTERACTIVITY;
	}

}
