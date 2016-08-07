package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.yoosee.R;
import com.jwkj.adapter.AboutPagerAdapter;
import com.jwkj.global.Constants;

public class AboutActivity extends BaseActivity implements OnClickListener {
	ImageView img_one, img_two, img_three;
	ImageView back;
	ViewPager pager;
	int current_pager = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		initComponent();
		loadPager();
	}

	public void initComponent() {
		img_one = (ImageView) findViewById(R.id.img_one);
		img_two = (ImageView) findViewById(R.id.img_two);
		img_three = (ImageView) findViewById(R.id.img_three);
		back = (ImageView) findViewById(R.id.back_btn);
		pager = (ViewPager) findViewById(R.id.about_pager);
		back.setOnClickListener(this);
	}

	public void loadPager() {
		List<View> views = new ArrayList<View>();
		ImageView one_bg = new ImageView(this);
		one_bg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		one_bg.setScaleType(ScaleType.FIT_CENTER);
		one_bg.setBackgroundResource(R.drawable.about_one);
		views.add(one_bg);

		ImageView two_bg = new ImageView(this);
		two_bg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		two_bg.setScaleType(ScaleType.FIT_CENTER);
		two_bg.setBackgroundResource(R.drawable.about_two);
		views.add(two_bg);

		ImageView three_bg = new ImageView(this);
		three_bg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		three_bg.setScaleType(ScaleType.FIT_CENTER);
		three_bg.setBackgroundResource(R.drawable.about_three);
		views.add(three_bg);
		AboutPagerAdapter adapter = new AboutPagerAdapter(views);

		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					img_one.setImageResource(R.drawable.about_bottom_p);
					img_two.setImageResource(R.drawable.about_bottom);
					img_three.setImageResource(R.drawable.about_bottom);
				} else if (position == 1) {
					img_one.setImageResource(R.drawable.about_bottom);
					img_two.setImageResource(R.drawable.about_bottom_p);
					img_three.setImageResource(R.drawable.about_bottom);
				} else if (position == 2) {
					img_one.setImageResource(R.drawable.about_bottom);
					img_two.setImageResource(R.drawable.about_bottom);
					img_three.setImageResource(R.drawable.about_bottom_p);
				}
			}

		});
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			finish();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ABOUTACTIVITY;
	}
}
