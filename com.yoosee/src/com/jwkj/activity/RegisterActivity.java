package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.NetManager;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private Button mNext;
	private RelativeLayout choose_country;
	private EditText phoneNum;
	private TextView dfault_name, dfault_count;
	boolean myreceiverIsReg = false;
	boolean isDialogCanel = false;
	NormalDialog dialog;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);
		mContext = this;
		initCompent();
		regFilter();
	}

	public void initCompent() {
		mNext = (Button) findViewById(R.id.next);
		phoneNum = (EditText) findViewById(R.id.account_name);
		choose_country = (RelativeLayout) findViewById(R.id.country);
		dfault_name = (TextView) findViewById(R.id.name);
		dfault_count = (TextView) findViewById(R.id.count);

		if (getResources().getConfiguration().locale.getCountry().equals("TW")) {
			dfault_count.setText("+886");
			String name = SearchListActivity.getNameByCode(mContext, 886);
			dfault_name.setText(name);
		} else if (getResources().getConfiguration().locale.getCountry()
				.equals("CN")) {
			dfault_count.setText("+86");
			String name = SearchListActivity.getNameByCode(mContext, 86);
			dfault_name.setText(name);
		} else {
			dfault_count.setText("+1");
			String name = SearchListActivity.getNameByCode(mContext, 1);
			dfault_name.setText(name);
		}
		mNext.setOnClickListener(this);
		choose_country.setOnClickListener(this);
	}

	public void regFilter() {
		myreceiverIsReg = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.ACTION_COUNTRY_CHOOSE);
		this.registerReceiver(mReceiver, filter);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constants.Action.ACTION_COUNTRY_CHOOSE)) {
				String[] info = intent.getStringArrayExtra("info");
				dfault_name.setText(info[0]);
				dfault_count.setText("+" + info[1]);
			}
		}
	};

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		if(resId == R.id.next){
			getPhoneCode();
		}else if(resId == R.id.country){
			Intent i = new Intent(this, SearchListActivity.class);
			startActivity(i);
		}
	}

	private void getPhoneCode() {
		final String phone = phoneNum.getText().toString();
		if (phone == null || phone.equals("")) {
			T.showShort(mContext, R.string.input_phone);
			return;
		}

		if (phone.length() < 6 || phone.length() > 15) {
			T.showShort(this, R.string.phone_too_long);
			return;
		}

		dialog = new NormalDialog(this, this.getResources().getString(
				R.string.waiting_verify_code), "", "", "");
		dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				isDialogCanel = true;
			}

		});
		isDialogCanel = false;
		dialog.showDialog();

		String count = dfault_count.getText().toString();
		new GetPhoneCodeTask(count.substring(1, count.length()), phone)
				.execute();

	}

	class GetPhoneCodeTask extends AsyncTask {
		String CountryCode;
		String PhoneNO;

		public GetPhoneCodeTask(String CountryCode, String PhoneNO) {
			this.CountryCode = CountryCode;
			this.PhoneNO = PhoneNO;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			Utils.sleepThread(1000);
			return NetManager.getInstance(mContext).getPhoneCode(CountryCode,
					PhoneNO);
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub
			int result = (Integer) object;

			switch (result) {
			case NetManager.SESSION_ID_ERROR:
				Intent relogin = new Intent();
				relogin.setAction(Constants.Action.SESSION_ID_ERROR);
				MyApp.app.sendBroadcast(relogin);
				break;
			case NetManager.CONNECT_CHANGE:
				new GetPhoneCodeTask(CountryCode, PhoneNO).execute();
				return;
			case NetManager.GET_PHONE_CODE_SUCCESS:
				if (isDialogCanel) {
					return;
				}
				if (null != dialog) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					if (CountryCode.equals("86")) {
						Intent i = new Intent(mContext,
								VerifyPhoneActivity.class);
						i.putExtra("phone", PhoneNO);
						i.putExtra("count", CountryCode);
						startActivity(i);
						finish();
					} else {
						Intent i = new Intent(mContext, RegisterActivity2.class);
						i.putExtra("phone", PhoneNO);
						i.putExtra("count", CountryCode);
						startActivity(i);
						finish();
					}
				}
				break;
			case NetManager.GET_PHONE_CODE_TOO_TIMES:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					T.showShort(mContext, R.string.get_phone_code_too_times);
				}
				break;
			case NetManager.GET_PHONE_CODE_PHONE_FORMAT_ERROR:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					Utils.showPromptDialog(mContext, R.string.prompt,
							R.string.phone_format_error);
				}
				break;
			case NetManager.GET_PHONE_CODE_PHONE_USED:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					Utils.showPromptDialog(mContext, R.string.prompt,
							R.string.phone_number_used);
				}
				break;
			default:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					Utils.showPromptDialog(mContext, R.string.prompt,
							R.string.registerfail);
				}
				break;
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (myreceiverIsReg) {
			this.unregisterReceiver(mReceiver);
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_REGISTERACTIVITY;
	}
}
