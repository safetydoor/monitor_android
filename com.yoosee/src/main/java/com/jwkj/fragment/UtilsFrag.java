package com.jwkj.fragment;

import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.activity.AlarmSetActivity;
import com.jwkj.activity.ImageBrowser;
import com.jwkj.activity.MainActivity;
import com.jwkj.activity.QRcodeActivity;
import com.jwkj.adapter.ImageBrowserAdapter;

import com.jwkj.global.NpcCommon;

public class UtilsFrag extends BaseFragment implements OnClickListener {
	private Context mContext;
	File[] files;
	GridView list;
	ImageBrowserAdapter adapter;
	int screenWidth, screenHeight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_utils, container, false);
		mContext = MainActivity.mContext;
		if (null == files) {
			files = new File[0];
		}
		initComponent(view);
		return view;

	}

	public void initComponent(View view) {
		list = (GridView) view.findViewById(R.id.list_grid);
		DisplayMetrics dm = new DisplayMetrics();
		adapter = new ImageBrowserAdapter(mContext);
		list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

	}
}
