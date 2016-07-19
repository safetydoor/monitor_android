package com.amenuo.monitor.action;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by laps on 7/17/16.
 */
public class FullScreenAction {

    public static void setToFullScreen(Activity activity){
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window=activity.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }
}
