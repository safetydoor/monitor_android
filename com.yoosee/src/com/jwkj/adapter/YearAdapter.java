package com.jwkj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;

public class YearAdapter extends BaseAdapter {
	String[] data;
	Context context;

	public YearAdapter(Context context, String[] data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data[arg0 % data.length];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout view = (RelativeLayout) arg1;
		if (null == view) {

			view = (RelativeLayout) (LayoutInflater.from(context).inflate(
					R.layout.list_date_item, null));
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return true;
				}

			});
		}

		TextView text = (TextView) view.findViewById(R.id.text);
		text.setClickable(false);
		text.setFocusable(false);
		text.setText(data[arg0 % data.length]);
		return view;
	}

}
