package com.amenuo.monitor.action;

import android.os.Handler;
import android.os.Message;

import com.amenuo.monitor.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by laps on 7/17/16.
 */
public class CountDownAction {

    private Handler mHandler;
    private Timer mTimer;
    private TimerTask mTask;

    public CountDownAction(Handler handler){
        this.mHandler = handler;
    }

    public void start(){
        stop();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = Constants.MSG_WHAT_COUNTDOWN;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTask, 1000, 1000);
    }

    public void stop(){
        if (mTask != null) {
            mTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTask = null;
        mTimer = null;
    }

}
