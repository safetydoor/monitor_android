package com.amenuo.monitor.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.amenuo.monitor.R;
import com.amenuo.monitor.fragment.MainAdFragment;
import com.amenuo.monitor.fragment.MainWeatherFragment;
import com.amenuo.monitor.lib.CirclePageIndicator;
import com.amenuo.monitor.model.AdModel;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by laps on 7/15/16.
 */
public class MainHeaderView extends FrameLayout {


    private CirclePageIndicator mPageIndicator;
    private ViewPager mViewPager;
    public MainHeaderView(Context context) {
        super(context);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_main_header, this);

        FragmentActivity activity = (FragmentActivity)context;
        mViewPager = (ViewPager)root.findViewById(R.id.main_header_pager);
        mViewPager.setAdapter(new MainHeaderAdapter(activity.getSupportFragmentManager()));

        mPageIndicator = (CirclePageIndicator)root.findViewById(R.id.main_header_indicator);
        mPageIndicator.setViewPager(mViewPager);
    }

    public void setSlidingMenu(final SlidingMenu menu){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) { }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    default:
                        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                        break;
                }
            }

        });
    }

    private class MainHeaderAdapter extends FragmentPagerAdapter{

        public MainHeaderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return MainWeatherFragment.getInstance();
            }else{
                MainAdFragment adFragment = MainAdFragment.getInstance();
                AdModel adModel = new AdModel(
                        "点击进入广告",
                        "http://www.tmall.com",
                        "http://img5.imgtn.bdimg.com/it/u=1319787381,824476088&fm=21&gp=0.jpg");
                adFragment.setAd(adModel);
                return adFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
