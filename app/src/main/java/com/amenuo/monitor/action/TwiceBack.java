package com.amenuo.monitor.action;

/**
 * Created by laps on 6/12/16.
 */
public class TwiceBack {

    private static int intevalMillis = 1000;
    private int times = 0;
    private long lastPressTime = 0;

    public void backPress(){
        long now = System.currentTimeMillis();
        if (now - lastPressTime > intevalMillis){
            times = 0;
        }
        times ++;
        lastPressTime = now;
    }

    public boolean canBack(){
        return times >= 2;
    }
}
