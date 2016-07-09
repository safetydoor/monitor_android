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
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.NetManager;

public class VerifyPhoneActivity extends BaseActivity implements
        OnClickListener {
    private String count;
    private String phone;
    private TextView phone_view;
    private EditText verify_code;
    private Button resend, next;
    boolean isDialogCanel = false;
    NormalDialog dialog;
    private Context mContext;
    public static final int CHANGE_BUTTON_TEXT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone);
        mContext = this;
        count = getIntent().getStringExtra("count");
        phone = getIntent().getStringExtra("phone");
        initCompent();
        changeButton();
    }

    public void initCompent() {
        phone_view = (TextView) findViewById(R.id.phone);
        verify_code = (EditText) findViewById(R.id.verify_code);
        resend = (Button) findViewById(R.id.resend);
        next = (Button) findViewById(R.id.next);

        phone_view.setText("+" + count + " " + phone);
        resend.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.resend) {
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
        new GetPhoneCodeTask(count, phone).execute();
    }

    public void checkCode() {
        String code = verify_code.getText().toString();
        if (code == null || code.equals("")) {
            T.showShort(mContext, R.string.input_vf_code);
            return;
        }
        dialog = new NormalDialog(this, this.getResources().getString(
                R.string.verifing), "", "", "");
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
        new VerifyCodeTask(count, phone, code).execute();
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

    class VerifyCodeTask extends AsyncTask {
        String countryCode;
        String phoneNO;
        String code;

        public VerifyCodeTask(String countryCode, String phoneNO, String code) {
            this.countryCode = countryCode;
            this.phoneNO = phoneNO;
            this.code = code;
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            Utils.sleepThread(1000);
            return NetManager.getInstance(mContext).verifyPhoneCode(
                    countryCode, phoneNO, code);
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
                    new VerifyCodeTask(countryCode, phoneNO, code).execute();
                    return;
                case NetManager.VERIFY_CODE_SUCCESS:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        Intent i = new Intent(mContext, RegisterActivity2.class);
                        i.putExtra("phone", phone);
                        i.putExtra("count", count);
                        i.putExtra("code", code);
                        startActivity(i);
                        finish();
                    }
                    break;
                case NetManager.VERIFY_CODE_ERROR:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        Utils.showPromptDialog(mContext, R.string.prompt,
                                R.string.vfcode_error);
                    }
                    break;
                case NetManager.VERIFY_CODE_TIME_OUT:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        Utils.showPromptDialog(mContext, R.string.prompt,
                                R.string.vfcode_timeout);
                    }
                    break;
                default:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        T.showShort(mContext, R.string.operator_error);
                    }
                    break;
            }
        }

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

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_VERIFYPHONEACTIVITY;
    }
}
