package com.amenuo.monitor.task;

import android.content.Intent;
import android.os.AsyncTask;

import com.amenuo.monitor.application.MonitorApplication;
import com.amenuo.monitor.utils.HttpRequest;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;

import org.json.JSONObject;

/**
 * Created by laps on 7/17/16.
 */
public class RegisterTask extends AsyncTask<String, Void, Object> {

    private Callback mCallback;

    String phoneNumber;
    String password;
    String verifyCode;

    public RegisterTask(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    protected Object doInBackground(String... params) {

        String versionFlag = "1";
        String email = "";
        String countryCode = "86";
        this.phoneNumber = params[0];
        this.password = params[1];
        String rePassword = this.password;
        this.verifyCode = params[2];
        String ignoreSafeWarning = "1";
        return NetManager.getInstance(MonitorApplication.getContext()).register(versionFlag, email,
                countryCode, phoneNumber, password, rePassword, verifyCode,
                ignoreSafeWarning);
    }

    @Override
    protected void onPostExecute(Object object) {
        // TODO Auto-generated method stub
        RegisterResult result = NetManager
                .createRegisterResult((JSONObject) object);
        switch (Integer.parseInt(result.error_code)) {
            case NetManager.CONNECT_CHANGE:
                new RegisterTask(mCallback).execute(phoneNumber, password, verifyCode);
                return;
            case NetManager.REGISTER_SUCCESS:
                if (mCallback != null) {
                    mCallback.onRegisterResult(true);
                    HttpRequest.requestRegister(phoneNumber, password, null);
                }
                break;

            default:
                if (mCallback != null) {
                    mCallback.onRegisterResult(false);
                }
                break;
        }
    }

    @Override
    protected void onCancelled() {
        if (mCallback != null) {
            mCallback.onRegisterResult(false);
        }
    }

    public interface Callback {

        public void onRegisterResult(boolean success);
    }

}
