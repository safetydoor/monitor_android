package com.jwkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;

public class ForwardActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Account activeUser = AccountPersist.getInstance().getActiveAccountInfo(
				this);
		if (null != activeUser) {
			if (!activity_stack
					.containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
				Log.e("my", "forward:MainActivity");
				Intent i = new Intent(this, MainActivity.class);
				this.startActivity(i);
			}
		}
		finish();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_FORWARDACTIVITY;
	}

}
