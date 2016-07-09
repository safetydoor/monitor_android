package com.jwkj.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class AboutPagerAdapter extends PagerAdapter {
	private List<View> views;

	public AboutPagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
