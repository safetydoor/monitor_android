package com.amenuo.monitor.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.amenuo.monitor.R;
import com.amenuo.monitor.activity.MainPageActivity;

/**
 * Created by laps on 7/17/16.
 */
public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private Context mContext;

    LoginTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {


        return false;
    }


}
