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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class ModifyAccountPhoneActivity extends BaseActivity implements
        OnClickListener {
    private Button mNext;
    private RelativeLayout choose_country;
    private EditText phoneNum;
    private ImageView mBack;
    private TextView dfault_name, dfault_count;
    boolean myreceiverIsReg = false;
    boolean isDialogCanel = false;
    NormalDialog dialog;
    MyInputDialog dialog_input;
    private Context mContext;
    RelativeLayout dialog_input_mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account_phone);
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
        mBack = (ImageView) findViewById(R.id.back_btn);
        Account account = AccountPersist.getInstance().getActiveAccountInfo(
                mContext);
        String phone = "";
        String countryCode = "86";
        if (null != account) {
            phone = account.phone;
            countryCode = account.countryCode;
            if (countryCode.equals("") || countryCode.equals("0")) {
                countryCode = "86";
            }
            if (phone.equals("0")) {
                phone = "";
            }
        }
        String countryName = SearchListActivity.getNameByCode(mContext,
                Integer.parseInt(countryCode));

        dfault_count.setText("+" + countryCode);
        dfault_name.setText(countryName);
        phoneNum.setText(phone);
        dialog_input_mask = (RelativeLayout) findViewById(R.id.dialog_input_mask);
        mBack.setOnClickListener(this);
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
        if (resId == R.id.back_btn) {
            finish();
        } else if (resId == R.id.next) {
            getPhoneCode();
        } else if (resId == R.id.country) {
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
                                    ModifyAccountPhoneActivity2.class);
                            i.putExtra("phone", PhoneNO);
                            i.putExtra("countryCode", CountryCode);
                            startActivity(i);
                            finish();
                        } else {
                            showInputPwd(PhoneNO, CountryCode);
                        }
                    }
                    break;
                case NetManager.GET_PHONE_CODE_PHONE_USED:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        T.showShort(mContext, R.string.phone_number_used);
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
                default:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        String toast = Utils.getStringByResouceID(R.string.operator_error) + "(" + result + ")";
                        T.showShort(mContext, toast);
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

    public void showInputPwd(final String phone, final String countryCode) {
        dialog_input = new MyInputDialog(mContext);
        dialog_input.setTitle(mContext.getResources().getString(
                R.string.change_phone));
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
                        new SetAccountInfoTask(password, phone, countryCode)
                                .execute();
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

    class SetAccountInfoTask extends AsyncTask {
        private String password;
        private String phone;
        private String countryCode;

        public SetAccountInfoTask(String password, String phone,
                                  String countryCode) {
            this.password = password;
            this.phone = phone;
            this.countryCode = countryCode;
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
                    NpcCommon.mThreeNum, phone, account.email, countryCode,
                    account.sessionId, password, "1", "");
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
                    new SetAccountInfoTask(password, phone, countryCode).execute();
                    return;
                case NetManager.SET_ACCOUNT_SUCCESS:
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog = null;
                    }

                    Account account = AccountPersist.getInstance()
                            .getActiveAccountInfo(mContext);
                    account.phone = phone;
                    account.countryCode = countryCode;
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
        return Constants.ActivityInfo.ACTIVITY_MODIFYACCOUNTPHONEACTIVITY;
    }
}
