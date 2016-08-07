package com.jwkj.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.adapter.BellChoiceAdapter;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.data.SystemDataManager;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;

public class SettingBellRingActivity extends BaseActivity implements
		OnClickListener {
	Button save_btn;
	ImageView back_btn;
	ListView list_sys_bell;
	Vibrator vibrator;
	MediaPlayer player;
	RelativeLayout set_bellRing_btn, set_sd_bell_btn;
	Context context;
	MyReceiver receiver;
	TextView selectBell;
	boolean myreceiverIsReg = false;
	BellChoiceAdapter adapter;
	int checkedId;
	int selectPos;
	int vibrateState;
	int bellType;

	int settingType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_bell_ring);
		context = this;
		settingType = getIntent().getIntExtra("type", 0);

		initCompent();
		registerMonitor();
		initSelectMusicName();
	}

	public void initCompent() {
		player = new MediaPlayer();
		back_btn = (ImageView) findViewById(R.id.back_btn);
		save_btn = (Button) findViewById(R.id.save);
		set_sd_bell_btn = (RelativeLayout) findViewById(R.id.set_sd_bell_btn);
		selectBell = (TextView) findViewById(R.id.selectBell);
		list_sys_bell = (ListView) findViewById(R.id.list_sys_bell);
		initSelectState();
		ArrayList<HashMap<String, String>> bells = SystemDataManager
				.getInstance().getSysBells(this);
		adapter = new BellChoiceAdapter(this, bells);
		adapter.setCheckedId(checkedId);
		list_sys_bell.setAdapter(adapter);
		list_sys_bell.setSelection(selectPos);
		list_sys_bell.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> data = (HashMap<String, String>) adapter
						.getItem(arg2);
				int id = Integer.parseInt(data.get("bellId"));
				checkedId = id;
				selectPos = arg2;
				adapter.setCheckedId(id);
				adapter.notifyDataSetChanged();
				playMusic(checkedId);
			}

		});

		set_sd_bell_btn.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		back_btn.setOnClickListener(this);
	}

	public void initSelectState() {
		if (settingType == SettingSystemActivity.SET_TYPE_COMMING_RING) {
			selectPos = SharedPreferencesManager.getInstance()
					.getCBellSelectPos(this);
			bellType = SharedPreferencesManager.getInstance()
					.getCBellType(this);

			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				checkedId = SharedPreferencesManager.getInstance()
						.getCSystemBellId(this);
				selectBell.setText("");
			} else {
				checkedId = SharedPreferencesManager.getInstance()
						.getCSdBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSdBellById(context, checkedId);
				if (null != data) {
					selectBell.setText(data.get("bellName"));
				}
				checkedId = -1;
				selectPos = 1;
			}
		} else if (settingType == SettingSystemActivity.SET_TYPE_ALLARM_RING) {
			selectPos = SharedPreferencesManager.getInstance()
					.getABellSelectPos(this);
			bellType = SharedPreferencesManager.getInstance()
					.getABellType(this);

			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				checkedId = SharedPreferencesManager.getInstance()
						.getASystemBellId(this);
				selectBell.setText("");
			} else {
				checkedId = SharedPreferencesManager.getInstance()
						.getASdBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSdBellById(context, checkedId);
				if (null != data) {
					selectBell.setText(data.get("bellName"));
				}
				checkedId = -1;
				selectPos = 1;
			}
		}

	}

	public void registerMonitor() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SettingSystemActivity.ACTION_CHANGEBELL);
		receiver = new MyReceiver();
		this.registerReceiver(receiver, filter);
		myreceiverIsReg = true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			this.finish();
		} else if (id == R.id.save) {
			if (checkedId == -1) {
				T.showShort(this, R.string.savebell_error);
			} else {
				if (settingType == SettingSystemActivity.SET_TYPE_COMMING_RING) {
					SharedPreferencesManager.getInstance().putCSystemBellId(
							checkedId, this);
					SharedPreferencesManager.getInstance().putCBellSelectPos(
							selectPos, this);
					SharedPreferencesManager.getInstance().putCBellType(
							SharedPreferencesManager.TYPE_BELL_SYS, this);
					Intent i = new Intent();
					i.setAction(SettingSystemActivity.ACTION_CHANGEBELL);
					sendBroadcast(i);
				} else if (settingType == SettingSystemActivity.SET_TYPE_ALLARM_RING) {
					SharedPreferencesManager.getInstance().putASystemBellId(
							checkedId, this);
					SharedPreferencesManager.getInstance().putABellSelectPos(
							selectPos, this);
					SharedPreferencesManager.getInstance().putABellType(
							SharedPreferencesManager.TYPE_BELL_SYS, this);
					Intent i = new Intent();
					i.setAction(SettingSystemActivity.ACTION_CHANGEBELL);
					sendBroadcast(i);
				}
				this.finish();
			}
		} else if (id == R.id.set_sd_bell_btn) {
			Intent go_set_sd_bell = new Intent(this,
					SettingSdBellActivity.class);
			go_set_sd_bell.putExtra("type", settingType);
			startActivity(go_set_sd_bell);
		}
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					SettingSystemActivity.ACTION_CHANGEBELL)) {
				initSelectMusicName();
				initSelectState();
				list_sys_bell.setSelection(selectPos);
				adapter.setCheckedId(checkedId);
				adapter.notifyDataSetChanged();
			}

		}

	}

	// 加载选择的音乐文字
	public void initSelectMusicName() {
		if (settingType == SettingSystemActivity.SET_TYPE_COMMING_RING) {
			int cbellType = SharedPreferencesManager.getInstance()
					.getCBellType(this);
			if (cbellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				int bellId = SharedPreferencesManager.getInstance()
						.getCSystemBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSystemBellById(this, bellId);
				if (null != data) {
					selectBell.setText("");
				}
			} else {
				int bellId = SharedPreferencesManager.getInstance()
						.getCSdBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSdBellById(this, bellId);
				if (null != data) {
					selectBell.setText(data.get("bellName"));
				}
			}
		} else if (settingType == SettingSystemActivity.SET_TYPE_ALLARM_RING) {
			int abellType = SharedPreferencesManager.getInstance()
					.getABellType(this);
			if (abellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				int bellId = SharedPreferencesManager.getInstance()
						.getASystemBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSystemBellById(this, bellId);
				if (null != data) {
					selectBell.setText("");
				}
			} else {
				int bellId = SharedPreferencesManager.getInstance()
						.getASdBellId(this);
				HashMap<String, String> data = SystemDataManager.getInstance()
						.findSdBellById(this, bellId);
				if (null != data) {
					selectBell.setText(data.get("bellName"));
				}
			}
		}
	}

	public void playMusic(int bellId) {

		try {
			player.reset();
			HashMap<String, String> data;
			data = SystemDataManager.getInstance().findSystemBellById(context,
					bellId);
			String path = data.get("path");
			if (null == path || "".equals(path)) {

			} else {
				player.setDataSource(path);
				player.prepare();
				player.start();
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		player.stop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myreceiverIsReg) {
			this.unregisterReceiver(receiver);
		}
		player.stop();
		player.release();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_SETTINGBELLRINGACTIVITY;
	}
}
