package com.jwkj.adapter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.CallActivity;
import com.yoosee.R;
import com.jwkj.activity.AddContactNextActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PValue;

public class IpcListAdapter extends BaseAdapter {
	private Context context;
	private NormalDialog dialog;
	private ArrayList<String> data;
	private HashMap<String, InetAddress> addresses = new HashMap<String, InetAddress>();
	private HashMap<String, String> names = new HashMap<String, String>();
	private HashMap<String, Integer> flags = new HashMap<String, Integer>();
	private HashMap<String, Integer> types = new HashMap<String, Integer>();
	boolean isShowAnim = true;

	public IpcListAdapter(Context context, ArrayList<String> data) {
		this.context = context;
		this.data = data;
	}

	final static class ViewHolder {
		public HeaderView headerImg;
		public TextView nameText;
		public TextView addressText;
		public ImageView typeImg;
		public RelativeLayout main;
		public ImageView operatorImg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHoler;
		if (null == convertView) {
			viewHoler = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_ipc_item, null);
			viewHoler.headerImg = (HeaderView) convertView
					.findViewById(R.id.header_img);
			viewHoler.typeImg = (ImageView) convertView
					.findViewById(R.id.type_img);
			viewHoler.nameText = (TextView) convertView
					.findViewById(R.id.ipc_name);
			viewHoler.operatorImg = (ImageView) convertView
					.findViewById(R.id.operator_img);
			viewHoler.main = (RelativeLayout) convertView
					.findViewById(R.id.main);
			viewHoler.addressText = (TextView) convertView
					.findViewById(R.id.ipc_address);
			convertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHolder) convertView.getTag();
		}

		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}

		});
		if (position == data.size() - 1 && isShowAnim) {
			convertView.startAnimation(AnimationUtils.loadAnimation(context,
					R.anim.ipc_item_down));
		}

		TextView nameText = viewHoler.nameText;
		TextView addressText = viewHoler.addressText;
		HeaderView headerImg = viewHoler.headerImg;
		ImageView typeImg = viewHoler.typeImg;
		RelativeLayout main = viewHoler.main;
		ImageView operatorImg = viewHoler.operatorImg;
		final String threeNum = data.get(position);
		final InetAddress address = addresses.get(threeNum);
		headerImg.updateImage(threeNum, false);
		final Contact contact = FList.getInstance().isContact(threeNum);
		final int deviceType = types.get(threeNum);
		final int flag = flags.get(threeNum);
		Log.e("shake", "id:" + threeNum + " type:" + deviceType + " flag:"
				+ flag + " ip:" + address.getHostAddress());
		addressText.setText(address.getHostAddress());

		switch (deviceType) {
		case P2PValue.DeviceType.NPC:
			typeImg.setImageResource(R.drawable.ic_device_type_npc);
			break;
		case P2PValue.DeviceType.IPC:
			typeImg.setImageResource(R.drawable.ic_device_type_ipc);
			break;
		case P2PValue.DeviceType.DOORBELL:
			typeImg.setImageResource(R.drawable.ic_device_type_door_bell);
			break;
		case P2PValue.DeviceType.UNKNOWN:
			typeImg.setImageResource(R.drawable.ic_device_type_unknown);
			break;
		case P2PValue.DeviceType.NVR:
			typeImg.setImageResource(R.drawable.ic_device_type_nvr);
			break;
		default:
			typeImg.setImageResource(R.drawable.ic_device_type_unknown);
			break;
		}
		if (contact != null) {
			nameText.setText(contact.contactName);
			// if(flag==Constants.DeviceFlag.ALREADY_SET_PASSWORD){
			operatorImg.setImageResource(R.drawable.ic_shake_monitor);
			// }else if(flag==Constants.DeviceFlag.UNSET_PASSWORD){
			// operatorImg.setImageResource(R.drawable.add);
			// //创建密码
			// }
		} else {
			nameText.setText(threeNum);
			if (flag == Constants.DeviceFlag.ALREADY_SET_PASSWORD) {
				operatorImg.setImageResource(R.drawable.add);
			} else if (flag == Constants.DeviceFlag.UNSET_PASSWORD) {
				// 创建密码 并添加好友
				operatorImg.setImageResource(R.drawable.add);
			}
		}
		String mark = address.getHostAddress();
		final String ipFlag = mark.substring(mark.lastIndexOf(".") + 1,
				mark.length());
		main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != contact) {
					if (flag == Constants.DeviceFlag.ALREADY_SET_PASSWORD) {
						Intent monitor = new Intent();
						monitor.setClass(context, CallActivity.class);
						String mark = address.getHostAddress();
						monitor.putExtra("callId", contact.contactId);
						monitor.putExtra("contactName", contact.contactName);
						monitor.putExtra(
								"ipFlag",
								mark.substring(mark.lastIndexOf(".") + 1,
										mark.length()));
						monitor.putExtra("password", contact.contactPassword);
						monitor.putExtra("isOutCall", true);
						monitor.putExtra("type",
								Constants.P2P_TYPE.P2P_TYPE_MONITOR);
						context.startActivity(monitor);
					} else if (flag == Constants.DeviceFlag.UNSET_PASSWORD) {
						// 创建密码
						// addContact(threeNum,deviceType,true,ipFlag);
						Intent monitor = new Intent();
						monitor.setClass(context, CallActivity.class);
						String mark = address.getHostAddress();
						monitor.putExtra("callId", contact.contactId);
						monitor.putExtra("contactName", contact.contactName);
						monitor.putExtra(
								"ipFlag",
								mark.substring(mark.lastIndexOf(".") + 1,
										mark.length()));
						monitor.putExtra("password", contact.contactPassword);
						monitor.putExtra("isOutCall", true);
						monitor.putExtra("type",
								Constants.P2P_TYPE.P2P_TYPE_MONITOR);
						context.startActivity(monitor);
						Log.e("contact", "contactname" + contact.contactName);
						Log.e("contact", "contactid" + contact.contactId);
						Log.e("contact", "contactpassword"
								+ contact.contactPassword);
					}
				} else {
					if (flag == Constants.DeviceFlag.ALREADY_SET_PASSWORD) {
						addContact(threeNum, deviceType, false, "");
					} else if (flag == Constants.DeviceFlag.UNSET_PASSWORD) {
						// 创建密码 并添加好友
						addContact(threeNum, deviceType, true, "");
					}
				}
			}

		});
		return convertView;
	}

	public void addContact(String threeNum, int deviceType,
			boolean isCreatePassword, String ipFlag) {
		Contact saveContact = new Contact();
		saveContact.contactId = threeNum;
		saveContact.contactType = deviceType;
		saveContact.messageCount = 0;
		saveContact.activeUser = NpcCommon.mThreeNum;

		Intent modify = new Intent();
		modify.setClass(context, AddContactNextActivity.class);
		modify.putExtra("isCreatePassword", isCreatePassword);
		modify.putExtra("contact", saveContact);
		modify.putExtra("ipFlag", ipFlag);
		context.startActivity(modify);
	}

	public void updateData(String id, InetAddress address, String name,
			int flag, int type) {
		if (!data.contains(id)) {
			data.add(id);
			addresses.put(id, address);
			names.put(id, name);
			flags.put(id, flag);
			types.put(id, type);
			notifyDataSetChanged();
		}

	}

	public void updateFlag(String threeNum, boolean isInit) {
		if (isInit) {
			flags.put(threeNum, Constants.DeviceFlag.ALREADY_SET_PASSWORD);
		} else {
			flags.put(threeNum, Constants.DeviceFlag.UNSET_PASSWORD);
		}
		notifyDataSetChanged();
	}

	public void closeAnim() {
		isShowAnim = false;
	}

	public void clear() {
		isShowAnim = true;
		data.clear();
		notifyDataSetChanged();
	}
}
