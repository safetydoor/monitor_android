package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.Account;
import com.jwkj.entity.LocalDevice;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;

public class LoginActivity extends BaseActivity implements OnClickListener {
	public static Context mContext;
	TextView title_text;
	private boolean isRegFilter = false;

	public static final int ANIMATION_END = 2;
	public static final int ACCOUNT_NO_EXIST = 3;
	Button mLogin, mregister;
	TextView mRegister_phone, mRegister_email;
	private EditText mAccountPwd;
	private EditText mAccountName;
	private String mInputName, mInputPwd;
	RelativeLayout remember_pass;
	RelativeLayout dialog_remember;
	ImageView remember_pwd_img;
	private boolean isDialogCanel = false;
	NormalDialog dialog;
	TextView dfault_name, dfault_count;
	RelativeLayout choose_country;
	RadioButton type_phone, type_email;
	int current_type;
	TextView forget_pwd, tv_Anonymous_login;
	boolean isApEnter;
	NormalDialog dialog_enter_ap;
	boolean isExitAp;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		mContext = this;
		initComponent();
		if (SharedPreferencesManager.getInstance().getRecentLoginType(mContext) == Constants.LoginType.PHONE) {
			current_type = Constants.LoginType.PHONE;
			choose_country.setVisibility(RelativeLayout.VISIBLE);
			type_phone.setChecked(true);
		} else {
			choose_country.setVisibility(RelativeLayout.GONE);
			current_type = Constants.LoginType.EMAIL;
			type_email.setChecked(true);
		}
		regFilter();
		initRememberPass();
	}

	public void initComponent() {
		title_text = (TextView) findViewById(R.id.title_text);
		mLogin = (Button) findViewById(R.id.login);
		mregister = (Button) findViewById(R.id.register);
		// mRegister_phone = (TextView)findViewById(R.id.register_phone);
		// mRegister_email = (TextView)findViewById(R.id.register_email);

		mAccountName = (EditText) findViewById(R.id.phone_number);
		mAccountPwd = (EditText) findViewById(R.id.password);
		dialog_remember = (RelativeLayout) findViewById(R.id.dialog_remember);
		remember_pass = (RelativeLayout) findViewById(R.id.remember_pass);
		remember_pwd_img = (ImageView) findViewById(R.id.remember_pwd_img);
		dfault_name = (TextView) findViewById(R.id.name);
		dfault_count = (TextView) findViewById(R.id.count);
		choose_country = (RelativeLayout) findViewById(R.id.country_layout);

		type_phone = (RadioButton) findViewById(R.id.type_phone);
		type_email = (RadioButton) findViewById(R.id.type_email);

		forget_pwd = (TextView) findViewById(R.id.forget_pwd);
		forget_pwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_Anonymous_login = (TextView) findViewById(R.id.tv_Anonymous_login);

		forget_pwd.setOnClickListener(this);
		type_phone.setOnClickListener(this);
		type_email.setOnClickListener(this);
		choose_country.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mregister.setOnClickListener(this);
		// mRegister_phone.setOnClickListener(this);
		// mRegister_email.setOnClickListener(this);
		remember_pass.setOnClickListener(this);
		tv_Anonymous_login.setOnClickListener(this);
		// if (isApEnter) {
		// Intent i = new Intent(mContext, MainActivity.class);
		// startActivity(i);
		// } else {
		// // List<LocalDevice> aplist=APList.aplist;
		// // if(aplist.size()>0){
		// // NormalDialog dialog = new NormalDialog(mContext, mContext
		// // .getResources().getString(R.string.ap_device), mContext
		// // .getResources().getString(R.string.ap_device_enter),
		// // mContext.getResources().getString(R.string.ensure),
		// // mContext.getResources().getString(R.string.cancel));
		// // dialog.setOnButtonOkListener(new
		// // NormalDialog.OnButtonOkListener() {
		// //
		// // @Override
		// // public void onClick() {
		// // // TODO Auto-generated method stub
		// // SharedPreferencesManager.getInstance().putIsApEnter(mContext,
		// // true);
		// // Intent i=new Intent(mContext, MainActivity.class);
		// // startActivity(i);
		// // }
		// // });
		// // // dialog.setCanceledOnTouchOutside(false);
		// // dialog.showNormalDialog();
		// // }
		// showApDialog();
		// }
	}

	public void initRememberPass() {
		String recentName = "";
		String recentPwd = "";
		String recentCode = "";
		if (current_type == Constants.LoginType.PHONE) {
			recentName = SharedPreferencesManager.getInstance().getData(
					mContext, SharedPreferencesManager.SP_FILE_GWELL,
					SharedPreferencesManager.KEY_RECENTNAME);
			recentPwd = SharedPreferencesManager.getInstance().getData(
					mContext, SharedPreferencesManager.SP_FILE_GWELL,
					SharedPreferencesManager.KEY_RECENTPASS);
			recentCode = SharedPreferencesManager.getInstance().getData(
					mContext, SharedPreferencesManager.SP_FILE_GWELL,
					SharedPreferencesManager.KEY_RECENTCODE);

			if (!recentName.equals("")) {
				mAccountName.setText(recentName);
			} else {
				mAccountName.setText("");
			}

			if (!recentCode.equals("")) {
				dfault_count.setText("+" + recentCode);
				String name = SearchListActivity.getNameByCode(mContext,
						Integer.parseInt(recentCode));
				dfault_name.setText(name);
			} else {
				if (getResources().getConfiguration().locale.getCountry()
						.equals("TW")) {
					dfault_count.setText("+886");
					String name = SearchListActivity.getNameByCode(mContext,
							886);
					dfault_name.setText(name);
				} else if (getResources().getConfiguration().locale
						.getCountry().equals("CN")) {
					dfault_count.setText("+86");
					String name = SearchListActivity
							.getNameByCode(mContext, 86);
					dfault_name.setText(name);
				} else {
					dfault_count.setText("+1");
					String name = SearchListActivity.getNameByCode(mContext, 1);
					dfault_name.setText(name);
				}
			}

			if (SharedPreferencesManager.getInstance().getIsRememberPass(
					mContext)) {
				remember_pwd_img.setImageResource(R.drawable.ic_remember_pwd);
				if (!recentPwd.equals("")) {
					mAccountPwd.setText(recentPwd);
				} else {
					mAccountPwd.setText("");
				}
			} else {
				remember_pwd_img.setImageResource(R.drawable.ic_unremember_pwd);
				mAccountPwd.setText("");
			}
		} else {
			recentName = SharedPreferencesManager.getInstance().getData(
					mContext, SharedPreferencesManager.SP_FILE_GWELL,
					SharedPreferencesManager.KEY_RECENTNAME_EMAIL);
			recentPwd = SharedPreferencesManager.getInstance().getData(
					mContext, SharedPreferencesManager.SP_FILE_GWELL,
					SharedPreferencesManager.KEY_RECENTPASS_EMAIL);

			if (!recentName.equals("")) {
				mAccountName.setText(recentName);
			} else {
				mAccountName.setText("");
			}

			if (SharedPreferencesManager.getInstance().getIsRememberPass_email(
					mContext)) {
				remember_pwd_img.setImageResource(R.drawable.ic_remember_pwd);
				if (!recentPwd.equals("")) {
					mAccountPwd.setText(recentPwd);
				} else {
					mAccountPwd.setText("");
				}
			} else {
				remember_pwd_img.setImageResource(R.drawable.ic_unremember_pwd);
				mAccountPwd.setText("");
			}
		}
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.REPLACE_EMAIL_LOGIN);
		filter.addAction(Constants.Action.REPLACE_PHONE_LOGIN);
		filter.addAction(Constants.Action.ACTION_COUNTRY_CHOOSE);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.Action.REPLACE_EMAIL_LOGIN)) {
				type_email.setChecked(true);
				type_phone.setChecked(false);
				choose_country.setVisibility(RelativeLayout.GONE);
				mAccountName.setText(intent.getStringExtra("username"));
				mAccountPwd.setText(intent.getStringExtra("password"));
				current_type = Constants.LoginType.EMAIL;
				login();
			} else if (intent.getAction().equals(
					Constants.Action.REPLACE_PHONE_LOGIN)) {
				type_email.setChecked(false);
				type_phone.setChecked(true);
				choose_country.setVisibility(RelativeLayout.VISIBLE);
				mAccountName.setText(intent.getStringExtra("username"));
				mAccountPwd.setText(intent.getStringExtra("password"));
				dfault_count.setText("+" + intent.getStringExtra("code"));
				current_type = Constants.LoginType.PHONE;
				login();
			} else if (intent.getAction().equals(
					Constants.Action.ACTION_COUNTRY_CHOOSE)) {
				String[] info = intent.getStringArrayExtra("info");
				dfault_name.setText(info[0]);
				dfault_count.setText("+" + info[1]);
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.forget_pwd) {
			if (WifiUtils.getInstance().isConnectAP()) {
				T.showLong(mContext, R.string.check_wifi);
			} else {
				Uri uri = Uri.parse(Constants.FORGET_PASSWORD_URL);
				Intent open_web = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(open_web);
			}
		} else if (id == R.id.country_layout) {
			Intent i = new Intent(mContext, SearchListActivity.class);
			startActivity(i);
		} else if (id == R.id.type_phone) {
			type_phone.setChecked(true);
			type_email.setChecked(false);
			choose_country.setVisibility(RelativeLayout.VISIBLE);
			current_type = Constants.LoginType.PHONE;
			initRememberPass();
		} else if (id == R.id.type_email) {
			type_phone.setChecked(false);
			type_email.setChecked(true);
			choose_country.setVisibility(RelativeLayout.GONE);
			current_type = Constants.LoginType.EMAIL;
			initRememberPass();
		} else if (id == R.id.login) {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean isGprsConnected=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ? true : false ;
			if (WifiUtils.getInstance().isConnectAP()&&isGprsConnected==false) {
				T.showLong(mContext, R.string.check_wifi);
			}else if(WifiUtils.getInstance().isConnectAP()&&isGprsConnected==true){
				String wifiName=WifiUtils.getInstance().getConnectWifiName();
				WifiUtils.getInstance().DisConnectWifi(wifiName);
				login();
			}else {
				login();
			}
		} else if (id == R.id.register) {
			if (WifiUtils.getInstance().isConnectAP()) {
				T.showLong(mContext, R.string.check_wifi);
			} else {
				Intent register = new Intent(mContext,
						AltogetherRegisterActivity.class);
				startActivity(register);
			}
		} else if (id == R.id.remember_pass) {
			boolean isChecked = false;
			if (current_type == Constants.LoginType.PHONE) {
				isChecked = SharedPreferencesManager.getInstance()
						.getIsRememberPass(mContext);
			} else {
				isChecked = SharedPreferencesManager.getInstance()
						.getIsRememberPass_email(mContext);
			}
			if (isChecked) {
				TextView dialog_text = (TextView) dialog_remember
						.findViewById(R.id.dialog_text);
				ImageView dialog_img = (ImageView) dialog_remember
						.findViewById(R.id.dialog_img);
				dialog_img.setImageResource(R.drawable.ic_unremember_pwd);
				dialog_text.setText(R.string.un_rem_pass);
				dialog_text.setGravity(Gravity.CENTER);
				dialog_remember.setVisibility(RelativeLayout.VISIBLE);
				Animation anim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				anim.setDuration(200);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = ANIMATION_END;
						mHandler.sendMessageDelayed(msg, 500);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

				});
				dialog_remember.startAnimation(anim);

				if (current_type == Constants.LoginType.PHONE) {
					SharedPreferencesManager.getInstance().putIsRememberPass(
							mContext, false);
				} else {
					SharedPreferencesManager.getInstance()
							.putIsRememberPass_email(mContext, false);
				}

				remember_pwd_img.setImageResource(R.drawable.ic_unremember_pwd);
			} else {
				TextView dialog_text = (TextView) dialog_remember
						.findViewById(R.id.dialog_text);
				ImageView dialog_img = (ImageView) dialog_remember
						.findViewById(R.id.dialog_img);
				dialog_img.setImageResource(R.drawable.ic_remember_pwd);
				dialog_text.setText(R.string.rem_pass);
				dialog_text.setGravity(Gravity.CENTER);
				dialog_remember.setVisibility(RelativeLayout.VISIBLE);
				Animation anim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				anim.setDuration(200);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = ANIMATION_END;
						mHandler.sendMessageDelayed(msg, 500);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

				});
				dialog_remember.startAnimation(anim);
				if (current_type == Constants.LoginType.PHONE) {
					SharedPreferencesManager.getInstance().putIsRememberPass(
							mContext, true);
				} else {
					SharedPreferencesManager.getInstance()
							.putIsRememberPass_email(mContext, true);
				}
				remember_pwd_img.setImageResource(R.drawable.ic_remember_pwd);

			}
		} else if (id == R.id.tv_Anonymous_login) {
			Account account = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext);
			if (null == account) {
				account = new Account();
			}
			account.three_number = "0517401";
			account.rCode1 = "0";
			account.rCode2 = "0";
			// account.phone = "0";
			// account.email = "0";
			account.sessionId = "0";
			// account.countryCode = "0";
			AccountPersist.getInstance().setActiveAccount(mContext, account);
			NpcCommon.mThreeNum = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext).three_number;
			Intent login = new Intent(mContext, MainActivity.class);
			startActivity(login);
			((LoginActivity) mContext).finish();
		}
	}

	private Handler mHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case ANIMATION_END:
				Animation anim_on = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				anim_on.setDuration(300);
				dialog_remember.startAnimation(anim_on);
				dialog_remember.setVisibility(RelativeLayout.GONE);
				break;
			case ACCOUNT_NO_EXIST:
				T.showShort(mContext, R.string.account_no_exist);
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				break;
			default:
				break;
			}

			return false;
		}
	});

	@Override
	public void onBackPressed() {
		super.isGoExit(true);
		this.finish();
	}

	private void login() {
		mInputName = mAccountName.getText().toString().trim();
		mInputPwd = mAccountPwd.getText().toString().trim();
		if ((mInputName != null && !mInputName.equals(""))
				&& (mInputPwd != null && !mInputPwd.equals(""))) {
			if (null != dialog && dialog.isShowing()) {
				Log.e("my", "isShowing");
				return;
			}
			dialog = new NormalDialog(mContext);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					isDialogCanel = true;
				}

			});
			dialog.setTitle(mContext.getResources().getString(
					R.string.login_ing));
			dialog.showLoadingDialog();
			dialog.setCanceledOnTouchOutside(false);
			isDialogCanel = false;

			if (current_type == Constants.LoginType.PHONE) {
				String name = dfault_count.getText().toString() + "-"
						+ mInputName;
				new LoginTask(name, mInputPwd).execute();
			} else {
				if (Utils.isNumeric(mInputName)) {
					if (mInputName.charAt(0) != '0') {
						mHandler.sendEmptyMessage(ACCOUNT_NO_EXIST);
						return;
					}
					new LoginTask(mInputName, mInputPwd).execute();
				} else {
					new LoginTask(mInputName, mInputPwd).execute();
				}
			}

		} else {
			if ((mInputName == null || mInputName.equals(""))
					&& (mInputPwd != null && !mInputPwd.equals(""))) {
				T.showShort(mContext, R.string.input_account);
			} else if ((mInputName != null && !mInputName.equals(""))
					&& (mInputPwd == null || mInputPwd.equals(""))) {
				T.showShort(mContext, R.string.input_password);
			} else {
				T.showShort(mContext, R.string.input_tip);
			}
		}
	}

	class LoginTask extends AsyncTask {
		String username;
		String password;

		public LoginTask(String username, String password) {
			this.username = username;
			this.password = password;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			Utils.sleepThread(1000);
			return NetManager.getInstance(mContext).login(username, password);
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub
			LoginResult result = NetManager
					.createLoginResult((JSONObject) object);
			switch (Integer.parseInt(result.error_code)) {
			case NetManager.SESSION_ID_ERROR:
				Intent i = new Intent();
				i.setAction(Constants.Action.SESSION_ID_ERROR);
				MyApp.app.sendBroadcast(i);
				break;
			case NetManager.CONNECT_CHANGE:
				new LoginTask(username, password).execute();
				return;
			case NetManager.LOGIN_SUCCESS:
				if (isDialogCanel) {
					return;
				}
				if (null != dialog) {
					dialog.dismiss();
					dialog = null;
				}

				if (current_type == Constants.LoginType.PHONE) {
					SharedPreferencesManager.getInstance()
							.putData(mContext,
									SharedPreferencesManager.SP_FILE_GWELL,
									SharedPreferencesManager.KEY_RECENTNAME,
									mInputName);
					SharedPreferencesManager.getInstance().putData(mContext,
							SharedPreferencesManager.SP_FILE_GWELL,
							SharedPreferencesManager.KEY_RECENTPASS, mInputPwd);
					String code = dfault_count.getText().toString();
					code = code.substring(1, code.length());
					SharedPreferencesManager.getInstance().putData(mContext,
							SharedPreferencesManager.SP_FILE_GWELL,
							SharedPreferencesManager.KEY_RECENTCODE, code);
					SharedPreferencesManager.getInstance().putRecentLoginType(
							mContext, Constants.LoginType.PHONE);
				} else {
					SharedPreferencesManager.getInstance().putData(mContext,
							SharedPreferencesManager.SP_FILE_GWELL,
							SharedPreferencesManager.KEY_RECENTNAME_EMAIL,
							mInputName);
					SharedPreferencesManager.getInstance().putData(mContext,
							SharedPreferencesManager.SP_FILE_GWELL,
							SharedPreferencesManager.KEY_RECENTPASS_EMAIL,
							mInputPwd);
					SharedPreferencesManager.getInstance().putRecentLoginType(
							mContext, Constants.LoginType.EMAIL);
				}

				String codeStr1 = String.valueOf(Long.parseLong(result.rCode1));
				String codeStr2 = String.valueOf(Long.parseLong(result.rCode2));
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext);
				if (null == account) {
					account = new Account();
				}
				account.three_number = result.contactId;
				account.phone = result.phone;
				account.email = result.email;
				account.sessionId = result.sessionId;
				account.rCode1 = codeStr1;
				account.rCode2 = codeStr2;
				account.countryCode = result.countryCode;
				AccountPersist.getInstance()
						.setActiveAccount(mContext, account);
				NpcCommon.mThreeNum = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext).three_number;
				Intent login = new Intent(mContext, MainActivity.class);
				startActivity(login);
				((LoginActivity) mContext).finish();
				break;
			case NetManager.LOGIN_USER_UNEXIST:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					T.showShort(mContext, R.string.account_no_exist);
				}
				break;
			case NetManager.LOGIN_PWD_ERROR:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					T.showShort(mContext, R.string.password_error);
				}
				break;
			default:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (!isDialogCanel) {
					T.showShort(mContext, R.string.loginfail);
				}
				break;
			}
		}

	}

//	public void showApDialog() {
//		if (isExitAp) {
//			return;
//		}
//		List<LocalDevice> aplist = APList.aplist;
//		if (aplist.size() > 0) {
//			if (dialog_enter_ap == null || !dialog_enter_ap.isShowing()) {
//				dialog_enter_ap = new NormalDialog(mContext, mContext
//						.getResources().getString(R.string.ap_device), mContext
//						.getResources().getString(R.string.ap_device_enter),
//						mContext.getResources().getString(R.string.ensure),
//						mContext.getResources().getString(R.string.cancel));
//				dialog_enter_ap
//						.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
//
//							@Override
//							public void onClick() {
//								// TODO Auto-generated method stub
//								NpcCommon.mThreeNum = "0517401";
//								Account account = AccountPersist.getInstance()
//										.getActiveAccountInfo(mContext);
//								if (null == account) {
//									account = new Account();
//								}
//								account.three_number = "0517401";
//								account.rCode1 = "0";
//								account.rCode2 = "0";
//								// account.phone = "0";
//								// account.email = "0";
//								account.sessionId = "0";
//								// account.countryCode = "0";
//								AccountPersist.getInstance().setActiveAccount(
//										mContext, account);
//								NpcCommon.mThreeNum = AccountPersist
//										.getInstance().getActiveAccountInfo(
//												mContext).three_number;
//								Intent i = new Intent(mContext,
//										MainActivity.class);
//								startActivity(i);
//								((LoginActivity) mContext).finish();
//							}
//						});
//				dialog_enter_ap
//						.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {
//
//							@Override
//							public void onClick() {
//								// TODO Auto-generated method stub
//								isExitAp = false;
//								dialog_enter_ap.dismiss();
//							}
//						});
//				dialog_enter_ap.showNormalDialog();
//				dialog_enter_ap.setCanceledOnTouchOutside(false);
//			}
//
//		}
//	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			isRegFilter = false;
			mContext.unregisterReceiver(mReceiver);
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_LOGINACTIVITY;
	}

}
