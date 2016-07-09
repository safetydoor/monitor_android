package com.jwkj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.jwkj.widget.MyInputDialog;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.network.NetManager;

public class UnbindPhoneActivity extends BaseActivity implements
        OnClickListener {
    ImageView back_btn;
    Button unbind;
    Context mContext;
    NormalDialog dialog;
    MyInputDialog dialog_input;
    RelativeLayout dialog_input_mask;
    TextView phone_text;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_unbind_phone);
        mContext = this;
        initComponent();
    }

    public void initComponent() {

        back_btn = (ImageView) findViewById(R.id.back_btn);
        unbind = (Button) findViewById(R.id.unbind);
        phone_text = (TextView) findViewById(R.id.phone_text);
        dialog_input_mask = (RelativeLayout) findViewById(R.id.dialog_input_mask);

        Account account = AccountPersist.getInstance().getActiveAccountInfo(
                mContext);
        phone_text.setText("+" + account.countryCode + "-" + account.phone);
        unbind.setOnClickListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int resId = v.getId();
        if (resId == R.id.back_btn) {
            finish();
        } else if (resId == R.id.unbind) {
            showInputPwd("0", "0", "0");
        }
    }

    public void showInputPwd(final String phone, final String countryCode,
                             final String checkCode) {
        dialog_input = new MyInputDialog(mContext);
        dialog_input.setTitle(mContext.getResources().getString(
                R.string.unbind_phone));
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
                    account.phone = "";
                    account.countryCode = "";
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
        return Constants.ActivityInfo.ACTIVITY_UNBINDPHONEACTIVITY;
    }
}
