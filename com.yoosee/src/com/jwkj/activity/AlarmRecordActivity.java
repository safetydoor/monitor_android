package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.adapter.AlarmRecordAapter2;
import com.jwkj.adapter.AlarmRecordAdapter;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.lib.pullToRefresh.PullToRefreshBase;
import com.lib.pullToRefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.lib.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.lib.pullToRefresh.PullToRefreshListView;
import com.p2p.core.network.AlarmRecordResult;
import com.p2p.core.network.AlarmRecordResult.SAlarmRecord;
import com.p2p.core.network.GetAlarmRecordListResult;
import com.p2p.core.network.NetManager;

public class AlarmRecordActivity extends BaseActivity implements
		OnClickListener {
	ImageView back_btn, clear_btn;
	ListView list_record;
	AlertDialog mExitDialog;
	AlarmRecordAdapter adapter;
	Context context;
	private boolean isRegFilter = false;
	List<AlarmRecord> list;
	public static AlarmRecordAapter2 adapter2;
	ListView mlistview;
	PullToRefreshListView mpull_refresh_list;
	RelativeLayout layout_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_record);
		context = this;
		initComponent();
		regFilter();
		Account acount = AccountPersist.getInstance().getActiveAccountInfo(
				context);
		// new alarmTask().execute();
		Log.e("my", "AlarmRecordActivity");
	}

	public void initComponent() {
		back_btn = (ImageView) findViewById(R.id.back_btn);
		clear_btn = (ImageView) findViewById(R.id.clear);
		list_record = (ListView) findViewById(R.id.list_allarm);
		mpull_refresh_list = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);
		// mpull_refresh_list
		// .setOnRefreshListener(new OnRefreshListener<ListView>() {
		// @Override
		// public void onRefresh(
		// PullToRefreshBase<ListView> refreshView) {
		// String label = DateUtils.formatDateTime(context,
		// System.currentTimeMillis(),
		// DateUtils.FORMAT_SHOW_TIME
		// | DateUtils.FORMAT_SHOW_DATE
		// | DateUtils.FORMAT_ABBREV_ALL);
		//
		// // Update the LastUpdatedLabel
		// refreshView.getLoadingLayoutProxy()
		// .setLastUpdatedLabel(label);
		//
		// // Do work to refresh the list here.
		// new alarmTask().execute();
		//
		// }
		// });
		// mpull_refresh_list
		// .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
		//
		// @Override
		// public void onLastItemVisible() {
		// String index = adapter2.getLastIndex();
		// if(Integer.parseInt(index)>1){
		// layout_loading.setVisibility(RelativeLayout.VISIBLE);
		// Animation anim = AnimationUtils.loadAnimation(context,
		// android.R.anim.fade_in);
		// anim.setDuration(500);
		// layout_loading.startAnimation(anim);
		// new GetHistoryDataTask(index).execute();
		// }else{
		// layout_loading.setVisibility(RelativeLayout.GONE);
		// T.showShort(context, "已经加载完");
		// }
		// }
		//
		// });
		// mpull_refresh_list.setShowIndicator(false);
		// mlistview = mpull_refresh_list.getRefreshableView();
		// adapter2 = new AlarmRecordAapter2(context);
		// mlistview.setAdapter(adapter2);
		clear_btn.setOnClickListener(this);
		back_btn.setOnClickListener(this);

		list = DataManager.findAlarmRecordByActiveUser(context,
				NpcCommon.mThreeNum);
		adapter = new AlarmRecordAdapter(this, list);
		list_record.setAdapter(adapter);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.REFRESH_ALARM_RECORD);
		context.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.REFRESH_ALARM_RECORD)) {
				adapter.updateData();
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			this.finish();
		} else if (id == R.id.clear) {
			NormalDialog dialog = new NormalDialog(context, context
					.getResources().getString(R.string.delete_alarm_records),
					context.getResources().getString(R.string.confirm_clear),
					context.getResources().getString(R.string.clear), context
							.getResources().getString(R.string.cancel));
			dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					DataManager.clearAlarmRecord(context, NpcCommon.mThreeNum);
					adapter.updateData();
					adapter.notifyDataSetChanged();
				}
			});
			dialog.showDialog();
		}
	}

	// class alarmTask extends AsyncTask {
	// @Override
	// protected Object doInBackground(Object... arg0) {
	// Account acount;
	// try {
	// acount = AccountPersist.getInstance().getActiveAccountInfo(
	// context);
	// } catch (Exception e) {
	// return null;
	// }
	// return NetManager.getInstance(context).getAlarmMessage(
	// acount.three_number, acount.sessionId, 20, 2);
	// }
	//
	// @Override
	// protected void onPostExecute(Object object) {
	// mpull_refresh_list.onRefreshComplete();
	// AlarmRecordResult result = NetManager
	// .getAlarmRecords((JSONObject) object);
	// Log.e("error_code", result.error_code + "-------");
	// switch (Integer.parseInt(result.error_code)) {
	// case NetManager.SESSION_ID_ERROR:
	// Intent i = new Intent();
	// i.setAction(Constants.Action.SESSION_ID_ERROR);
	// context.sendBroadcast(i);
	// break;
	// case NetManager.CONNECT_CHANGE:
	// new alarmTask().execute();
	// break;
	// case NetManager.GET_ALARM_RECORD_SUCCESS:
	// List<SAlarmRecord> alarmRecords = result.alarmRecords;
	// for (SAlarmRecord s : alarmRecords) {
	// Log.e("alarmrecord", "messgeIds=" + s.messgeId
	// + "sourceId=" + s.sourceId + "pictureUrl="
	// + s.pictureUrl + "alarmTime=" + s.alarmTime
	// + "alarmType=" + s.alarmType + "defenceArea="
	// + s.defenceArea + "channel=" + s.channel
	// + "serverReceiveTime=" + s.serverReceiveTime);
	// }
	// adapter2.updateNewDate(alarmRecords);
	// break;
	// default:
	// break;
	// }
	// }
	// }
	//
	// private class GetHistoryDataTask extends AsyncTask {
	//
	// private String index;
	//
	// public GetHistoryDataTask(String index) {
	// this.index = index;
	// }
	//
	// @Override
	// protected Object doInBackground(Object... arg0) {
	// // Simulates a background job.
	// Account account = AccountPersist.getInstance()
	// .getActiveAccountInfo(context);
	// return NetManager.getInstance(context).getAlarmRecordList(
	// account.three_number, account.sessionId, index, 20, 1);
	// }
	//
	// @Override
	// protected void onPostExecute(Object object) {
	// // mListItems.addFirst("Added after refresh...");
	// // Call onRefreshComplete when the list has been refreshed.
	// AlarmRecordResult result = NetManager
	// .getAlarmRecords((JSONObject) object);
	//
	// switch (Integer.parseInt(result.error_code)) {
	// case NetManager.SESSION_ID_ERROR:
	// Intent i = new Intent();
	// i.setAction(Constants.Action.SESSION_ID_ERROR);
	// MyApp.app.sendBroadcast(i);
	// break;
	// case NetManager.CONNECT_CHANGE:
	// new GetHistoryDataTask(index).execute();
	// break;
	// case NetManager.GET_ALARM_RECORD_SUCCESS:
	// List<SAlarmRecord> alarmrecord = result.alarmRecords;
	// for (SAlarmRecord s : alarmrecord) {
	// Log.e("alarmrecord", "----"+"messgeIds=" + s.messgeId
	// + "sourceId=" + s.sourceId + "pictureUrl="
	// + s.pictureUrl + "alarmTime=" + s.alarmTime
	// + "alarmType=" + s.alarmType + "defenceArea="
	// + s.defenceArea + "channel=" + s.channel
	// + "serverReceiveTime=" + s.serverReceiveTime);
	// }
	// layout_loading.setVisibility(RelativeLayout.GONE);
	// adapter2.updateHistoryData(alarmrecord);
	// break;
	// }
	// }
	//
	// }

	@Override
	protected void onResume() {
		super.onResume();
		// adapter2.runImageThread();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			context.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
		// adapter2.stopImageThread();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ALARMRECORDACTIVITY;
	}
}
