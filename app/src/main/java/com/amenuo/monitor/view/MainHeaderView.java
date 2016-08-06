package com.amenuo.monitor.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amenuo.monitor.R;
import com.amenuo.monitor.fragment.MainAdFragment;
import com.amenuo.monitor.fragment.MainWeatherFragment;
import com.amenuo.monitor.lib.CirclePageIndicator;
import com.amenuo.monitor.manager.AdManager;
import com.amenuo.monitor.model.AdModel;
import com.amenuo.monitor.utils.HttpRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by laps on 7/15/16.
 */
public class MainHeaderView extends FrameLayout {


    private CirclePageIndicator mPageIndicator;
    private ViewPager mViewPager;
    private MainHeaderAdapter mMainHeaderAdapter;

    public MainHeaderView(Context context) {
        super(context);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_main_header, this);

        FragmentActivity activity = (FragmentActivity) context;
        mMainHeaderAdapter = new MainHeaderAdapter(activity.getSupportFragmentManager());
        mViewPager = (ViewPager) root.findViewById(R.id.main_header_pager);
        mViewPager.setAdapter(mMainHeaderAdapter);

        mPageIndicator = (CirclePageIndicator) root.findViewById(R.id.main_header_indicator);
        mPageIndicator.setViewPager(mViewPager);
        resetPageIndicator();
        HttpRequest.requestAds(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Activity activity = (Activity) getContext();
                    JSONObject object = new JSONObject(json);
                    int code = object.getInt("code");
                    if (code != 0) {
                        String msg = object.getString("msg");
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        String result = object.getString("result");
                        AdManager.getInstance().saveJson(result);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMainHeaderAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resetPageIndicator();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void resetPageIndicator(){
        if (AdManager.getInstance().getAdList().size() == 0){
            mPageIndicator.setVisibility(View.GONE);
        }else{
            mPageIndicator.setVisibility(View.VISIBLE);
        }
    }

    public void setSlidingMenu(final SlidingMenu menu) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

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

    private class MainHeaderAdapter extends FragmentPagerAdapter {

        private List<AdModel> adList;

        public MainHeaderAdapter(FragmentManager fm) {
            super(fm);
            adList = AdManager.getInstance().getAdList();
        }

        @Override
        public void notifyDataSetChanged() {
            adList = AdManager.getInstance().getAdList();
            super.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MainWeatherFragment.getInstance();
            } else {
                MainAdFragment adFragment = MainAdFragment.getInstance();
                adFragment.setAd(adList.get(position -1));
                return adFragment;
            }
        }

        @Override
        public int getCount() {
            if (adList == null) {
                return 1;
            } else {
                return 1 + adList.size();
            }
        }
    }
}
