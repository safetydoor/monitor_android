package com.jwkj.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.adapter.SysMsgAdapter;
import com.jwkj.data.DataManager;
import com.jwkj.data.SysMessage;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.lib.slideexpandable.ActionSlideExpandableListView;

public class SysMsgActivity extends BaseActivity implements OnClickListener {
	ActionSlideExpandableListView list;
	ImageView back;
	SysMsgAdapter adapter;
	boolean isRegReceiver = false;
	Context mContext;
	public static final String REFRESH = "com.jwkj.REFRESH";
	public static final String DELETE_REFESH = "com.jwkj.DELETE_REFESH";

	@Override
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		this.setContentView(R.layout.activity_sysmsg);
		mContext = this;

		initComponent();
		regFilter();
	}

	public void initComponent() {
		list = (ActionSlideExpandableListView) this
				.findViewById(R.id.list_sys_msg);
		back = (ImageView) findViewById(R.id.back_btn);

		back.setOnClickListener(this);
		List<SysMessage> data = DataManager.findSysMessageByActiveUser(
				mContext, NpcCommon.mThreeNum);
		adapter = new SysMsgAdapter(mContext, data);
		list.setAdapter(adapter);

		// listen for events in the two buttons for every list item.
		// the 'position' var will tell which list item is clicked
		list.setItemClickListener(new ActionSlideExpandableListView.OnItemClickListener() {

			@Override
			public void OnClick(int position, int type) {
				// TODO Auto-generated method stub
				adapter.upDateSysMsg(position, type);
			}
		});

		list.setItemActionListener(
				new ActionSlideExpandableListView.OnActionClickListener() {

					@Override
					public void onClick(View listView, View buttonview,
							int position) {

						int id = buttonview.getId();
						if (id == R.id.content) {
						}
					}

					// note that we also add 1 or more ids to the
					// setItemActionListener
					// this is needed in order for the listview to discover the
					// buttons
				}, R.id.content);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(REFRESH);
		filter.addAction(DELETE_REFESH);
		this.registerReceiver(receiver, filter);
		isRegReceiver = true;
	}

	public BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(REFRESH)) {
				adapter.refresh();
			} else if (intent.getAction().equals(DELETE_REFESH)) {
				List<SysMessage> data = DataManager.findSysMessageByActiveUser(
						mContext, NpcCommon.mThreeNum);
				adapter = null;
				adapter = new SysMsgAdapter(mContext, data);
				list.setAdapter(adapter);
			}
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegReceiver) {
			unregisterReceiver(receiver);
		}
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
		return Constants.ActivityInfo.ACTIVITY_SYSMSGACTIVITY;
	}

}
