package com.jwkj.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import com.yoosee.R;
import com.jwkj.data.DataManager;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;
import com.jwkj.utils.Utils;
import com.jwkj.widget.AlarmHeaderView;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PValue;
import com.p2p.core.network.AlarmRecordResult.SAlarmRecord;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmRecordAapter2 extends BaseAdapter {
	Context mcontext;
	List<SAlarmRecord> list;
	ImageThread mImageThread;
	int showCount = 20;

	public AlarmRecordAapter2(Context context) {
		mcontext = context;
		list = new ArrayList<SAlarmRecord>();
	}

	class ViewHolder {
		private TextView robotId;
		private TextView allarmType;
		private TextView allarmTime;
		private AlarmHeaderView headerView;

		public AlarmHeaderView getHeaderView() {
			return headerView;
		}

		public void setHeaderView(AlarmHeaderView headerView) {
			this.headerView = headerView;
		}

		public TextView getRobotId() {
			return robotId;
		}

		public void setRobotId(TextView robotId) {
			this.robotId = robotId;
		}

		public TextView getAllarmType() {
			return allarmType;
		}

		public void setAllarmType(TextView allarmType) {
			this.allarmType = allarmType;
		}

		public TextView getAllarmTime() {
			return allarmTime;
		}

		public void setAllarmTime(TextView allarmTime) {
			this.allarmTime = allarmTime;
		}

	}

	@Override
	public int getCount() {
		if (list.size() < showCount) {
			return list.size();
		}
		return showCount;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = arg1;
		final ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mcontext).inflate(
					R.layout.list_alarm_record_item2, null);
			holder = new ViewHolder();
			holder.setHeaderView((AlarmHeaderView) view
					.findViewById(R.id.header_img));
			holder.setRobotId((TextView) view.findViewById(R.id.robot_id));
			holder.setAllarmType((TextView) view.findViewById(R.id.allarm_type));
			holder.setAllarmTime((TextView) view.findViewById(R.id.allarm_time));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		SAlarmRecord ar = list.get(arg0);
		holder.getHeaderView().updateImage(NpcCommon.mThreeNum, ar.messgeId);
		holder.getRobotId().setText(ar.sourceId);
		switch (ar.alarmType) {
		case 1:
			holder.getAllarmType().setText(R.string.allarm_type1);
			break;
		case 2:
			holder.getAllarmType().setText(R.string.allarm_type2);
			break;
		case 3:
			holder.getAllarmType().setText(R.string.allarm_type3);
			break;
		// case 4:
		// holder.getAllarmType().setText(R.string.allarm_type4);
		// break;
		case 5:
			holder.getAllarmType().setText(R.string.allarm_type5);
			break;
		case 6:
			holder.getAllarmType().setText(R.string.allarm_type6);
			break;
		case 7:
			holder.getAllarmType().setText(R.string.allarm_type4);
			break;
		case 8:
			holder.getAllarmType().setText(R.string.defence);
			break;
		case 9:
			holder.getAllarmType().setText(R.string.no_defence);
			break;
		case 10:
			holder.getAllarmType().setText(R.string.battery_low_alarm);
			break;
		}
		holder.getAllarmTime().setText(Utils.ConvertTimeByLong(ar.alarmTime));
		return view;
	}

	public void updateNewDate(List<SAlarmRecord> datas) {
		if (datas.size() <= 0) {
			return;
		}
		Collections.sort(datas);
		if (!this.list.contains(datas.get(datas.size() - 1))) {
			this.list.clear();
		}

		for (SAlarmRecord gxar : datas) {
			if (!this.list.contains(gxar)) {
				this.list.add(gxar);
				// showCount++;
				Log.e("alarm", "messgeIds=" + gxar.messgeId + "sourceId="
						+ gxar.sourceId + "pictureUrl=" + gxar.pictureUrl
						+ "alarmTime=" + gxar.alarmTime + "alarmType="
						+ gxar.alarmType + "defenceArea=" + gxar.defenceArea
						+ "channel=" + gxar.channel + "serverReceiveTime="
						+ gxar.serverReceiveTime);
			}
		}

		Collections.sort(this.list);
		Log.e("my", "AlarmRecordCount:" + this.list.size());
		this.notifyDataSetChanged();
	}

	public void updateHistoryData(List<SAlarmRecord> data) {
		if (data.size() <= 0) {
			return;
		}

		int count = 0;
		for (SAlarmRecord gxar : data) {
			if (!this.list.contains(gxar)) {
				this.list.add(gxar);
				count++;
			}
		}
		Collections.sort(this.list);
		showCount = list.size();
		Log.e("my", "AlarmRecordCount:" + this.list.size() + "->showCount:"
				+ showCount);
		this.notifyDataSetChanged();
	}

	public class ImageFile {
		public String name;

		public ImageFile(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object object) {
			// TODO Auto-generated method stub
			ImageFile o = (ImageFile) object;
			if (this.name.equals(o.name)) {
				return true;
			} else {
				return false;
			}
		}

	}

	private boolean isExistImage(String index) {
		File file = new File("/sdcard/screenshot/tempHead/alarm/"
				+ NpcCommon.mThreeNum);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filenames[];
		filenames = file.list();
		List<ImageFile> list = new ArrayList<ImageFile>();
		for (int j = 0; j < filenames.length; j++) {
			list.add(new ImageFile(filenames[j].substring(0,
					filenames[j].indexOf("."))));
		}

		if (list.contains(new ImageFile(index))) {

			return true;
		} else {
			// for (SAlarmRecord ar : this.failureList) {
			// if (ar.index.equals(index)) {
			// return true;
			// }
			// }
			return false;
		}
	}

	private class ImageThread extends Thread {
		private boolean isRunImageThread = false;

		public void kill() {
			isRunImageThread = false;
		}

		public void run() {
			isRunImageThread = true;
			while (isRunImageThread) {
				int count = 0;
				// if (showCount > list.size()) {
				// count = list.size();
				// } else {
				// count = showCount;
				// }
				count = list.size();
				int iUserId = Integer.parseInt(NpcCommon.mThreeNum) | 0x80000000;
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mcontext);
				for (int i = 0; i < count; i++) {
					SAlarmRecord ar = list.get(i);
					if (!isExistImage(ar.messgeId)) {
						// String url =
						// "http://192.168.1.222/Alarm/AlarmImage.ashx?ID=05E0161C04D21DBD04F9EBD4E6DDB305&UserID=-2147383631&SessionID=10000";

						String url = ar.pictureUrl + "&UserID=" + iUserId
								+ "&SessionID=" + account.sessionId;
						Log.e("my", url);
						byte[] bImage = ImageUtils.getImageFromNetByUrl(url);
						if (null != bImage && bImage.length > 0) {
							String filePath = "/sdcard/screenshot/tempHead/alarm/"
									+ NpcCommon.mThreeNum;
							String fileName = ar.pictureUrl + ".jpg";
							ImageUtils.writeImageToDisk(bImage, filePath,
									fileName);
							// mHandler.sendEmptyMessage(0);
						} else {
							// if (!failureList.contains(ar)) {
							// failureList.add(ar);
							// }

						}
					} else {
						// Log.e("my","sb!");
					}
					Utils.sleepThread(100);
				}

			}
		}
	}

	public String getLastIndex() {
		// if (this.list.size() <= showCount && this.list.size() > 0) {
		// return this.list.get(this.list.size() - 1).messgeId;
		// } else {
		// return "";
		// }
		if (this.list.size() > 0) {
			return this.list.get(this.list.size() - 1).messgeId;
		} else {
			return "";
		}

	}

	public void runImageThread() {

		if (null != mImageThread) {
			mImageThread.kill();
			mImageThread = null;
		}

		mImageThread = new ImageThread();
		mImageThread.start();
	}

	public void stopImageThread() {
		if (null != mImageThread) {
			mImageThread.kill();
			mImageThread = null;
		}
	}
}
