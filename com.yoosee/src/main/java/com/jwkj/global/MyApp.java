package com.jwkj.global;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.RemoteViews;

import com.yoosee.R;
import com.jwkj.activity.ForwardActivity;
import com.jwkj.activity.ForwardDownActivity;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.listener.CrashHandler;
import com.p2p.core.update.UpdateManager;

public class MyApp extends Application {
	public static final String MAIN_SERVICE_START = Constants.PACKAGE_NAME
			+ "service.MAINSERVICE";
	public static final int NOTIFICATION_DOWN_ID = 0x53256562;
	public static final String LOGCAT = Constants.PACKAGE_NAME
			+ "service.LOGCAT";
	public static MyApp app;
	public static boolean isActive;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private RemoteViews cur_down_view;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		app = this;
		super.onCreate();
		isActive = true;
		if (AppConfig.DeBug.isWrightErroLog) {
//			CrashHandler crashHandler = CrashHandler.getInstance();
//			crashHandler.init(getApplicationContext());
		}
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	/**
	 * 创建挂机图标
	 */
	@SuppressWarnings("deprecation")
	public void showNotification() {
		boolean isShowNotify = SharedPreferencesManager.getInstance()
				.getIsShowNotify(this);
		if (isShowNotify) {
			mNotificationManager = getNotificationManager();
			mNotification = new Notification();

			long when = System.currentTimeMillis();
			mNotification = new Notification(R.drawable.ic_launcher, this
					.getResources().getString(R.string.app_name), when);

			// 放置在"正在运行"栏目中
			mNotification.flags = Notification.FLAG_ONGOING_EVENT;

			RemoteViews contentView = new RemoteViews(getPackageName(),
					R.layout.notify_status_bar);
			contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher);
			contentView.setTextViewText(
					R.id.title,
					this.getResources().getString(R.string.app_name)
							+ " "
							+ this.getResources().getString(
									R.string.running_in_the_background));
			// contentView.setTextViewText(R.id.text, "");
			// contentView.setLong(R.id.time, "setTime", when);
			// 指定个性化视图
			mNotification.contentView = contentView;

			Intent intent = new Intent(this, ForwardActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// 指定内容意图
			mNotification.contentIntent = contentIntent;
			mNotificationManager.notify(R.string.app_name, mNotification);
		}
	}

	public void hideNotification() {
		mNotificationManager = getNotificationManager();
		mNotificationManager.cancel(R.string.app_name);
	}

	/**
	 * 创建下载图标
	 */
	@SuppressWarnings("deprecation")
	public void showDownNotification(int state, int value) {
		boolean isShowNotify = SharedPreferencesManager.getInstance()
				.getIsShowNotify(this);
		if (isShowNotify) {
			mNotificationManager = getNotificationManager();
			mNotification = new Notification();

			long when = System.currentTimeMillis();
			mNotification = new Notification(R.drawable.ic_launcher, this
					.getResources().getString(R.string.app_name), when);
			// 放置在"正在运行"栏目中
			mNotification.flags = Notification.FLAG_ONGOING_EVENT
					| Notification.FLAG_AUTO_CANCEL;

			RemoteViews contentView = new RemoteViews(getPackageName(),
					R.layout.notify_down_bar);
			cur_down_view = contentView;
			contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher);

			Intent intent = new Intent(this, ForwardDownActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			switch (state) {
			case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
				cur_down_view
						.setTextViewText(
								R.id.down_complete_text,
								this.getResources().getString(
										R.string.down_complete_click));
				cur_down_view.setTextViewText(R.id.progress_value, "100%");
				contentView.setProgressBar(R.id.progress_bar, 100, 100, false);

				intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_SUCCESS);

				break;
			case UpdateManager.HANDLE_MSG_DOWNING:
				cur_down_view.setTextViewText(R.id.down_complete_text, this
						.getResources().getString(R.string.down_londing_click));
				cur_down_view.setTextViewText(R.id.progress_value, value + "%");
				contentView
						.setProgressBar(R.id.progress_bar, 100, value, false);

				intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWNING);
				break;
			case UpdateManager.HANDLE_MSG_DOWN_FAULT:
				cur_down_view.setTextViewText(R.id.down_complete_text, this
						.getResources().getString(R.string.down_fault_click));
				cur_down_view.setTextViewText(R.id.progress_value, value + "%");
				contentView
						.setProgressBar(R.id.progress_bar, 100, value, false);

				intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_FAULT);
				break;
			}
			mNotification.contentView = contentView;
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mNotification.contentIntent = contentIntent;

			mNotificationManager.notify(NOTIFICATION_DOWN_ID, mNotification);
		}
	}

	public void hideDownNotification() {
		mNotificationManager = getNotificationManager();
		mNotificationManager.cancel(NOTIFICATION_DOWN_ID);
	}
}
