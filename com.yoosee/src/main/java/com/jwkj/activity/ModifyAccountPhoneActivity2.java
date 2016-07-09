package com.jwkj.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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

public class ModifyAccountPhoneActivity2 extends BaseActivity implements
        OnClickListener {
    private String countryCode;
    private String phone;
    private TextView phone_view;
    private ImageView mBack;
    private EditText verify_code;
    private Button resend, mNext;
    boolean isDialogCanel = false;
    NormalDialog dialog;
    MyInputDialog dialog_input;
    private Context mContext;
    RelativeLayout dialog_input_mask;
    public static final int CHANGE_BUTTON_TEXT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account_phone2);
        mContext = this;
        countryCode = getIntent().getStringExtra("countryCode");
        phone = getIntent().getStringExtra("phone");
        initCompent();
        changeButton();
    }

    public void initCompent() {
        phone_view = (TextView) findViewById(R.id.phone);
        verify_code = (EditText) findViewById(R.id.verify_code);
        resend = (Button) findViewById(R.id.resend);
        mBack = (ImageView) findViewById(R.id.back_btn);
        mNext = (Button) findViewById(R.id.next);

        phone_view.setText("+" + countryCode + " " + phone);
        dialog_input_mask = (RelativeLayout) findViewById(R.id.dialog_input_mask);
        mBack.setOnClickListener(this);
        resend.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.back_btn) {
            finish();
        } else if (resId == R.id.resend) {
            resendCode();
        } else if (resId == R.id.next) {
            checkCode();
        }
    }

    private Handler mHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_BUTTON_TEXT:
                    int time = msg.arg1;
                    resend.setText(String.valueOf(time));
                    if (time == 0) {
                        resend.setText(R.string.resend);
                        resend.setClickable(true);
                    }
                    if (time == 180) {
                        resend.setClickable(false);
                    }
                    break;
                default:
                    break;
            }

            return false;
        }
    });

    public void resendCode() {
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
        new GetPhoneCodeTask(countryCode, phone).execute();
    }

    public void checkCode() {
        String code = verify_code.getText().toString();
        if (code == null || code.equals("")) {
            T.showShort(mContext, R.string.input_vf_code);
            return;
        }

        showInputPwd(phone, countryCode, code);
    }

    public void changeButton() {
        new Thread() {
            @Override
            public void run() {
                int time = 180;
                while (time >= 0) {
                    Message change = new Message();
                    change.what = CHANGE_BUTTON_TEXT;
                    change.arg1 = time;
                    mHandler.sendMessage(change);
                    time--;
                    Utils.sleepThread(1000);
                }
            }
        }.start();
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
                    Intent i = new Intent();
                    i.setAction(Constants.Action.SESSION_ID_ERROR);
                    MyApp.app.sendBroadcast(i);
                    break;
                case NetManager.CONNECT_CHANGE:
                    new GetPhoneCodeTask(CountryCode, PhoneNO).execute();
                    return;
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
                    changeButton();
                    break;
            }
        }

    }

    public void showInputPwd(final String phone, final String countryCode,
                             final String checkCode) {
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
                        new SetAccountInfoTask(password, phone, countryCode,
                                checkCode).execute();
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
        private String checkCode;

        public SetAccountInfoTask(String password, String phone,
                                  String countryCode, String checkCode) {
            this.password = password;
            this.phone = phone;
            this.countryCode = countryCode;
            this.checkCode = checkCode;
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
                    account.sessionId, password, "1", checkCode);
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
                    new SetAccountInfoTask(password, phone, countryCode, checkCode)
                            .execute();
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
        return Constants.ActivityInfo.ACTIVITY_MODIFYACCOUNTPHONEACTIVITY2;
    }
}
