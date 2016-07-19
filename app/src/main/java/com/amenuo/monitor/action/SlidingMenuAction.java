package com.amenuo.monitor.action;

import android.app.Activity;
import android.view.View;

import com.amenuo.monitor.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by laps on 7/17/16.
 */
public class SlidingMenuAction {

    private SlidingMenu mSlidingMenu;

    public SlidingMenuAction(Activity activity) {
        mSlidingMenu = new SlidingMenu(activity);
        mSlidingMenu = new SlidingMenu(activity);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setShadowWidthRes(R.dimen.main_sliding_left_shadow);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.main_sliding_left);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
    }

    public SlidingMenu getSlidingMenu(){
        return mSlidingMenu;
    }

    public void setMenu(int layoutId) {
        if (mSlidingMenu != null) {
             mSlidingMenu.setMenu(layoutId);
        }
    }

    public void setMenu(View view) {
        if (mSlidingMenu != null) {
            mSlidingMenu.setMenu(view);
        }
    }

    public void addIgnoreView(View view) {
        if (mSlidingMenu != null) {
            mSlidingMenu.addIgnoredView(view);
        }
    }

    public void toogle() {
        if (mSlidingMenu != null) {
            mSlidingMenu.toggle();
        }
    }
}
