package com.amenuo.monitor.task;

import android.os.AsyncTask;

import com.amenuo.monitor.action.LoginStateAction;

/**
 * Created by laps on 7/17/16.
 */
public class LoginTask extends AsyncTask<String, Void, Boolean> {

    private Callback mCallback;

    public LoginTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            String phoneNumber = params[0].toString();
            String password = params[1].toString();
            Thread.sleep(2000);
            if (phoneNumber.equals("13400000001") && password.equals("a123456")) {
                return true;
            }
        } catch (InterruptedException e) {
            return false;
        }

        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            LoginStateAction.saveState();
            mCallback.onLoginResult(true);
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
