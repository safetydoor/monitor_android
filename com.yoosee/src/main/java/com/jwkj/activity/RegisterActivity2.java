package com.jwkj.activity;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;

public class RegisterActivity2 extends BaseActivity implements OnClickListener {
    private String count;
    private String phone;
    private String code;
    private boolean isEmailRegister;
    private EditText email, pwd, confirm_pwd;
    private RelativeLayout layout_email;
    Button register;
    boolean isDialogCanel = false;
    private Context context;
    NormalDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form2);
        context = this;
        isEmailRegister = getIntent().getBooleanExtra("isEmailRegister", false);
        if (!isEmailRegister) {
            count = getIntent().getStringExtra("count");
            phone = getIntent().getStringExtra("phone");
            code = getIntent().getStringExtra("code");
        }
        initComponent();
    }

    public void initComponent() {
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        confirm_pwd = (EditText) findViewById(R.id.confirm_pwd);
        layout_email = (RelativeLayout) findViewById(R.id.layout_email);
        register = (Button) findViewById(R.id.register);
        if (isEmailRegister) {
            layout_email.setVisibility(RelativeLayout.VISIBLE);
        } else {
            if (count.equals("86")) {
                layout_email.setVisibility(RelativeLayout.GONE);
            } else {
                layout_email.setVisibility(RelativeLayout.VISIBLE);
            }
        }
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (view.getId() == R.id.register) {
            register();
        }
    }

    private void register() {
        final String email_str = email.getText().toString();
        final String pwd_str = pwd.getText().toString();
        final String confirm_pwd_str = confirm_pwd.getText().toString();
        if (isEmailRegister || !count.equals("86")) {
            if (null == email_str || "".equals(email_str)) {
                T.showShort(this, R.string.input_email);
                return;
            }

            if (email_str.length() > 32 || email_str.length() < 3) {
                T.showShort(this, R.string.email_too_long);
                return;
            }
        }

        if (null == pwd_str || "".equals(pwd_str)) {
            T.showShort(this, R.string.inputpassword);
            return;
        }

        if (pwd_str.length() > 27) {
            T.showShort(this, R.string.password_length_error);
            return;
        }

        if (null == confirm_pwd_str || "".equals(confirm_pwd_str)) {
            T.showShort(this, R.string.reinputpassword);
            return;
        }

        if (!pwd_str.equals(confirm_pwd_str)) {
            T.showShort(this, R.string.differentpassword);
            return;
        }

        dialog = new NormalDialog(this, this.getResources().getString(
                R.string.registering), "", "", "");
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
        if (isEmailRegister) {
            new RegisterTask("1", email_str, count, "", pwd_str,
                    confirm_pwd_str, code, "1").execute();
        } else {
            if (count.equals("86")) {
                new RegisterTask("1", "", count, phone, pwd_str,
                        confirm_pwd_str, code, "1").execute();
            } else {
                new RegisterTask("1", email_str, count, phone, pwd_str,
                        confirm_pwd_str, "", "1").execute();
            }
        }

    }

    class RegisterTask extends AsyncTask {
        String VersionFlag;
        String Email;
        String CountryCode;
        String PhoneNO;
        String Pwd;
        String RePwd;
        String VerifyCode;
        String IgnoreSafeWarning;

        public RegisterTask(String VersionFlag, String Email,
                            String CountryCode, String PhoneNO, String Pwd, String RePwd,
                            String VerifyCode, String IgnoreSafeWarning) {
            this.VersionFlag = VersionFlag;
            this.Email = Email;
            this.CountryCode = CountryCode;
            this.PhoneNO = PhoneNO;
            this.Pwd = Pwd;
            this.RePwd = RePwd;
            this.VerifyCode = VerifyCode;
            this.IgnoreSafeWarning = IgnoreSafeWarning;
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            Utils.sleepThread(1000);
            return NetManager.getInstance(context).register(VersionFlag, Email,
                    CountryCode, PhoneNO, Pwd, RePwd, VerifyCode,
                    IgnoreSafeWarning);
        }

        @Override
        protected void onPostExecute(Object object) {
            // TODO Auto-generated method stub
            RegisterResult result = NetManager
                    .createRegisterResult((JSONObject) object);
            switch (Integer.parseInt(result.error_code)) {
                case NetManager.SESSION_ID_ERROR:
                    Intent relogin = new Intent();
                    relogin.setAction(Constants.Action.SESSION_ID_ERROR);
                    MyApp.app.sendBroadcast(relogin);
                    break;
                case NetManager.CONNECT_CHANGE:
                    new RegisterTask(VersionFlag, Email, CountryCode, PhoneNO, Pwd,
                            RePwd, VerifyCode, IgnoreSafeWarning).execute();
                    return;
                case NetManager.REGISTER_SUCCESS:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        if (isEmailRegister) {
                            Intent i = new Intent();
                            i.setAction(Constants.Action.REPLACE_EMAIL_LOGIN);
                            i.putExtra("username", Email);
                            i.putExtra("password", Pwd);
                            context.sendBroadcast(i);
                            finish();
                        } else {
                            Intent i = new Intent();
                            i.setAction(Constants.Action.REPLACE_PHONE_LOGIN);
                            i.putExtra("username", PhoneNO);
                            i.putExtra("password", Pwd);
                            i.putExtra("code", CountryCode);
                            context.sendBroadcast(i);
                            finish();
                        }
                    }
                    break;
                case NetManager.REGISTER_EMAIL_USED:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        Utils.showPromptDialog(context, R.string.prompt,
                                R.string.email_used);
                    }
                    break;
                case NetManager.REGISTER_EMAIL_FORMAT_ERROR:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        Utils.showPromptDialog(context, R.string.prompt,
                                R.string.email_format_error);
                    }
                    break;
                case NetManager.REGISTER_PASSWORD_NO_MATCH:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }

                    break;

                default:
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!isDialogCanel) {
                        T.showShort(context, R.string.operator_error);
                    }
                    break;
            }
        }

    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_REGISTERACTIVITY2;
    }
}
