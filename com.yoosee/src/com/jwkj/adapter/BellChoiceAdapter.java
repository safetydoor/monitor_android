package com.jwkj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;

public class BellChoiceAdapter extends BaseAdapter {
	private Context context;
	public int checkedId = -1;
	public BellChoiceAdapter ba;
	private ArrayList<HashMap<String, String>> bells;

	public BellChoiceAdapter(Context context,
			ArrayList<HashMap<String, String>> bells) {
		this.context = context;
		this.bells = bells;
		ba = this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (bells == null) {
			return 0;
		} else {
			return bells.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return bells.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout item = (RelativeLayout) view;
		if (null == item) {
			item = (RelativeLayout) LayoutInflater.from(context).inflate(
					R.layout.choice_bell_list_item, null);
			item.setTag(item);
		} else {
			item = (RelativeLayout) item.getTag();
		}
		TextView bellName = (TextView) item.findViewById(R.id.bellName);
		final HashMap<String, String> bellinfo = bells.get(arg0);
		final RadioButton button = (RadioButton) item
				.findViewById(R.id.checkButton);
		bellName.setText(bellinfo.get("bellName"));
		final String bellId = bellinfo.get("bellId");
		if (bellId.equals(String.valueOf(checkedId))) {
			button.setChecked(true);
		} else {
			button.setChecked(false);
		}

		return item;
	}

	public void setCheckedId(int id) {
		this.checkedId = id;
	}
}
