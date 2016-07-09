package com.jwkj.utils;

import java.util.HashMap;

import android.app.Service;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.util.Log;

import com.yoosee.R;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.data.SystemDataManager;
import com.jwkj.global.MyApp;

public class MusicManger {
	private static MusicManger manager = null;
	private static MediaPlayer player;
	private Vibrator vibrator;

	private MusicManger() {
	}

	private boolean isVibrate = false;

	public synchronized static MusicManger getInstance() {
		if (null == manager) {
			synchronized (MusicManger.class) {
				if (null == manager) {
					manager = new MusicManger();
				}
			}
		}
		return manager;
	}

	public void playCommingMusic() {
		if (null != player) {
			return;
		}
		try {
			player = new MediaPlayer();

			int bellType = SharedPreferencesManager.getInstance().getCBellType(
					MyApp.app);
			HashMap<String, String> data;
			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				int bellId = SharedPreferencesManager.getInstance()
						.getCSystemBellId(MyApp.app);
				data = SystemDataManager.getInstance().findSystemBellById(
						MyApp.app, bellId);
			} else {
				int bellId = SharedPreferencesManager.getInstance()
						.getCSdBellId(MyApp.app);
				data = SystemDataManager.getInstance().findSdBellById(
						MyApp.app, bellId);
			}

			String path = data.get("path");
			if (null == path || "".equals(path)) {

			} else {
				player.reset();
				player.setDataSource(path);
				player.setLooping(true);
				player.prepare();
				player.start();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (null != player) {
				player.stop();
				player.release();
				player = null;
			}
		}
	}

	public void playAlarmMusic() {
		if (null != player) {
			return;
		}
		try {
			player = new MediaPlayer();
			HashMap<String, String> data;
			int bellType = SharedPreferencesManager.getInstance().getABellType(
					MyApp.app);

			if (bellType == SharedPreferencesManager.TYPE_BELL_SYS) {
				int bellId = SharedPreferencesManager.getInstance()
						.getASystemBellId(MyApp.app);
				data = SystemDataManager.getInstance().findSystemBellById(
						MyApp.app, bellId);
			} else {
				int bellId = SharedPreferencesManager.getInstance()
						.getASdBellId(MyApp.app);
				data = SystemDataManager.getInstance().findSdBellById(
						MyApp.app, bellId);
			}
			if (data == null) {
				return;
			}
			String path = data.get("path");
			if (null == path || "".equals(path)) {

			} else {
				player.reset();
				player.setDataSource(path);
				player.setLooping(true);
				player.prepare();
				player.start();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (null != player) {
				player.stop();
				player.release();
				player = null;
			}
		}
	}

	public void playMsgMusic() {
		try {
			final MediaPlayer msgPlayer = MediaPlayer.create(MyApp.app,
					R.raw.message);
			// msgPlayer.prepare();
			msgPlayer.start();
			msgPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					msgPlayer.release();
				}
			});
		} catch (Exception e) {
			Log.e("my", "msg music error!");
		}
	}

	public void stop() {
		if (null != player) {
			player.stop();
			player.release();
			player = null;
		}
	}

	public void Vibrate() {
		if (isVibrate) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				isVibrate = true;
				while (isVibrate) {
					if (null == vibrator) {
						vibrator = (Vibrator) MyApp.app
								.getSystemService(Service.VIBRATOR_SERVICE);
					}
					vibrator.vibrate(400);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void stopVibrate() {
		isVibrate = false;
	}
}
