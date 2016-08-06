package com.amenuo.monitor.task;

import android.os.AsyncTask;

import com.amenuo.monitor.action.LoginStateAction;
import com.amenuo.monitor.application.MonitorApplication;
import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;

import org.json.JSONObject;

/**
 * Created by laps on 7/17/16.
 */
public class LoginTask extends AsyncTask<String, Void, Object> {

    private Callback mCallback;
    private String mUserName;
    private String mPassWord;

    public LoginTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(String... params) {

        try {
            String userName = params[0].toString();
            String passWord = params[1].toString();
            this.mUserName = userName;
            this.mPassWord = passWord;
            return NetManager.getInstance(MonitorApplication.getContext()).login("+86-" + userName, passWord);
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Object object) {
        if (object == null){
            return;
        }
        LoginResult result = NetManager
                .createLoginResult((JSONObject) object);
        int code = Integer.parseInt(result.error_code);
        if (code == NetManager.LOGIN_SUCCESS) {
            LoginStateAction.saveState(this.mUserName, this.mPassWord, result);
            mCallback.onLoginResult(true);
        } else if (code == NetManager.CONNECT_CHANGE) {
            new LoginTask(this.mCallback).execute(this.mUserName, this.mPassWord);
        } else {
            mCallback.onLoginResult(false);
        }
    }

    @Override
    protected void onCancelled() {
        mCallback.onLoginResult(false);
    }

    public interface Callback {

        public void onLoginResult(boolean success);
    }
}
