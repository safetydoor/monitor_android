package com.jwkj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwkj.wheel.widget.AbstractWheelAdapter;
import com.yoosee.R;

public class DataTimeZoneAdapter extends AbstractWheelAdapter {
	Context context;
	int start;
	int end;
//  设备给的时区
//	static float fTimeZoneVal[MAX_TIMEZONE_NUM] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,16.5,14.5,15.5,17.5,20.5,7.5};
	double [] half_time_zone={-11,-10,-9,-8,-7,-6,-5,-4,-3.5,-3,-2,-1,0,1,2,3,3.5,4,4.5,5,5.5,6,6.5,7,8,9,9.5,10,11,12};
//	对应发的下标
	int[] time={0,1,2,3,4,5,6,7,29,8,9,10,11,12,13,14,25,15,26,16,24,17,27,18,19,20,28,21,22,23};
	double [] half_zone={5.5,3.5,4.5,6.5,9.5,-3.5};
	public DataTimeZoneAdapter(Context context, int start, int end) {
		this.context = context;
		this.start = start;
		this.end = end;
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return 30;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_wheel_date_item, null);
		}
		TextView text = (TextView) view.findViewById(R.id.text);
		String time_zone=String.valueOf(half_time_zone[index]);
		String []zones=time_zone.split("\\.");
		if(zones.length>=2){
			if(!zones[1].equals("5")){
				int time=(int) half_time_zone[index];
				time_zone=String.valueOf(time);
			}
		}
	    text.setText(time_zone);
		return view;
	}
		

}
