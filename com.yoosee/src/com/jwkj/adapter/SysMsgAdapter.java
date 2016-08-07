package com.jwkj.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.SysMsgActivity;
import com.jwkj.data.DataManager;
import com.jwkj.data.SysMessage;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;

public class SysMsgAdapter extends BaseAdapter {
	private AlertDialog mDeleteDialog;
	List<SysMessage> list;
	Context context;

	int lastExpandId = -1;
	HashMap<Integer, TextView> cacheText = new HashMap<Integer, TextView>();
	HashMap<Integer, ImageView> cacheArrow = new HashMap<Integer, ImageView>();
	HashMap<Integer, WebView> cacheContent = new HashMap<Integer, WebView>();

	public SysMsgAdapter(Context context, List<SysMessage> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout view = (LinearLayout) arg1;
		if (null == view) {
			view = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.list_sysmsg_item, null);
		}

		final SysMessage msg = list.get(position);
		RelativeLayout toggle = (RelativeLayout) view
				.findViewById(R.id.expandable_toggle_button);

		toggle.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e("my", "long click");
				// createDeleteDialog(msg.getId(),position);
				NormalDialog dialog = new NormalDialog(context,
						context.getResources().getString(
								R.string.delete_sys_messages), context
								.getResources().getString(
										R.string.confirm_delete), context
								.getResources().getString(R.string.delete),
						context.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						DataManager.deleteSysMessage(context, msg.id);
						Intent i = new Intent();
						i.setAction(SysMsgActivity.DELETE_REFESH);
						context.sendBroadcast(i);
						Intent k = new Intent();
						k.setAction(Constants.Action.RECEIVE_SYS_MSG);
						context.sendBroadcast(k);
					}
				});
				dialog.showDialog();
				return true;
			}

		});

		TextView text = (TextView) view.findViewById(R.id.title);
		TextView time = (TextView) view.findViewById(R.id.date);
		ImageView arrow = (ImageView) toggle.findViewById(R.id.arrow);
		WebView content = (WebView) view.findViewById(R.id.content);

		if (msg.id == lastExpandId) {
			Animation rotate = new RotateAnimation(0f, 180f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setFillAfter(true);
			rotate.setDuration(380);
			arrow.startAnimation(rotate);
		} else {
			arrow.setImageResource(R.drawable.arrow2);
		}

		String[] data = Utils.getMsgInfo(msg, context);
		text.setText(data[0]);
		time.setText(Utils.ConvertTimeByLong(Long.parseLong(msg.msg_time)));
		if (msg.msgState == SysMessage.MESSAGE_STATE_READED) {
			text.setTextColor(context.getResources().getColor(
					R.color.text_color_light));
			// time.setTextColor(context.getResources().getColor(R.color.text_color_light));
		} else {
			text.setTextColor(context.getResources().getColor(R.color.black));
			// time.setTextColor(context.getResources().getColor(R.color.black));
		}

		cacheText.put(msg.id, text);
		cacheArrow.put(msg.id, arrow);
		cacheContent.put(msg.id, content);
		return view;
	}

	public void upDateSysMsg(int position, int type) {
		SysMessage msg = list.get(position);
		ImageView arrow = cacheArrow.get(msg.id);
		if (type == 1) {
			WebView content = cacheContent.get(msg.id);
			String[] data = Utils.getMsgInfo(msg, context);
			content.loadDataWithBaseURL(null, data[1], "text/html", "utf-8",
					null);
		}

		if (lastExpandId == -1) {
			Animation rotate = new RotateAnimation(0f, 180f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setFillAfter(true);
			rotate.setDuration(380);
			arrow.startAnimation(rotate);
			lastExpandId = msg.id;
		} else if (lastExpandId == msg.id) {
			Animation rotate = new RotateAnimation(180f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setFillAfter(true);
			rotate.setDuration(380);
			arrow.startAnimation(rotate);
			lastExpandId = -1;
		} else {
			ImageView last_arrow = cacheArrow.get(lastExpandId);
			Animation last_rotate = new RotateAnimation(180f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			last_rotate.setFillAfter(true);
			last_rotate.setDuration(380);
			last_arrow.startAnimation(last_rotate);

			Animation rotate = new RotateAnimation(0f, 180f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setFillAfter(true);
			rotate.setDuration(380);
			arrow.startAnimation(rotate);
			lastExpandId = msg.id;
		}

		if (msg.msgState == SysMessage.MESSAGE_STATE_NO_READ) {
			DataManager.updateSysMessageState(context, msg.id,
					SysMessage.MESSAGE_STATE_READED);
			cacheText.get(msg.id).setTextColor(
					context.getResources().getColor(R.color.text_color_light));
			Intent i = new Intent();
			i.setAction(Constants.Action.RECEIVE_SYS_MSG);
			context.sendBroadcast(i);
			msg.msgState = SysMessage.MESSAGE_STATE_READED;
		}

		Log.e("my", "lastExpandId:" + lastExpandId);
	}

	public void refresh() {
		list = DataManager.findSysMessageByActiveUser(context,
				NpcCommon.mThreeNum);
		notifyDataSetChanged();
	}

}
