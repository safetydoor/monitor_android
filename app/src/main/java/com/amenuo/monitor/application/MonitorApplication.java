package com.amenuo.monitor.application;

import android.app.Application;
import android.content.Context;

//import com.jwkj.global.MyApp;

import cn.smssdk.SMSSDK;

/**
 * Created by laps on 7/9/16.
 */
public class MonitorApplication extends Application {

    private static String APPKEY = "f3fc6baa9ac4";
    private static String APPSECRET = "7f3dedcb36d92deebcb373af921d635a";
    private static MonitorApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        application = this;
    }

    public static Context getContext(){
        if (application != null){
            return application.getApplicationContext();
        }
        return null;
    }
}
