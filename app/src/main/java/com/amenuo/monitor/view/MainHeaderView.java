package com.amenuo.monitor.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.amenuo.monitor.R;
import com.amenuo.monitor.lib.CirclePageIndicator;

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

    private class MainHeaderAdapter extends FragmentPagerAdapter{

        public MainHeaderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return PageFragment.getInstance();
            }else{
                return Page2Fragment.getInstance();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class PageFragment extends Fragment {

        private static PageFragment instance;

        public static  PageFragment getInstance(){
            if(instance==null){
                instance = new PageFragment();
            }
            return instance;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            MainLumpView lumpView = new MainLumpView(this.getActivity());
            lumpView.setImageResource(R.drawable.main_weather);
            lumpView.setText("天气预报");
            return lumpView;
        }
    }

    public static class Page2Fragment extends Fragment {

        private static Page2Fragment instance;

        public static  Page2Fragment getInstance(){
            if(instance==null){
                instance = new Page2Fragment();
            }
            return instance;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            MainLumpView lumpView = new MainLumpView(this.getActivity());
            lumpView.setImageResource(R.drawable.main_weather);
            lumpView.setText("天气预报");
            return lumpView;
        }
    }
}
