package com.amenuo.monitor.action;

import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by laps on 7/17/16.
 */
public class CountDown {

    private CallBack mCallback;
    private Timer mTimer;
    private TimerTask mTask;

    public void start(CallBack callBack){
        stop();
        this.mCallback = callBack;
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
//                message.what = MSG_WHAT_COUNTDOWN;
//                handler.sendMessage(message);
            }
        };
        mTimer.schedule(mTask, 1000, 1000);
    }

    public void stop(){

    }

    public interface CallBack{
        public void leftTime(int time);
    }
}
