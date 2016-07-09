package com.jwkj.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.activity.AlarmPictrueActivity;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.yoosee.R;

public class AlarmRecordAdapter extends BaseAdapter {
	List<AlarmRecord> list;
	Context mContext;

	public AlarmRecordAdapter(Context context, List<AlarmRecord> list) {
		this.mContext = context;
		this.list = list;
	}

	class ViewHolder {
		private ImageView iv_alarm_pictrue;
		private TextView robotId;
		private TextView allarmType;
		private TextView allarmTime;
		private LinearLayout layout_extern;
		private TextView text_group;
		private TextView text_item;
		private TextView text_type;

		public TextView getText_type() {
			return text_type;
		}

		public void setText_type(TextView text_type) {
			this.text_type = text_type;
		}

		public LinearLayout getLayout_extern() {
			return layout_extern;
		}

		public void setLayout_extern(LinearLayout layout_extern) {
			this.layout_extern = layout_extern;
		}

		public TextView getText_group() {
			return text_group;
		}

		public void setText_group(TextView text_group) {
			this.text_group = text_group;
		}

		public TextView getText_item() {
			return text_item;
		}

		public void setText_item(TextView text_item) {
			this.text_item = text_item;
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

		public ImageView getIv_alarm_pictrue() {
			return iv_alarm_pictrue;
		}

		public void setIv_alarm_pictrue(ImageView iv_alarm_pictrue) {
			this.iv_alarm_pictrue = iv_alarm_pictrue;
		}
        
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		ViewHolder holder;
		if (null == view) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_alarm_record_item, null);
			holder = new ViewHolder();
			holder.setRobotId((TextView) view.findViewById(R.id.robot_id));
			holder.setAllarmType((TextView) view.findViewById(R.id.allarm_type));
			holder.setAllarmTime((TextView) view.findViewById(R.id.allarm_time));
			holder.setLayout_extern((LinearLayout) view
					.findViewById(R.id.layout_extern));
			holder.setText_group((TextView) view.findViewById(R.id.text_group));
			holder.setText_item((TextView) view.findViewById(R.id.text_item));
			holder.setText_type((TextView) view.findViewById(R.id.tv_type));
			holder.setIv_alarm_pictrue((ImageView)view.findViewById(R.id.iv_alarm_pictrue));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final AlarmRecord ar = list.get(arg0);
		holder.getRobotId().setText(Utils.getDeviceName(ar.deviceId));
		holder.getAllarmTime().setText(
				Utils.ConvertTimeByLong(Long.parseLong(ar.alarmTime)));
		holder.getLayout_extern().setVisibility(RelativeLayout.GONE);
//		if(!ar.alarmPictruePath.equals("")){
//			 Bitmap bitmap = ImageUtils.getBitmap(ar.alarmPictruePath);
//		     holder.getIv_alarm_pictrue().setImageBitmap(bitmap);
//		     holder.getIv_alarm_pictrue().setVisibility(View.VISIBLE);
//		}else{
//			 holder.getIv_alarm_pictrue().setVisibility(View.GONE);
//		}
		switch (ar.alarmType) {
		case 1:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type1);
			if (ar.group >= 0 && ar.item >= 0) {
				holder.getLayout_extern().setVisibility(RelativeLayout.VISIBLE);
				holder.getText_group().setText(
						mContext.getResources().getString(R.string.area)
								+ ":"
								+ Utils.getDefenceAreaByGroup(mContext,
										ar.group));
				holder.getText_item().setText(
						mContext.getResources().getString(R.string.channel)
								+ ":" + (ar.item + 1));
			}
			break;
		case 2:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type2);
			break;
		case 3:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type3);
			break;
		// case 4:
		// holder.getAllarmType().setText(R.string.allarm_type4);
		// break;
		case 5:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type5);
			break;
		case 6:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type6);
			if (ar.group >= 0 && ar.item >= 0) {
				holder.getLayout_extern().setVisibility(RelativeLayout.VISIBLE);
				holder.getText_group().setText(
						mContext.getResources().getString(R.string.area)
								+ ":"
								+ Utils.getDefenceAreaByGroup(mContext,
										ar.group));
				holder.getText_item().setText(
						mContext.getResources().getString(R.string.channel)
								+ ":" + (ar.item + 1));
			}
			break;
		case 7:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type4);
			break;
		case 8:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.defence);
			break;
		case 9:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.no_defence);
			break;
		case 10:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.battery_low_alarm);
			break;
		case 13:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.alarm_type);
			break;
		case 15:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.record_failed);
			break;
		default:
			holder.getText_type().setText(R.string.not_know);
			holder.getAllarmType().setText(String.valueOf(ar.alarmType));
			break;
		}

		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				NormalDialog dialog = new NormalDialog(mContext, mContext
						.getResources()
						.getString(R.string.delete_alarm_records), mContext
						.getResources().getString(R.string.are_you_sure_delete)
						+ " " + ar.deviceId + "?", mContext.getResources()
						.getString(R.string.delete), mContext.getResources()
						.getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub

						DataManager.deleteAlarmRecordById(mContext, ar.id);
						Intent refreshAlarm = new Intent();
						refreshAlarm
								.setAction(Constants.Action.REFRESH_ALARM_RECORD);
						mContext.sendBroadcast(refreshAlarm);
					}
				});
				dialog.showDialog();
				return true;
			}

		});
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ar.alarmPictruePath.equals("")){
					return;
				}
				Intent alarm_pictrue=new Intent(mContext, AlarmPictrueActivity.class);
				alarm_pictrue.putExtra("alarmPictruePath", ar.alarmPictruePath);
				mContext.startActivity(alarm_pictrue);
			}
		});
		return view;
	}

	public void updateData() {
		this.list = DataManager.findAlarmRecordByActiveUser(mContext,
				NpcCommon.mThreeNum);
	}
}
