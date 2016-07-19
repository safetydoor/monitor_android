package com.amenuo.monitor.task;

import android.os.AsyncTask;

/**
 * Created by laps on 7/17/16.
 */
public class RegisterTask extends AsyncTask<String, Void, Boolean>{

    private Callback mCallback;

    public RegisterTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            String phoneNumber = params[0].toString();
            String verificationCode = params[1].toString();
            String password = params[2].toString();
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
            mCallback.onRegisterResult(true);
        } else {
            mCallback.onRegisterResult(false);
        }
    }

    @Override
    protected void onCancelled() {
        mCallback.onRegisterResult(false);
    }

    public interface Callback {

        public void onRegisterResult(boolean success);
    }

}
