package com.jwkj.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.yoosee.R;
import com.jwkj.adapter.BellChoiceAdapter;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.data.SystemDataManager;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;

public class SettingSdBellActivity extends BaseActivity implements
		OnClickListener {
	Button save_btn;
	ImageView back_btn;
	ListView list_sd_bell;
	android.media.MediaPlayer player;
	int checkedId;
	int bellType;
	int selectPos;
	Context context;
	int settingType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_sd_bell);
		settingType = getIntent().getIntExtra("type", 0);
		context = this;
		initCompent();

	}

	public void initCompent() {
		back_btn = (ImageView) findViewById(R.id.back_btn);
		save_btn = (Button) findViewById(R.id.save);
		list_sd_bell = (ListView) findViewById(R.id.list_sd_bell);
		player = new MediaPlayer();
		initSelectState();
		ArrayList<HashMap<String, String>> bells = SystemDataManager
				.getInstance().getSdBells(this);
		final BellChoiceAdapter adapter = new BellChoiceAdapter(this, bells);
		adapter.setCheckedId(checkedId);
		list_sd_bell.setAdapter(adapter);
		list_sd_bell.setSelection(selectPos);
		list_sd_bell.setOnItemClickListener(new OnItemClickListener() {

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

		back_btn.setOnClickListener(this);
		save_btn.setOnClickListener(this);
	}

	public void initSelectState() {
		if (settingType == SettingSystemActivity.SET_TYPE_COMMING_RING) {
			selectPos = SharedPreferencesManager.getInstance()
					.getCBellSelectPos(this);
			bellType = SharedPreferencesManager.getInstance()
					.getCBellType(this);
			checkedId = SharedPreferencesManager.getInstance().getCSdBellId(
					this);
			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				checkedId = -1;
				selectPos = 1;
			} else {

			}
		} else if (settingType == SettingSystemActivity.SET_TYPE_ALLARM_RING) {
			selectPos = SharedPreferencesManager.getInstance()
					.getABellSelectPos(this);
			bellType = SharedPreferencesManager.getInstance()
					.getABellType(this);
			checkedId = SharedPreferencesManager.getInstance().getASdBellId(
					this);
			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				checkedId = -1;
				selectPos = 1;
			} else {

			}
		}

	}

	public void playMusic(int bellId) {

		try {
			player.reset();
			bellType = SharedPreferencesManager.getInstance().getCBellType(
					context);
			HashMap<String, String> data;
			data = SystemDataManager.getInstance().findSdBellById(context,
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
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			this.finish();
		} else if (id == R.id.save) {
			if (checkedId == -1) {
				T.showShort(this, R.string.savebell_error);
			} else {
				if (settingType == SettingSystemActivity.SET_TYPE_COMMING_RING) {
					SharedPreferencesManager.getInstance().putCSdBellId(
							checkedId, this);
					SharedPreferencesManager.getInstance().putCBellSelectPos(
							selectPos, this);
					SharedPreferencesManager.getInstance().putCBellType(
							SharedPreferencesManager.TYPE_BELL_SD, this);
					Intent i = new Intent();
					i.setAction(SettingSystemActivity.ACTION_CHANGEBELL);
					sendBroadcast(i);
				} else if (settingType == SettingSystemActivity.SET_TYPE_ALLARM_RING) {
					SharedPreferencesManager.getInstance().putASdBellId(
							checkedId, this);
					SharedPreferencesManager.getInstance().putABellSelectPos(
							selectPos, this);
					SharedPreferencesManager.getInstance().putABellType(
							SharedPreferencesManager.TYPE_BELL_SD, this);
					Intent i = new Intent();
					i.setAction(SettingSystemActivity.ACTION_CHANGEBELL);
					sendBroadcast(i);
				}
				this.finish();
			}
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
		player.stop();
		player.release();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_SETTINGSDBELLACTIVITY;
	}

}
